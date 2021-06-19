package com.sbnz.ibar.services;

import com.sbnz.ibar.config.FilesConfig;
import com.sbnz.ibar.dto.*;
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
import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.kie.api.runtime.KieSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final FilesConfig filesConfig;

    @Transactional
    public List<BookDto> getAll() {
        List<Book> books = bookRepository.findAll();

        return books.stream().map(this::toBookDto).collect(Collectors.toList());
    }

    public BookDto getById(UUID id) throws EntityDoesNotExistException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Book> book = bookRepository.findById(id);
        Book b = book.orElseThrow(EntityNotFoundException::new);

        if (user instanceof Reader) {
            Subscription s = subscriptionRepository.findByBuyerId(user.getId())
                    .orElseThrow(() -> new EntityDoesNotExistException(Subscription.class.getName(), user.getId()));
            if (!s.getPurchasedPlan().getCategories().contains(b.getCategory())) {
                b.setPdf(null);
            }
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

        KieSession booksSession = kieService.getBooksSession();
        booksSession.setGlobal("loggedReader", r);

        List<BookResponse> responses = new ArrayList<>();
        booksSession.insert(responses);

        this.authorRepository.getReadAuthors(userId).forEach(booksSession::insert);
        this.categoryRepository.getReadCategories(userId).forEach(booksSession::insert);
        this.reviewRepository.getReviewsByReaderIdAndReaderCategory(userId, r.isMale())
                .forEach(booksSession::insert);
        this.readingProgressRepository.getReadingProgressByReaderIdAndReaderCategory(userId, r.isMale())
                .forEach(booksSession::insert);
        this.readingListItemRepository.getReadingListByReaderIdAndReaderCategory(userId, r.isMale())
                .forEach(booksSession::insert);
        this.bookRepository.getUnread(userId).forEach(booksSession::insert);

        booksSession.fireAllRules();

        return responses.stream().map(br -> this.toBookDto(br.getBook())).limit(10).collect(Collectors.toList());

    }

    public UUID setCover(MultipartFile coverFile) {

        if (coverFile == null) {
            throw new NullPointerException();
        }

        BufferedImage bufferedImage;

        try {
            bufferedImage = Thumbnails.of(coverFile.getInputStream()).size(1000, 1000).asBufferedImage();
        } catch (IOException e) {
            return null;
        }

        UUID id = UUID.randomUUID();

        try {
            savePhoto(filesConfig.getCoverPath(), bufferedImage, id);
        } catch (IOException e) {
            System.out.println("Exception:" + e);
        }
        return id;
    }

    private void savePhoto(String path,
                           BufferedImage bufferedImage,
                           UUID id) throws IOException {
        Path fileStorageLocation = Paths.get(path)
                .toAbsolutePath().normalize();
        Path targetLocation = fileStorageLocation.resolve(String.format("%s.png", id));
        File output = new File(targetLocation.toString());
        ImageIO.write(bufferedImage, "png", output);
    }

    public ContentFileDto setPdf(MultipartFile pdfFile) {

        if (pdfFile == null) {
            throw new NullPointerException();
        }

        String id = UUID.randomUUID() + ".pdf";
        long numPages = -1;
        try (OutputStream os = Files
                .newOutputStream(Paths.get(filesConfig.getPdfPath(), id))) {
            PDDocument doc = PDDocument.load(pdfFile.getBytes());
            numPages = doc.getNumberOfPages();
            doc.close();
            os.write(pdfFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ContentFileDto(id, numPages);
    }


    public BookDto create(BookDto dto) throws EntityDoesNotExistException {
        return save(dto);
    }

    public BookDto update(BookDto dto) throws EntityDoesNotExistException {
        if (dto.getId() == null || !bookRepository.existsById(dto.getId())) {
            throw new EntityDoesNotExistException(Book.class.getName(), dto.getId());
        }
        return save(dto);
    }

    private BookDto save(BookDto dto) throws EntityDoesNotExistException {
        Book book = bookMapper.toEntity(dto);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityDoesNotExistException(Category.class.getName(), dto.getCategoryId()));

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(dto.getAuthorIds()));

        book.setCategory(category);
        book.setAuthors(authors);

        book = bookRepository.save(book);

        return bookMapper.toBookDto(book);
    }

    @Transactional
    public boolean delete(UUID id) throws IOException {
        Book existingBook = this.bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (this.doesExistReadingProgressContainingBookWithGivenId(id)) {
            return false;
        }

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