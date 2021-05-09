package com.ktsnwt.project.team9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.Achievement;

@Repository
public interface IAchievementRepository extends JpaRepository<Achievement, Long> {

	Achievement findByName(String name);
}
