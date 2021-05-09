package com.ktsnwt.project.team9.config;

import com.ktsnwt.project.team9.utils.Constants;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAsync
public class KieConfig {

    @Bean
    public KieContainer kieContainer() {
        KieServices kieService = KieServices.Factory.get();
        KieContainer kieContainer = kieService.newKieContainer(kieService
                .newReleaseId(Constants.KIE_GROUP_ID, Constants.KIE_ARTIFACT_ID, Constants.KIE_VERSION));
        KieScanner kieScanner = kieService.newKieScanner(kieContainer);
        kieScanner.start(Constants.SCAN_INTERVAL);
        return kieContainer;
    }

}
