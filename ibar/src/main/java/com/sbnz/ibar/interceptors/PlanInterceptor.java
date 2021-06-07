package com.sbnz.ibar.interceptors;

import com.sbnz.ibar.model.Admin;
import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.model.Subscription;
import com.sbnz.ibar.repositories.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PlanInterceptor implements HandlerInterceptor {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler)
            throws Exception {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (o instanceof Admin) {
            return true;
        } else if (o instanceof Reader) {
            UUID userId = ((Reader) o).getId();
            Optional<Subscription> optionalSubscription = subscriptionRepository.findByBuyerId(userId);
            if (!optionalSubscription.isPresent()) {
                response.sendError(HttpStatus.FORBIDDEN.value(), "No subscription.");
                return false;
            }
            Subscription subscription = optionalSubscription.orElse(new Subscription());
            Instant expirationDate = subscription.getDateOfPurchase()
                    .plus(subscription.getPurchasedPlan().getDayDuration(), ChronoUnit.DAYS);

            if (Instant.now().isAfter(expirationDate)) {
                response.sendError(HttpStatus.FORBIDDEN.value(), "Subscription has expired.");
                return false;
            }
            return true;
        }
        return false;
    }
}
