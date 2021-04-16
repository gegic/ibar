package com.ktsnwt.project.team9.service.integration;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.AdminConstants;
import com.ktsnwt.project.team9.constants.RegisteredUserConstants;
import com.ktsnwt.project.team9.model.CulturalOffer;
import com.ktsnwt.project.team9.model.RegisteredUser;
import com.ktsnwt.project.team9.services.implementations.CulturalOfferService;
import com.ktsnwt.project.team9.services.implementations.RegisteredUserService;

import javassist.NotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class RegisteredUserServiceIntegrationTest {

	@Autowired
	RegisteredUserService regUserService;
	
	@Autowired
	private CulturalOfferService coService;
	
	@Test
	public void testGetAll_ShouldReturnAllRegUsers() {
		List<RegisteredUser>  users = (List<RegisteredUser>) regUserService.getAll();
		
		assertEquals(RegisteredUserConstants.NUMBER_OF_ITEMS, users.size());
	}
	
	@Test
	public void testFindAll_WithExistingPageable_ShouldReturnPageOfRegUsers() {
		Pageable pageable = PageRequest.of(RegisteredUserConstants.PAGE, RegisteredUserConstants.PAGE_SIZE);
		Page<RegisteredUser> userPage = regUserService.findAll(pageable);
		
		assertEquals(5, userPage.getNumberOfElements());
	}
	
	@Test
	public void testFindAll_WithNonExistingPageable_ShouldReturnEmptyPageOfRegUsers() {
		Pageable pageable = PageRequest.of(RegisteredUserConstants.NON_EXISTING_PAGE, RegisteredUserConstants.PAGE_SIZE);
		Page<RegisteredUser> userPage = regUserService.findAll(pageable);
		
		assertEquals(0, userPage.getNumberOfElements());
	}
	
	@Test
	public void testGetById_WithExistingId_ShouldReturnRegUser() {
		RegisteredUser user = regUserService.getById(RegisteredUserConstants.USER_ID);
		
		assertNotNull(user);
		assertEquals(RegisteredUserConstants.USER_ID, user.getId());
	}
	
	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		RegisteredUser user = regUserService.getById(RegisteredUserConstants.NON_EXISTING_USER_ID);
		
		assertNull(user);
	}
	
	@Test
	public void testFindByEmail_WithExistingEmail_ShouldReturnRegUser() {
		RegisteredUser user = regUserService.findByEmail(RegisteredUserConstants.EXISTING_EMAIL);
		
		assertNotNull(user);
		assertEquals(RegisteredUserConstants.EXISTING_EMAIL, user.getEmail());
	}
	
	@Test
	public void testFindByEmail_WithNonExistingEmail_ShouldReturnNull() {
		RegisteredUser user = regUserService.findByEmail(RegisteredUserConstants.NON_EXISTING_EMAIL);
		
		assertNull(user);
	}
	
	@Test
	public void testFindByUsername_WithExistingUsername_ShouldReturnRegUser() {
		RegisteredUser user = regUserService.findByUsername(RegisteredUserConstants.EXISTING_USERNAME);
		
		assertNotNull(user);
		assertEquals(RegisteredUserConstants.EXISTING_USERNAME, user.getUsername());
	}
	
	@Test
	public void testFindByUsername_WithNonExistingUsername_ShouldReturnRegUser() {
		RegisteredUser user = regUserService.findByUsername(RegisteredUserConstants.NON_EXISTING_USERNAME);
		
		assertNull(user);
	}
	
	@Test
	public void testSearchRegUsers_WithAllCorrectValues_ShouldReturnPageOfRegUsers() {
		Pageable pageable = PageRequest.of(RegisteredUserConstants.PAGE, RegisteredUserConstants.PAGE_SIZE);
		Page<RegisteredUser> userPage = regUserService.searchRegUsers(pageable, AdminConstants.PART);
		
		assertEquals(5, userPage.getNumberOfElements());
	}
	
	@Test
	public void testSearchRegUsers_WithNonExistingPageAndExistingValue_ShouldReturnEmptyPageOfRegUsers() {
		Pageable pageable = PageRequest.of(RegisteredUserConstants.NON_EXISTING_PAGE, RegisteredUserConstants.PAGE_SIZE);
		Page<RegisteredUser> userPage = regUserService.searchRegUsers(pageable, AdminConstants.PART);
		
		assertEquals(0, userPage.getNumberOfElements());
	}
	
	@Test
	public void testSearchRegUsers_WithNonExistingPageAndNonExistingValue_ShouldReturnEmptyPageOfRegUsers() {
		Pageable pageable = PageRequest.of(RegisteredUserConstants.PAGE, RegisteredUserConstants.PAGE_SIZE);
		Page<RegisteredUser> userPage = regUserService.searchRegUsers(pageable, AdminConstants.NON_EXISTING_PART);
		
		assertEquals(0, userPage.getNumberOfElements());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDelete_WithExistingId_ShouldReturnTrue() throws Exception {
		List<RegisteredUser> users = (List<RegisteredUser>) regUserService.getAll();
		int number = users.size();
		boolean status = regUserService.delete(RegisteredUserConstants.USER_ID);
		List<RegisteredUser> userDeleted = (List<RegisteredUser>) regUserService.getAll();
		int afterDeleting = userDeleted.size();
		assertTrue(status);
		assertEquals(number - 1, afterDeleting);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testDelete_WithNonExistingId_ShouldThrowNoSuchElementException() throws Exception {
		regUserService.delete(RegisteredUserConstants.NON_EXISTING_USER_ID);
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreate_WithAllCorrectValues_ShouldReturnCreatedRegUser() throws Exception {
		List<RegisteredUser> users = (List<RegisteredUser>) regUserService.getAll();
		int number = users.size();
		RegisteredUser a = new RegisteredUser(RegisteredUserConstants.NEW_USERNAME, RegisteredUserConstants.NEW_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		RegisteredUser created = regUserService.create(a);
		List<RegisteredUser> userCreated = (List<RegisteredUser>) regUserService.getAll();
		int afterCreating = userCreated.size();
		
		assertNotNull(created);
		assertEquals(number + 1, afterCreating);
		assertFalse(created.isVerified());
		assertEquals(RegisteredUserConstants.NEW_USERNAME, created.getUsername());
		assertEquals(RegisteredUserConstants.NEW_EMAIL, created.getEmail());
		assertEquals(RegisteredUserConstants.FIRSTNAME, created.getFirstName());
		assertEquals(RegisteredUserConstants.LASTNAME, created.getLastName());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingUsername_ShouldThrowIllegalArgumentException() throws Exception {
		RegisteredUser user = new RegisteredUser(RegisteredUserConstants.EXISTING_USERNAME, RegisteredUserConstants.NEW_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		regUserService.create(user);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingEmail_ShouldThrowIllegalArgumentException() throws Exception {
		RegisteredUser user = new RegisteredUser(RegisteredUserConstants.NEW_USERNAME, RegisteredUserConstants.EXISTING_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		regUserService.create(user);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingEmailAndExistingUsername_ShouldThrowIllegalArgumentException() throws Exception {
		RegisteredUser user = new RegisteredUser(RegisteredUserConstants.EXISTING_USERNAME, RegisteredUserConstants.EXISTING_EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		regUserService.create(user);
	}
	
	@Test
	public void testGetSubscribed_WithValidCulturalOffer_ShouldReturnUsers() {
		CulturalOffer co = coService.getById(RegisteredUserConstants.CULTURAL_OFFER_ID);
		List<RegisteredUser> users = regUserService.getSubscribed(co);
		
		assertNotNull(users);
		assertEquals(RegisteredUserConstants.SUBSCRIBE_NO, users.size());
	}
	
	@Test
	public void testIsSubscribed_WithValidEmailAndCOIDAndSubscribed_ShouldReturnTrue() throws NotFoundException {
		boolean res = regUserService.isSubscribed(RegisteredUserConstants.SUBSCRIBE_EMAIL, 1L);
		assertTrue(res);
	}
	
	@Test
	public void testIsSubscribed_WithValidEmailAndCOIDAndNotSubscribed_ShouldReturnFalse() throws NotFoundException {
		boolean res = regUserService.isSubscribed(RegisteredUserConstants.SUBSCRIBE_EMAIL, 5L);
		assertFalse(res);
	}
	
	@Test(expected = NotFoundException.class)
	public void testIsSubscribed_WithInvalidEmail_ShouldThrowNotFoundException() throws NotFoundException {
		regUserService.isSubscribed("RNG_EMAIL", 1L);
	}

}
