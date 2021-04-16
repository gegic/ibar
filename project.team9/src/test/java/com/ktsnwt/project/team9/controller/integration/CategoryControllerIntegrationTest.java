package com.ktsnwt.project.team9.controller.integration;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.dto.CategoryDTO;
import com.ktsnwt.project.team9.dto.UserLoginDTO;
import com.ktsnwt.project.team9.dto.response.UserTokenStateDTO;
import com.ktsnwt.project.team9.helper.implementations.CustomPageImplementation;
import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.services.implementations.CategoryService;
import javassist.NotFoundException;

import static com.ktsnwt.project.team9.constants.CategoryConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CategoryControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CategoryService categoryService;

	
	  private String accessToken;
	  
	  public void login(String username, String password) {
		  ResponseEntity<UserTokenStateDTO> responseEntity =
		  restTemplate.postForEntity("/auth/login", new UserLoginDTO(username,
		  password), UserTokenStateDTO.class); accessToken = "Bearer " +
		  responseEntity.getBody().getAccessToken(); 
	  }
	 
	
	@Test
	public void testGetAllCategory_ShouldReturnAllCategory() {
		int size = ((List<Category>) categoryService.getAll()).size();

		ResponseEntity<CategoryDTO[]> responseEntity = restTemplate.getForEntity("/api/categories",
				CategoryDTO[].class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(size, responseEntity.getBody().length);
	}

	@Test
	public void testGetAllCategory_WithPageable_ShouldReturnFirst5Category() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);
		int size = categoryService.findAll(pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<CategoryDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<CategoryDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<CategoryDTO>> responseEntity = restTemplate
				.exchange("/api/categories/by-page?page=" + PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(PAGE_NO ,responseEntity.getBody().getNumber());
		//Postavljen je size iz service-a jer ne znamo velicinu stranice koje ce se vratiti
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetAllCategory_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(NON_EXISTING_PAGE_NO, PAGE_SIZE);
		int size = categoryService.findAll(pageable).getNumberOfElements();
		
		ParameterizedTypeReference<CustomPageImplementation<CategoryDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<CategoryDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<CategoryDTO>> responseEntity = restTemplate
				.exchange("/api/categories/by-page?page=" + NON_EXISTING_PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetCategory_WithExistingId_ShouldReturnCategoryDTO() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<CategoryDTO> responseEntity = restTemplate.exchange("/api/categories/1", HttpMethod.GET,
                new HttpEntity<>(headers),
                CategoryDTO.class);
		

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(EXISTING_DESC, responseEntity.getBody().getDescription());
		assertEquals(EXISTING_NAME, responseEntity.getBody().getName());
	}

	@Test
	public void testGetCategory_WithNonExistingId_ShouldReturnNotFound() {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<CategoryDTO> responseEntity = restTemplate.exchange("/api/categories/10000", HttpMethod.GET,
                new HttpEntity<>(headers),
                CategoryDTO.class);

		
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void testCreateCategory_WithValidParameters_ShouldReturnCreatedCategory() throws Exception {
		int beforeSize = ((List<Category>) categoryService.getAll()).size();

		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<CategoryDTO> responseEntity = restTemplate.postForEntity("/api/categories",
				new HttpEntity<>(CATEGORY_FOR_CREATE, headers),
				CategoryDTO.class);

		
		CategoryDTO category = responseEntity.getBody();

		List<Category> newCategory = (List<Category>) categoryService.getAll();
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertNotNull(category);
		assertEquals(CATEGORY_FOR_CREATE.getName(), newCategory.get(newCategory.size()-1).getName());
		assertEquals(beforeSize + 1, newCategory.size());
	}

	@Test
	public void testCreateCategory_WithEmptyName_ShouldReturnBadRequest() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<CategoryDTO> responseEntity = restTemplate.postForEntity("/api/categories",
				new HttpEntity<>(new CategoryDTO(), headers),
				CategoryDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testUpdateCategory_WithValidParameters_ShouldReturnUpdatedCategory() {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<CategoryDTO> responseEntity =
                restTemplate.exchange("/api/categories/"+ EXISTING_CATEGORY_ID, HttpMethod.PUT,
                        new HttpEntity<CategoryDTO>(CATEGORY_FOR_UPDATE, headers),
                        CategoryDTO.class);
		
		CategoryDTO category = responseEntity.getBody();
		
		Category baseCategory =categoryService.getById(EXISTING_CATEGORY_ID);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(category);
		//Proveri da li je promenio kontekst
		assertEquals(EXISTING_CATEGORY_ID, category.getId());
		assertEquals(CATEGORY_FOR_UPDATE.getName(), category.getName());
		//Proveri da li je promenio kontekst u bazi
		assertEquals(EXISTING_CATEGORY_ID, baseCategory.getId());
		assertEquals(CATEGORY_FOR_UPDATE.getName(), baseCategory.getName());
		
		
	}
	
	@Test
	public void testUpdateCategory_WithEmptyContent_ShouldReturnBadRequest() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<CategoryDTO> responseEntity =
                restTemplate.exchange("/api/categories/"+ EXISTING_CATEGORY_ID, HttpMethod.PUT,
                        new HttpEntity<CategoryDTO>(new CategoryDTO(), headers),
                        CategoryDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testUpdateCategory_WithNonExistingId_ShouldReturnBadRequest() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<CategoryDTO> responseEntity =
                restTemplate.exchange("/api/categories/"+ NON_EXISTING_CATEGORY_ID, HttpMethod.PUT,
                        new HttpEntity<CategoryDTO>(CATEGORY_FOR_UPDATE, headers),
                        CategoryDTO.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDeleteCategory_WithExistingId_ShouldReturnTrue() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		int length = ((List<Category>) categoryService.getAll()).size();

		ResponseEntity<String> responseEntity = restTemplate.exchange(
				"/api/categories/" + EXISTING_CATEGORY_ID, HttpMethod.DELETE,
				new HttpEntity<Object>(null, headers), String.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(Boolean.parseBoolean(responseEntity.getBody()));
		assertEquals(length - 1, ((List<Category>) categoryService.getAll()).size());
	}

	@Test
	public void testDeleteCategory_WithNonExistingId_ShouldReturnNotFound() throws NotFoundException {
		
		login("email_adresa1@gmail.com", "sifra123");
        HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/categories/" + NON_EXISTING_CATEGORY_ID, HttpMethod.DELETE,
				new HttpEntity<Object>(null, headers), String.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	
	@Test
	public void testGetAllCategoryByName_WithPageableAndExistingValue_ShouldReturnFirst5Category() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);
		int size = categoryService.findByName(EXISTING_VALUE, pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<CategoryDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<CategoryDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<CategoryDTO>> responseEntity = restTemplate
				.exchange("/api/categories/by-page/" + EXISTING_VALUE + "?page=" + PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(PAGE_NO ,responseEntity.getBody().getNumber());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}

	@Test
	public void testGetAllCategoryByName_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(NON_EXISTING_PAGE_NO, PAGE_SIZE);
		int size = categoryService.findByName(EXISTING_VALUE, pageable).getNumberOfElements();
		
		ParameterizedTypeReference<CustomPageImplementation<CategoryDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<CategoryDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<CategoryDTO>> responseEntity = restTemplate
				.exchange("/api/categories/by-page/" + EXISTING_VALUE + "?page=" + NON_EXISTING_PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetAllCategoryByName_WithNonExistingValue_ShouldReturnEmptyCollection() {
		Pageable pageable = PageRequest.of(PAGE_NO, PAGE_SIZE);
		int size = categoryService.findByName("RNG", pageable).getNumberOfElements();
		
		ParameterizedTypeReference<CustomPageImplementation<CategoryDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<CategoryDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<CategoryDTO>> responseEntity = restTemplate
				.exchange("/api/categories/by-page/" + "RNG" + "?page=" + PAGE_NO + "&size=" + PAGE_SIZE, HttpMethod.GET, null, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	
}




