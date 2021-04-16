package com.ktsnwt.project.team9.service.unit;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.AdminConstants;
import com.ktsnwt.project.team9.constants.RegisteredUserConstants;
import com.ktsnwt.project.team9.model.RegisteredUser;
import com.ktsnwt.project.team9.repositories.IRegisteredUser;
import com.ktsnwt.project.team9.repositories.IUserRepository;
import com.ktsnwt.project.team9.services.implementations.AuthorityService;
import com.ktsnwt.project.team9.services.implementations.RegisteredUserService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class RegisteredUserServiceUnitTest {

	@Autowired
	RegisteredUserService regService;
	
	@MockBean
	IRegisteredUser registeredUserRepository;
	
	@MockBean
	IUserRepository userRepository;
	
	@MockBean
	AuthorityService authService;
	
	@MockBean
	PasswordEncoder passwordEncoder;
	
	@Before
	public void setup() throws Exception {
		RegisteredUser userSaved = new RegisteredUser(RegisteredUserConstants.USERNAME, RegisteredUserConstants.EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		userSaved.setId(RegisteredUserConstants.USER_ID);
		
		RegisteredUser sameUsername = new RegisteredUser(RegisteredUserConstants.EXISTING_USERNAME, RegisteredUserConstants.NEW_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		RegisteredUser sameEmail = new RegisteredUser(RegisteredUserConstants.NEW_USERNAME, RegisteredUserConstants.EXISTING_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		
		RegisteredUser newuser = new RegisteredUser(RegisteredUserConstants.NEW_USERNAME, RegisteredUserConstants.NEW_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME); 
		newuser.setAuthorities(RegisteredUserConstants.AUTHORITIES);
		newuser.setPassword(RegisteredUserConstants.EPASSWORD);
		
		RegisteredUser newuserSaved = new RegisteredUser(RegisteredUserConstants.NEW_USERNAME, RegisteredUserConstants.NEW_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME); 
		newuserSaved.setAuthorities(RegisteredUserConstants.AUTHORITIES);
		newuserSaved.setPassword(RegisteredUserConstants.EPASSWORD);
		newuserSaved.setId(RegisteredUserConstants.NEW_USER_ID);
		
		given(userRepository.findByUsername(RegisteredUserConstants.EXISTING_USERNAME)).willReturn(sameUsername);
		given(userRepository.findByEmail(RegisteredUserConstants.EXISTING_EMAIL)).willReturn(sameEmail);
		
		given(registeredUserRepository.findById(RegisteredUserConstants.USER_ID)).willReturn(Optional.of(userSaved));
		given(registeredUserRepository.findById(RegisteredUserConstants.NON_EXISTING_USER_ID)).willReturn(Optional.empty());
		
		given(userRepository.findByUsername(RegisteredUserConstants.NEW_USERNAME)).willReturn(null);
		given(userRepository.findByEmail(RegisteredUserConstants.NEW_EMAIL)).willReturn(null);
		given(authService.findByName(RegisteredUserConstants.ROLE)).willReturn(RegisteredUserConstants.AUTHORITIES);
		given(passwordEncoder.encode(RegisteredUserConstants.PASSWORD)).willReturn(RegisteredUserConstants.EPASSWORD);
		
		doNothing().when(registeredUserRepository).deleteById(RegisteredUserConstants.USER_ID);
		
		given(registeredUserRepository.save(newuser)).willReturn(newuserSaved);
	}
	
	@Test
	public void testGetById_WithExistingId_ShouldReturnRegisteredUser() {
		RegisteredUser r = regService.getById(RegisteredUserConstants.USER_ID);
		
		verify(registeredUserRepository, times(1)).findById(RegisteredUserConstants.USER_ID);
		assertEquals(RegisteredUserConstants.USER_ID, r.getId());
	}
	
	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNUll() {
		RegisteredUser r = regService.getById(RegisteredUserConstants.NON_EXISTING_USER_ID);
		
		verify(registeredUserRepository, times(1)).findById(RegisteredUserConstants.NON_EXISTING_USER_ID);
		assertEquals(null, r);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingUsername_ShouldThrowIllegalArgumentException() throws Exception {
		RegisteredUser sameUsername = new RegisteredUser(RegisteredUserConstants.EXISTING_USERNAME, RegisteredUserConstants.NEW_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		regService.create(sameUsername);
		
		verify(userRepository, times(1)).findByUsername(RegisteredUserConstants.EXISTING_USERNAME);
		verify(userRepository, times(0)).findByEmail(AdminConstants.NEW_EMAIL);
		verify(userRepository, times(0)).findByUsername(AdminConstants.EXISTING_USERNAME);
		verify(authService, times(0)).findByName(RegisteredUserConstants.ROLE);
		verify(passwordEncoder, times(0)).encode(RegisteredUserConstants.PASSWORD);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingEmail_ShouldThrowIllegalArgumentException() throws Exception {
		RegisteredUser sameEmail = new RegisteredUser(RegisteredUserConstants.NEW_USERNAME, RegisteredUserConstants.EXISTING_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		regService.create(sameEmail);
		
		verify(userRepository, times(1)).findByEmail(RegisteredUserConstants.EXISTING_EMAIL);
		verify(userRepository, times(1)).findByUsername(AdminConstants.NEW_USERNAME);
		verify(userRepository, times(0)).findByUsername(AdminConstants.EXISTING_USERNAME);
		verify(authService, times(0)).findByName(RegisteredUserConstants.ROLE);
		verify(passwordEncoder, times(0)).encode(RegisteredUserConstants.PASSWORD);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingUsernameAndEmail_ShouldThrowIllegalArgumentException() throws Exception {
		RegisteredUser sameUsernameAndMail = new RegisteredUser(RegisteredUserConstants.EXISTING_USERNAME, RegisteredUserConstants.EXISTING_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		regService.create(sameUsernameAndMail);
		
		verify(userRepository, times(1)).findByUsername(RegisteredUserConstants.EXISTING_USERNAME);
		verify(userRepository, times(0)).findByEmail(AdminConstants.EXISTING_EMAIL);
		verify(userRepository, times(0)).findByUsername(AdminConstants.EXISTING_USERNAME);
		verify(authService, times(0)).findByName(RegisteredUserConstants.ROLE);
		verify(passwordEncoder, times(0)).encode(RegisteredUserConstants.PASSWORD);
	}
	
	@Test
	public void testCreate_WithAllCorrectValues_ShouldCreateAdmin() throws Exception {
		RegisteredUser newuser = new RegisteredUser(RegisteredUserConstants.NEW_USERNAME, RegisteredUserConstants.NEW_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME); 
		RegisteredUser saved = regService.create(newuser);
		
		verify(userRepository, times(1)).findByEmail(AdminConstants.NEW_EMAIL);
		verify(userRepository, times(1)).findByUsername(AdminConstants.NEW_USERNAME);
		verify(authService, times(1)).findByName(RegisteredUserConstants.ROLE);
		verify(passwordEncoder, times(1)).encode(RegisteredUserConstants.PASSWORD);
		assertEquals(RegisteredUserConstants.NEW_EMAIL, saved.getEmail());
		assertEquals(RegisteredUserConstants.NEW_USERNAME, saved.getUsername());
		assertEquals(RegisteredUserConstants.FIRSTNAME, saved.getFirstName());
		assertEquals(RegisteredUserConstants.LASTNAME, saved.getLastName());
		assertEquals(RegisteredUserConstants.EPASSWORD, saved.getPassword());
		assertEquals(RegisteredUserConstants.AUTHORITIES, saved.getAuthorities());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testDelete_WithNonExistingId_ShouldThrowNoSuchElementException() throws Exception {
		regService.delete(RegisteredUserConstants.NON_EXISTING_USER_ID);
		
		verify(registeredUserRepository, times(1)).findById(RegisteredUserConstants.NON_EXISTING_USER_ID);
		verify(registeredUserRepository, times(0)).deleteById(RegisteredUserConstants.NON_EXISTING_USER_ID);
	}
	
	@Test
	public void testDelete_WithExistingId_ShouldDeleteRegisteredUser() throws Exception {
		boolean status = regService.delete(RegisteredUserConstants.USER_ID);
		
		verify(registeredUserRepository, times(1)).findById(RegisteredUserConstants.USER_ID);
		verify(registeredUserRepository, times(1)).deleteById(RegisteredUserConstants.USER_ID);
		assertTrue(status);
	}
}
