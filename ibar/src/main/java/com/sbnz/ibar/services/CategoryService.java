package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.CategoryDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.CategoryMapper;
import com.sbnz.ibar.model.Category;
import com.sbnz.ibar.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(this::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getById(UUID id)
            throws EntityNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);

        return this.toCategoryDto(category.orElseThrow(EntityNotFoundException::new));
    }

    public CategoryDto getByName(String name)
            throws EntityNotFoundException {
        Optional<Category> category = categoryRepository.getByName(name);

        return this.toCategoryDto(category.orElseThrow(EntityNotFoundException::new));
    }

    public CategoryDto create(CategoryDto categoryDto)
            throws EntityAlreadyExistsException {
        Optional<Category> existCategory = categoryRepository.getByName(categoryDto.getName());

        if (existCategory.isPresent()) {
            Category category = existCategory.get();

            throw new EntityAlreadyExistsException(category.getName(), category.getId());
        }

        Category category = new Category(categoryDto);

        category.setId(UUID.randomUUID());

        category = categoryRepository.save(category);

        return this.toCategoryDto(category);
    }

    public CategoryDto update(UUID id, CategoryDto entity)
            throws EntityDoesNotExistException, EntityAlreadyExistsException {
        Optional<Category> existCategory = categoryRepository.getByName(entity.getName());

        if (existCategory.isPresent()) {
            throw new EntityAlreadyExistsException(entity.getName(), id);
        }

        existCategory = categoryRepository.findById(id);

        if (existCategory.isEmpty()) {
            throw new EntityDoesNotExistException(entity.getName(), id);
        }

        Category category = existCategory.get();

        category.setActive(entity.isActive());
        category.setDescription(entity.getDescription());
        category.setName(entity.getName());

        category = categoryRepository.save(category);

        return this.toCategoryDto(category);
    }

    public boolean delete(UUID id) {
        if (!this.checkAllValiditiesForDeletingCategory(id)) {
            return false;
        }

        categoryRepository.deleteById(id);

        return true;
    }

    private CategoryDto toCategoryDto(Category category) {
        return categoryMapper.toDto(category);
    }

    private boolean checkAllValiditiesForDeletingCategory(UUID id) {
        if (!this.doesExistCategoryWithGivenId(id)) {
            return false;
        }

        if (this.doesExistBookThatContainsCategoryWithGivenId(id)) {
            return false;
        }

        if (this.doesExistPlanThatContainsCategoryWithGivenId(id)) {
            return false;
        }

        return true;
    }

    private boolean doesExistCategoryWithGivenId(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);

        return category.isPresent();
    }

    private boolean doesExistBookThatContainsCategoryWithGivenId(UUID id) {
        Category category = categoryRepository.checkDoesBookContainCategoryWithGivenId(id);

        return category != null;
    }

    private boolean doesExistPlanThatContainsCategoryWithGivenId(UUID id) {
        Category category = categoryRepository.checkDoesPlanContainCategoryWithGivenId(id);

        return category != null;
    }
}
