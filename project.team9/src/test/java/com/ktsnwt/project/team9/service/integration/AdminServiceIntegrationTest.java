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
import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.services.implementations.AdminService;

import static org.junit.Assert.assertEquals;
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
public class AdminServiceIntegrationTest {

	@Autowired
	AdminService adminService;
	
	@Test
	public void testGetAll_ShouldReturnAllAdmins() {
		List<Admin> admins = (List<Admin>) adminService.getAll();
		
		assertEquals(AdminConstants.NUMBER_OF_ITEMS, admins.size());
	}
	
	@Test
	public void testFindAll_WithExistingPageable_ShouldReturnPageOfAdmins() {
		Pageable pageable = PageRequest.of(AdminConstants.PAGE, AdminConstants.PAGE_SIZE);
		Page<Admin> adminPage = adminService.findAll(pageable);
		
		assertEquals(5, adminPage.getNumberOfElements());
	}
	
	@Test
	public void testFindAll_WithNonExistingPageable_ShouldReturnEmptyPageOfAdmins() {
		Pageable pageable = PageRequest.of(AdminConstants.NON_EXISTING_PAGE, AdminConstants.PAGE_SIZE);
		Page<Admin> adminPage = adminService.findAll(pageable);
		
		assertEquals(0, adminPage.getNumberOfElements());
	}
	
	@Test
	public void testGetById_WithExistingId_ShouldReturnAdmin() {
		Admin admin = adminService.getById(AdminConstants.ADMIN_ID);
		
		assertNotNull(admin);
		assertEquals(AdminConstants.ADMIN_ID, admin.getId());
	}
	
	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNull() {
		Admin admin = adminService.getById(AdminConstants.NON_EXISTING_ADMIN_ID);
		
		assertNull(admin);
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testDelete_WithExistingIdAndNoCulturalOffers_ShouldReturnTrue() throws Exception {
		List<Admin> admins = (List<Admin>) adminService.getAll();
		int number = admins.size();
		boolean status = adminService.delete(AdminConstants.ADMIN_NO_CO_ID);
		List<Admin> adminDeleted = (List<Admin>) adminService.getAll();
		int afterDeleting = adminDeleted.size();
		
		assertTrue(status);
		assertEquals(number - 1, afterDeleting);
	}
	
	@Test(expected = Exception.class)
	public void testDelete_WithExistingIdAndCulturalOffers_ShouldThrowException() throws Exception {
		adminService.delete(AdminConstants.ADMIN_ID);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testDelete_WithNonExistingId_ShouldThrowNoSuchElementException() throws Exception {
		adminService.delete(AdminConstants.NON_EXISTING_ADMIN_ID);
	}
	
	@Test
	public void testSearchAdmins_WithAllCorrectValues_ShouldReturnPageOfAdmins() {
		Pageable pageable = PageRequest.of(AdminConstants.PAGE, AdminConstants.PAGE_SIZE);
		Page<Admin> adminPage = adminService.searchAdmins(pageable, AdminConstants.PART);
		
		assertEquals(5, adminPage.getNumberOfElements());
	}
	
	@Test
	public void testSearchAdmins_WithNonExistingPageAndExistingValue_ShouldReturnEmptyPageOfAdmins() {
		Pageable pageable = PageRequest.of(AdminConstants.NON_EXISTING_PAGE, AdminConstants.PAGE_SIZE);
		Page<Admin> adminPage = adminService.searchAdmins(pageable, AdminConstants.PART);
		
		assertEquals(0, adminPage.getNumberOfElements());
	}
	
	@Test
	public void testSearchAdmins_WithNonExistingPageAndNonExistingValue_ShouldReturnEmptyPageOfAdmins() {
		Pageable pageable = PageRequest.of(AdminConstants.PAGE, AdminConstants.PAGE_SIZE);
		Page<Admin> adminPage = adminService.searchAdmins(pageable, AdminConstants.NON_EXISTING_PART);
		
		assertEquals(0, adminPage.getNumberOfElements());
	}
	
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void testCreate_WithAllCorrectValues_ShouldReturnCreatedAdmin() throws Exception {
		List<Admin> admins = (List<Admin>) adminService.getAll();
		int number = admins.size();
		Admin a = new Admin(AdminConstants.NEW_USERNAME, AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		Admin created = adminService.create(a);
		List<Admin> adminCreated = (List<Admin>) adminService.getAll();
		int afterCreating = adminCreated.size();
		
		assertNotNull(created);
		assertEquals(number + 1, afterCreating);
		assertTrue(created.isActive());
		assertEquals(AdminConstants.NEW_USERNAME, created.getUsername());
		assertEquals(AdminConstants.NEW_EMAIL, created.getEmail());
		assertEquals(AdminConstants.FIRSTNAME, created.getFirstName());
		assertEquals(AdminConstants.LASTNAME, created.getLastName());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingUsername_ShouldThrowIllegalArgumentException() throws Exception {
		Admin a = new Admin(AdminConstants.EXISTING_USERNAME, AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		adminService.create(a);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingEmail_ShouldThrowIllegalArgumentException() throws Exception {
		Admin a = new Admin(AdminConstants.NEW_USERNAME, AdminConstants.EXISTING_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		adminService.create(a);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingEmailAndExistingUsername_ShouldThrowIllegalArgumentException() throws Exception {
		Admin a = new Admin(AdminConstants.EXISTING_USERNAME, AdminConstants.EXISTING_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		adminService.create(a);
	}
}
