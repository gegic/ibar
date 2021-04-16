package com.ktsnwt.project.team9.repository.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.UserConstants;
import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.repositories.IUserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class UserRepositoryIntegrationTest {

	@Autowired
	IUserRepository userRepository;
	
	@Test
	public void testFindByEmail_WithExistingEmail_ShouldReturnUser() {
		User user = userRepository.findByEmail(UserConstants.EMAIL);
		
		assertEquals(UserConstants.EMAIL, user.getEmail());
	}
	
	@Test
	public void testFindByEmail_WithNonExistingEmail_ShouldReturnNull() {
		User user = userRepository.findByEmail(UserConstants.NON_EXISTING_EMAIL);
		
		assertEquals(null, user);
	}
	
	@Test
	public void testFindByUsername_WithExistingUsername_ShouldReturnUser() {
		User user = userRepository.findByUsername(UserConstants.USERNAME);
		
		assertEquals(UserConstants.USERNAME, user.getUsername());
	}
	
	@Test
	public void testFindByUsername_WithNonExistingUsername_ShouldReturnNull() {
		User user = userRepository.findByUsername(UserConstants.NON_EXISTING_USERNAME);
		
		assertEquals(null, user);
	}
}
