package com.sbnz.ibar.drools;

import com.sbnz.ibar.model.Admin;
import com.sbnz.ibar.model.User;
import com.sbnz.ibar.rto.EmailCheckFact;
import com.sbnz.ibar.rto.IpCheckFact;
import com.sbnz.ibar.rto.events.OnLoggedIn;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class LoginTest {

    private KieSession kieSession;

    @Before
    public void setUp() {
        KieServices ks = KieServices.Factory.get();

        KieContainer kc = ks.newKieClasspathContainer();

        kieSession = kc.newKieSession("login-session");
    }

    @Test
    public void testFirstUnsuccessfulLogin() {
        User admin = createUserForLogin();

        createFact(1, admin);

        int numberOfRules = kieSession.fireAllRules();

        assertEquals(0, numberOfRules);
    }

    @Test
    public void test5TimesUnsuccessfullLogin() {
        User admin = createUserForLogin();

        createFact(5, admin);

        int numberOfRules = kieSession.fireAllRules();

        assertEquals(1, numberOfRules);
    }

    @Test
    public void testCheckIsUserBlockedAfter5TimesUnsuccessfulLogin() {
        User admin = createUserForLogin();

        createFact(5, admin);

        kieSession.insert(new EmailCheckFact("123.123.123.123", false));

        int numberOfRules = kieSession.fireAllRules();

        assertEquals(2, numberOfRules);
    }

    @Test
    public void testCheckIsUserBlockedWhenDidNotFailLogin() {
        User admin = createUserForLogin();

        kieSession.insert(new EmailCheckFact("123.123.123.123", false));

        int numberOfRules = kieSession.fireAllRules();

        assertEquals(1, numberOfRules);
    }

    @Test
    public void test10TimesUnsuccessfulLogin() {
        User admin = createUserForLogin();

        createFact(10, admin);

        int numberOfRules = kieSession.fireAllRules();

        assertEquals(2, numberOfRules);
    }

    @Test
    public void testCheckIsIpAddressBlockedAfter10TimesUnsuccessfulLogin() {
        User admin = createUserForLogin();

        createFact(10, admin);

        kieSession.insert(new IpCheckFact("123.123.123.123", false));

        int numberOfRules = kieSession.fireAllRules();

        assertEquals(3, numberOfRules);
    }

    @Test
    public void testCheckIsUsersIoAddressBlockedWhenDidnotFailLogin() {
        kieSession.insert(new IpCheckFact("123.123.123.123", false));

        int numberOfRules = kieSession.fireAllRules();

        assertEquals(1, numberOfRules);
    }

    private void createFact(int numberOfFacts, User user) {
        for (int i = 0; i < numberOfFacts; i++) {
            kieSession.insert(new OnLoggedIn(new Date(), user, "123.123.123.123", false));
        }
    }

    private User createUserForLogin() {
        return new Admin(
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
}
