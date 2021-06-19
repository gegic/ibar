package com.sbnz.ibar.utils;

import com.sbnz.ibar.model.*;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class Factory {
    public List<Author> createAuthors() {
        List<Author> authors = new ArrayList<>();

        authors.add(new Author(
                UUID.randomUUID(),
                "Author 1",
                "Some description",
                null,
                null,
                3.8,
                null));

        authors.add(new Author(
                UUID.randomUUID(),
                "Author 12",
                "Some description",
                null,
                null,
                1.8,
                null));

        return authors;
    }

    public List<Book> createBooks(List<Author> authors) {
        List<Book> books = new ArrayList<>();

        Category category = new Category(UUID.randomUUID(), "Category 1");
        Author author1 = authors.get(0);
        Author author2 = authors.get(1);

        Set<Author> authors1 = new HashSet<Author>();
        authors1.add(author1);

        Set<Author> authors2 = new HashSet<Author>();
        authors2.add(author1);
        authors2.add(author2);

        books.add(new Book(
                UUID.randomUUID(),
                "Book 1",
                "Some description",
                3.6,
                0L,
                null,
                null,
                300,
                category,
                authors1
        ));

        books.add(new Book(
                UUID.randomUUID(),
                "Book 2",
                "Some description",
                1.8,
                0L,
                null,
                null,
                300,
                category,
                authors2
        ));

        return books;
    }

    public Book createBook(Category category, Set<Author> authors) {
        return new Book(
                UUID.randomUUID(),
                "Book 1",
                "Some description",
                1,
                1,
                null,
                null,
                300,
                category,
                authors
        );
    }

    public void createDataWithNegativeReview(KieSession kieSession) {
        Reader reader1 = new Reader(
                UUID.randomUUID(),
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );

        Author author1 = new Author(
                UUID.randomUUID(),
                "Author 1",
                "Some description",
                null,
                null,
                1,
                null);

        Author author2 = new Author(
                UUID.randomUUID(),
                "Author 2",
                "Some description",
                null,
                null,
                0,
                null);

        Category category1 = new Category(UUID.randomUUID(), "Cat1");
        Category category2 = new Category(UUID.randomUUID(), "Cat2");

        HashSet<Author> authors1 = new HashSet<>();
        authors1.add(author1);

        HashSet<Author> authors2 = new HashSet<>();
        authors1.add(author2);

        Book book1 = new Book(
                UUID.randomUUID(),
                "Book 1",
                "Some description",
                1,
                1,
                null,
                null,
                300,
                category1,
                authors1
        );

        Book book2 = new Book(
                UUID.randomUUID(),
                "Book 2",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category1,
                authors1
        );

        Book book3 = new Book(
                UUID.randomUUID(),
                "Book 3",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category1,
                authors1
        );

        Book book4 = new Book(
                UUID.randomUUID(),
                "Book 4",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category2,
                authors1
        );

        Book book5 = new Book(
                UUID.randomUUID(),
                "Book 5",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category2,
                authors2
        );

        Review review = new Review(UUID.randomUUID(), "", 1, book1, reader1, null);

        ReadingProgress readingProgress = new ReadingProgress(UUID.randomUUID(), book1, reader1, 290L, null);

        kieSession.insert(review);

        kieSession.insert(readingProgress);

        kieSession.insert(author1);

        kieSession.insert(book2);
        kieSession.insert(book3);
        kieSession.insert(book4);
        kieSession.insert(book5);
    }

    public void createDataForUserWithoutActivity(KieSession kieSession) {
        Author author1 = new Author(
                UUID.randomUUID(),
                "Author 1",
                "Some description",
                null,
                null,
                0,
                null);

        Author author2 = new Author(
                UUID.randomUUID(),
                "Author 2",
                "Some description",
                null,
                null,
                0,
                null);

        Category category1 = new Category(UUID.randomUUID(), "Cat1");
        Category category2 = new Category(UUID.randomUUID(), "Cat2");

        HashSet<Author> authors1 = new HashSet<>();
        authors1.add(author1);

        HashSet<Author> authors2 = new HashSet<>();
        authors1.add(author2);

        Book book1 = new Book(
                UUID.randomUUID(),
                "Book 1",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category1,
                authors1
        );

        Book book2 = new Book(
                UUID.randomUUID(),
                "Book 2",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category1,
                authors1
        );

        Book book3 = new Book(
                UUID.randomUUID(),
                "Book 3",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category1,
                authors1
        );

        Book book4 = new Book(
                UUID.randomUUID(),
                "Book 4",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category2,
                authors1
        );

        Book book5 = new Book(
                UUID.randomUUID(),
                "Book 5",
                "Some description",
                0,
                0L,
                null,
                null,
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

    public void createDataWithPositiveReview(KieSession kieSession, Reader reader1) {
        Author author1 = new Author(
                UUID.randomUUID(),
                "Author 1",
                "Some description",
                null,
                null,
                5,
                null);

        Author author2 = new Author(
                UUID.randomUUID(),
                "Author 2",
                "Some description",
                null,
                null,
                0,
                null);

        Category category1 = new Category(UUID.randomUUID(), "Cat1");
        Category category2 = new Category(UUID.randomUUID(), "Cat2");

        HashSet<Author> authors1 = new HashSet<>();
        authors1.add(author1);

        HashSet<Author> authors2 = new HashSet<>();
        authors1.add(author2);

        Book book1 = new Book(
                UUID.randomUUID(),
                "Book 1",
                "Some description",
                5,
                1,
                null,
                null,
                300,
                category1,
                authors1
        );

        Book book2 = new Book(
                UUID.randomUUID(),
                "Book 2",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category1,
                authors1
        );

        Book book3 = new Book(
                UUID.randomUUID(),
                "Book 3",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category1,
                authors1
        );

        Book book4 = new Book(
                UUID.randomUUID(),
                "Book 4",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category2,
                authors1
        );

        Book book5 = new Book(
                UUID.randomUUID(),
                "Book 5",
                "Some description",
                0,
                0L,
                null,
                null,
                300,
                category2,
                authors2
        );

        Review review = new Review(UUID.randomUUID(), "", 5, book1, reader1, null);

        ReadingProgress readingProgress = new ReadingProgress(UUID.randomUUID(), book1, reader1, 290L, null);

        kieSession.insert(review);

        kieSession.insert(readingProgress);

        kieSession.insert(author1);

        kieSession.insert(book1);
        kieSession.insert(book2);
        kieSession.insert(book3);
        kieSession.insert(book4);
        kieSession.insert(book5);
    }

    public Rank createRank(String name, long points, Rank previousRank) {
        Rank r = new Rank();
        r.setId(UUID.randomUUID());
        r.setName(name);
        r.setPoints(points);
        if (previousRank != null) {
            previousRank.setHigherRank(r);
        }
        return r;
    }

    public Reader createReader() {
        Reader r = new Reader(
                UUID.randomUUID(),
                "email@gmail.com",
                "password123",
                "Milan",
                "Marinkovic",
                0L,
                new ArrayList<>(),
                true
        );
        r.setMale(true);
        r.setAge(22);
        return r;
    }

    public Plan createPlan(Category c) {
        return new Plan(
                UUID.randomUUID(),
                "Plan",
                10,
                Set.of(c),
                null,
                "opis",
                10L
        );
    }

    public Review createReview(int rating, Book book, Reader reader) {
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
