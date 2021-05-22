package com.sbnz.ibar.rto;

import com.sbnz.ibar.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFact {
    private Category category;
    private long numRead = 0;
    private long numUnread = 0;
    private long numReadingList = 0;
    private long numReviews = 0;
    private double averageRating = 0;
}
