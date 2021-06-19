package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.ReadingListItemDto;
import com.sbnz.ibar.model.ReadingListItem;
import org.springframework.stereotype.Component;

@Component
public class ReadingListItemMapper {
    public ReadingListItemDto toDto(ReadingListItem entity) {
        ReadingListItemDto dto = new ReadingListItemDto();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }

        dto.setBookId(entity.getBook().getId());
        dto.setReaderId(entity.getReader().getId());

        return dto;
    }
}
