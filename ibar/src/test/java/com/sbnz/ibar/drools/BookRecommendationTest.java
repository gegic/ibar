package com.sbnz.ibar.drools;

import com.sbnz.ibar.dto.BookDto;
import com.sbnz.ibar.model.*;
import com.sbnz.ibar.model.enums.BookType;
import com.sbnz.ibar.rto.BookResponse;
import com.sbnz.ibar.rto.BookResponseFilter;
import com.sbnz.ibar.services.KieService;
import com.sbnz.ibar.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class BookRecommendationTest {

    private KieSession kieSession;

    private BookResponseFilter bookResponseFilter;

    @Before
    public void setUp() {
        bookResponseFilter = new BookResponseFilter();

        KieServices ks = KieServices.Factory.get();

        KieContainer kc = ks.newKieClasspathContainer();

        kieSession = kc.newKieSession("books-session");
        kieSession.getAgenda().getAgendaGroup("recommendations").setFocus();
    }

    @Test
    public void testWhenNothingReadAndNoSimilarUsersReturnsBooksWith0Points() {
        List<BookResponse> recommendations = new ArrayList<>();

        Reader reader = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        setGlobals(kieSession, reader);

        createDataForUserWithoutActivity(kieSession);

        kieSession.fireAllRules();

        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);

        recommendations = bookResponses.stream().map(qm -> (BookResponse) qm)
                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(10).collect(Collectors.toList());

        kieSession.dispose();

        for (BookResponse bookResponse : recommendations) {
            assertTrue(bookResponse.getPoints() == 0);
        }

        assertEquals(recommendations.size(), 5);
    }

    @Test
    public void testWhenUserPositiveRateBook() {
        List<BookResponse> recommendations;

        Reader reader = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        setGlobals(kieSession, reader);

        createDataWithPositiveReview(kieSession);

        kieSession.fireAllRules();

        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);

        recommendations = bookResponses.stream().map(qm -> (BookResponse) qm)
                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(10).collect(Collectors.toList());

        kieSession.dispose();

        for (BookResponse bookResponse : recommendations) {
            if (bookResponse.getBook().getId() != 5)
                assertTrue(bookResponse.getPoints() == 52);
            else {
                assertTrue(bookResponse.getPoints() == 0);
            }
        }

        assertEquals(recommendations.size(), 4);
    }

    @Test
    public void testWhenUserNegativeRateBook() {
        List<BookResponse> recommendations;

        Reader reader = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        setGlobals(kieSession, reader);

        createDataWithNegativeReview(kieSession);

        kieSession.fireAllRules();

        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);

        recommendations = bookResponses.stream().map(qm -> (BookResponse) qm)
                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(10).collect(Collectors.toList());

        kieSession.dispose();

        for (BookResponse bookResponse : recommendations) {
            if (bookResponse.getBook().getId() != 5)
                assertTrue(bookResponse.getPoints() == 2);
            else {
                assertTrue(bookResponse.getPoints() == 0);
            }
        }

        assertEquals(recommendations.size(), 4);
    }

    @Test
    public void testWhenUserReadBook() {
        List<BookResponse> recommendations;

        Reader reader = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        setGlobals(kieSession, reader);

        createDataWhenUserReadBook(kieSession);

        kieSession.fireAllRules();

        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);

        recommendations = bookResponses.stream().map(qm -> (BookResponse) qm)
                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(10).collect(Collectors.toList());

        kieSession.dispose();

        for (BookResponse bookResponse : recommendations) {
            if (bookResponse.getBook().getId() != 5)
                assertTrue(bookResponse.getPoints() == 54);
            else {
                assertTrue(bookResponse.getPoints() == 0);
            }
        }

        assertEquals(recommendations.size(), 3);
    }

    @Test
    public void testWhenUserUnReadBook() {
        List<BookResponse> recommendations;

        Reader reader = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        setGlobals(kieSession, reader);

        createDataWhenUserUnReadBook(kieSession);

        kieSession.fireAllRules();

        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);

        recommendations = bookResponses.stream().map(qm -> (BookResponse) qm)
                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(10).collect(Collectors.toList());

        kieSession.dispose();

        for (BookResponse bookResponse : recommendations) {
            if (bookResponse.getBook().getId() != 5)
                assertTrue(bookResponse.getPoints() == 50);
            else {
                assertTrue(bookResponse.getPoints() == 0);
            }
        }

        assertEquals(recommendations.size(), 3);
    }

    @Test
    public void testWhenUserHaveReadingList() {
        List<BookResponse> recommendations;

        Reader reader = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        setGlobals(kieSession, reader);

        createDataWhenUserHaveReadingList(kieSession);

        kieSession.fireAllRules();

        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);

        recommendations = bookResponses.stream().map(qm -> (BookResponse) qm)
                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(10).collect(Collectors.toList());

        kieSession.dispose();

        for (BookResponse bookResponse : recommendations) {
            if (bookResponse.getBook().getId() != 5)
                assertTrue(bookResponse.getPoints() == 67);
            else {
                assertTrue(bookResponse.getPoints() == 0);
            }
        }

        assertEquals(recommendations.size(), 3);
    }

    private KieSession setGlobals(KieSession kieSession, Reader reader) {
        kieSession.setGlobal("highRatingPoints", 10L);
        kieSession.setGlobal("averageRatingPoints", 3L);
        kieSession.setGlobal("lowRatingPoints", 2L);
        kieSession.setGlobal("readPoints", 2L);
        kieSession.setGlobal("readingListPoints", 15L);
        kieSession.setGlobal("recommendationThreshold", 10L);
        kieSession.setGlobal("loggedReader", reader);

        return kieSession;
    }

    private void createDataWhenUserReadBook(KieSession kieSession) {
        Reader reader1 = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        Author author1 = new Author(
                1L,
                "Author 1",
                "Some description",
                null,
                null,
                5,
                null);

        Author author2 = new Author(
                2L,
                "Author 2",
                "Some description",
                null,
                null,
                0,
                null);

        Category category1 = new Category(1L, "Cat1", "", true);
        Category category2 = new Category(2L, "Cat2", "", true);

        HashSet<Author> authors1 = new HashSet<>();
        authors1.add(author1);

        HashSet<Author> authors2 = new HashSet<>();
        authors1.add(author2);

        Book book1 = new Book(
                1L,
                "Book 1",
                "Some description",
                5,
                1L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book2 = new Book(
                2L,
                "Book 2",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book3 = new Book(
                3L,
                "Book 3",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book4 = new Book(
                4L,
                "Book 4",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors1
        );

        Book book5 = new Book(
                5L,
                "Book 5",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors2
        );

        Review review = new Review(1L, "", 5, book1, reader1, null);

        ReadingProgress readingProgress = new ReadingProgress(1L, book1, reader1, 290L, null);
        ReadingProgress readingProgress2 = new ReadingProgress(2L, book2, reader1, 290L, null);

        kieSession.insert(review);

        kieSession.insert(readingProgress);
        kieSession.insert(readingProgress2);

        kieSession.insert(author1);

        kieSession.insert(book3);
        kieSession.insert(book4);
        kieSession.insert(book5);
    }

    private void createDataWhenUserUnReadBook(KieSession kieSession) {
        Reader reader1 = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        Author author1 = new Author(
                1L,
                "Author 1",
                "Some description",
                null,
                null,
                5,
                null);

        Author author2 = new Author(
                2L,
                "Author 2",
                "Some description",
                null,
                null,
                0,
                null);

        Category category1 = new Category(1L, "Cat1", "", true);
        Category category2 = new Category(2L, "Cat2", "", true);

        HashSet<Author> authors1 = new HashSet<>();
        authors1.add(author1);

        HashSet<Author> authors2 = new HashSet<>();
        authors1.add(author2);

        Book book1 = new Book(
                1L,
                "Book 1",
                "Some description",
                5,
                1L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book2 = new Book(
                2L,
                "Book 2",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book3 = new Book(
                3L,
                "Book 3",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book4 = new Book(
                4L,
                "Book 4",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors1
        );

        Book book5 = new Book(
                5L,
                "Book 5",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors2
        );

        Review review = new Review(1L, "", 5, book1, reader1, null);

        ReadingProgress readingProgress = new ReadingProgress(1L, book1, reader1, 290L, null);
        ReadingProgress readingProgress2 = new ReadingProgress(2L, book2, reader1, 10L, Instant.parse("1980-04-09T15:30:45.123Z"));

        kieSession.insert(review);

        kieSession.insert(readingProgress);
        kieSession.insert(readingProgress2);

        kieSession.insert(author1);

        kieSession.insert(book3);
        kieSession.insert(book4);
        kieSession.insert(book5);
    }

    private void createDataWhenUserHaveReadingList(KieSession kieSession) {
        Reader reader1 = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        Author author1 = new Author(
                1L,
                "Author 1",
                "Some description",
                null,
                null,
                5,
                null);

        Author author2 = new Author(
                2L,
                "Author 2",
                "Some description",
                null,
                null,
                0,
                null);

        Category category1 = new Category(1L, "Cat1", "", true);
        Category category2 = new Category(2L, "Cat2", "", true);

        HashSet<Author> authors1 = new HashSet<>();
        authors1.add(author1);

        HashSet<Author> authors2 = new HashSet<>();
        authors1.add(author2);

        Book book1 = new Book(
                1L,
                "Book 1",
                "Some description",
                5,
                1L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book2 = new Book(
                2L,
                "Book 2",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book3 = new Book(
                3L,
                "Book 3",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book4 = new Book(
                4L,
                "Book 4",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors1
        );

        Book book5 = new Book(
                5L,
                "Book 5",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors2
        );

        Review review = new Review(1L, "", 5, book1, reader1, null);

        ReadingProgress readingProgress = new ReadingProgress(1L, book1, reader1, 290L, null);

        ReadingListItem readingListItem1 = new ReadingListItem(1L, book2, reader1);

        kieSession.insert(review);

        kieSession.insert(readingProgress);

        kieSession.insert(readingListItem1);

        kieSession.insert(author1);

        kieSession.insert(book3);
        kieSession.insert(book4);
        kieSession.insert(book5);
    }

    private void createDataWithPositiveReview(KieSession kieSession) {
        Reader reader1 = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        Author author1 = new Author(
                1L,
                "Author 1",
                "Some description",
                null,
                null,
                5,
                null);

        Author author2 = new Author(
                2L,
                "Author 2",
                "Some description",
                null,
                null,
                0,
                null);

        Category category1 = new Category(1L, "Cat1", "", true);
        Category category2 = new Category(2L, "Cat2", "", true);

        HashSet<Author> authors1 = new HashSet<>();
        authors1.add(author1);

        HashSet<Author> authors2 = new HashSet<>();
        authors1.add(author2);

        Book book1 = new Book(
                1L,
                "Book 1",
                "Some description",
                5,
                1L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book2 = new Book(
                2L,
                "Book 2",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book3 = new Book(
                3L,
                "Book 3",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book4 = new Book(
                4L,
                "Book 4",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors1
        );

        Book book5 = new Book(
                5L,
                "Book 5",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors2
        );

        Review review = new Review(1L, "", 5, book1, reader1, null);

        ReadingProgress readingProgress = new ReadingProgress(1L, book1, reader1, 290L, null);

        kieSession.insert(review);

        kieSession.insert(readingProgress);

        kieSession.insert(author1);

        kieSession.insert(book2);
        kieSession.insert(book3);
        kieSession.insert(book4);
        kieSession.insert(book5);
    }

    private void createDataWithNegativeReview(KieSession kieSession) {
        Reader reader1 = new Reader(
                1L,
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        Author author1 = new Author(
                1L,
                "Author 1",
                "Some description",
                null,
                null,
                1,
                null);

        Author author2 = new Author(
                2L,
                "Author 2",
                "Some description",
                null,
                null,
                0,
                null);

        Category category1 = new Category(1L, "Cat1", "", true);
        Category category2 = new Category(2L, "Cat2", "", true);

        HashSet<Author> authors1 = new HashSet<>();
        authors1.add(author1);

        HashSet<Author> authors2 = new HashSet<>();
        authors1.add(author2);

        Book book1 = new Book(
                1L,
                "Book 1",
                "Some description",
                1,
                1L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book2 = new Book(
                2L,
                "Book 2",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book3 = new Book(
                3L,
                "Book 3",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book4 = new Book(
                4L,
                "Book 4",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors1
        );

        Book book5 = new Book(
                5L,
                "Book 5",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors2
        );

        Review review = new Review(1L, "", 1, book1, reader1, null);

        ReadingProgress readingProgress = new ReadingProgress(1L, book1, reader1, 290L, null);

        kieSession.insert(review);

        kieSession.insert(readingProgress);

        kieSession.insert(author1);

        kieSession.insert(book2);
        kieSession.insert(book3);
        kieSession.insert(book4);
        kieSession.insert(book5);
    }

    private void createDataForUserWithoutActivity(KieSession kieSession) {
        Author author1 = new Author(
                1L,
                "Author 1",
                "Some description",
                null,
                null,
                0,
                null);

        Author author2 = new Author(
                2L,
                "Author 2",
                "Some description",
                null,
                null,
                0,
                null);

        Category category1 = new Category(1L, "Cat1", "", true);
        Category category2 = new Category(2L, "Cat2", "", true);

        HashSet<Author> authors1 = new HashSet<>();
        authors1.add(author1);

        HashSet<Author> authors2 = new HashSet<>();
        authors1.add(author2);

        Book book1 = new Book(
                1L,
                "Book 1",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book2 = new Book(
                2L,
                "Book 2",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book3 = new Book(
                3L,
                "Book 3",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category1,
                authors1
        );

        Book book4 = new Book(
                4L,
                "Book 4",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors1
        );

        Book book5 = new Book(
                5L,
                "Book 5",
                "Some description",
                0,
                0L,
                null,
                null,
                BookType.E_BOOK,
                300,
                category2,
                authors2
        );

        kieSession.insert(book1);
        kieSession.insert(book2);
        kieSession.insert(book3);
        kieSession.insert(book4);
        kieSession.insert(book5);
    }
}
