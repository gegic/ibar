package com.ktsnwt.project.team9.repository.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.VerificationTokenConstants;
import com.ktsnwt.project.team9.model.VerificationToken;
import com.ktsnwt.project.team9.repositories.VerificationTokenRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class VerificationTokenRepositoryIntegrationTest {

	@Autowired
	VerificationTokenRepository vtRepository;
	
	@Test
	public void testFindByToken_WithExisting_token_ShouldReturnVerificationToken() {
		VerificationToken vt = vtRepository.findByToken(VerificationTokenConstants.TOKEN);
		
		assertEquals(VerificationTokenConstants.TOKEN, vt.getToken());
	}
	
	@Test
	public void testFindByToken_WithNonExisting_token_ShouldReturnNull() {
		VerificationToken vt = vtRepository.findByToken(VerificationTokenConstants.NON_EXISTING_TOKEN);
		
		assertEquals(null, vt);
	}
}
