package com.ktsnwt.project.team9.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.Reader;

@Repository
public interface IReaderRepository extends JpaRepository<Reader, Long> {

	Reader findByEmail(String email);

	@Query(value = "SELECT * FROM users_table a WHERE a.type='RU' AND ((LOWER(a.email) LIKE LOWER(?1)) OR (LOWER(a.first_name) LIKE LOWER(?1)) OR (LOWER(a.last_name) LIKE LOWER(?1)))", nativeQuery = true)
	Page<Reader> findByEmailOrFirstNameOrLastNameContainingIgnoreCase(String value,
                                                                                Pageable pageable);

	@Query(value = "SELECT * FROM users_table a WHERE a.type='RU' AND achievement_id=?1", nativeQuery = true)
	Iterable<Reader> findByAchievementId(Long titleId);

}
