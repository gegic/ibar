package com.sbnz.ibar.drools;

import com.sbnz.ibar.model.*;
import com.sbnz.ibar.repositories.UserRepository;
import com.sbnz.ibar.rto.RankCheckFact;
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
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    @Autowired
    private Factory factory;


    @Before
    public void setup(){
        userRepository = mock(UserRepository.class);
        ranksSession.setGlobal("userRepository", userRepository);
        ranksSession.setGlobal("resultMap", new HashMap<>());
    }

    @Test
    public void testRank() {

        Reader r = factory.createReader();
        when(userRepository.save(Mockito.any(Reader.class))).thenAnswer(i -> i.<Reader>getArgument(0));
        r = userRepository.save(r);
        Category c = new Category(UUID.randomUUID(), "Kategorija");
        Rank rank0 = factory.createRank("Rank0", 0, null);
        Rank rank1 = factory.createRank("Rank1", 10, rank0);
        Rank rank2 = factory.createRank("Rank2", 13, rank1);
        Rank rank3 = factory.createRank("Rank3", 15, rank2);

        r.setRank(rank0);

        ranksSession.insert(rank0);
        ranksSession.insert(rank1);
        ranksSession.insert(rank2);
        ranksSession.insert(rank3);

        Plan p = factory.createPlan(c);
        OnSubscribed onSubscribed1 = new OnSubscribed(r, p);
        ranksSession.insert(onSubscribed1);
        ranksSession.fireAllRules();

        assertEquals(3.3, r.getPoints(), 0.1);
        assertEquals(rank0, r.getRank());

        OnSubscribed onSubscribed2 = new OnSubscribed(r, p);
        onSubscribed2.setTimestamp(Instant.now().plus(4, ChronoUnit.DAYS).toEpochMilli());
        ranksSession.insert(onSubscribed2);
        ranksSession.fireAllRules();

        assertEquals(5.3, r.getPoints(), 0.1);
        assertEquals(rank3, r.getRank());

    }

    @Test
    public void testCheckRank() {
        Reader r = factory.createReader();

        Rank rank0 = factory.createRank("Rank0", 0, null);
        Rank rank1 = factory.createRank("Rank1", 10, rank0);
        Rank rank2 = factory.createRank("Rank2", 13, rank1);
        Rank rank3 = factory.createRank("Rank3", 15, rank2);

        ranksSession.insert(rank0);
        ranksSession.insert(rank1);
        ranksSession.insert(rank2);
        ranksSession.insert(rank3);
        r.setRank(rank3);
        RankCheckFact rcf = new RankCheckFact(r, rank1, false);
        ranksSession.insert(rcf);
        ranksSession.fireAllRules();

        assertTrue(rcf.isHigher());
    }





}
