package com.sbnz.ibar.drools;

import com.sbnz.ibar.model.*;
import com.sbnz.ibar.repositories.UserRepository;
import com.sbnz.ibar.rto.events.OnReview;
import com.sbnz.ibar.rto.events.OnSubscribed;
import com.sbnz.ibar.utils.Factory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class ReadingTest {
    @Autowired
    @Qualifier("readingSession")
    private KieSession readingSession;

    @Autowired
    private Factory factory;

    private UserRepository userRepository;

    @Before
    public void setup(){
        userRepository = mock(UserRepository.class);
        readingSession.setGlobal("userRepository", userRepository);
    }

    @Test
    public void testLowReviews() {
        when(userRepository.save(Mockito.any(Reader.class))).thenAnswer(i -> i.<Reader>getArgument(0));

        Reader r = createReader();
        Category c = createCategory();

        Plan p = createPlan(c);

        OnSubscribed onSubscribed = new OnSubscribed(r, p);
        readingSession.insert(onSubscribed);

        Set<Author> a = new HashSet<>();
        a.add(createAuthor());
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            books.add(factory.createBook(c, a));
        }
        for (Book book : books) {
            Review review = factory.createReview(1, book, r);
            OnReview onReview = new OnReview(review);
            readingSession.insert(onReview);
            readingSession.fireAllRules();
        }

        assertFalse(r.isEnabled());
    }

    @Test
    public void testHighReviews() {
        when(userRepository.save(Mockito.any(Reader.class))).thenAnswer(i -> i.<Reader>getArgument(0));

        Reader r = createReader();
        Category c = createCategory();

        Plan p = createPlan(c);

        OnSubscribed onSubscribed = new OnSubscribed(r, p);
        readingSession.insert(onSubscribed);

        Set<Author> a = new HashSet<>();
        a.add(createAuthor());
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            books.add(factory.createBook(c, a));
        }
        for (Book book : books) {
            Review review = factory.createReview(5, book, r);
            OnReview onReview = new OnReview(review);
            readingSession.insert(onReview);
            readingSession.fireAllRules();
        }

        assertFalse(r.isEnabled());
    }

    @Test
    public void testSevenLowReviews() {
        when(userRepository.save(Mockito.any(Reader.class))).thenAnswer(i -> i.<Reader>getArgument(0));

        Reader r = createReader();
        Category c = createCategory();

        Plan p = createPlan(c);

        OnSubscribed onSubscribed = new OnSubscribed(r, p);
        readingSession.insert(onSubscribed);

        Set<Author> a = new HashSet<>();
        a.add(createAuthor());
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            books.add(factory.createBook(c, a));
        }
        for (Book book : books) {
            Review review = factory.createReview(1, book, r);
            OnReview onReview = new OnReview(review);
            readingSession.insert(onReview);
            readingSession.fireAllRules();
        }

        assertTrue(r.isEnabled());
    }

    @Test
    public void testSevenHighReviews() {
        when(userRepository.save(Mockito.any(Reader.class))).thenAnswer(i -> i.<Reader>getArgument(0));

        Reader r = createReader();
        Category c = createCategory();

        Plan p = createPlan(c);

        OnSubscribed onSubscribed = new OnSubscribed(r, p);
        readingSession.insert(onSubscribed);

        Set<Author> a = new HashSet<>();
        a.add(createAuthor());
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            books.add(factory.createBook(c, a));
        }
        for (Book book : books) {
            Review review = factory.createReview(5, book, r);
            OnReview onReview = new OnReview(review);
            readingSession.insert(onReview);
            readingSession.fireAllRules();
        }

        assertTrue(r.isEnabled());
    }

    private Reader createReader() {
        return new Reader(
                UUID.randomUUID(),
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );
    }

    private Plan createPlan(Category c) {
        return new Plan(
                UUID.randomUUID(),
                "Plan",
                100,
                Set.of(c),
                null,
                "opis",
                10L
        );
    }
    private Author createAuthor() {
        return new Author(
                UUID.randomUUID(),
                "Author",
                "Some description",
                null,
                null,
                1,
                null
        );
    }

    private Category createCategory() {
        return new Category(UUID.randomUUID(), "Category");
    }



}
