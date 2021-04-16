package com.ktsnwt.project.team9.repository.integration;

import static org.junit.Assert.assertEquals;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.AdminConstants;
import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.repositories.IAdminRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class AdminRepositoryIntegrationTest {
	
	@Autowired
	IAdminRepository adminRepository;
	
	@Test
	public void testFindByUsernameOrEmailOrFirstNameOrLastNameContainingIgnoreCase_WithExistingPartUpperCase_ShouldReturnAdminPage() {
		Pageable pageable = PageRequest.of(AdminConstants.PAGE, AdminConstants.PAGE_SIZE);
		Page<Admin> adminPage = adminRepository.findByUsernameOrEmailOrFirstNameOrLastNameContainingIgnoreCase(AdminConstants.PART.toUpperCase(), pageable);
		
		assertEquals(5, adminPage.getContent().size());
	}
	
	@Test
	public void testFindByUsernameOrEmailOrFirstNameOrLastNameContainingIgnoreCase_WithExistingPartLowerCase_ShouldReturnAdminPage() {
		Pageable pageable = PageRequest.of(AdminConstants.PAGE, AdminConstants.PAGE_SIZE);
		Page<Admin> adminPage = adminRepository.findByUsernameOrEmailOrFirstNameOrLastNameContainingIgnoreCase(AdminConstants.PART.toLowerCase(), pageable);
		
		assertEquals(5, adminPage.getContent().size());
	}
	
	@Test
	public void testFindByUsernameOrEmailOrFirstNameOrLastNameContainingIgnoreCase_WithNonExistingPart_ShouldReturnAdminPageWithNoContent() {
		Pageable pageable = PageRequest.of(AdminConstants.PAGE, AdminConstants.PAGE_SIZE);
		Page<Admin> adminPage = adminRepository.findByUsernameOrEmailOrFirstNameOrLastNameContainingIgnoreCase(AdminConstants.NON_EXISTING_PART, pageable);
		
		assertEquals(0, adminPage.getTotalElements());
	}

}
