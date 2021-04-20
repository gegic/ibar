package com.ktsnwt.project.team9.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.RegisteredUser;

@Repository
public interface IRegisteredUser extends JpaRepository<RegisteredUser, Long> {

	RegisteredUser findByEmail(String email);

	RegisteredUser findByUsername(String username);

	@Query(value = "SELECT * FROM users_table a WHERE a.type='RU' AND ((LOWER(a.username) LIKE LOWER(?1)) OR (LOWER(a.email) LIKE LOWER(?1)) OR (LOWER(a.first_name) LIKE LOWER(?1)) OR (LOWER(a.last_name) LIKE LOWER(?1)))", nativeQuery = true)
	Page<RegisteredUser> findByUsernameOrEmailOrFirstNameOrLastNameContainingIgnoreCase(String value,
			Pageable pageable);

	Iterable<RegisteredUser> findbyTitle(Long title);

}
