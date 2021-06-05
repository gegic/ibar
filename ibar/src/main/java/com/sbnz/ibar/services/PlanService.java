package com.sbnz.ibar.services;

import java.util.Optional;
import java.util.UUID;

import com.sbnz.ibar.model.Plan;
import com.sbnz.ibar.repositories.CategoryRepository;
import com.sbnz.ibar.repositories.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;

@Service
public class PlanService {


	private PlanRepository packageRepository;


	private CategoryRepository categoryRepository;

	public Iterable<Plan> getAll() {
		return packageRepository.findAll();
	}

	public Plan getById(UUID id) {
		Optional<Plan> packageEntity = packageRepository.findById(id);

		return packageEntity.orElse(null);
	}

	public Plan create(Plan entity) throws Exception {
		Plan planEntity = packageRepository.findByName(entity.getName());

		if (planEntity != null) {
			throw new Exception("Package with given name already exists.");
		}

		planEntity = packageRepository.save(entity);

		return planEntity;
	}

	public boolean delete(UUID id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public Plan update(UUID id, Plan entity) throws Exception {
		Plan planEntity = getById(id);

		if (planEntity == null) {
			throw new NotFoundException("Package with given id doesn't exist");
		}

		planEntity.setName(entity.getName());
		planEntity.setPrice(entity.getPrice());
		planEntity.setDuration(entity.getDuration());

		packageRepository.save(entity);

		return entity;
	}

}
