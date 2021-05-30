package com.sbnz.ibar;

import com.sbnz.ibar.drools.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthorRatingSearchTest.class,
        BookRatingSearchTest.class,
        BookAuthorsNameSearchTest.class,

        LoginTest.class,

        BookRecommendationTest.class
})
public class TestAll {

}
