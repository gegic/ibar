package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.ReviewDto;
import com.sbnz.ibar.dto.ReviewNumbersDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.exceptions.UserNotLoggedInException;
import com.sbnz.ibar.mapper.ReviewMapper;
import com.sbnz.ibar.model.*;
import com.sbnz.ibar.repositories.BookRepository;
import com.sbnz.ibar.repositories.ReviewRepository;
import com.sbnz.ibar.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public Page<ReviewDto> getReviews(UUID bookId, Pageable p) {
        return this.reviewRepository.getAllByBookId(bookId, p).map(reviewMapper::toDto);
    }

    public List<ReviewNumbersDto> findAndGroupByRating(UUID bookId) {
        return this.reviewRepository.findAndGroupByRating(bookId);
    }

    public ReviewDto userReviewByBook(UUID bookId) throws EntityDoesNotExistException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return reviewRepository.findByBookIdAndReaderId(bookId, user.getId()).map(reviewMapper::toDto)
                .orElseThrow(() -> new EntityDoesNotExistException(Review.class.getName(), user.getId()));
    }

    public ReviewDto create(ReviewDto reviewDto) throws EntityDoesNotExistException, EntityAlreadyExistsException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.getId().equals(reviewDto.getUserId())) {
            throw new UserNotLoggedInException("The passed user was not authorized.");
        }

        Book b = bookRepository.findById(reviewDto.getBookId())
                .orElseThrow(() -> new EntityDoesNotExistException(Book.class.getName(), reviewDto.getBookId()));

        Reader u = (Reader) userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new EntityDoesNotExistException(Reader.class.getName(), reviewDto.getUserId()));

        if (reviewRepository.existsByBookIdAndReaderId(reviewDto.getBookId(), reviewDto.getUserId())) {
            throw new EntityAlreadyExistsException(Review.class.getName(), reviewDto.getBookId());
        }

        long newNumReviews = b.getNumReviews() + 1;
        double newRating = (b.getAverageRating() * b.getNumReviews() + reviewDto.getRating()) / newNumReviews;

        Review r = new Review();
        r.setBook(b);
        r.setReader(u);
        r.setContent(reviewDto.getContent());
        r.setRating(reviewDto.getRating());
        r = reviewRepository.save(r);

        b.setAverageRating(newRating);
        b.setNumReviews(newNumReviews);

        bookRepository.save(b);

        return reviewMapper.toDto(r);
    }

    public ReviewDto update(ReviewDto reviewDto) throws EntityDoesNotExistException {
        Book b = bookRepository.findById(reviewDto.getBookId())
                .orElseThrow(() -> new EntityDoesNotExistException(Book.class.getName(), reviewDto.getBookId()));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.getId().equals(reviewDto.getUserId())) {
            throw new UserNotLoggedInException("The passed user was not authorized.");
        }

        Review r = reviewRepository
                .findByBookIdAndReaderId(reviewDto.getBookId(), reviewDto.getUserId())
                .orElseThrow(() -> new EntityDoesNotExistException(Review.class.getName(), reviewDto.getBookId()));

        int oldRating = r.getRating();

        r.setTimeAdded(Instant.now());
        r.setContent(reviewDto.getContent());
        r.setRating(reviewDto.getRating());
        r = reviewRepository.save(r);

        double newAverageRating = (b.getAverageRating() * b.getNumReviews() - oldRating + r.getRating())
                / b.getNumReviews();

        b.setAverageRating(newAverageRating);
        bookRepository.save(b);

        return reviewMapper.toDto(r);
    }
}
