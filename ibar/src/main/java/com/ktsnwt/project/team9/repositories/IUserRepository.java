package com.ktsnwt.project.team9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	User findByEmail(String email);

}
