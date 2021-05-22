package com.sbnz.ibar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ReadingProgressDto {
    private long id;
    private long bookId;
    private long readerId;
    private long progress;
    private double percentage;
    private Instant lastOpened;
}
