package com.ktsnwt.project.team9.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.MarkAuthor;

@Repository
public interface IMarkAuthorRepository extends JpaRepository<MarkAuthor, Long> {

	@Query(value = "SELECT * FROM mark m WHERE m.user_id = ?1 AND m.author_id = ?2", nativeQuery = true)
	MarkAuthor findByGraderAndAuthorId(Long userId, Long authorId);
	
	List<MarkAuthor> findByAuthorId(Long authorId);

}

