package com.sbnz.ibar.config;

import com.sbnz.ibar.utils.Utils;
import lombok.AllArgsConstructor;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static com.sbnz.ibar.utils.Utils.LOGIN_SESSION;
import static com.sbnz.ibar.utils.Utils.READING_SESSION;

@Configuration
@EnableTransactionManagement
@EnableAsync
@AllArgsConstructor
public class KieConfig {

    @Bean
    public KieContainer kieContainer() {
        KieServices kieService = KieServices.Factory.get();
        KieContainer kieContainer = kieService.newKieContainer(kieService
                .newReleaseId(Utils.KIE_GROUP_ID, Utils.KIE_ARTIFACT_ID, Utils.KIE_VERSION));
        KieScanner kieScanner = kieService.newKieScanner(kieContainer);
        kieScanner.start(Utils.SCAN_INTERVAL);
        return kieContainer;
    }

    @Bean(name = "loginSession")
    public KieSession loginSession() {
        return kieContainer().newKieSession(LOGIN_SESSION);
    }

    @Bean(name = "readingSession")
    public KieSession readingSession() {
        return kieContainer().newKieSession(READING_SESSION);
    }


}
