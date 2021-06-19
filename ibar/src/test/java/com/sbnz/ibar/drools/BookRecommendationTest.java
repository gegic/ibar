package com.sbnz.ibar.drools;

import com.sbnz.ibar.model.*;
import com.sbnz.ibar.repositories.UserRepository;
import com.sbnz.ibar.rto.BookResponse;
import com.sbnz.ibar.rto.BookResponseFilter;
import com.sbnz.ibar.services.KieService;
import com.sbnz.ibar.utils.Factory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.sbnz.ibar.utils.Utils.BOOKS_SESSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class BookRecommendationTest {

    @Autowired
    private Factory factory;
    @Autowired
    private KieContainer kieContainer;
    @Autowired
    private UserRepository userRepository;

    private KieSession booksSession;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        booksSession = kieContainer.newKieSession(BOOKS_SESSION);
        booksSession.setGlobal("highRatingPoints", 10L);
        booksSession.setGlobal("averageRatingPoints", 3L);
        booksSession.setGlobal("lowRatingPoints", 2L);
        booksSession.setGlobal("readPoints", 2L);
        booksSession.setGlobal("readingListPoints", 15L);
        booksSession.setGlobal("recommendationThreshold", 4L);

    }

    @Test
    public void testNoMoviesRecommended() {
        List<BookResponse> recommendations = new ArrayList<>();

        Reader reader = new Reader(
                UUID.randomUUID(),
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        booksSession.setGlobal("loggedReader", reader);

        factory.createDataForUserWithoutActivity(booksSession);

        booksSession.insert(recommendations);

        booksSession.fireAllRules();

        assertEquals(5, recommendations.size());
    }

    @Test
    public void testWhenUserPositiveRateBook() {
        List<BookResponse> recommendations = new ArrayList<>();

        Reader reader = new Reader(
                UUID.randomUUID(),
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        booksSession.setGlobal("loggedReader", reader);

        factory.createDataWithPositiveReview(booksSession, reader);

        booksSession.insert(recommendations);

        booksSession.fireAllRules();

        recommendations = recommendations.stream().limit(4).collect(Collectors.toList());;

        List<BookResponse> br52 = recommendations.stream().filter(br -> br.getPoints() == 52).collect(Collectors.toList());
        List<BookResponse> br0 = recommendations.stream().filter(br -> br.getPoints() == 0).collect(Collectors.toList());
        assertEquals(4, recommendations.size());
        assertEquals(recommendations.size(), br52.size() + br0.size());
    }
//
//    @Test
//    public void testWhenUserNegativeRateBook() {
//        List<BookResponse> recommendations;
//
//        Reader reader = new Reader(
//                UUID.randomUUID(),
//                "email@gmail.com",
//                "password123",
//                "Milan",
//                "Marinkovic",
//                0L,
//                new ArrayList<>(),
//                true
//        );
//
//        setGlobals(kieSession, reader);
//
//        createDataWithNegativeReview(kieSession);
//
//        kieSession.fireAllRules();
//
//        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);
//
//        recommendations = bookResponses.stream().map(qm -> (BookResponse) qm)
//                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(10).collect(Collectors.toList());
//
//        kieSession.dispose();
//
////        for (BookResponse bookResponse : recommendations) {
////            if (bookResponse.getBook().getId() != 5)
////                assertTrue(bookResponse.getPoints() == 2);
////            else {
////                assertTrue(bookResponse.getPoints() == 0);
////            }
////        }
//
//        assertEquals(recommendations.size(), 4);
//    }
//
//    @Test
//    public void testWhenUserReadBook() {
//        List<BookResponse> recommendations;
//
//        Reader reader = new Reader(
//                UUID.randomUUID(),
//                "email@gmail.com",
//                "password123",
//                "Milan",
//                "Marinkovic",
//                0L,
//                new ArrayList<>(),
//                true
//        );
//
//        setGlobals(kieSession, reader);
//
//        createDataWhenUserReadBook(kieSession);
//
//        kieSession.fireAllRules();
//
//        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);
//
//        recommendations = bookResponses.stream().map(qm -> (BookResponse) qm)
//                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(10).collect(Collectors.toList());
//
//        kieSession.dispose();
//
////        for (BookResponse bookResponse : recommendations) {
////            if (bookResponse.getBook().getId() != 5)
////                assertTrue(bookResponse.getPoints() == 54);
////            else {
////                assertTrue(bookResponse.getPoints() == 0);
////            }
////        }
//
//        assertEquals(recommendations.size(), 3);
//    }
//
//    @Test
//    public void testWhenUserUnReadBook() {
//        List<BookResponse> recommendations;
//
//        Reader reader = new Reader(
//                UUID.randomUUID(),
//                "email@gmail.com",
//                "password123",
//                "Milan",
//                "Marinkovic",
//                0L,
//                new ArrayList<>(),
//                true
//        );
//
//        setGlobals(kieSession, reader);
//
//        createDataWhenUserUnReadBook(kieSession);
//
//        kieSession.fireAllRules();
//
//        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);
//
//        recommendations = bookResponses.stream().map(qm -> (BookResponse) qm)
//                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(10).collect(Collectors.toList());
//
//        kieSession.dispose();
//
////        for (BookResponse bookResponse : recommendations) {
////            if (bookResponse.getBook().getId() != 5)
////                assertTrue(bookResponse.getPoints() == 50);
////            else {
////                assertTrue(bookResponse.getPoints() == 0);
////            }
////        }
//
//        assertEquals(recommendations.size(), 3);
//    }
//
//    @Test
//    public void testWhenUserHaveReadingList() {
//        List<BookResponse> recommendations;
//
//        Reader reader = new Reader(
//                UUID.randomUUID(),
//                "email@gmail.com",
//                "password123",
//                "Milan",
//                "Marinkovic",
//                0L,
//                new ArrayList<>(),
//                true
//        );
//
//        setGlobals(kieSession, reader);
//
//        createDataWhenUserHaveReadingList(kieSession);
//
//        kieSession.fireAllRules();
//
//        Collection<?> bookResponses = kieSession.getObjects(bookResponseFilter);
//
//        recommendations = bookResponses.stream().map(qm -> (BookResponse) qm)
//                .sorted(Comparator.comparingDouble(BookResponse::getPoints)).limit(10).collect(Collectors.toList());
//
//        kieSession.dispose();
////
////        for (BookResponse bookResponse : recommendations) {
////            if (bookResponse.getBook().getId() != 5)
////                assertTrue(bookResponse.getPoints() == 67);
////            else {
////                assertTrue(bookResponse.getPoints() == 0);
////            }
////        }
//
//        assertEquals(recommendations.size(), 3);
//    }
//
//    private void createDataWhenUserReadBook(KieSession kieSession) {
//        Reader reader1 = new Reader(
//                UUID.randomUUID(),
//                "email@gmail.com",
//                "password123",
//                "Milan",
//                "Marinkovic",
//                0L,
//                new ArrayList<>(),
//                true
//        );
//
//        Author author1 = new Author(
//                UUID.randomUUID(),
//                "Author 1",
//                "Some description",
//                null,
//                null,
//                5,
//                null);
//
//        Author author2 = new Author(
//                UUID.randomUUID(),
//                "Author 2",
//                "Some description",
//                null,
//                null,
//                0,
//                null);
//
//        Category category1 = new Category(UUID.randomUUID(), "Cat1");
//        Category category2 = new Category(UUID.randomUUID(), "Cat2");
//
//        HashSet<Author> authors1 = new HashSet<>();
//        authors1.add(author1);
//
//        HashSet<Author> authors2 = new HashSet<>();
//        authors1.add(author2);
//
//        Book book1 = new Book(
//                UUID.randomUUID(),
//                "Book 1",
//                "Some description",
//                5,
//                1L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book2 = new Book(
//                UUID.randomUUID(),
//                "Book 2",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book3 = new Book(
//                UUID.randomUUID(),
//                "Book 3",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book4 = new Book(
//                UUID.randomUUID(),
//                "Book 4",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category2,
//                authors1
//        );
//
//        Book book5 = new Book(
//                UUID.randomUUID(),
//                "Book 5",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category2,
//                authors2
//        );
//
//        Review review = new Review(UUID.randomUUID(), "", 5, book1, reader1, null);
//
//        ReadingProgress readingProgress = new ReadingProgress(UUID.randomUUID(), book1, reader1, 290L, null);
//        ReadingProgress readingProgress2 = new ReadingProgress(UUID.randomUUID(), book2, reader1, 290L, null);
//
//        kieSession.insert(review);
//
//        kieSession.insert(readingProgress);
//        kieSession.insert(readingProgress2);
//
//        kieSession.insert(author1);
//
//        kieSession.insert(book3);
//        kieSession.insert(book4);
//        kieSession.insert(book5);
//    }
//
//    private void createDataWhenUserUnReadBook(KieSession kieSession) {
//        Reader reader1 = new Reader(
//                UUID.randomUUID(),
//                "email@gmail.com",
//                "password123",
//                "Milan",
//                "Marinkovic",
//                0L,
//                new ArrayList<>(),
//                true
//        );
//
//        Author author1 = new Author(
//                UUID.randomUUID(),
//                "Author 1",
//                "Some description",
//                null,
//                null,
//                5,
//                null);
//
//        Author author2 = new Author(
//                UUID.randomUUID(),
//                "Author 2",
//                "Some description",
//                null,
//                null,
//                0,
//                null);
//
//        Category category1 = new Category(UUID.randomUUID(), "Cat1");
//        Category category2 = new Category(UUID.randomUUID(), "Cat2");
//
//        HashSet<Author> authors1 = new HashSet<>();
//        authors1.add(author1);
//
//        HashSet<Author> authors2 = new HashSet<>();
//        authors1.add(author2);
//
//        Book book1 = new Book(
//                UUID.randomUUID(),
//                "Book 1",
//                "Some description",
//                5,
//                1L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book2 = new Book(
//                UUID.randomUUID(),
//                "Book 2",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book3 = new Book(
//                UUID.randomUUID(),
//                "Book 3",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book4 = new Book(
//                UUID.randomUUID(),
//                "Book 4",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category2,
//                authors1
//        );
//
//        Book book5 = new Book(
//                UUID.randomUUID(),
//                "Book 5",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category2,
//                authors2
//        );
//
//        Review review = new Review(UUID.randomUUID(), "", 5, book1, reader1, null);
//
//        ReadingProgress readingProgress = new ReadingProgress(UUID.randomUUID(), book1, reader1, 290L, null);
//        ReadingProgress readingProgress2 = new ReadingProgress(UUID.randomUUID(), book2, reader1, 10L, Instant.parse("1980-04-09T15:30:45.123Z"));
//
//        kieSession.insert(review);
//
//        kieSession.insert(readingProgress);
//        kieSession.insert(readingProgress2);
//
//        kieSession.insert(author1);
//
//        kieSession.insert(book3);
//        kieSession.insert(book4);
//        kieSession.insert(book5);
//    }
//
//    private void createDataWhenUserHaveReadingList(KieSession kieSession) {
//        Reader reader1 = new Reader(
//                UUID.randomUUID(),
//                "email@gmail.com",
//                "password123",
//                "Milan",
//                "Marinkovic",
//                0L,
//                new ArrayList<>(),
//                true
//        );
//
//        Author author1 = new Author(
//                UUID.randomUUID(),
//                "Author 1",
//                "Some description",
//                null,
//                null,
//                5,
//                null);
//
//        Author author2 = new Author(
//                UUID.randomUUID(),
//                "Author 2",
//                "Some description",
//                null,
//                null,
//                0,
//                null);
//
//        Category category1 = new Category(UUID.randomUUID(), "Cat1");
//        Category category2 = new Category(UUID.randomUUID(), "Cat2");
//
//        HashSet<Author> authors1 = new HashSet<>();
//        authors1.add(author1);
//
//        HashSet<Author> authors2 = new HashSet<>();
//        authors1.add(author2);
//
//        Book book1 = new Book(
//                UUID.randomUUID(),
//                "Book 1",
//                "Some description",
//                5,
//                1L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book2 = new Book(
//                UUID.randomUUID(),
//                "Book 2",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book3 = new Book(
//                UUID.randomUUID(),
//                "Book 3",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book4 = new Book(
//                UUID.randomUUID(),
//                "Book 4",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category2,
//                authors1
//        );
//
//        Book book5 = new Book(
//                UUID.randomUUID(),
//                "Book 5",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category2,
//                authors2
//        );
//
//        Review review = new Review(UUID.randomUUID(), "", 5, book1, reader1, null);
//
//        ReadingProgress readingProgress = new ReadingProgress(UUID.randomUUID(), book1, reader1, 290L, null);
//
//        ReadingListItem readingListItem1 = new ReadingListItem(UUID.randomUUID(), book2, reader1);
//
//        kieSession.insert(review);
//
//        kieSession.insert(readingProgress);
//
//        kieSession.insert(readingListItem1);
//
//        kieSession.insert(author1);
//
//        kieSession.insert(book3);
//        kieSession.insert(book4);
//        kieSession.insert(book5);
//    }
//
//    private void createDataWithNegativeReview(KieSession kieSession) {
//        Reader reader1 = new Reader(
//                UUID.randomUUID(),
//                "email@gmail.com",
//                "password123",
//                "Milan",
//                "Marinkovic",
//                0L,
//                new ArrayList<>(),
//                true
//        );
//
//        Author author1 = new Author(
//                UUID.randomUUID(),
//                "Author 1",
//                "Some description",
//                null,
//                null,
//                1,
//                null);
//
//        Author author2 = new Author(
//                UUID.randomUUID(),
//                "Author 2",
//                "Some description",
//                null,
//                null,
//                0,
//                null);
//
//        Category category1 = new Category(UUID.randomUUID(), "Cat1");
//        Category category2 = new Category(UUID.randomUUID(), "Cat2");
//
//        HashSet<Author> authors1 = new HashSet<>();
//        authors1.add(author1);
//
//        HashSet<Author> authors2 = new HashSet<>();
//        authors1.add(author2);
//
//        Book book1 = new Book(
//                UUID.randomUUID(),
//                "Book 1",
//                "Some description",
//                1,
//                1,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book2 = new Book(
//                UUID.randomUUID(),
//                "Book 2",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book3 = new Book(
//                UUID.randomUUID(),
//                "Book 3",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category1,
//                authors1
//        );
//
//        Book book4 = new Book(
//                UUID.randomUUID(),
//                "Book 4",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category2,
//                authors1
//        );
//
//        Book book5 = new Book(
//                UUID.randomUUID(),
//                "Book 5",
//                "Some description",
//                0,
//                0L,
//                null,
//                null,
//                300,
//                category2,
//                authors2
//        );
//
//        Review review = new Review(UUID.randomUUID(), "", 1, book1, reader1, null);
//
//        ReadingProgress readingProgress = new ReadingProgress(UUID.randomUUID(), book1, reader1, 290L, null);
//
//        kieSession.insert(review);
//
//        kieSession.insert(readingProgress);
//
//        kieSession.insert(author1);
//
//        kieSession.insert(book2);
//        kieSession.insert(book3);
//        kieSession.insert(book4);
//        kieSession.insert(book5);
//    }
}
