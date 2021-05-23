package com.sbnz.ibar.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import com.sbnz.ibar.dto.BookDto;
import com.sbnz.ibar.dto.RatingIntervalDto;
import com.sbnz.ibar.dto.ReadingProgressDto;
import com.sbnz.ibar.mapper.BookMapper;
import com.sbnz.ibar.mapper.ReadingProgressMapper;
import com.sbnz.ibar.model.*;
import com.sbnz.ibar.repositories.*;
import com.sbnz.ibar.rto.BookResponse;
import com.sbnz.ibar.rto.BookResponseFilter;
import com.sbnz.ibar.utils.Utils;
import lombok.AllArgsConstructor;

import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.ObjectDataCompiler;
import org.drools.template.objects.ArrayDataProvider;
import org.kie.api.runtime.KieSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sbnz.ibar.mapper.FileService;

import javassist.NotFoundException;

@Service
@AllArgsConstructor
public class BookService {

    private final long RECOMMENDATION_THRESHOLD = 10;

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final ReviewRepository reviewRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final ReadingProgressRepository readingProgressRepository;
    private final ReadingProgressMapper readingProgressMapper;
    private final KieService kieService;
    private final BookMapper bookMapper;
    private final ReadingListItemRepository readingListItemRepository;
    private final BookResponseFilter bookResponseFilter;

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Transactional
    public Iterable<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book getById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    public List<BookDto> getTopRated(long userId) {
        Pageable pageLimit = PageRequest.of(0, 10);
        return this.bookRepository.getTopRated(userId, pageLimit).stream()
                .map(bookMapper::toBookDto).collect(Collectors.toList());
    }

    public List<BookDto> getRecommended(long userId) {
        Reader r = (Reader) this.userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);


        KieSession kieSession = this.kieService.getSession(Utils.BOOKS_SESSION, Utils.BOOKS_AGENDA);

        kieSession.setGlobal("highRatingPoints", 10L);
        kieSession.setGlobal("averageRatingPoints", 3L);
        kieSession.setGlobal("lowRatingPoints", 2L);
        kieSession.setGlobal("readPoints", 2L);
        kieSession.setGlobal("readingListPoints", 15L);
        kieSession.setGlobal("recommendationThreshold", this.RECOMMENDATION_THRESHOLD);
        kieSession.setGlobal("loggedReader", r);

        this.authorRepository.getReadAuthors(userId).forEach(kieSession::insert);
        this.categoryRepository.getReadCategories(userId).forEach(kieSession::insert);
        this.reviewRepository.getReviewsByReaderIdAndReaderCategory(userId, r.isMale())
                .forEach(kieSession::insert);
        this.readingProgressRepository.getReadingProgressByReaderIdAndReaderCategory(userId, r.isMale())
                .forEach(kieSession::insert);
        this.readingListItemRepository.getReadingListByReaderIdAndReaderCategory(userId, r.isMale())
                .forEach(kieSession::insert);
        this.bookRepository.getUnread(userId).forEach(kieSession::insert);

        kieSession.fireAllRules();

        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);

        kieSession.dispose();

        return bookResponses.stream().map(qm -> (BookResponse) qm)
                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(this.RECOMMENDATION_THRESHOLD)
                .map(br -> bookMapper.toBookDto(br.getBook())).collect(Collectors.toList());

    }

    public List<BookDto> findAllByRatingInterval(RatingIntervalDto ratingIntervalDTO) throws FileNotFoundException {
        // TODO replace with classpath
        InputStream template = new FileInputStream("../drools/src/main/resources/rules.templates/bookRatingSearch.drt");

        ObjectDataCompiler converter = new ObjectDataCompiler();

        List<RatingIntervalDto> data = new ArrayList<>();

        data.add(ratingIntervalDTO);

        String drl = converter.compile(data, template);

        return this.fireTemplateRules(drl).stream().map(bookMapper::toBookDto).collect(Collectors.toList());
    }

    public List<BookDto> findAllByAuthorsName(String authorsName) throws FileNotFoundException {
        // TODO replace with classpath
        InputStream template = new FileInputStream("../drools/src/main/resources/rules.templates/bookAuthorsNameSearch.drt");

        DataProviderCompiler converter = new DataProviderCompiler();

        DataProvider dataProvider = new ArrayDataProvider(new String[][]{
                new String[]{authorsName}
        });

        String drl = converter.compile(dataProvider, template);

        return this.fireTemplateRules(drl).stream().map(bookMapper::toBookDto).collect(Collectors.toList());
    }

    private ArrayList<Book> fireTemplateRules(String drl) {
        ArrayList<Book> result = new ArrayList<>();

        KieSession kieSession = kieService.createKieSessionFromDRL(drl);

        for (Book book : getAll()) {
            kieSession.insert(book);
        }

        kieSession.setGlobal("result", result);

        kieSession.fireAllRules();

        return result;
    }

    public Book create(Book entity) throws Exception {
        return null;
    }

    @Transactional
    public boolean delete(Long id) throws Exception {
        Book existingBook = getById(id);
        if (existingBook == null) {
            throw new NotFoundException("Book with given id doesn't exist.");
        }

        fileService.deleteImageFromFile(existingBook.getCover());

        reviewRepository.deleteAllByBookId(id);

        bookRepository.deleteById(id);

        return true;
    }

    public Book update(Long id, Book entity) throws NotFoundException {
        return null;
    }

    @Transactional
    public Book update(Long id, Book entity, MultipartFile newImage) throws NotFoundException, IOException {
        Book existingBook = getById(id);
        if (existingBook == null) {
            throw new NotFoundException("Book with given id doesn't exist.");
        }

        Category category = categoryRepository.getOne(entity.getCategory().getId());
        if (category == null) {
            throw new NotFoundException("Category doesn't exist.");
        }

        existingBook.setCategory(category);
        existingBook.setDescription(entity.getDescription());
        existingBook.setType(entity.getType());

        Set<Author> writtenBy = getAuthorsOfBook(entity);

        existingBook.setAuthors(writtenBy);

        if (!newImage.isEmpty()) {
            fileService.uploadNewImage(newImage, existingBook.getCover());
        }
        return bookRepository.save(existingBook);
    }

    @Transactional
    public Book create(Book entity, MultipartFile file) throws Exception {
        Category category = categoryRepository.getOne(entity.getCategory().getId());
        if (category == null) {
            throw new NotFoundException("Category doesn't exist.");
        }

        entity.setCategory(category);

        String imagePath = fileService.saveImage(file, entity.getName());

        entity.setCover(imagePath);

        entity.setAuthors(getAuthorsOfBook(entity));

        return bookRepository.save(entity);
    }

    public ReadingProgressDto setReadingProgress(long bookId, long readerId, long progress) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId));

        User user = this.userRepository.findById(readerId)
                .orElseThrow(() -> new EntityNotFoundException("Reader with id " + readerId));

        if (!(user instanceof Reader)) {
            throw new IllegalArgumentException("Passed id of a non-reader user " + readerId);
        }

        Reader reader = (Reader) user;

        ReadingProgress readingProgress = this.readingProgressRepository.findByBookIdAndReaderId(bookId, readerId)
                .orElse(new ReadingProgress(book, reader));

        if (progress > readingProgress.getProgress()) {
            readingProgress.setProgress(progress);
            KieSession kieSession = kieService.getSession(Utils.BOOKS_SESSION, Utils.BOOKS_AGENDA);
            kieSession.insert(readingProgress);
            kieService.runSession(kieSession);
            readingProgress = readingProgressRepository.save(readingProgress);
        }
        return readingProgressMapper.toDto(readingProgress);
    }

    public Page<Book> getByCategoryId(Long id, Pageable pageable) {
        return bookRepository.getByCategoryId(id, pageable);
    }

    public Page<Book> findByCategoryIdAndNameContains(Long id, String name, Pageable pageable) {
        return bookRepository.findByCategoryIdAndNameContainingIgnoreCase(id, name, pageable);
    }

    public Page<Book> findByNameContains(String name, Pageable pageable) {
        return bookRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    private Set<Author> getAuthorsOfBook(Book entity) throws NotFoundException {
        Set<Author> writtenBy = new HashSet<Author>();

        for (Author author : entity.getAuthors()) {
            author = authorRepository.findById(author.getId()).orElse(null);

            if (author == null)
                throw new NotFoundException("Author with given id doesn't exists.");

            writtenBy.add(author);
        }

        return writtenBy;
    }

}
