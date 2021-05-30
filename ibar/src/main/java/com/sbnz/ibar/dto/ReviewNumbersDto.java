package com.sbnz.ibar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewNumbersDto {
    private int rating;
    private long numReviews;
}