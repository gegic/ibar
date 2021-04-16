package com.ktsnwt.project.team9.service.integration;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.helper.implementations.CategoryMapper;
import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.services.implementations.CategoryService;

import static com.ktsnwt.project.team9.constants.CategoryConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CategoryServiceIntegrationTest {


	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryMapper categoryMapper;
	
	@Test
	public void testGetAll_ShouldReturnAllCategory() {
		List<Category> category = (List<Category>) categoryService.getAll();

		assertEquals(TOTAL_NO, category.size());
	}

	@Test
	public void testFindAll_WithFirstPageable_ShouldReturnFirst5Category() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);

		Page<Category> category = categoryService.findAll(pageable);

		assertEquals(5, category.getNumberOfElements());
	}

	@Test
	public void testFindAll_WithSecondPageable_ShouldReturnSecond5Category() {
		Pageable pageable = PageRequest.of(PAGE_NO+1, PAGE_SIZE);

		Page<Category> category = categoryService.findAll(pageable);

		assertEquals(5, category.getNumberOfElements());
	}

	@Test
	public void testFindAll_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(1000, PAGE_SIZE);

		Page<Category> category = categoryService.findAll(pageable);

		assertEquals(0, category.getNumberOfElements());
	}

	@Test
	public void testGetById_WithExistingId_ShouldReturnCategoryWithThatId() {
		Category category = categoryService.getById(1L);

		assertEquals(CATEGORY_NO_1.getDescription(), category.getDescription());
		assertEquals(CATEGORY_NO_1.getName(), category.getName());
		
	}

	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		Category category = categoryService.getById(NON_EXISTING_CATEGORY_ID);
		assertNull(category);
	}

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreate_WithValidParameters_ShouldReturnCreatedCategory() throws Exception {
		int length = ((List<Category>) categoryService.getAll()).size();
		
		Category categoryBefore = categoryMapper.toEntity(CATEGORY_FOR_CREATE);
		
		Category categoryAfter = categoryService.create(categoryBefore);
		
		assertEquals(categoryBefore.getDescription(), categoryAfter.getDescription());
		assertEquals(categoryBefore.getName(), categoryAfter.getName());
		assertEquals(length + 1, categoryService.getAll().size());
	}
	
	@Test(expected = Exception.class)
	public void testCreate_WithExistingName_ShouldThrowException() throws Exception {
		Category categoryBefore = categoryMapper.toEntity(CATEGORY_FOR_CREATE);
		categoryBefore.setName("Manastiri");
		categoryService.create(categoryBefore);
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testUpdate_WithNewValidParameters_ShouldReturnUpdatedCategory() throws Exception {
		

		Category categoryBefore = categoryMapper.toEntity(CATEGORY_FOR_UPDATE);
		Category categoryAfter = categoryService.update(EXISTING_CATEGORY_ID, categoryBefore);
		
		assertEquals(categoryBefore.getDescription(), categoryAfter.getDescription());
		assertEquals(categoryBefore.getName(), categoryAfter.getName());

	}

	@Test(expected = Exception.class)
	public void testUpdate_WithNonExistingId_ShouldThrowException() throws Exception {
		Category categoryBefore = categoryMapper.toEntity(CATEGORY_FOR_UPDATE);
		categoryService.update(NON_EXISTING_CATEGORY_ID, categoryBefore);
	}
	
	@Test(expected = Exception.class)
	public void testUpdate_WithExistingName_ShouldThrowException() throws Exception {
		Category categoryBefore = categoryMapper.toEntity(CATEGORY_FOR_CREATE);
		categoryBefore.setName("Manastiri");
		categoryService.create(categoryBefore);
	}	
	
	@Test(expected = Exception.class)
	public void testUpdate_WithNonExistingCulturalOfferId_ShouldThrowException() throws Exception {
		Category categoryBefore = categoryMapper.toEntity(CATEGORY_FOR_UPDATE);
		categoryBefore.setName("Manastiri");
		categoryService.update(EXISTING_CATEGORY_ID, categoryBefore);
	}
	

	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDelete_WithExistingId_ShouldReturnTrue() throws Exception {
		int length = ((List<Category>) categoryService.getAll()).size();
		
		Boolean status = categoryService.delete(EXISTING_CATEGORY_ID);

		assertTrue(status);
		assertEquals(length - 1, ((List<Category>) categoryService.getAll()).size());
	}

	@Test(expected = Exception.class)
	public void testDelete_WithNonExistingId_ShouldThrowException() throws Exception {
		categoryService.delete(NON_EXISTING_CATEGORY_ID);
	}

	
	@Test
	public void testFindByName_WithFirstPageable_ShouldReturnFirst5Category() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);

		Page<Category> category = categoryService.findByName(EXISTING_VALUE, pageable);

		assertEquals(1, category.getNumberOfElements());
	}

	@Test
	public void testFindBy_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(1000, PAGE_SIZE);

		Page<Category> category = categoryService.findByName(EXISTING_VALUE, pageable);

		assertEquals(0, category.getNumberOfElements());
	}
	
	@Test
	public void testFindBy_WithNonExistingValue_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(1000, PAGE_SIZE);

		Page<Category> category = categoryService.findByName("RNG", pageable);

		assertEquals(0, category.getNumberOfElements());
	}

}
