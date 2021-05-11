package com.ktsnwt.project.team9.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.model.Plan;
import com.ktsnwt.project.team9.repositories.ICategoryRepository;
import com.ktsnwt.project.team9.repositories.IPlanRepository;

import javassist.NotFoundException;

@Service
public class PlanService {

	@Autowired
	private IPlanRepository packageRepository;

	@Autowired
	private ICategoryRepository categoryRepository;

	public Iterable<Plan> getAll() {
		return packageRepository.findAll();
	}

	public Plan getById(Long id) {
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

	public boolean delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public Plan update(Long id, Plan entity) throws Exception {
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
