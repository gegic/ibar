package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	Page<Book> getByCategoryId(Long id, Pageable pageable);

	Page<Book> findByCategoryIdAndNameContainingIgnoreCase(Long id, String name, Pageable pageable);

	Page<Book> findByNameContainingIgnoreCase(String name, Pageable pageable);

	@Query("select b from Book b where b not in (select bk from ReadingProgress rp join rp.book bk where rp.reader.id = :readerId) order by b.averageRating desc")
	List<Book> getTopRated(long readerId, Pageable limitPage);

	@Query("select b from Book b where b not in (select bk from ReadingProgress rp join rp.book bk where rp.reader.id = :readerId)")
	List<Book> getUnread(long readerId);
}
