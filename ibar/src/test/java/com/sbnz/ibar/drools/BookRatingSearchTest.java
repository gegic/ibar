package com.sbnz.ibar.drools;

import com.sbnz.ibar.dto.RatingIntervalDto;
import com.sbnz.ibar.model.Author;
import com.sbnz.ibar.model.Book;
import com.sbnz.ibar.model.Category;
import com.sbnz.ibar.utils.Factory;
import org.drools.template.ObjectDataCompiler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class BookRatingSearchTest {

    @Autowired
    private Factory factory;

    @Test
    public void getAuthorsByRatingFrom1To2() throws FileNotFoundException {
        ArrayList<Book> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules/templates/bookRatingSearch.drt");

        List<RatingIntervalDto> data = new ArrayList<>();

        data.add(new RatingIntervalDto(1, 2));

        ObjectDataCompiler converter = new ObjectDataCompiler();

        String drl = converter.compile(data, template);

        KieSession kieSession = createKieSessionFromDRL(drl);

        List<Author> authors = factory.createAuthors();
        List<Book> books = factory.createBooks(authors);

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

        List<Author> authors = factory.createAuthors();
        List<Book> books = factory.createBooks(authors);

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

        List<Author> authors = factory.createAuthors();
        List<Book> books = factory.createBooks(authors);

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

}
