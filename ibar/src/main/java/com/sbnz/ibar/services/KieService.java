package com.sbnz.ibar.services;

import lombok.AllArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KieService {
    private final KieContainer kieContainer;

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
}
