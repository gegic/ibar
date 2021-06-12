package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.BookDto;
import com.sbnz.ibar.dto.FilterDto;
import com.sbnz.ibar.dto.RatingIntervalDto;
import com.sbnz.ibar.dto.ReadingProgressDto;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.BookMapper;
import com.sbnz.ibar.mapper.FileService;
import com.sbnz.ibar.mapper.ReadingProgressMapper;
import com.sbnz.ibar.model.*;
import com.sbnz.ibar.repositories.*;
import com.sbnz.ibar.rto.BookResponse;
import com.sbnz.ibar.rto.BookResponseFilter;
import com.sbnz.ibar.utils.Utils;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.kie.api.runtime.KieSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public List<BookDto> getAll() {
        List<Book> books = bookRepository.findAll();

        return books.stream().map(this::toBookDto).collect(Collectors.toList());
    }

    public BookDto getById(UUID id) throws EntityDoesNotExistException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Book> book = bookRepository.findById(id);
        Subscription s = subscriptionRepository.findByBuyerId(user.getId())
                .orElseThrow(() -> new EntityDoesNotExistException(Subscription.class.getName(), user.getId()));
        Book b = book.orElseThrow(EntityNotFoundException::new);
        if (!s.getPurchasedPlan().getCategories().contains(b.getCategory())) {
            b.setPdf(null);
        }
        return this.toBookDto(b);
    }

    public List<BookDto> search(String searchQuery, FilterDto filterDto) throws FileNotFoundException {
        List<Book> books = bookRepository.findByNameContainsIgnoreCase(searchQuery);
        if (filterDto.getAuthorsName() != null && filterDto.getAuthorsName().length() > 0) {
            books = this.filterByAuthorsName(books, filterDto.getAuthorsName());
        }
        if (filterDto.getRatingInterval() != null) {
            books = this.filterByRatingInterval(books, filterDto.getRatingInterval());
        }
        return books.stream().map(bookMapper::toBookDto).collect(Collectors.toList());
    }

    private List<Book> filterByAuthorsName(List<Book> books, String authorsName) throws FileNotFoundException {
        List<Book> result = new ArrayList<>();
        KieSession kieSession = kieService.getBookByAuthorsNameSearchSession(authorsName);
        kieSession.setGlobal("result", result);
        books.forEach(kieSession::insert);

        kieSession.fireAllRules();
        kieSession.dispose();
        return result;
    }

    private List<Book> filterByRatingInterval(List<Book> books, RatingIntervalDto ratingInterval)
            throws FileNotFoundException {
        List<Book> result = new ArrayList<>();
        KieSession kieSession = kieService.getBookByRatingSearchSession(ratingInterval);
        kieSession.setGlobal("result", result);
        books.forEach(kieSession::insert);

        kieSession.fireAllRules();
        kieSession.dispose();
        return result;
    }
    public List<BookDto> getTopRated() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = user.getId();

        Pageable pageLimit = PageRequest.of(0, 10);
        return this.bookRepository.getTopRated(userId, pageLimit).stream()
                .map(this::toBookDto).collect(Collectors.toList());
    }

    public List<BookDto> getRecommended() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UUID userId = user.getId();

        Reader r = (Reader) this.userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);


        KieSession kieSession = this.kieService.getSession(Utils.BOOKS_SESSION, Utils.RECOMMENDATIONS_AGENDA);

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
                .map(br -> this.toBookDto(br.getBook())).collect(Collectors.toList());

    }



    public Book create(Book entity) throws Exception {
        return null;
    }

    public Book update(UUID id, Book entity) throws NotFoundException {
        return null;
    }

    @Transactional
    public Book update(UUID id, Book entity, MultipartFile newImage) throws NotFoundException, IOException {
        Book existingBook = this.bookRepository.findById(id).orElse(null);
        if (existingBook == null) {
            throw new NotFoundException("Book with given id doesn't exist.");
        }

        Category category = categoryRepository.findById(entity.getCategory().getId()).orElse(null);
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
        Category category = categoryRepository.findById(entity.getCategory().getId()).orElse(null);
        if (category == null) {
            throw new NotFoundException("Category doesn't exist.");
        }

        entity.setCategory(category);

        String imagePath = fileService.saveImage(file, entity.getName());

        entity.setCover(imagePath);

        entity.setAuthors(getAuthorsOfBook(entity));

        return bookRepository.save(entity);
    }

    @Transactional
    public boolean delete(UUID id) throws IOException {
        Book existingBook = this.bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (this.doesExistReadingProgressContainingBookWithGivenId(id)) {
            return false;
        }

        this.removeImageAndPDFOfBookFromFileSystem(existingBook);

        this.removeReviewAndReadingListEntitiesThatContainsBookWithGivenId(id);

        bookRepository.deleteById(id);

        return true;
    }

    public ReadingProgressDto setReadingProgress(UUID bookId, UUID readerId, long progress) {
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
            KieSession kieSession = kieService.getSession(Utils.BOOKS_SESSION, Utils.RECOMMENDATIONS_AGENDA);
            kieSession.insert(readingProgress);
            kieService.runSession(kieSession);
            readingProgress = readingProgressRepository.save(readingProgress);
        }
        return readingProgressMapper.toDto(readingProgress);
    }

    public Page<Book> getByCategoryId(UUID id, Pageable pageable) {
        return bookRepository.getByCategoryId(id, pageable);
    }

    public Page<Book> findByCategoryIdAndNameContains(UUID id, String name, Pageable pageable) {
        return bookRepository.findByCategoryIdAndNameContainingIgnoreCase(id, name, pageable);
    }

    private boolean doesExistReadingProgressContainingBookWithGivenId(UUID id) {
        return readingProgressRepository.countByBookId(id) > 0;
    }

    private void removeImageAndPDFOfBookFromFileSystem(Book book) throws IOException {
        fileService.deleteImageFromFile(book.getCover());

        fileService.deleteImageFromFile(book.getPdf());
    }

    private void removeReviewAndReadingListEntitiesThatContainsBookWithGivenId(UUID id) {
        reviewRepository.deleteAllByBookId(id);

        readingListItemRepository.deleteAllByBookId(id);
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

    private BookDto toBookDto(Book entity) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BookDto dto = bookMapper.toBookDto(entity);
        dto.setInReadingList(readingListItemRepository
                .findByBookIdAndReaderId(entity.getId(), user.getId()).isPresent());
        dto.setNumRead(readingProgressRepository.countByBookId(entity.getId()));
        return dto;
    }
}