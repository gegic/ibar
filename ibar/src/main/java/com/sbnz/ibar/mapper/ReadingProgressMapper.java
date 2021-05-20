package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.ReadingProgressDto;
import com.sbnz.ibar.model.ReadingProgress;
import org.springframework.stereotype.Component;

@Component
public class ReadingProgressMapper {
    public ReadingProgressDto toDto(ReadingProgress entity) {
        ReadingProgressDto dto = new ReadingProgressDto();
        dto.setId(entity.getId());
        dto.setBookId(entity.getBook().getId());
        dto.setReaderId(entity.getReader().getId());
        dto.setProgress(entity.getProgress());
        dto.setPercentage(entity.getPercentage());
        dto.setRead(entity.isRead());
        return dto;
    }
}
