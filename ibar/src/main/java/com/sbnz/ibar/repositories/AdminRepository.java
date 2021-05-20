package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

	@Query(value ="SELECT * FROM users_table a WHERE a.type='AD' AND ((LOWER(a.email) LIKE LOWER(?1)) OR (LOWER(a.first_name) LIKE LOWER(?1)) OR (LOWER(a.last_name) LIKE LOWER(?1)))", nativeQuery=true)
	Page<Admin> findByEmailOrFirstNameOrLastNameContainingIgnoreCase(String value, Pageable pageable);

}
