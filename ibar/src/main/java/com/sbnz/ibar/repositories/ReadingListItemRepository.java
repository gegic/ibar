package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.ReadingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadingListItemRepository extends JpaRepository<ReadingListItem, UUID> {

	List<ReadingListItem> getByBookId(UUID bookId);

	List<ReadingListItem> getByReaderId(UUID readerId);

	void deleteAllByBookId(UUID bookId);

	void deleteByBookIdAndReaderId(UUID bookId, UUID readerId);

	Optional<ReadingListItem> findByBookIdAndReaderId(UUID bookId, UUID readerId);

	@Query("select rl from ReadingListItem rl where rl.reader.id = :readerId or rl.reader.male = :isMale")
	List<ReadingListItem> getReadingListByReaderIdAndReaderCategory(UUID readerId, boolean isMale);
}
