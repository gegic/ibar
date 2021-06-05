package com.sbnz.ibar.controllers;

import com.sbnz.ibar.services.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbnz.ibar.mapper.CategoryMapper;

@RestController
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:4200", maxAge = 3600)
public class CategoryController {


	private CategoryService categoryService;


	private CategoryMapper categoryMapper;

//	@PreAuthorize("permitAll()")
//	@GetMapping
//	public ResponseEntity<Iterable<CategoryDTO>> getAllCategorys() {
//		List<CategoryDTO> categorysDTO = categoryMapper.toDTOList(categoryService.getAll());
//		return new ResponseEntity<Iterable<CategoryDTO>>(categorysDTO, HttpStatus.OK);
//	}
//
//	@PreAuthorize("permitAll()")
//	@GetMapping(value="/by-page/{value}")
//	public ResponseEntity<Page<CategoryDTO>> getAllCategoryByName(@PathVariable("value") String value, Pageable pageable){
//		Page<Category> page = categoryService.findByName(value, pageable);
//        List<CategoryDTO> categoryDTO = categoryMapper.toDTOList(page.toList());
//        Page<CategoryDTO> pageCategoryDTO = new PageImpl<CategoryDTO>(categoryDTO.stream().collect(Collectors.toList()),page.getPageable(),page.getTotalElements());
//        return new ResponseEntity<Page<CategoryDTO>>(pageCategoryDTO, HttpStatus.OK);
//	}
//
//	@PreAuthorize("permitAll()")
//	@GetMapping(value= "/by-page")
//	public ResponseEntity<Page<CategoryDTO>> getAllCategory(Pageable pageable){
//		Page<Category> page = categoryService.findAll(pageable);
//        List<CategoryDTO> categoryDTO = categoryMapper.toDTOList(page.toList());
//        Page<CategoryDTO> pageCategoryDTO = new PageImpl<CategoryDTO>(categoryDTO.stream().collect(Collectors.toList()),page.getPageable(),page.getTotalElements());
//        return new ResponseEntity<Page<CategoryDTO>>(pageCategoryDTO, HttpStatus.OK);
//	}
//
//	@GetMapping(value = "/{id}")
//	public ResponseEntity<CategoryDTO> getCategory(@PathVariable UUID id) {
//
//		Category category = categoryService.getById(id);
//		if (category == null) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//		return new ResponseEntity<CategoryDTO>(categoryMapper.toDto(category), HttpStatus.OK);
//	}
//
//	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO CategoryDTO) {
//
//		try {
//			return new ResponseEntity<CategoryDTO>(
//					categoryMapper
//							.toDto(categoryService.create(categoryMapper.toEntity(CategoryDTO))),
//					HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
//
//	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable UUID id,
//			@Valid @RequestBody CategoryDTO CategoryDTO) {
//
//		try {
//			return new ResponseEntity<CategoryDTO>(
//					categoryMapper
//							.toDto(categoryService.update(id, categoryMapper.toEntity(CategoryDTO))),
//					HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
//
//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<Boolean> deleteCategory(@PathVariable UUID id) {
//		try {
//			return new ResponseEntity<Boolean>(categoryService.delete(id), HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
//		}
//	}
}
