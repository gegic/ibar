package com.ktsnwt.project.team9.service.integration;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.AuthorityConstants;
import com.ktsnwt.project.team9.model.Authority;
import com.ktsnwt.project.team9.services.implementations.AuthorityService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class AuthorityServiceIntegrationTest {

	@Autowired
	AuthorityService authService;
	
	@Test
	public void testFindById_WithExistingId_ShouldReturnAuthorityList() {
		List<Authority> auths = authService.findById(AuthorityConstants.ID);
		
		assertNotNull(auths);
		assertEquals(1, auths.size());
	}
	
	@Test
	public void testFindById_WithNonExistingId_ShouldReturnEmptyAuthorityList() {
		List<Authority> auths = authService.findById(AuthorityConstants.NON_EXISTING_ID);
		
		assertNotNull(auths);
		assertEquals(0, auths.size());
	}
	
	@Test
	public void testFindByName_WithExistingName_ShouldReturnAuthorityList() {
		List<Authority> auths = authService.findByName(AuthorityConstants.AUTHORITY_NAME);
		
		assertNotNull(auths);
		assertEquals(1, auths.size());
	}
	
	@Test
	public void testFindByName_WithNonExistingName_ShouldReturnEmptyAuthorityList() {
		List<Authority> auths = authService.findByName(AuthorityConstants.NON_EXISTING_AUTHORITY_NAME);
		
		assertNotNull(auths);
		assertEquals(0, auths.size());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testConfirmRegistration_WithNonExistingToken_ShouldThrowNoSuchElementException() throws Exception {
		authService.confirmRegistration(AuthorityConstants.NON_EXISTING_TOKEN);
	}
}
