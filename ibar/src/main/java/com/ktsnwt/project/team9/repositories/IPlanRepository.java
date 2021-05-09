package com.ktsnwt.project.team9.repositories;

import com.ktsnwt.project.team9.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlanRepository extends JpaRepository<Plan, Long> {

	Plan findByName(String name);
}
