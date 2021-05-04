package com.ktsnwt.project.team9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktsnwt.project.team9.model.Subscription;

@Repository
public interface ISubscriptionRepository extends JpaRepository<Subscription, Long> {

}
