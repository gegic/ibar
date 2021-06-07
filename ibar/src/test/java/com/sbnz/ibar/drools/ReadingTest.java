package com.sbnz.ibar.drools;

import com.sbnz.ibar.model.*;
import com.sbnz.ibar.model.enums.BookType;
import com.sbnz.ibar.rto.events.OnReview;
import com.sbnz.ibar.rto.events.OnSubscribed;
import org.drools.core.event.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class ReadingTest {
    @Autowired
    @Qualifier("readingSession")
    private KieSession readingSession;

    @Test
    public void testLowReviews() {
//        readingSession.addEventListener(new DefaultRuleRuntimeEventListener());
//        readingSession.addEventListener(new DebugAgendaEventListener());
//        readingSession.addEventListener(new DebugProcessEventListener());
        Reader r = createReader();

        OnSubscribed onSubscribed = new OnSubscribed(r);
        readingSession.insert(onSubscribed);

        Set<Author> a = new HashSet<>();
        a.add(createAuthor());
        Category c = createCategory();
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            books.add(createBook(c, a));
        }
        for (Book book : books) {
            Review review = createReview(1, book, r);
            OnReview onReview = new OnReview(review);
            readingSession.insert(onReview);
            readingSession.fireAllRules();
        }
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
        return new Category(UUID.randomUUID(), "Category", "", true);
    }

    private Book createBook(Category category, Set<Author> authors) {
        return new Book(
                UUID.randomUUID(),
                "Book 1",
                "Some description",
                1,
                1,
                null,
                null,
                BookType.E_BOOK,
                300,
                category,
                authors
        );
    }

    private Review createReview(int rating, Book book, Reader reader) {
        return new Review(
                UUID.randomUUID(),
                "content",
                rating,
                book,
                reader,
                Instant.now()
        );
    }
}
