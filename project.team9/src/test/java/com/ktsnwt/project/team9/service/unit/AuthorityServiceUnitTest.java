package com.ktsnwt.project.team9.service.unit;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.AuthorityConstants;
import com.ktsnwt.project.team9.constants.RegisteredUserConstants;
import com.ktsnwt.project.team9.constants.VerificationTokenConstants;
import com.ktsnwt.project.team9.model.Authority;
import com.ktsnwt.project.team9.model.RegisteredUser;
import com.ktsnwt.project.team9.model.VerificationToken;
import com.ktsnwt.project.team9.repositories.IAuthorityRepository;
import com.ktsnwt.project.team9.repositories.IRegisteredUser;
import com.ktsnwt.project.team9.services.implementations.AuthorityService;
import com.ktsnwt.project.team9.services.implementations.VerificationTokenService;

import static org.junit.Assert.assertEquals;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class AuthorityServiceUnitTest {
	
	@Autowired
	AuthorityService authorityService;
	
	@MockBean
	IAuthorityRepository authorityRepository;
	
	@MockBean
	VerificationTokenService verificationTokenService;
	
	@MockBean
	IRegisteredUser registeredUserRepository;
	
	@Before
	public void setup() throws Exception {
		RegisteredUser user = new RegisteredUser(RegisteredUserConstants.USERNAME, RegisteredUserConstants.EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		user.setId(RegisteredUserConstants.USER_ID);
		user.setVerified(false);
		user.setAuthorities(RegisteredUserConstants.AUTHORITIES);
		
		Authority admin = new Authority(AuthorityConstants.ID, AuthorityConstants.AUTHORITY_NAME);
		
		RegisteredUser verifiedUser = new RegisteredUser(RegisteredUserConstants.USERNAME, RegisteredUserConstants.EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		verifiedUser.setId(RegisteredUserConstants.USER_ID);
		verifiedUser.setVerified(false);
		verifiedUser.setAuthorities(RegisteredUserConstants.AUTHORITIES);
		
		VerificationToken vt = new VerificationToken();
		vt.setId(VerificationTokenConstants.ID);
		vt.setToken(VerificationTokenConstants.TOKEN);
		vt.setUser(user);
		
		given(authorityRepository.findById(AuthorityConstants.NON_EXISTING_ID)).willReturn(Optional.empty());
		given(authorityRepository.findById(AuthorityConstants.ID)).willReturn(Optional.of(admin));
		
		given(authorityRepository.findByName(AuthorityConstants.NON_EXISTING_AUTHORITY_NAME)).willReturn(null);
		given(authorityRepository.findByName(AuthorityConstants.AUTHORITY_NAME)).willReturn(admin);
		
		given(verificationTokenService.findByToken(VerificationTokenConstants.NON_EXISTING_TOKEN)).willReturn(null);
		given(verificationTokenService.findByToken(VerificationTokenConstants.TOKEN)).willReturn(vt);
		
		given(registeredUserRepository.save(verifiedUser)).willReturn(verifiedUser);
	}
	
	@Test
	public void testFindById_WithNonExistingId_ShouldReturnEmptyList() {
		List<Authority> authorities = authorityService.findById(AuthorityConstants.NON_EXISTING_ID);
		verify(authorityRepository, times(1)).findById(AuthorityConstants.NON_EXISTING_ID);
		assertEquals(0, authorities.size());
	}
	
	@Test
	public void testFindById_WithExistingId_ShouldReturnList() {
		List<Authority> authorities = authorityService.findById(AuthorityConstants.ID);
    
		verify(authorityRepository, times(1)).findById(AuthorityConstants.ID);
		assertEquals(1, authorities.size());
		assertEquals(AuthorityConstants.ID, authorities.get(0).getId());
		assertEquals(AuthorityConstants.AUTHORITY_NAME, authorities.get(0).getName());
	}
	
	@Test
	public void testFindByName_WithNonExistingName_ShouldReturnEmptyList() {
		List<Authority> authorities = authorityService.findByName(AuthorityConstants.NON_EXISTING_AUTHORITY_NAME);
		
		verify(authorityRepository, times(1)).findByName(AuthorityConstants.NON_EXISTING_AUTHORITY_NAME);
		assertEquals(0, authorities.size());
	}
	
	@Test
	public void testFindByName_WithNExistingName_ShouldReturnList() {
		List<Authority> authorities = authorityService.findByName(AuthorityConstants.AUTHORITY_NAME);
		
		verify(authorityRepository, times(1)).findByName(AuthorityConstants.AUTHORITY_NAME);
		assertEquals(1, authorities.size());
		assertEquals(AuthorityConstants.ID, authorities.get(0).getId());
		assertEquals(AuthorityConstants.AUTHORITY_NAME, authorities.get(0).getName());
	}
	

	@Test(expected = NoSuchElementException.class)
	public void testConfirmRegistration_WithNonExistingToken_ShouldThrowException() throws Exception {
		authorityService.confirmRegistration(VerificationTokenConstants.NON_EXISTING_TOKEN);
		
		verify(verificationTokenService, times(1)).findByToken(VerificationTokenConstants.NON_EXISTING_TOKEN);
		verify(registeredUserRepository, times(0)).save(Mockito.any(RegisteredUser.class));
	}
	
	@Test 
	public void testConfirmRegistration_WithExistingToken_ShouldVerifyUser() throws Exception {
		RegisteredUser user = new RegisteredUser(RegisteredUserConstants.USERNAME, RegisteredUserConstants.EMAIL, RegisteredUserConstants.PASSWORD, RegisteredUserConstants.FIRSTNAME, RegisteredUserConstants.LASTNAME);
		user.setId(RegisteredUserConstants.USER_ID);
		user.setVerified(true);
		user.setAuthorities(RegisteredUserConstants.AUTHORITIES);
		authorityService.confirmRegistration(VerificationTokenConstants.TOKEN);
		
		verify(verificationTokenService, times(1)).findByToken(VerificationTokenConstants.TOKEN);
		verify(registeredUserRepository, times(1)).save(user);
	}

}
