package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.ReviewDto;
import com.sbnz.ibar.dto.ReviewNumbersDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.services.ReviewService;
import com.sbnz.ibar.utils.PageableExtractor;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(path = "/book/{bookId}")
    ResponseEntity<Page<ReviewDto>> getReviews(@PathVariable UUID bookId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "id,desc") String[] sort) {

        Pageable p = PageableExtractor.extract(page, size, sort);
        return ResponseEntity.ok(reviewService.getReviews(bookId, p));
    }

    @GetMapping(path="/by-rating/book/{bookId}")
    public ResponseEntity<List<ReviewNumbersDto>> groupByRating(@PathVariable UUID bookId) {
        List<ReviewNumbersDto> reviewNumbers = reviewService.findAndGroupByRating(bookId);
        return ResponseEntity.ok(reviewNumbers);
    }

    @GetMapping(path="/user/book/{bookId}")
    public ResponseEntity<ReviewDto> userReview(@PathVariable UUID bookId) throws EntityDoesNotExistException {
        return ResponseEntity.ok(reviewService.userReviewByBook(bookId));
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto)
            throws EntityAlreadyExistsException, EntityDoesNotExistException {
        return ResponseEntity.ok(reviewService.create(reviewDto));
    }

    @PutMapping
    public ResponseEntity<ReviewDto> updateReview(@RequestBody ReviewDto reviewDto)
            throws EntityDoesNotExistException {
        return ResponseEntity.ok(reviewService.update(reviewDto));
    }
}
