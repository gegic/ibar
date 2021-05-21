package com.sbnz.ibar.rto;

import com.sbnz.ibar.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationPoints {
    private Book book;
    private long points;
}
