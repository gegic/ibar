package com.ktsnwt.project.team9.service.unit;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.AdminConstants;
import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.repositories.IAdminRepository;
import com.ktsnwt.project.team9.repositories.IUserRepository;
import com.ktsnwt.project.team9.services.implementations.AdminService;
import com.ktsnwt.project.team9.services.implementations.AuthorityService;
import com.ktsnwt.project.team9.services.implementations.GenerateRandomPasswordService;
import com.ktsnwt.project.team9.services.implementations.MailService;

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
public class AdminServiceUnitTest {

	@Autowired
	AdminService adminService;
	
	@MockBean
	IAdminRepository adminRepository;
	
	@MockBean
	IUserRepository userRepository;
	
	@MockBean
	PasswordEncoder passwordEncoder;
	
	@MockBean
	AuthorityService authService;
	
	@MockBean
	GenerateRandomPasswordService grpService;
	
	@MockBean
	MailService mailService;
	
	@Before
	public void setup() throws Exception {
		
		Admin admin1saved = new Admin(AdminConstants.USERNAME, AdminConstants.EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		admin1saved.setId(AdminConstants.ADMIN_ID);
		
		Admin sameUsername = new Admin(AdminConstants.EXISTING_USERNAME, AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		Admin sameEmail = new Admin(AdminConstants.NEW_USERNAME, AdminConstants.EXISTING_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		Admin newAdmin = new Admin(AdminConstants.NEW_USERNAME, AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		newAdmin.setPassword(AdminConstants.EPASSWORD);
		newAdmin.setAuthorities(AdminConstants.AUTHORITIES);
		
		Admin newAdminsaved = new Admin(AdminConstants.NEW_USERNAME, AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		newAdminsaved.setId(AdminConstants.NEW_ADMIN_ID);
		newAdminsaved.setAuthorities(AdminConstants.AUTHORITIES);
		newAdminsaved.setPassword(AdminConstants.EPASSWORD);
		
		Admin withCo = new Admin(AdminConstants.ADMIN_CO_ID);
		withCo.setCulturalOffers(AdminConstants.CO);
		
		given(adminRepository.findById(AdminConstants.ADMIN_ID)).willReturn(Optional.of(admin1saved));
		given(adminRepository.findById(AdminConstants.NON_EXISTING_ADMIN_ID)).willReturn(Optional.empty());
		given(adminRepository.findById(AdminConstants.ADMIN_CO_ID)).willReturn(Optional.of(withCo));
		
		given(userRepository.findByUsername(AdminConstants.EXISTING_USERNAME)).willReturn(sameUsername);
		given(userRepository.findByEmail(AdminConstants.EXISTING_EMAIL)).willReturn(sameEmail);
		
		given(userRepository.findByUsername(AdminConstants.NEW_USERNAME)).willReturn(null);
		given(userRepository.findByEmail(AdminConstants.NEW_EMAIL)).willReturn(null);
		given(authService.findByName(AdminConstants.ROLE)).willReturn(AdminConstants.AUTHORITIES);
		given(grpService.generateRandomPassword()).willReturn(AdminConstants.GPASSWORD);
		given(passwordEncoder.encode(AdminConstants.GPASSWORD)).willReturn(AdminConstants.EPASSWORD);
		doNothing().when(mailService).sendMail(AdminConstants.NEW_EMAIL, "Account activation", "You are now new administrator of Cultural content Team 9. Congratulations!\n Your credentials are: \n\tUsername: " + AdminConstants.NEW_USERNAME + 
        		"\n\tPassoword: "  + AdminConstants.GPASSWORD);
		
		given(adminRepository.save(newAdmin)).willReturn(newAdminsaved);
		doNothing().when(adminRepository).deleteById(AdminConstants.ADMIN_ID);
	}
	
	@Test
	public void testGetById_WithExistingId_ShouldReturnAdmin() {
		Admin a = adminService.getById(AdminConstants.ADMIN_ID);
		
		verify(adminRepository, times(1)).findById(AdminConstants.ADMIN_ID);
		assertEquals(AdminConstants.ADMIN_ID, a.getId());
	}
	
	@Test
	public void testGetById_WithNonExistingId_ShouldReturnNUll() {
		Admin a = adminService.getById(AdminConstants.NON_EXISTING_ADMIN_ID);
		
		verify(adminRepository, times(1)).findById(AdminConstants.NON_EXISTING_ADMIN_ID);
		assertEquals(null, a);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingUsername_ShouldThrowIllegalArgumentException() throws Exception {
		Admin sameUsername = new Admin(AdminConstants.EXISTING_USERNAME, AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		adminService.create(sameUsername);
		
		verify(userRepository, times(1)).findByUsername(AdminConstants.EXISTING_USERNAME);
		verify(userRepository, times(0)).findByEmail(AdminConstants.NEW_EMAIL);
		verify(authService, times(0)).findByName(AdminConstants.ROLE);
		verify(grpService, times(0)).generateRandomPassword();
		verify(passwordEncoder, times(0)).encode(AdminConstants.GPASSWORD);
		verify(mailService, times(0)).sendMail(AdminConstants.NEW_EMAIL, "Account activation", "You are now new administrator of Cultural content Team 9. Congratulations!\n Your credentials are: \n\tUsername: " + AdminConstants.NEW_USERNAME + 
        		"\n\tPassoword: "  + AdminConstants.GPASSWORD);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingEmail_ShouldThrowIllegalArgumentException() throws Exception {
		Admin sameEmail = new Admin(AdminConstants.NEW_USERNAME, AdminConstants.EXISTING_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		adminService.create(sameEmail);
		
		verify(userRepository, times(1)).findByEmail(AdminConstants.EXISTING_EMAIL);
		verify(userRepository, times(1)).findByUsername(AdminConstants.NEW_USERNAME);
		verify(authService, times(0)).findByName(AdminConstants.ROLE);
		verify(grpService, times(0)).generateRandomPassword();
		verify(passwordEncoder, times(0)).encode(AdminConstants.GPASSWORD);
		verify(mailService, times(0)).sendMail(AdminConstants.NEW_EMAIL, "Account activation", "You are now new administrator of Cultural content Team 9. Congratulations!\n Your credentials are: \n\tUsername: " + AdminConstants.NEW_USERNAME + 
        		"\n\tPassoword: "  + AdminConstants.GPASSWORD);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithExistingUsernameAndExistingEmail_ShouldThrowIllegalArgumentException() throws Exception {
		Admin sameUsernameAndMail = new Admin(AdminConstants.EXISTING_USERNAME, AdminConstants.EXISTING_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		adminService.create(sameUsernameAndMail);
		
		verify(userRepository, times(1)).findByUsername(AdminConstants.EXISTING_USERNAME);
		verify(userRepository, times(0)).findByEmail(AdminConstants.EXISTING_EMAIL);
		verify(authService, times(0)).findByName(AdminConstants.ROLE);
		verify(grpService, times(0)).generateRandomPassword();
		verify(passwordEncoder, times(0)).encode(AdminConstants.GPASSWORD);
		verify(mailService, times(0)).sendMail(AdminConstants.NEW_EMAIL, "Account activation", "You are now new administrator of Cultural content Team 9. Congratulations!\n Your credentials are: \n\tUsername: " + AdminConstants.NEW_USERNAME + 
        		"\n\tPassoword: "  + AdminConstants.GPASSWORD);
	}
	
	@Test
	public void testCreate_WithAllCorrectValues_ShouldCreateAdmin() throws Exception {
		Admin newAdmin = new Admin(AdminConstants.NEW_USERNAME, AdminConstants.NEW_EMAIL, AdminConstants.FIRSTNAME, AdminConstants.LASTNAME);
		Admin a = adminService.create(newAdmin);
		
		verify(userRepository, times(1)).findByEmail(AdminConstants.NEW_EMAIL);
		verify(userRepository, times(1)).findByUsername(AdminConstants.NEW_USERNAME);
		verify(authService, times(1)).findByName(AdminConstants.ROLE);
		verify(grpService, times(1)).generateRandomPassword();
		verify(passwordEncoder, times(1)).encode(AdminConstants.GPASSWORD);
		verify(mailService, times(1)).sendMail(AdminConstants.NEW_EMAIL, "Account activation", "You are now new administrator of Cultural content Team 9. Congratulations!\n Your credentials are: \n\tUsername: " + AdminConstants.NEW_USERNAME + 
        		"\n\tPassoword: "  + AdminConstants.GPASSWORD);
		assertEquals(AdminConstants.NEW_EMAIL, a.getEmail());
		assertEquals(AdminConstants.NEW_USERNAME, a.getUsername());
		assertEquals(AdminConstants.FIRSTNAME, a.getFirstName());
		assertEquals(AdminConstants.LASTNAME, a.getLastName());
		assertEquals(AdminConstants.EPASSWORD, a.getPassword());
		assertEquals(AdminConstants.AUTHORITIES, a.getAuthorities());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testDelete_WithNonExistingId_ShouldThrowIllegalArgumentException() throws Exception {
		adminService.delete(AdminConstants.NON_EXISTING_ADMIN_ID);
		
		verify(adminRepository, times(1)).findById(AdminConstants.NON_EXISTING_ADMIN_ID);
		verify(adminRepository, times(0)).deleteById(AdminConstants.NON_EXISTING_ADMIN_ID);
	}
	
	@Test(expected = Exception.class)
	public void testDelete_WithCulturalOffers_ShouldThrowIllegalArgumentException() throws Exception {
		adminService.delete(AdminConstants.ADMIN_CO_ID);
		
		verify(adminRepository, times(1)).findById(AdminConstants.ADMIN_CO_ID);
		verify(adminRepository, times(0)).deleteById(AdminConstants.ADMIN_CO_ID);
	}
	
	@Test
	public void testDelete_WithExistingId_ShouldDeleteAdmin() throws Exception {
		boolean status = adminService.delete(AdminConstants.ADMIN_ID);
		
		verify(adminRepository, times(1)).findById(AdminConstants.ADMIN_ID);
		verify(adminRepository, times(1)).deleteById(AdminConstants.ADMIN_ID);
		assertTrue(status);
	}
}
