package com.sbnz.ibar.drools;

import com.sbnz.ibar.model.*;
import com.sbnz.ibar.model.enums.BookType;
import com.sbnz.ibar.repositories.RankRepository;
import com.sbnz.ibar.repositories.UserRepository;
import com.sbnz.ibar.rto.RankCheckFact;
import com.sbnz.ibar.rto.events.OnReview;
import com.sbnz.ibar.rto.events.OnSubscribed;
import org.drools.core.event.DebugAgendaEventListener;
import org.drools.core.event.DebugProcessEventListener;
import org.drools.core.event.DefaultRuleRuntimeEventListener;
import org.drools.core.time.impl.JDKTimerService;
import org.drools.core.time.impl.JDKTimerServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.kie.api.time.SessionClock;
import org.kie.api.time.SessionPseudoClock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class RanksTest {
    @Autowired
    @Qualifier("ranksSession")
    private KieSession ranksSession;

    private UserRepository userRepository;


    @Before
    public void setup(){
        userRepository = mock(UserRepository.class);
        ranksSession.setGlobal("userRepository", userRepository);
        ranksSession.setGlobal("resultMap", new HashMap<>());
    }

    @Test
    public void testRank() {

        Reader r = createReader();
        when(userRepository.save(Mockito.any(Reader.class))).thenAnswer(i -> i.<Reader>getArgument(0));
        r = userRepository.save(r);
        Category c = createCategory();
        Rank rank0 = createRank("Rank0", 0, null);
        Rank rank1 = createRank("Rank1", 10, rank0);
        Rank rank2 = createRank("Rank2", 13, rank1);
        Rank rank3 = createRank("Rank3", 15, rank2);

        r.setRank(rank0);

        ranksSession.insert(rank0);
        ranksSession.insert(rank1);
        ranksSession.insert(rank2);
        ranksSession.insert(rank3);

        Plan p = createPlan(c);
        OnSubscribed onSubscribed1 = new OnSubscribed(r, p);
        ranksSession.insert(onSubscribed1);
        ranksSession.fireAllRules();

        assertEquals(1, r.getPoints(), 0.1);
        assertEquals(rank0, r.getRank());

        OnSubscribed onSubscribed2 = new OnSubscribed(r, p);
        onSubscribed2.setTimestamp(Instant.now().plus(4, ChronoUnit.DAYS).toEpochMilli());
        ranksSession.insert(onSubscribed2);
        ranksSession.fireAllRules();

        assertEquals(7, r.getPoints(), 0.1);
        assertEquals(rank1, r.getRank());

        OnSubscribed onSubscribed3 = new OnSubscribed(r, p);
        onSubscribed3.setTimestamp(Instant.now().plus(5, ChronoUnit.DAYS).toEpochMilli());
        ranksSession.insert(onSubscribed3);
        ranksSession.fireAllRules();

        assertEquals(13, r.getPoints(), 0.1);
        assertEquals(rank2, r.getRank());

        RankCheckFact rcf = new RankCheckFact(r, rank1, false);
        ranksSession.insert(rcf);
        ranksSession.fireAllRules();

        assertTrue(rcf.isHigher());
    }

    private Rank createRank(String name, long points, Rank previousRank) {
        Rank r = new Rank();
        r.setId(UUID.randomUUID());
        r.setName(name);
        r.setPoints(points);
        if (previousRank != null) {
            previousRank.setHigherRank(r);
        }
        return r;
    }
    private Reader createReader() {
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

    private Plan createPlan(Category c) {
        return new Plan(
                UUID.randomUUID(),
                "Plan",
                100,
                Set.of(c),
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
