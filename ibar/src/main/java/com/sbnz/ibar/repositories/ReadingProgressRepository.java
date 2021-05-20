package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {

	List<ReadingProgress> getByBookId(long bookId);

	List<ReadingProgress> getByReaderId(long readerId);

	Optional<ReadingProgress> findByBookIdAndReaderId(long bookId, long readerId);
}
