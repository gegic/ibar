package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.RatingIntervalDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.ObjectDataCompiler;
import org.drools.template.objects.ArrayDataProvider;
import org.drools.template.objects.ObjectDataProvider;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

@Service
@AllArgsConstructor
public class KieService {
    private final KieContainer kieContainer;

    @Getter
    @Qualifier("loginSession")
    private final KieSession loginSession;

    @Getter
    @Qualifier("readingSession")
    private final KieSession readingSession;

    @Getter
    @Qualifier("ranksSession")
    private final KieSession ranksSession;

    @Getter
    @Qualifier("classifySession")
    private final KieSession classifySession;

    @Getter
    @Qualifier("booksSession")
    private final KieSession booksSession;

    public KieSession getSession() {
        return this.kieContainer.newKieSession();
    }

    public KieSession getSession(String sessionName, String agendaGroup) {
        KieSession ks = this.kieContainer.newKieSession(sessionName);
        ks.getAgenda().getAgendaGroup(agendaGroup).setFocus();
        return ks;
    }

    public void runSession(KieSession kieSession) {
        kieSession.fireAllRules();
        kieSession.dispose();
        kieSession.destroy();
    }

    public KieSession createKieSessionFromDrl(String drl) {
        KieHelper kieHelper = new KieHelper();

        kieHelper.addContent(drl, ResourceType.DRL);

        return kieHelper.build().newKieSession();
    }

    public KieSession getBookByAuthorsNameSearchSession(String authorsName) throws FileNotFoundException {
        InputStream template = new FileInputStream(ResourceUtils
                .getFile("src\\main\\resources\\rules\\templates\\bookAuthorsNameSearch.drt"));
        DataProviderCompiler converter = new DataProviderCompiler();
        DataProvider dataProvider = new ArrayDataProvider(new String[][]{
                new String[]{authorsName}
        });
        String drl = converter.compile(dataProvider, template);
        return createKieSessionFromDrl(drl);
    }

    public KieSession getBookByRatingSearchSession(RatingIntervalDto ratingIntervalDto) throws FileNotFoundException {
        InputStream template = new FileInputStream(ResourceUtils
                .getFile("src\\main\\resources\\rules\\templates\\bookRatingSearch.drt"));
        ObjectDataCompiler converter = new ObjectDataCompiler();
        String drl = converter.compile(Collections.singletonList(ratingIntervalDto), template);
        return createKieSessionFromDrl(drl);
    }
}
