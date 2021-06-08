package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {

    Page<Author> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("select distinct authors from ReadingProgress rp join rp.book b join b.authors authors where rp.reader.id = :userId")
    List<Author> getReadAuthors(UUID userId);

    @Query(value = "select count(*) from book_authors ba where ba.authors_id = :authorId", nativeQuery = true)
    int getNumberOfBooks(UUID authorId);
}
