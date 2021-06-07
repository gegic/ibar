package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.ReadingProgressDto;
import com.sbnz.ibar.model.ReadingProgress;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class ReadingProgressMapper {
    public ReadingProgressDto toDto(ReadingProgress entity) {
        ReadingProgressDto dto = new ReadingProgressDto();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto.setBookId(entity.getBook().getId());
        dto.setReaderId(entity.getReader().getId());
        dto.setProgress(entity.getProgress());
        dto.setPercentage(entity.getPercentage());
        dto.setLastOpened(entity.getLastOpened());
        dto.getLastOpened().plus(7, ChronoUnit.DAYS);
        return dto;
    }
}
