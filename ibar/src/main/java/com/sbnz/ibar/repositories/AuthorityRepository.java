package com.sbnz.ibar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbnz.ibar.model.Authority;

import java.util.UUID;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	
	Authority findByName(String name);
	
}
