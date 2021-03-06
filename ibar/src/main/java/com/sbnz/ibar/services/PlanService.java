package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.PlanDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.PlanMapper;
import com.sbnz.ibar.model.*;
import com.sbnz.ibar.repositories.*;
import com.sbnz.ibar.rto.RankCheckFact;
import com.sbnz.ibar.rto.events.OnSubscribed;
import lombok.AllArgsConstructor;
import org.kie.api.runtime.KieSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PlanService {

	private final PlanMapper planMapper;
	private final PlanRepository planRepository;
	private final CategoryRepository categoryRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final UserRepository userRepository;
	private final RankRepository rankRepository;
	private final KieService kieService;

	public List<PlanDto> getAll() {
		return planRepository.findAll().stream().map(planMapper::toDto).collect(Collectors.toList());
	}

	public PlanDto getById(UUID id) throws EntityDoesNotExistException {
		return planRepository.findById(id).map(planMapper::toDto).
				orElseThrow(() -> new EntityDoesNotExistException(Plan.class.getName(), id));
	}

	public void subscribe(PlanDto dto) throws EntityAlreadyExistsException, EntityDoesNotExistException {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (subscriptionRepository.existsByBuyerId(u.getId())) {
			subscriptionRepository.deleteByBuyerId(u.getId());
		}
		Optional<Plan> optionalPlan = planRepository.findById(dto.getId());
		if (optionalPlan.isEmpty()) {
			throw new EntityDoesNotExistException(Plan.class.getName(), dto.getId());
		}
		Reader r = (Reader) userRepository.findById(u.getId())
				.orElseThrow(() -> new EntityDoesNotExistException(Reader.class.getName(), u.getId()));

		Plan p = optionalPlan.get();

		KieSession ranksSession = kieService.getRanksSession();
		RankCheckFact rcf = new RankCheckFact(r, p.getRank(), false);
		ranksSession.insert(rcf);
		ranksSession.fireAllRules();

		if (!rcf.isHigher()) {
			throw new EntityDoesNotExistException(Rank.class.getName(), "rank too low");
		}

		Subscription s = new Subscription();
		s.setBuyer(r);
		s.setPurchasedPlan(p);
		s.setDateOfPurchase(Instant.now());

		OnSubscribed event = new OnSubscribed();
		event.setUser(r);
		event.setPlan(p);
		KieSession readingSession = kieService.getReadingSession();
		readingSession.insert(event);
		readingSession.fireAllRules();

		ranksSession.insert(event);
		ranksSession.fireAllRules();
		subscriptionRepository.save(s);
	}

    public PlanDto create(PlanDto dto) throws Exception {
        Optional<Plan> optionalPlan = planRepository.findByName(dto.getName());

        if (optionalPlan.isPresent()) {
            throw new EntityAlreadyExistsException(Plan.class.getName(), dto.getName());
        }

        Plan entity = new Plan();

        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setDayDuration(dto.getDayDuration());
        entity.setDescription(dto.getDescription());

        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));
        entity.setCategories(categories);

        if (dto.getRankId() == null) {
            throw new EntityDoesNotExistException(Rank.class.getName(), null);
        }

        Optional<Rank> optionalRank = rankRepository.findById(dto.getRankId());

        if (optionalRank.isEmpty()) {
            throw new EntityDoesNotExistException(Rank.class.getName(), dto.getRankId());
        }

        Rank rank = optionalRank.get();
        entity.setRank(rank);

        entity = planRepository.save(entity);

        return planMapper.toDto(entity);
    }

    public boolean delete(UUID id) {
        if (isAnyUserSubscribedOnThisPlan(id)) {
            return false;
        }

        planRepository.deleteById(id);

        return true;
    }

    public PlanDto update(UUID id, PlanDto dto) throws Exception {
        Optional<Plan> optionalPlan = planRepository.findById(dto.getId());

        if (!optionalPlan.isPresent()) {
            throw new EntityDoesNotExistException(Plan.class.getName(), dto.getId());
        }

        Plan entity = optionalPlan.get();

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setDayDuration(dto.getDayDuration());

        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));
        entity.setCategories(categories);

        Optional<Rank> optionalRank = rankRepository.findById(dto.getRankId());

        if (optionalRank.isEmpty()) {
            throw new EntityDoesNotExistException(Rank.class.getName(), dto.getRankId());
        }

        Rank rank = optionalRank.get();
        entity.setRank(rank);

        entity = planRepository.save(entity);

        return planMapper.toDto(entity);
    }

    private boolean isAnyUserSubscribedOnThisPlan(UUID planId) {
        return this.subscriptionRepository.findByPurchasedPlanId(planId).isPresent();
    }
}
