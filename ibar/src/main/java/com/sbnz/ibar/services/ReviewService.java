package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.ReviewDto;
import com.sbnz.ibar.dto.ReviewNumbersDto;
import com.sbnz.ibar.mapper.ReviewMapper;
import com.sbnz.ibar.model.User;
import com.sbnz.ibar.repositories.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public Page<ReviewDto> getReviews(long bookId, Pageable p) {
        return this.reviewRepository.getAllByBookId(bookId, p).map(reviewMapper::toDto);
    }

    public List<ReviewNumbersDto> findAndGroupByRating(long culturalOfferingId) {
        return this.reviewRepository.findAndGroupByRating(culturalOfferingId);
    }

    public ReviewDto userReviewByBook(long bookId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return reviewRepository.findByBookIdAndReaderId(bookId, user.getId()).map(reviewMapper::toDto).orElse(null);
    }

}
