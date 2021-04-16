package com.ktsnwt.project.team9.services.implementations;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.repositories.ICategoryRepository;
import com.ktsnwt.project.team9.services.interfaces.ICategoryService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class CategoryService implements ICategoryService {
	
	@Autowired
	private ICategoryRepository categoryRepository;

	@Override
	public Collection<Category> getAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Category getById(Long id) {
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public Category create(Category entity) {
		return categoryRepository.save(entity);
	}

	@Override
	public boolean delete(Long id) throws Exception {
		Category category = categoryRepository.findById(id).orElse(null);
		if(category == null)
			throw new Exception("Category with this ID doesn't exist.");
		
		categoryRepository.deleteById(id);
		return true;
	}

	@Override
	public Category update(Long id, Category entity) throws Exception {
		Category category = categoryRepository.findById(id).orElse(null);
		if(category == null)
			throw new Exception("Category with this ID doesn't exist.");
		category.setActive(entity.isActive());
		category.setDescription(entity.getDescription());
		category.setName(entity.getName());
		return categoryRepository.save(category);
	}

	public Page<Category> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}
	
	public Page<Category> findByName(String value, Pageable pageable){
		return categoryRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(value, value, pageable);
	}
}
