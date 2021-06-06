package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {

	Optional<Plan> findByName(String name);
}
