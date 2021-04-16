package com.ktsnwt.project.team9.service.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.VerificationTokenConstants;
import com.ktsnwt.project.team9.model.VerificationToken;
import com.ktsnwt.project.team9.services.implementations.VerificationTokenService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class VerificationTokenServiceIntegrationTest {

	@Autowired
	VerificationTokenService verificationTokenService;
	
	@Test
	public void testFindByToken_WithExistingToken_ShouldReturnToken() {
		VerificationToken token = verificationTokenService.findByToken(VerificationTokenConstants.TOKEN);
		
		assertNotNull(token);
		assertEquals(VerificationTokenConstants.TOKEN, token.getToken());
	}
	
	@Test
	public void testFindByToken_WithNonExistingToken_ShouldReturnNull() {
		VerificationToken token = verificationTokenService.findByToken(VerificationTokenConstants.NON_EXISTING_TOKEN);
		
		assertNull(token);		
	}
	
	@Test
	public void testSaveToken_WithAllCorrectValues_ShouldSaveToken() {
		VerificationToken newToken = new VerificationToken(VerificationTokenConstants.NEW_TOKEN, VerificationTokenConstants.USER);
		verificationTokenService.saveToken(newToken);
		VerificationToken token = verificationTokenService.findByToken(VerificationTokenConstants.NEW_TOKEN);
		
		assertNotNull(token);
		assertEquals(VerificationTokenConstants.NEW_TOKEN, token.getToken());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testSaveToken_WithNonExistingUser_ShouldThrowNoSuchElementException() {
		VerificationToken newToken = new VerificationToken(VerificationTokenConstants.NEW_TOKEN, VerificationTokenConstants.NON_EXISTING_USER);
		verificationTokenService.saveToken(newToken);
	}
	
}
