package com.ktsnwt.project.team9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPackageRepository extends JpaRepository<com.ktsnwt.project.team9.model.Package, Long> {

	com.ktsnwt.project.team9.model.Package findByName(String name);
}
