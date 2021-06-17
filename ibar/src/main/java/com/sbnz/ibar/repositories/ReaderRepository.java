package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Reader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, UUID> {

	Reader findByEmail(String email);

	@Query(value = "SELECT * FROM users_table a WHERE a.type='RU' AND ((LOWER(a.email) LIKE LOWER(?1)) OR (LOWER(a.first_name) LIKE LOWER(?1)) OR (LOWER(a.last_name) LIKE LOWER(?1)))", nativeQuery = true)
	Page<Reader> findByEmailOrFirstNameOrLastNameContainingIgnoreCase(String value,
                                                                                Pageable pageable);

	List<Reader> findByRankId(UUID rankId);

}
