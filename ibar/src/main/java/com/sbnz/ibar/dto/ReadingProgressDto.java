package com.sbnz.ibar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ReadingProgressDto {
    private UUID id;
    private UUID bookId;
    private UUID readerId;
    private long progress;
    private double percentage;
    private Instant lastOpened;
}
