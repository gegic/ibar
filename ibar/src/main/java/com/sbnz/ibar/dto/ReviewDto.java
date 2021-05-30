package com.sbnz.ibar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private Integer rating;
    private String content;
    private Long userId;
    private Long bookId;
    private Instant timeAdded;
    private String userFirstName;
    private String userLastName;

}
