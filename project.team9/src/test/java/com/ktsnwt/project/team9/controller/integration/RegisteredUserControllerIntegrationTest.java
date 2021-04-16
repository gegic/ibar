package com.ktsnwt.project.team9.controller.integration;

import static org.junit.Assert.assertNotNull;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.RegisteredUserConstants;
import com.ktsnwt.project.team9.dto.UserLoginDTO;
import com.ktsnwt.project.team9.dto.response.UserResDTO;
import com.ktsnwt.project.team9.dto.response.UserTokenStateDTO;
import com.ktsnwt.project.team9.helper.implementations.CustomPageImplementation;
import com.ktsnwt.project.team9.services.implementations.RegisteredUserService;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class RegisteredUserControllerIntegrationTest {
	
	@Autowired
	RegisteredUserService regService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private String accessToken;
	
	public void login(String username, String password) {
		ResponseEntity<UserTokenStateDTO> responseEntity = restTemplate.postForEntity("/auth/login", new UserLoginDTO(username, password), UserTokenStateDTO.class); 
		accessToken = "Bearer " + responseEntity.getBody().getAccessToken(); 
	}
	
	@Test
	public void testGetAllRegisteredUsers_WithLoginAsAdmin_ShouldReturnAllAdmins() {
		login(RegisteredUserConstants.ADMIN_EMAIL, RegisteredUserConstants.EXISTING_PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

        ResponseEntity<UserResDTO[]> responseEntity = restTemplate.exchange("/api/registered-users", HttpMethod.GET, httpEntity, UserResDTO[].class);

        UserResDTO[] users = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(RegisteredUserConstants.NUMBER_OF_ITEMS, users.length);
	}
	
	@Test
	public void testGetAllRegUsers_WithExistingPageable_ShouldReturnRegUserPage() {
		login(RegisteredUserConstants.ADMIN_EMAIL, RegisteredUserConstants.EXISTING_PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        
		Pageable pageable = PageRequest.of(RegisteredUserConstants.PAGE, RegisteredUserConstants.PAGE_SIZE);
		int size = regService.findAll(pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<UserResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<UserResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<UserResDTO>> responseEntity = restTemplate.exchange("/api/registered-users/by-page?page=" + RegisteredUserConstants.PAGE+ "&size=" + RegisteredUserConstants.PAGE_SIZE, HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetRegUsers_WithNonExistingPageable_ShouldReturnEmptyCollection() {
		login(RegisteredUserConstants.ADMIN_EMAIL, RegisteredUserConstants.EXISTING_PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        
		Pageable pageable = PageRequest.of(RegisteredUserConstants.NON_EXISTING_PAGE, RegisteredUserConstants.PAGE_SIZE);
		int size = regService.findAll(pageable).getNumberOfElements();
		ParameterizedTypeReference<CustomPageImplementation<UserResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<UserResDTO>>() {
		};

		ResponseEntity<CustomPageImplementation<UserResDTO>> responseEntity = restTemplate.exchange("/api/registered-users/by-page?page=" + RegisteredUserConstants.NON_EXISTING_PAGE + "&size=" + RegisteredUserConstants.PAGE_SIZE, HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testGetRegUser_WithExistingId_ShouldReturnRegUser() {
		login(RegisteredUserConstants.EXISTING_EMAIL, RegisteredUserConstants.EXISTING_PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        
		ResponseEntity<UserResDTO> responseEntity = restTemplate.exchange("/api/registered-users/" + RegisteredUserConstants.USER_ID, HttpMethod.GET, httpEntity, UserResDTO.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(RegisteredUserConstants.EXISTING_EMAIL, responseEntity.getBody().getEmail());
	}

	@Test
	public void testGetRegUser_WithNonExistingId_ShouldReturnNotFound() {
		login(RegisteredUserConstants.EXISTING_EMAIL, RegisteredUserConstants.EXISTING_PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        
		ResponseEntity<UserResDTO> responseEntity = restTemplate.exchange("/api/registered-user/" + RegisteredUserConstants.NON_EXISTING_USER_ID, HttpMethod.GET, httpEntity, UserResDTO.class);

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void testSearchRegUserss_WithAllCorrectValues_ShouldReturnRegUserPage() {
		login(RegisteredUserConstants.ADMIN_EMAIL, RegisteredUserConstants.EXISTING_PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        Pageable pageable = PageRequest.of(RegisteredUserConstants.PAGE, RegisteredUserConstants.PAGE_SIZE);
		int size = regService.searchRegUsers(pageable, RegisteredUserConstants.SEARCH_VALUE).getNumberOfElements();
        ParameterizedTypeReference<CustomPageImplementation<UserResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<UserResDTO>>() {
		};
		
        ResponseEntity<CustomPageImplementation<UserResDTO>> responseEntity = restTemplate.exchange("/api/registered-users/search/" + RegisteredUserConstants.SEARCH_VALUE + "?page=" + RegisteredUserConstants.PAGE+ "&size=" + RegisteredUserConstants.PAGE_SIZE, HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testSearchRegUsers_WithNonExistingPageAndExistingSearchValue_ShouldReturnEmptyRegUserPage() {
		login(RegisteredUserConstants.ADMIN_EMAIL, RegisteredUserConstants.EXISTING_PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        Pageable pageable = PageRequest.of(RegisteredUserConstants.NON_EXISTING_PAGE, RegisteredUserConstants.PAGE_SIZE);
		int size = regService.searchRegUsers(pageable, RegisteredUserConstants.SEARCH_VALUE).getNumberOfElements();
        ParameterizedTypeReference<CustomPageImplementation<UserResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<UserResDTO>>() {
		};
		
        ResponseEntity<CustomPageImplementation<UserResDTO>> responseEntity = restTemplate.exchange("/api/registered-users/search/" + RegisteredUserConstants.SEARCH_VALUE + "?page=" + RegisteredUserConstants.NON_EXISTING_PAGE+ "&size=" + RegisteredUserConstants.PAGE_SIZE, HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testSearchRegUsers_WithNonExistingSearchValue_ShouldReturnEmptyRegUserPage() {
		login(RegisteredUserConstants.ADMIN_EMAIL, RegisteredUserConstants.EXISTING_PASSWORD);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        Pageable pageable = PageRequest.of(RegisteredUserConstants.PAGE, RegisteredUserConstants.PAGE_SIZE);
		int size = regService.searchRegUsers(pageable, RegisteredUserConstants.NON_EXISTING_SEARCH_VALUE).getNumberOfElements();
        ParameterizedTypeReference<CustomPageImplementation<UserResDTO>> type = new ParameterizedTypeReference<CustomPageImplementation<UserResDTO>>() {
		};
		
        ResponseEntity<CustomPageImplementation<UserResDTO>> responseEntity = restTemplate.exchange("/api/registered-users/search/" + RegisteredUserConstants.NON_EXISTING_SEARCH_VALUE + "?page=" + RegisteredUserConstants.PAGE+ "&size=" + RegisteredUserConstants.PAGE_SIZE, HttpMethod.GET, httpEntity, type);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(size, responseEntity.getBody().getNumberOfElements());
	}
	
	@Test
	public void testIsSubscribed_WithExistingEmailAndCOIDAndSubscribed_ShouldReturnTrue() throws NotFoundException {
		boolean isSub = regService.isSubscribed(RegisteredUserConstants.SUBSCRIBE_EMAIL, 1L);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/registered-users/is-subscibed/" + 
				RegisteredUserConstants.SUBSCRIBE_EMAIL + '/' + 1, HttpMethod.POST, null ,String.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(isSub, Boolean.parseBoolean(responseEntity.getBody()));
	}
	
	@Test
	public void testIsSubscribed_WithNotExistingEmail_ShouldReturnBadRequest() {
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/registered-users/is-subscibed/" + 
				"RNG_EMAIL" + '/' + 1, HttpMethod.POST, null ,String.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testIsSubscribed_WithExistingEmailAndCOIDAndNotSubscribed_ShouldReturnFalse() throws NotFoundException {
		boolean isSub = regService.isSubscribed(RegisteredUserConstants.EXISTING_EMAIL, 1000L);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/registered-users/is-subscibed/" + 
				RegisteredUserConstants.SUBSCRIBE_EMAIL + '/' + 1000, HttpMethod.POST, null ,String.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(isSub, Boolean.parseBoolean(responseEntity.getBody()));
	}
	
}
