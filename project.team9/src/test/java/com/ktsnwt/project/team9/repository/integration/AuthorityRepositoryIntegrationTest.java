package com.ktsnwt.project.team9.repository.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.AuthorityConstants;
import com.ktsnwt.project.team9.model.Authority;
import com.ktsnwt.project.team9.repositories.IAuthorityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class AuthorityRepositoryIntegrationTest {

	@Autowired
	IAuthorityRepository authorityRepository;
	
	@Test
	public void testFindByName_WithExistingName_ShouldReturnAuthority() {
		Authority authority = authorityRepository.findByName(AuthorityConstants.AUTHORITY_NAME);
		
		assertEquals(AuthorityConstants.AUTHORITY_NAME, authority.getName());
	}
	
	@Test
	public void testFindByName_WithNonExistingName_ShouldReturnNull() {
		Authority authority = authorityRepository.findByName(AuthorityConstants.NON_EXISTING_AUTHORITY_NAME);
		
		assertEquals(null, authority);
	}
}
