package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.CategoryDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:4200", maxAge = 3600)
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Iterable<CategoryDto>> getAllCategories() {
        List<CategoryDto> categoryDtos = categoryService.getAll();

        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id)
            throws EntityNotFoundException {
        CategoryDto category = categoryService.getById(id);

        return ResponseEntity.ok(category);
    }

    @GetMapping(value = "/name/{name}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable String name)
            throws EntityNotFoundException {
        CategoryDto category = categoryService.getByName(name);

        return ResponseEntity.ok(category);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
            throws EntityAlreadyExistsException {
        CategoryDto newCategory = categoryService.create(categoryDto);

        return ResponseEntity.ok(newCategory);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryDto categoryDto)
            throws EntityDoesNotExistException, EntityAlreadyExistsException {
        CategoryDto updatedCategory = categoryService.update(id, categoryDto);

        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable UUID id) {
        Boolean deleteResult = categoryService.delete(id);

        return ResponseEntity.ok(deleteResult);
    }

}
