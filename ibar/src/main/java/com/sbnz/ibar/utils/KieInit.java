package com.sbnz.ibar.utils;

import com.sbnz.ibar.model.AgeClass;
import com.sbnz.ibar.model.Rank;
import com.sbnz.ibar.repositories.AgeClassRepository;
import com.sbnz.ibar.repositories.RankRepository;
import com.sbnz.ibar.services.KieService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@AllArgsConstructor
public class KieInit {
    private final RankRepository rankRepository;
    private final AgeClassRepository ageClassRepository;
    private final KieService kieService;

    @PostConstruct
    private void initKie() {
        List<Rank> ranks = rankRepository.findAll();
        ranks.forEach(r -> kieService.getRanksSession().insert(r));
        List<AgeClass> ageClasses = ageClassRepository.findAll();
        ageClasses.forEach(ac -> kieService.getClassifySession().insert(ac));
    }
}
