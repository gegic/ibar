package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

	Plan findByName(String name);
}
