package com.sbnz.ibar.drools;

import com.sbnz.ibar.model.Book;
import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.repositories.UserRepository;
import com.sbnz.ibar.rto.BookResponseFilter;
import com.sbnz.ibar.services.KieService;
import com.sbnz.ibar.utils.Utils;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;

@AllArgsConstructor
public class BookRecommendationTest {

    private final KieService kieService;
    private final BookResponseFilter bookResponseFilter;
    private final UserRepository userRepository;

    @Test
    public void whenNothingReadAndNoSimilarUsersReturnsNoBooks() {

    }

}
