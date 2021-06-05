package com.sbnz.ibar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private UUID id;
    private Integer rating;
    private String content;
    private UUID userId;
    private UUID bookId;
    private Instant timeAdded;
    private String userFirstName;
    private String userLastName;

}
