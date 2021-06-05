package com.sbnz.ibar.drools;

import com.sbnz.ibar.dto.RatingIntervalDto;
import com.sbnz.ibar.model.Author;
import com.sbnz.ibar.model.Book;
import com.sbnz.ibar.model.Category;
import com.sbnz.ibar.model.enums.BookType;
import org.drools.template.ObjectDataCompiler;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class BookRatingSearchTest {

    @Test
    public void getAuthorsByRatingFrom1To2() throws FileNotFoundException {
        ArrayList<Book> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules/templates/bookRatingSearch.drt");

        List<RatingIntervalDto> data = new ArrayList<>();

        data.add(new RatingIntervalDto(1, 2));

        ObjectDataCompiler converter = new ObjectDataCompiler();

        String drl = converter.compile(data, template);

        KieSession kieSession = createKieSessionFromDRL(drl);

        List<Author> authors = createAuthors();
        List<Book> books = createBooks(authors);

        for (Book book : books) {
            kieSession.insert(book);
        }

        kieSession.setGlobal("result", result);

        kieSession.fireAllRules();

        kieSession.dispose();

        assertEquals(1, result.size());
    }

    @Test
    public void getAuthorsByRatingFrom1To5() throws FileNotFoundException {
        ArrayList<Book> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules/templates/bookRatingSearch.drt");

        List<RatingIntervalDto> data = new ArrayList<>();

        data.add(new RatingIntervalDto(1, 5));

        ObjectDataCompiler converter = new ObjectDataCompiler();

        String drl = converter.compile(data, template);

        KieSession kieSession = createKieSessionFromDRL(drl);

        List<Author> authors = createAuthors();
        List<Book> books = createBooks(authors);

        for (Book book : books) {
            kieSession.insert(book);
        }

        kieSession.setGlobal("result", result);

        kieSession.fireAllRules();

        kieSession.dispose();

        assertEquals(2, result.size());
    }

    @Test
    public void getAuthorsByRatingFrom3To5() throws FileNotFoundException {
        ArrayList<Book> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules/templates/bookRatingSearch.drt");

        List<RatingIntervalDto> data = new ArrayList<>();

        data.add(new RatingIntervalDto(3, 5));

        ObjectDataCompiler converter = new ObjectDataCompiler();

        String drl = converter.compile(data, template);

        KieSession kieSession = createKieSessionFromDRL(drl);

        List<Author> authors = createAuthors();
        List<Book> books = createBooks(authors);

        for (Book book : books) {
            kieSession.insert(book);
        }

        kieSession.setGlobal("result", result);

        kieSession.fireAllRules();

        kieSession.dispose();

        assertEquals(1, result.size());
    }

    private KieSession createKieSessionFromDRL(String drl) {
        KieHelper kieHelper = new KieHelper();

        kieHelper.addContent(drl, ResourceType.DRL);

        return kieHelper.build().newKieSession();
    }

    private List<Author> createAuthors() {
        List<Author> authors = new ArrayList<>();

        authors.add(new Author(
                UUID.randomUUID(),
                "Fjodor Mihailovič Dostojevski",
                "Some description",
                null,
                null,
                3.8,
                null));

        authors.add(new Author(
                UUID.randomUUID(),
                "Momo Kapor",
                "Some description",
                null,
                null,
                1.8,
                null));

        return authors;
    }

    private List<Book> createBooks(List<Author> authors) {
        List<Book> books = new ArrayList<>();

        Category category = new Category(UUID.randomUUID(), "Category 1", "Some description", true);
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
                BookType.E_BOOK,
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
                BookType.AUDIO_BOOK,
                300,
                category,
                authors2
        ));

        return books;
    }
}
