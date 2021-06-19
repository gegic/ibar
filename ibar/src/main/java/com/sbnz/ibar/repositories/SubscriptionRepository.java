package com.sbnz.ibar.repositories;

import com.sbnz.ibar.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findByBuyerId(UUID uuid);

    Optional<Subscription> findByPurchasedPlanId(UUID uuid);

    boolean existsByBuyerId(UUID id);

    @Modifying(flushAutomatically = true)
    @Query("delete from Subscription s where s.buyer.id = :id")
    int deleteByBuyerId(UUID id);
}
