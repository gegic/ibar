package com.ktsnwt.project.team9.controller.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.ktsnwt.project.team9.constants.AdminConstants;
import com.ktsnwt.project.team9.dto.AdminDTO;
import com.ktsnwt.project.team9.dto.UserLoginDTO;
import com.ktsnwt.project.team9.dto.response.UserResDTO;
import com.ktsnwt.project.team9.dto.response.UserTokenStateDTO;
import com.ktsnwt.project.team9.helper.implementations.CustomPageImplementation;
import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.services.implementations.AdminService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class AdminControllerIntegrationTest {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private String accessToken;
	
	public void login(String username, String password) {
		ResponseEntity<UserTokenStateDTO> responseEntity = restTemplate.postForEntity("/auth/login", new UserLoginDTO(username, password), UserTokenStateDTO.class); 
		accessToken = "Bearer " + responseEntity.getBody().getAccessToken(); 
	}
	
	@Test
	public void testGetAllAdmins_WithLoginAsAdmin_ShouldReturnAllAdmins() {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

        ResponseEntity<AdminDTO[]> responseEntity = restTemplate.exchange("/api/admins", HttpMethod.GET, httpEntity, AdminDTO[].class);

        AdminDTO[] admins = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(AdminConstants.NUMBER_OF_ITEMS, admins.length);
		
	}
	
	@Test
	public void testGetAllAdmins_WithExistingPageable_ShouldReturnAdminPage() {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        
		Pageable pageable = PageRequest.of(AdminConstants.PAGE, AdminConstants.PAGE_SIZE);
		int size = adminService.findAll(pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<UserResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<UserResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<UserResDTO>> responseEntity = restTemplate.exchange("/api/admins/by-page?page=" + AdminConstants.PAGE+ "&size=" + AdminConstants.PAGE_SIZE, HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetAdmins_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        
		Pageable pageable = PageRequest.of(AdminConstants.NON_EXISTING_PAGE, AdminConstants.PAGE_SIZE);
		int size = adminService.findAll(pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<UserResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<UserResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<UserResDTO>> responseEntity = restTemplate.exchange("/api/admins/by-page?page=" + AdminConstants.NON_EXISTING_PAGE + "&size=" + AdminConstants.PAGE_SIZE, HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetAdmin_WithExistingId_ShouldReturnAdmin() {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        
		ResponseEntity<UserResDTO> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.ADMIN_ID, HttpMethod.GET, httpEntity, UserResDTO.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(AdminConstants.EXISTING_EMAIL, responseEntity.getBody().getEmail());
	}

	@Test
	public void testGetAdmin_WithNonExistingId_ShouldReturnNotFound() {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        
		ResponseEntity<UserResDTO> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.NON_EXISTING_ADMIN_ID, HttpMethod.GET, httpEntity, UserResDTO.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void testSearchAdmins_WithAllCorrectValues_ShouldReturnAdminPage() {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        Pageable pageable = PageRequest.of(AdminConstants.PAGE, AdminConstants.PAGE_SIZE);
		int size = adminService.searchAdmins(pageable,AdminConstants.SEARCH_VALUE).getNumberOfElements();
        ParameterizedTypeReference<CustomPageImplementation<UserResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<UserResDTO>>() {
		};
		
        ResponseEntity<CustomPageImplementation<UserResDTO>> responseEntity = restTemplate.exchange("/api/admins/search/" + AdminConstants.SEARCH_VALUE + "?page=" + AdminConstants.PAGE+ "&size=" + AdminConstants.PAGE_SIZE, HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testSearchAdmins_WithNonExistingPageAndExistingSearchValue_ShouldReturnEmptyAdminPage() {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        Pageable pageable = PageRequest.of(AdminConstants.NON_EXISTING_PAGE, AdminConstants.PAGE_SIZE);
		int size = adminService.searchAdmins(pageable, AdminConstants.SEARCH_VALUE).getNumberOfElements();
        ParameterizedTypeReference<CustomPageImplementation<UserResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<UserResDTO>>() {
		};
		
        ResponseEntity<CustomPageImplementation<UserResDTO>> responseEntity = restTemplate.exchange("/api/admins/search/" + AdminConstants.SEARCH_VALUE + "?page=" + AdminConstants.NON_EXISTING_PAGE+ "&size=" + AdminConstants.PAGE_SIZE, HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testSearchAdmins_WithNonExistingSearchValue_ShouldReturnEmptyAdminPage() {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        Pageable pageable = PageRequest.of(AdminConstants.PAGE, AdminConstants.PAGE_SIZE);
		int size = adminService.searchAdmins(pageable,AdminConstants.NON_EXISTING_SEARCH_VALUE).getNumberOfElements();
        ParameterizedTypeReference<CustomPageImplementation<UserResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<UserResDTO>>() {
		};
		
        ResponseEntity<CustomPageImplementation<UserResDTO>> responseEntity = restTemplate.exchange("/api/admins/search/" + AdminConstants.NON_EXISTING_SEARCH_VALUE + "?page=" + AdminConstants.PAGE+ "&size=" + AdminConstants.PAGE_SIZE, HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDelete_WithExistingIdNoCO_ShouldDeleteAdmin() {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        int size = 0;
        for(@SuppressWarnings("unused") Admin a : adminService.getAll()) {
        	size++;
        }
       
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.ADMIN_NO_CO_ID, HttpMethod.DELETE, httpEntity, Boolean.class);
        
        int afterDeleting = 0;
        for(@SuppressWarnings("unused") Admin a : adminService.getAll()) {
        	afterDeleting++;
        }
        
        assertEquals(size - 1, afterDeleting);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody());
	}
	
	@Test
	public void testDelete_WithNonExistingId_ShouldReturnNotFound() throws Exception {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.NON_EXISTING_ADMIN_ID, HttpMethod.DELETE, httpEntity, String.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

	}
	
	@Test
	public void testDelete_WithCulturalOffers_ShouldReturnBadRequest() throws Exception {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.ADMIN_CO_ID, HttpMethod.DELETE, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreate_WithAllCorrectValues_ShouldReturnCreatedAdmin() throws Exception {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		AdminDTO admin = new AdminDTO(AdminConstants.NEW_USERNAME, AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(admin, headers);
        int size = 0;
        for(@SuppressWarnings("unused") Admin a : adminService.getAll()) {
        	size++;
        }
        
        ResponseEntity<UserResDTO> responseEntity = restTemplate.exchange("/api/admins", HttpMethod.POST, httpEntity, UserResDTO.class);
        
        int afterCreating = 0;
        for(@SuppressWarnings("unused") Admin a : adminService.getAll()) {
        	afterCreating++;
        }
        assertEquals(size + 1, afterCreating);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	public void testCreate_WithEmptyUsername_ShouldReturnBadRequest() throws Exception {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		AdminDTO admin = new AdminDTO("", AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(admin, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.ADMIN_CO_ID, HttpMethod.DELETE, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

	}
	
	@Test
	public void testCreate_WithEmptyEmail_ShouldReturnBadRequest() throws Exception {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		AdminDTO admin = new AdminDTO(AdminConstants.NEW_USERNAME, "", AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(admin, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.ADMIN_CO_ID, HttpMethod.DELETE, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

	}
	
	@Test
	public void testCreate_WithEmptyFirstname_ShouldReturnBadRequest() throws Exception {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		AdminDTO admin = new AdminDTO(AdminConstants.NEW_USERNAME, AdminConstants.NEW_EMAIL, "", AdminConstants.LASTNAME);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(admin, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.ADMIN_CO_ID, HttpMethod.DELETE, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

	}
	
	@Test
	public void testCreate_WithEmptyLastname_ShouldReturnBadRequest() throws Exception {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		AdminDTO admin = new AdminDTO(AdminConstants.NEW_USERNAME, AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, "");
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(admin, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.ADMIN_CO_ID, HttpMethod.DELETE, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testCreate_WithExistingUsername_ShouldReturnBadRequest() throws Exception {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		AdminDTO admin = new AdminDTO(AdminConstants.EXISTING_USERNAME, AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(admin, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.ADMIN_CO_ID, HttpMethod.DELETE, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

	}
	
	@Test
	public void testCreate_WithExistingEmail_ShouldReturnBadRequest() throws Exception {
		login(AdminConstants.EXISTING_EMAIL, AdminConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
		AdminDTO admin = new AdminDTO(AdminConstants.NEW_USERNAME, AdminConstants.EXISTING_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(admin, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/admins/" + AdminConstants.ADMIN_CO_ID, HttpMethod.DELETE, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

	}
}
