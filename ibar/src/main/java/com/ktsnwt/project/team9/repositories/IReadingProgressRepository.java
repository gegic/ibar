package com.ktsnwt.project.team9.repositories;

import com.ktsnwt.project.team9.model.ReadingProgress;
import com.ktsnwt.project.team9.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {
	ReadingProgress getByBookId(long bookId);

	ReadingProgress getByReaderId(long readerId);
}
