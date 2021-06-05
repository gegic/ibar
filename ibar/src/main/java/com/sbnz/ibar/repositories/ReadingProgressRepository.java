package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.ReadingProgress;
import com.sbnz.ibar.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, UUID> {

	long countByBookId(UUID bookId);

	List<ReadingProgress> getByBookId(UUID bookId);

	List<ReadingProgress> getByReaderId(UUID readerId);

	Optional<ReadingProgress> findByBookIdAndReaderId(UUID bookId, UUID readerId);

	@Query("select rp from ReadingProgress rp where rp.reader.id = :readerId or rp.reader.male = :isMale")
	List<ReadingProgress> getReadingProgressByReaderIdAndReaderCategory(UUID readerId, boolean isMale);
}
