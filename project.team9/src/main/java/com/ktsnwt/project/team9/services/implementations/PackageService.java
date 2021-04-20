package com.ktsnwt.project.team9.services.implementations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.model.Package;
import com.ktsnwt.project.team9.repositories.ICategoryRepository;
import com.ktsnwt.project.team9.repositories.IPackageRepository;
import com.ktsnwt.project.team9.services.interfaces.IPackageService;

import javassist.NotFoundException;

public class PackageService implements IPackageService {

	private IPackageRepository packageRepository;

	private ICategoryRepository categoryRepository;

	@Override
	public Iterable<Package> getAll() {
		return packageRepository.findAll();
	}

	@Override
	public Package getById(Long id) {
		Optional<Package> packageEntity = packageRepository.findById(id);

		if (!packageEntity.isPresent()) {
			return null;
		}

		return packageEntity.get();
	}

	@Override
	public Package create(Package entity) throws Exception {
		Package packageEntity = packageRepository.findByName(entity.getName());

		if (packageEntity != null) {
			throw new Exception("Package with given name already exists.");
		}

		Set<Category> categories = new HashSet<>();

		for (Category category : entity.getCategories()) {
			Category categoryEntity = categoryRepository.findById(category.getId()).orElse(null);

			if (categoryEntity != null) {
				categories.add(categoryEntity);
			}
		}

		entity.setCategories(setCategories(entity));

		packageEntity = packageRepository.save(entity);

		return packageEntity;
	}

	@Override
	public boolean delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Package update(Long id, Package entity) throws Exception {
		Package packageEntity = getById(id);

		if (packageEntity == null) {
			throw new NotFoundException("Package with given id doesn't exist");
		}

		packageEntity.setName(entity.getName());
		packageEntity.setPrice(entity.getPrice());
		packageEntity.setDuration(entity.getDuration());
		packageEntity.setBookType(entity.getBookType());

		entity.setCategories(setCategories(entity));

		packageRepository.save(entity);

		return entity;
	}

	private Set<Category> setCategories(Package entity) {
		Set<Category> categories = new HashSet<>();

		for (Category category : entity.getCategories()) {
			Category categoryEntity = categoryRepository.findById(category.getId()).orElse(null);

			if (categoryEntity != null) {
				categories.add(categoryEntity);
			}
		}

		return categories;
	}

}
