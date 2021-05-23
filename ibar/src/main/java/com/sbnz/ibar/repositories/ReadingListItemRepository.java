package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.ReadingListItem;
import com.sbnz.ibar.model.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingListItemRepository extends JpaRepository<ReadingListItem, Long> {

	List<ReadingListItem> getByBookId(long bookId);

	List<ReadingListItem> getByReaderId(long readerId);

	Optional<ReadingListItem> findByBookIdAndReaderId(long bookId, long readerId);

	@Query("select rl from ReadingListItem rl where rl.reader.id = :readerId or rl.reader.male = :isMale")
	List<ReadingListItem> getReadingListByReaderIdAndReaderCategory(long readerId, boolean isMale);
}
