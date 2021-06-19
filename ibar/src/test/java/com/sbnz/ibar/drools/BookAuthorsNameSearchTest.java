package com.sbnz.ibar.drools;

import com.sbnz.ibar.model.Author;
import com.sbnz.ibar.model.Book;
import com.sbnz.ibar.model.Category;
import com.sbnz.ibar.utils.Factory;
import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.objects.ArrayDataProvider;
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
public class BookAuthorsNameSearchTest {

    @Autowired
    private Factory factory;

    @Test
    public void getBooksByAuthorsName() throws FileNotFoundException {
        ArrayList<Book> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules/templates/bookAuthorsNameSearch.drt");

        DataProviderCompiler converter = new DataProviderCompiler();

        DataProvider dataProvider = new ArrayDataProvider(new String[][]{
                new String[]{"1"}
        });

        String drl = converter.compile(dataProvider, template);

        KieSession kieSession = createKieSessionFromDRL(drl);

        List<Author> authors = factory.createAuthors();
        List<Book> books = factory.createBooks(authors);

        for (Book book : books) {
            kieSession.insert(book);
        }

        kieSession.setGlobal("result", result);

        kieSession.fireAllRules();

        kieSession.dispose();

        assertEquals(3, result.size());
    }

    @Test
    public void getBooksByAuthorsName2() throws FileNotFoundException {
        ArrayList<Book> result = new ArrayList<Book>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules/templates/bookAuthorsNameSearch.drt");

        DataProviderCompiler converter = new DataProviderCompiler();

        DataProvider dataProvider = new ArrayDataProvider(new String[][]{
                new String[]{"2"}
        });

        String drl = converter.compile(dataProvider, template);

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
    public void getBooksByAuthorsName3() throws FileNotFoundException {
        ArrayList<Book> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules/templates/bookAuthorsNameSearch.drt");

        DataProviderCompiler converter = new DataProviderCompiler();

        DataProvider dataProvider = new ArrayDataProvider(new String[][]{
                new String[]{"fgfd"}
        });

        String drl = converter.compile(dataProvider, template);

        KieSession kieSession = createKieSessionFromDRL(drl);

        List<Author> authors = factory.createAuthors();
        List<Book> books = factory.createBooks(authors);

        for (Book book : books) {
            kieSession.insert(book);
        }

        kieSession.setGlobal("result", result);

        kieSession.fireAllRules();

        kieSession.dispose();

        assertEquals(0, result.size());
    }

    private KieSession createKieSessionFromDRL(String drl) {
        KieHelper kieHelper = new KieHelper();

        kieHelper.addContent(drl, ResourceType.DRL);

        return kieHelper.build().newKieSession();
    }


}
