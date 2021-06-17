package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RankRepository extends JpaRepository<Rank, UUID> {
	Rank findByName(String name);
}
