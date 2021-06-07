package com.sbnz.ibar.drools;

import com.sbnz.ibar.dto.RatingIntervalDto;
import com.sbnz.ibar.model.Author;
import org.drools.template.ObjectDataCompiler;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AuthorRatingSearchTest {

    @Test
    public void getAuthorsByRatingFrom1To2() throws FileNotFoundException {
        ArrayList<Author> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules/templates/authorRatingSearch.drt");

        List<RatingIntervalDto> data = new ArrayList<>();

        data.add(new RatingIntervalDto(1, 2));

        ObjectDataCompiler converter = new ObjectDataCompiler();

        String drl = converter.compile(data, template);

        KieSession kieSession = createKieSessionFromDRL(drl);

        List<Author> authors = createAuthors();

        for (Author author : authors) {
            kieSession.insert(author);
        }

        kieSession.setGlobal("result", result);

        kieSession.fireAllRules();

        kieSession.dispose();

        assertEquals(1, result.size());
    }

    @Test
    public void getAuthorsByRatingFrom1To5() throws FileNotFoundException {
        ArrayList<Author> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules/templates/authorRatingSearch.drt");

        List<RatingIntervalDto> data = new ArrayList<>();

        data.add(new RatingIntervalDto(1, 5));

        ObjectDataCompiler converter = new ObjectDataCompiler();

        String drl = converter.compile(data, template);

        KieSession kieSession = createKieSessionFromDRL(drl);

        List<Author> authors = createAuthors();

        for (Author author : authors) {
            kieSession.insert(author);
        }

        kieSession.setGlobal("result", result);

        kieSession.fireAllRules();

        kieSession.dispose();

        assertEquals(2, result.size());
    }

    @Test
    public void getAuthorsByRatingFrom3To5() throws FileNotFoundException {
        ArrayList<Author> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules/templates/authorRatingSearch.drt");

        List<RatingIntervalDto> data = new ArrayList<>();

        data.add(new RatingIntervalDto(3, 5));

        ObjectDataCompiler converter = new ObjectDataCompiler();

        String drl = converter.compile(data, template);

        KieSession kieSession = createKieSessionFromDRL(drl);

        List<Author> authors = createAuthors();

        for (Author author : authors) {
            kieSession.insert(author);
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
                "Author 1",
                "Some description",
                null,
                null,
                3.8,
                null));

        authors.add(new Author(
                UUID.randomUUID(),
                "Author 1",
                "Some description",
                null,
                null,
                1.8,
                null));

        return authors;
    }
}
