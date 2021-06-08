package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("select distinct category from ReadingProgress rp join rp.book b join b.category category where rp.reader.id = :userId")
    List<Category> getReadCategories(UUID userId);

    @Query("select distinct c from Category c, Book b where c.id = :categoryId and c.id = b.category.id")
    Category checkDoesBookContainCategoryWithGivenId(UUID categoryId);

    @Query("select distinct pc from Plan p join p.categories pc where pc.id = :categoryId")
    Category checkDoesPlanContainCategoryWithGivenId(UUID categoryId);

    Optional<Category> getByName(String name);

}
