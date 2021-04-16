package com.ktsnwt.project.team9.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.UserConstants;
import com.ktsnwt.project.team9.dto.UserDTO;
import com.ktsnwt.project.team9.dto.UserLoginDTO;
import com.ktsnwt.project.team9.dto.response.UserTokenStateDTO;
import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.services.implementations.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class UserControllerIntegrationTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private String accessToken;
	
	public void login(String username, String password) {
		ResponseEntity<UserTokenStateDTO> responseEntity = restTemplate.postForEntity("/auth/login", new UserLoginDTO(username, password), UserTokenStateDTO.class); 
		accessToken = "Bearer " + responseEntity.getBody().getAccessToken(); 
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testChangeProfile_WithDifferentNonExistingUsername_ShouldChangeProfile() {
		login(UserConstants.EMAIL, UserConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        User forUpdate = userService.findByEmail(UserConstants.EMAIL);
        UserDTO updated = new UserDTO(UserConstants.USERNAME_UPDATED, forUpdate.getEmail(), forUpdate.getPassword(), UserConstants.FIRST_NAME_UPDATED, UserConstants.LAST_NAME_UPDATED);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(updated, headers);

        ResponseEntity<User> responseEntity = restTemplate.exchange("/api/users/change-profile", HttpMethod.PUT, httpEntity, User.class);
        
        User user = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(UserConstants.USERNAME_UPDATED, user.getUsername());
        assertEquals(UserConstants.FIRST_NAME_UPDATED, user.getFirstName());
        assertEquals(UserConstants.LAST_NAME_UPDATED, user.getLastName());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testChangeProfile_WithSameUsername_ShouldChangeProfile() {
		login(UserConstants.EMAIL, UserConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        User forUpdate = userService.findByEmail(UserConstants.EMAIL);
        UserDTO updated = new UserDTO(UserConstants.USERNAME, forUpdate.getEmail(), forUpdate.getPassword(), UserConstants.FIRST_NAME_UPDATED, UserConstants.LAST_NAME_UPDATED);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(updated, headers);

        ResponseEntity<User> responseEntity = restTemplate.exchange("/api/users/change-profile", HttpMethod.PUT, httpEntity, User.class);
        
        User user = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(UserConstants.USERNAME, user.getUsername());
        assertEquals(UserConstants.FIRST_NAME_UPDATED, user.getFirstName());
        assertEquals(UserConstants.LAST_NAME_UPDATED, user.getLastName());
	}
	
	@Test
	public void testChangeProfile_WithDifferentExistingUsername_ShouldReturnBadRequest() {
		login(UserConstants.EMAIL, UserConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        User forUpdate = userService.findByEmail(UserConstants.EMAIL);
        UserDTO updated = new UserDTO(UserConstants.USERNAME2, forUpdate.getEmail(), forUpdate.getPassword(), UserConstants.FIRST_NAME_UPDATED, UserConstants.LAST_NAME_UPDATED);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(updated, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/users/change-profile", HttpMethod.PUT, httpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testChangeProfile_WithEmptyFirstName_ShouldReturnBadRequest() {
		login(UserConstants.EMAIL, UserConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        User forUpdate = userService.findByEmail(UserConstants.EMAIL);
        UserDTO updated = new UserDTO(UserConstants.USERNAME_UPDATED, forUpdate.getEmail(), forUpdate.getPassword(), "", UserConstants.LAST_NAME_UPDATED);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(updated, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/users/change-profile", HttpMethod.PUT, httpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testChangeProfile_WithEmptyLastName_ShouldReturnBadRequest() {
		login(UserConstants.EMAIL, UserConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        
        User forUpdate = userService.findByEmail(UserConstants.EMAIL);
        UserDTO updated = new UserDTO(UserConstants.USERNAME_UPDATED, forUpdate.getEmail(), forUpdate.getPassword(), UserConstants.FIRST_NAME_UPDATED, "");
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(updated, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/users/change-profile", HttpMethod.PUT, httpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
}
