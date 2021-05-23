package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.sbnz.ibar.utils.Utils;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	Review getByBookId(long bookId);

	Review getByReaderId(long readerId);

	void deleteAllByBookId(long bookId);

	@Query("select review from Review review where review.reader.id = :readerId or review.reader.male = :isMale")
	List<Review> getReviewsByReaderIdAndReaderCategory(long readerId, boolean isMale);
}
