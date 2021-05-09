package com.ktsnwt.project.team9.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.MarkBook;

@Repository
public interface IMarkBookRepository extends JpaRepository<MarkBook, Long> {

	@Query(value = "SELECT * FROM mark m WHERE m.user_id = ?1 AND m.book_id = ?2", nativeQuery = true)
	MarkBook findByGraderAndBookId(Long userId, Long bookId);

	List<MarkBook> findByBookId(Long bookId);

}