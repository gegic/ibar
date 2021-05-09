package com.ktsnwt.project.team9.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.Admin;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, Long> {

	@Query(value ="SELECT * FROM users_table a WHERE a.type='AD' AND ((LOWER(a.username) LIKE LOWER(?1)) OR (LOWER(a.email) LIKE LOWER(?1)) OR (LOWER(a.first_name) LIKE LOWER(?1)) OR (LOWER(a.last_name) LIKE LOWER(?1)))", nativeQuery=true)
	Page<Admin> findByUsernameOrEmailOrFirstNameOrLastNameContainingIgnoreCase(String value, Pageable pageable);

}
