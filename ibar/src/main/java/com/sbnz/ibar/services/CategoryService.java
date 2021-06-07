package com.sbnz.ibar.services;

import com.sbnz.ibar.model.Category;
import com.sbnz.ibar.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class CategoryService {
	

	private CategoryRepository categoryRepository;

	public Collection<Category> getAll() {
		return categoryRepository.findAll();
	}

	public Category getById(UUID id) {
		return categoryRepository.findById(id).orElse(null);
	}

	public Category create(Category entity) {
		return categoryRepository.save(entity);
	}

	public boolean delete(UUID id) throws Exception {
		Category category = categoryRepository.findById(id).orElse(null);
		if(category == null)
			throw new Exception("Category with this ID doesn't exist.");
		
		categoryRepository.deleteById(id);
		return true;
	}

	public Category update(UUID id, Category entity) throws Exception {
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
