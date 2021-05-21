package com.sbnz.ibar.rto;

import com.sbnz.ibar.model.Author;
import com.sbnz.ibar.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFact {
    private Category category;
    private long numRead;
    private long numUnread;
    private long numWishList;
    private long numReviews;
    private float averageRating;
}
