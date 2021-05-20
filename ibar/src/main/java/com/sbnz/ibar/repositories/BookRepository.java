package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	Page<Book> getByCategoryId(Long id, Pageable pageable);

	Page<Book> findByCategoryIdAndNameContainingIgnoreCase(Long id, String name, Pageable pageable);

	Page<Book> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
