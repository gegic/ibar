package com.ktsnwt.project.team9.repository.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.CategoryConstants;
import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.repositories.ICategoryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CategoryRepositoryIntegrationTest {

	@Autowired
	ICategoryRepository categoryRepository;

	@Test
	public void testFindByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase_WithExistingValue_ShouldReturnCategoryPage() {
		Pageable pageable = PageRequest.of(CategoryConstants.PAGE_NO, CategoryConstants.PAGE_SIZE);
		
		Page<Category> categoryPage = categoryRepository.
				findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase
				(CategoryConstants.EXISTING_VALUE, CategoryConstants.EXISTING_VALUE, pageable);

		assertEquals(1, categoryPage.getTotalElements() );
	}
	
	@Test
	public void testFindSubscribedCategory_WithExistingValue_ShouldReturnCategoryPageWithEmptyContent() {
		Pageable pageable = PageRequest.of(CategoryConstants.PAGE_NO, CategoryConstants.PAGE_SIZE);
		
		Page<Category> categoryPage = categoryRepository.
				findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase
				("RNG", "RNG", pageable);
		assertEquals(0, categoryPage.getTotalElements() );
	}
	

}
