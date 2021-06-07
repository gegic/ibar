package com.sbnz.ibar.repositories;

import com.sbnz.ibar.dto.ReviewNumbersDto;
import com.sbnz.ibar.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

	Optional<Review> findByBookIdAndReaderId(UUID bookId, UUID readerId);

	boolean existsByBookIdAndReaderId(UUID bookId, UUID readerId);

	void deleteAllByBookId(UUID bookId);

	@Query("select review from Review review where review.reader.id = :readerId or review.reader.male = :isMale")
	List<Review> getReviewsByReaderIdAndReaderCategory(UUID readerId, boolean isMale);

	Page<Review> getAllByBookId(UUID bookId, Pageable p);

	@Query("select new com.sbnz.ibar.dto.ReviewNumbersDto(r.rating, count(r)) from Review r where r.book.id = :bookId group by r.rating")
	List<ReviewNumbersDto> findAndGroupByRating(UUID bookId);
}
