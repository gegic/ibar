package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, UUID> {

	Achievement findByName(String name);
}
