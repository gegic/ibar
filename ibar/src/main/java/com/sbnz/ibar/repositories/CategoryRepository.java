package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Page<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description,Pageable pageable);
}
