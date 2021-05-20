package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

	Achievement findByName(String name);
}
