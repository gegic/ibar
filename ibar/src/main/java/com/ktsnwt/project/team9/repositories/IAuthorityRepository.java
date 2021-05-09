package com.ktsnwt.project.team9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.Authority;

@Repository
public interface IAuthorityRepository extends JpaRepository<Authority, Long> {
	
	Authority findByName(String name);
	
}
