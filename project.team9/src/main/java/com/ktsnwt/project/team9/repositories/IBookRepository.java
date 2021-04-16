package com.ktsnwt.project.team9.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.Book;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {

	Page<Book> getByCategoryId(Long id, Pageable pageable);

	Page<Book> findByCategoryIdAndNameContainingIgnoreCase(Long id, String name, Pageable pageable);

	Page<Book> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
