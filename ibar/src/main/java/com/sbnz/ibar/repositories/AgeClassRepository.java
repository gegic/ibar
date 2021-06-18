package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Admin;
import com.sbnz.ibar.model.AgeClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgeClassRepository extends JpaRepository<AgeClass, UUID> {

}
