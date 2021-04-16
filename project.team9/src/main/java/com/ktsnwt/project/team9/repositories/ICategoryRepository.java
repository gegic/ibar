package com.ktsnwt.project.team9.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

	Page<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description,Pageable pageable);
}
