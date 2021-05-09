package com.ktsnwt.project.team9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.Title;

@Repository
public interface ITitleRepository extends JpaRepository<Title, Long> {

	Title findByName(String name);
}
