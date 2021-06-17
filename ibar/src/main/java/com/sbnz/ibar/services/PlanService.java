package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.PlanDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.PlanMapper;
import com.sbnz.ibar.model.*;
import com.sbnz.ibar.repositories.CategoryRepository;
import com.sbnz.ibar.repositories.PlanRepository;
import com.sbnz.ibar.repositories.SubscriptionRepository;
import com.sbnz.ibar.repositories.UserRepository;
import com.sbnz.ibar.rto.events.OnSubscribed;
import lombok.AllArgsConstructor;
import org.kie.api.runtime.KieSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlanService {

	private final PlanMapper planMapper;
	private final PlanRepository planRepository;
	private final CategoryRepository categoryRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final UserRepository userRepository;
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
			throw new EntityAlreadyExistsException(Subscription.class.getName(), u.getId());
		}
		Optional<Plan> optionalPlan = planRepository.findById(dto.getId());
		if (optionalPlan.isEmpty()) {
			throw new EntityDoesNotExistException(Plan.class.getName(), dto.getId());
		}
		Reader r = (Reader) userRepository.findById(u.getId())
				.orElseThrow(() -> new EntityDoesNotExistException(Reader.class.getName(), u.getId()));

		Plan p = optionalPlan.get();
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
		KieSession ranksSession = kieService.getRanksSession();
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

		entity = planRepository.save(entity);

		return planMapper.toDto(entity);
	}

	public void delete(UUID id) {
		try {
			planRepository.deleteById(id);
		} catch (Exception ignored) {
		}
	}

	public PlanDto update(PlanDto dto) throws Exception {
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

		entity = planRepository.save(entity);

		return planMapper.toDto(entity);
	}

}
