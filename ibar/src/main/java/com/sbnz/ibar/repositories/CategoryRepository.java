package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

	Page<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description,Pageable pageable);

	@Query("select distinct category from ReadingProgress rp join rp.book b join b.category category where rp.reader.id = :userId")
	List<Category> getReadCategories(UUID userId);
}
