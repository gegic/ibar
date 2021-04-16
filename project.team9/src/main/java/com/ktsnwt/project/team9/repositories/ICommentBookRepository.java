package com.ktsnwt.project.team9.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ktsnwt.project.team9.model.CommentBook;

@Repository
public interface ICommentBookRepository extends JpaRepository<CommentBook, Long> {

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM comment WHERE comment_id = ?1", nativeQuery = true)
	void delete(Long id);

	Page<CommentBook> findByBookId(Long id, Pageable pageable);
}