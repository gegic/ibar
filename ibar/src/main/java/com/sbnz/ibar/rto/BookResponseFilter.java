package com.sbnz.ibar.rto;

import org.drools.core.ObjectFilter;
import org.springframework.stereotype.Component;

@Component
public class BookResponseFilter implements ObjectFilter {
    @Override
    public boolean accept(Object o) {
        return o instanceof BookResponse;
    }
}
