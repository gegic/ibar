package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	Review getByBookId(long bookId);

	Review getByReaderId(long readerId);

	void deleteAllByBookId(long bookId);
}
