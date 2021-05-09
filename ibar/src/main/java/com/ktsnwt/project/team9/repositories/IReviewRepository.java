package com.ktsnwt.project.team9.repositories;

import com.ktsnwt.project.team9.model.Achievement;
import com.ktsnwt.project.team9.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {
	Review getByBookId(long bookId);

	Review getByReaderId(long readerId);

	void deleteAllByBookId(long bookId);
}
