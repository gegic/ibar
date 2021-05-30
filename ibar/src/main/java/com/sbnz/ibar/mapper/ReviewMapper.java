package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.ReviewDto;
import com.sbnz.ibar.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDto toDto(Review entity) {
        if (entity == null) {
            return null;
        }
        ReviewDto dto = new ReviewDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setRating(entity.getRating());
        dto.setTimeAdded(entity.getTimeAdded());
        dto.setUserFirstName(entity.getReader().getFirstName());
        dto.setUserLastName(entity.getReader().getLastName());
        return dto;
    }

}
