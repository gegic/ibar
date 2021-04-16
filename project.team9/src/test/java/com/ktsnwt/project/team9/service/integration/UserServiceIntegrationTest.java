package com.ktsnwt.project.team9.service.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.UserConstants;
import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.services.implementations.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class UserServiceIntegrationTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Before
	public void setup() throws Exception {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(UserConstants.EMAIL, UserConstants.PASSWORD));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Test
	public void testFindByEmail_WithExistingEmail_ShouldReturnUser() {
		User user = userService.findByEmail(UserConstants.EMAIL);
		
		assertNotNull(user);
		assertEquals(UserConstants.EMAIL, user.getEmail());
	}
	
	@Test
	public void testFindByEmail_WithNonExistingEmail_ShouldReturnNull() {
		User user = userService.findByEmail(UserConstants.NON_EXISTING_EMAIL);
		
		assertNull(user);
	}
	
	@Test
	public void testFindByUsername_WithExistingUsername_ShouldReturnUser() {
		User user = userService.findByUsername(UserConstants.USERNAME);
		
		assertNotNull(user);
		assertEquals(UserConstants.USERNAME, user.getUsername());
	}
	
	@Test
	public void testFindByUsername_WithNonExistingUsername_ShouldReturnNull() {
		User user = userService.findByUsername(UserConstants.NON_EXISTING_USERNAME);
		
		assertNull(user);
	}
	
	@Test
	public void testLoadUserByUsername_WithExistingEmail_ShouldReturnUser() {
		User user = (User) userService.loadUserByUsername(UserConstants.EMAIL);
		
		assertNotNull(user);
		assertEquals(UserConstants.EMAIL, user.getEmail());
	}

	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsername_WithNonExistingEmail_ShouldThrowUsernameNotFoundException() {
		userService.loadUserByUsername(UserConstants.NON_EXISTING_EMAIL);
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testForgotPassword_WithExistingEmail_ShouldChangePassword() {
		String passwordBefore = userService.findByEmail(UserConstants.EMAIL).getPassword();
		userService.forgotPassword(UserConstants.EMAIL);
		String passwordAfter = userService.findByEmail(UserConstants.EMAIL).getPassword();
		
		assertNotEquals(passwordBefore, passwordAfter);
	}
	
	@Test(expected = UsernameNotFoundException.class)
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testForgotPassword_WithNonExistingEmail_ShouldThrowUsernameNotFoundException() {
		userService.forgotPassword(UserConstants.NON_EXISTING_EMAIL);
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testChangeProfile_WithDifferentNonExistingUsername_ShouldChangeProfile() throws Exception {
		User user = userService.findByEmail(UserConstants.EMAIL);
		user.setUsername(UserConstants.USERNAME_UPDATED);
		user.setFirstName(UserConstants.FIRST_NAME_UPDATED);
		user.setLastName(UserConstants.LAST_NAME_UPDATED);
		User changed = userService.changeProfile(user);
		
		assertEquals(UserConstants.USERNAME_UPDATED, changed.getUsername());
		assertEquals(UserConstants.FIRST_NAME_UPDATED, changed.getFirstName());
		assertEquals(UserConstants.LAST_NAME_UPDATED, changed.getLastName());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testChangeProfile_WithSameUsername_ShouldChangeProfile() throws Exception {
		User user = userService.findByEmail(UserConstants.EMAIL);
		user.setFirstName(UserConstants.FIRST_NAME_UPDATED);
		user.setLastName(UserConstants.LAST_NAME_UPDATED);
		User changed = userService.changeProfile(user);
		
		assertEquals(UserConstants.USERNAME, changed.getUsername());
		assertEquals(UserConstants.FIRST_NAME_UPDATED, changed.getFirstName());
		assertEquals(UserConstants.LAST_NAME_UPDATED, changed.getLastName());
	}
	
	@Test(expected = IllegalArgumentException.class)
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testChangeProfile_WithDifferentExistingUsername_ShouldThrowIllegalArgumentException() throws Exception {
		User user = userService.findByEmail(UserConstants.EMAIL);
		user.setUsername(UserConstants.USERNAME2);
		user.setFirstName(UserConstants.FIRST_NAME_UPDATED);
		user.setLastName(UserConstants.LAST_NAME_UPDATED);
		userService.changeProfile(user);
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testChangePassword_WithAllCorrectValues_ShouldChangePassword() throws Exception {
		userService.changePassword(UserConstants.PASSWORD, UserConstants.PASSWORD_UPDATED);
		User user = userService.findByEmail(UserConstants.EMAIL);
		
		assertNotEquals(UserConstants.PASSWORD, user.getPassword());
	}
	
	@Test(expected = BadCredentialsException.class)
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testChangePassword_WithWrongOldPassword_ShouldThrowBadCredentialsException() throws Exception {
		userService.changePassword(UserConstants.WRONG_PASSWORD, UserConstants.PASSWORD_UPDATED);
	}

}
