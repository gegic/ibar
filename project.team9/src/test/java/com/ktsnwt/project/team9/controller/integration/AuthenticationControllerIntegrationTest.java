package com.ktsnwt.project.team9.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
import com.ktsnwt.project.team9.dto.response.UserResDTO;
import com.ktsnwt.project.team9.dto.response.UserTokenStateDTO;
import com.ktsnwt.project.team9.services.implementations.RegisteredUserService;
import com.ktsnwt.project.team9.services.implementations.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class AuthenticationControllerIntegrationTest {

	@Autowired
	UserService userService;
	
	@Autowired
	RegisteredUserService registeredUserService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private String accessToken;
	
	public void login(String username, String password) {
		ResponseEntity<UserTokenStateDTO> responseEntity = restTemplate.postForEntity("/auth/login", new UserLoginDTO(username, password), UserTokenStateDTO.class); 
		accessToken = "Bearer " + responseEntity.getBody().getAccessToken(); 
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testLogin_WithCorrectCredentials_ShouldReturnToken() {
		UserLoginDTO credentials = new UserLoginDTO(UserConstants.EMAIL, UserConstants.PASSWORD);
		
		ResponseEntity<UserTokenStateDTO> responseEntity = restTemplate.postForEntity("/auth/login", credentials, UserTokenStateDTO.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	public void testLogin_WithIncorrectCredentials_ShouldReturnUnauthorized() {
		UserLoginDTO credentials = new UserLoginDTO(UserConstants.EMAIL, UserConstants.WRONG_PASSWORD);
		
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/auth/login", credentials, String.class);
		
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	public void testLogin_WithNonVerifiedUser_ShouldReturnBadRequest() {
		UserLoginDTO credentials = new UserLoginDTO(UserConstants.EMAIL_NOT_VERIFIED, UserConstants.PASSWORD);
		
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/auth/login", credentials, String.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testForgotPassword_WithExistingEmail_ShouldChangePassword() {
		PasswordReset pr = new PasswordReset(UserConstants.EMAIL);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/auth/forgot-password", pr, String.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	public void testForgotPassword_WithNonExistingEmail_ShouldReturnBadRequest() {
		PasswordReset pr = new PasswordReset(UserConstants.NON_EXISTING_EMAIL);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/auth/forgot-password", pr, String.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testChangePassword_WithCorrectOldPassword_ShouldChangePassword() {
		login(UserConstants.EMAIL, UserConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
		PasswordChanger pc = new PasswordChanger(UserConstants.PASSWORD, UserConstants.PASSWORD_UPDATED);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(pc, headers);
		String before = userService.findByEmail(UserConstants.EMAIL).getPassword();
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/auth/change-password", HttpMethod.POST, httpEntity, String.class);
		String after = userService.findByEmail(UserConstants.EMAIL).getPassword();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotEquals(before, after);
	}
	
	@Test
	public void testChangePassword_WithIncorrectOldPassword_ShouldReturnBadRequest() {
		login(UserConstants.EMAIL, UserConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
		PasswordChanger pc = new PasswordChanger(UserConstants.WRONG_PASSWORD, UserConstants.PASSWORD_UPDATED);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(pc, headers);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/auth/change-password", HttpMethod.POST, httpEntity, String.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testChangePassword_NotLoggedIn_ShouldReturnBadRequest() {
		PasswordChanger pc = new PasswordChanger(UserConstants.WRONG_PASSWORD, UserConstants.PASSWORD_UPDATED);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(pc);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/auth/change-password", HttpMethod.POST, httpEntity, String.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testConfirmRegistration_WithAllCorrectValues_ShouldConfirmRegistration() {
		boolean before = registeredUserService.findByEmail(UserConstants.EMAIL_NOT_VERIFIED).isVerified();
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/auth/confirm-registration/" + UserConstants.TOKEN, String.class);
		boolean after = registeredUserService.findByEmail(UserConstants.EMAIL).isVerified();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(false, before);
		assertEquals(true, after);
	}
	
	@Test
	public void testConfirmRegistration_WithNonExistingToken_ShouldReturnNotFound() {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/auth/confirm-registration/" + UserConstants.NON_EXISTING_TOKEN, String.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void testCurrentUser_LoggedIn_ShouldReturnCurrentUser() {
		login(UserConstants.EMAIL, UserConstants.PASSWORD);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		
		ResponseEntity<UserResDTO> responseEntity = restTemplate.exchange("/auth/current-user", HttpMethod.GET, httpEntity, UserResDTO.class);
		UserResDTO user = responseEntity.getBody();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(UserConstants.EMAIL, user.getEmail());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testSignUp_WithAllCorrectValues_ShouldConfirmRegistration() {
		UserDTO newUser = new UserDTO(UserConstants.NON_EXISTING_USERNAME, UserConstants.NON_EXISTING_EMAIL, UserConstants.PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		ResponseEntity<UserResDTO> responseEntity = restTemplate.postForEntity("/auth/sign-up/", newUser, UserResDTO.class);
		
		UserResDTO user = responseEntity.getBody();
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(UserConstants.NON_EXISTING_EMAIL, user.getEmail());
		assertEquals(UserConstants.NON_EXISTING_USERNAME, user.getUsername());
		assertEquals(UserConstants.FIRST_NAME, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME, user.getLastName());
	}
	
	@Test
	public void testSignUp_WithExistingUsername_ShouldReturnConflict() {
		UserDTO newUser = new UserDTO(UserConstants.USERNAME, UserConstants.NON_EXISTING_EMAIL, UserConstants.PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/auth/sign-up/", newUser, String.class);
		
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
	}
	
	@Test
	public void testSignUp_WithExistingEmail_ShouldReturnConflict() {
		UserDTO newUser = new UserDTO(UserConstants.NON_EXISTING_USERNAME, UserConstants.EMAIL, UserConstants.PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/auth/sign-up/", newUser, String.class);
		
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
	}
	
	@Test
	public void testSignUp_WithShorterPassword_ShouldReturnBadRequest() {
		UserDTO newUser = new UserDTO(UserConstants.NON_EXISTING_USERNAME, UserConstants.NON_EXISTING_EMAIL, UserConstants.SHORT_PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		ResponseEntity<UserResDTO> responseEntity = restTemplate.postForEntity("/auth/sign-up/", newUser, UserResDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testSignUp_WithEmptyLastName_ShouldReturnBadRequest() {
		UserDTO newUser = new UserDTO(UserConstants.NON_EXISTING_USERNAME, UserConstants.NON_EXISTING_EMAIL, UserConstants.PASSWORD, "", UserConstants.LAST_NAME);
		ResponseEntity<UserResDTO> responseEntity = restTemplate.postForEntity("/auth/sign-up/", newUser, UserResDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testSignUp_WithEmptyFirstName_ShouldReturnBadRequest() {
		UserDTO newUser = new UserDTO(UserConstants.NON_EXISTING_USERNAME, UserConstants.NON_EXISTING_EMAIL, UserConstants.PASSWORD, UserConstants.FIRST_NAME, "");
		ResponseEntity<UserResDTO> responseEntity = restTemplate.postForEntity("/auth/sign-up/", newUser, UserResDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void testSignUp_WithEmptyEmail_ShouldReturnBadRequest() {
		UserDTO newUser = new UserDTO(UserConstants.NON_EXISTING_USERNAME, "", UserConstants.PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		ResponseEntity<UserResDTO> responseEntity = restTemplate.postForEntity("/auth/sign-up/", newUser, UserResDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	static class PasswordReset {
		private String email;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	static class PasswordChanger {
		private String oldPassword;
		private String newPassword;
	}
	
}
