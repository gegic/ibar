package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByBuyerId(UUID uuid);

    boolean existsByBuyerId(UUID id);

    boolean deleteByBuyerId(UUID id);
}
