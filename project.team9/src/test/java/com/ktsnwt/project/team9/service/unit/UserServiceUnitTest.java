package com.ktsnwt.project.team9.service.unit;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ktsnwt.project.team9.constants.UserConstants;
import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.repositories.IRegisteredUser;
import com.ktsnwt.project.team9.repositories.IUserRepository;
import com.ktsnwt.project.team9.services.implementations.GenerateRandomPasswordService;
import com.ktsnwt.project.team9.services.implementations.MailService;
import com.ktsnwt.project.team9.services.implementations.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class UserServiceUnitTest {

	@Autowired
	UserService userService;
	
	@MockBean
	private IRegisteredUser userRepo;
	
	@MockBean
    private IUserRepository userRepository;

	@MockBean
    private PasswordEncoder passwordEncoder;

	@MockBean
    private AuthenticationManager authenticationManager;
    
	@MockBean
	private MailService mailService;
    
	@MockBean
    private GenerateRandomPasswordService grpService;
	
	@Before
	public void setup() throws Exception {
		User user = new User(UserConstants.USER_ID, UserConstants.USERNAME, UserConstants.EMAIL, UserConstants.PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		User user2 = new User(UserConstants.USER_ID2, UserConstants.USERNAME_UPDATED, UserConstants.EMAIL2, UserConstants.PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		User userUpdated = new User(UserConstants.USER_ID, UserConstants.USERNAME, UserConstants.EMAIL, UserConstants.PASSWORD, UserConstants.FIRST_NAME_UPDATED, UserConstants.LAST_NAME_UPDATED);
		User userUpdated2 = new User(UserConstants.USER_ID, UserConstants.USERNAME_UPDATED, UserConstants.EMAIL, UserConstants.PASSWORD, UserConstants.FIRST_NAME_UPDATED, UserConstants.LAST_NAME_UPDATED);
		User userForgotPassword = new User(UserConstants.USER_ID, UserConstants.USERNAME, UserConstants.EMAIL, UserConstants.ENCODED_RANDOM_PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		User userChangePassword = new User(UserConstants.USER_ID, UserConstants.USERNAME, UserConstants.EMAIL, UserConstants.ENCODED_UPDATED_PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(UserConstants.EMAIL, UserConstants.PASSWORD);
		UsernamePasswordAuthenticationToken authWrongToken = new UsernamePasswordAuthenticationToken(UserConstants.EMAIL, UserConstants.WRONG_PASSWORD);
		Authentication auth = new Authentication() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return null;
			}
			
			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
				
			}
			
			@Override
			public boolean isAuthenticated() {
				return false;
			}
			
			@Override
			public Object getPrincipal() {
				return user;
			}
			
			@Override
			public Object getDetails() {
				return null;
			}
			
			@Override
			public Object getCredentials() {
				return null;
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}
		};
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		given(userRepository.findByEmail(UserConstants.EMAIL)).willReturn(user);
		given(userRepository.findByEmail(UserConstants.NON_EXISTING_EMAIL)).willReturn(null);
		
		given(userRepository.findByUsername(UserConstants.USERNAME)).willReturn(user);
		given(userRepository.findByUsername(UserConstants.USERNAME_UPDATED)).willReturn(null);
		given(userRepository.findByUsername(UserConstants.USERNAME2)).willReturn(user2);
		
		given(userRepository.save(userUpdated)).willReturn(userUpdated);
		given(userRepository.save(userUpdated2)).willReturn(userUpdated2);
		given(userRepository.save(userForgotPassword)).willReturn(userForgotPassword);
		given(userRepository.save(userChangePassword)).willReturn(userChangePassword);
		
		given(grpService.generateRandomPassword()).willReturn(UserConstants.RANDOM_PASSWORD);
		given(passwordEncoder.encode(UserConstants.RANDOM_PASSWORD)).willReturn(UserConstants.ENCODED_RANDOM_PASSWORD);
		given(passwordEncoder.encode(UserConstants.PASSWORD_UPDATED)).willReturn(UserConstants.ENCODED_UPDATED_PASSWORD);

		given(authenticationManager.authenticate(authWrongToken)).willThrow(new BadCredentialsException("Bad credentials"));
		given(authenticationManager.authenticate(authToken)).willReturn(authToken);
		
		doNothing().when(mailService).sendMail(UserConstants.EMAIL, UserConstants.SUBJECT_EMAIL, UserConstants.TEXT_EMAIL);
	}
	
	@Test
	public void testLoadUserByUsername_WithExistingUsername_ShouldReturnUser() {
		User user = (User) userService.loadUserByUsername(UserConstants.EMAIL);
		
		verify(userRepository, times(1)).findByEmail(UserConstants.EMAIL);
		assertEquals(UserConstants.EMAIL, user.getEmail());
		assertEquals(UserConstants.USERNAME, user.getUsername());
		assertEquals(UserConstants.FIRST_NAME, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME, user.getLastName());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsername_WithNonExistingUsername_ShouldThrowUsernameNotFoundException() {
		User user = (User) userService.loadUserByUsername(UserConstants.NON_EXISTING_EMAIL);
		
		verify(userRepository, times(1)).findByEmail(UserConstants.NON_EXISTING_EMAIL);
		assertNull(user);
	}

	@Test
	public void testChangeProfile_WithDifferentNonExistingUsername_ShouldChangeProfile() throws Exception {
		User userUpdate = new User(UserConstants.USER_ID, UserConstants.USERNAME_UPDATED, UserConstants.EMAIL, UserConstants.PASSWORD_UPDATED, UserConstants.FIRST_NAME_UPDATED, UserConstants.LAST_NAME_UPDATED);
		User user = userService.changeProfile(userUpdate);
		
		verify(userRepository, times(1)).findByUsername(UserConstants.USERNAME_UPDATED);
		assertEquals(UserConstants.EMAIL, user.getEmail());
		assertEquals(UserConstants.USERNAME_UPDATED, user.getUsername());
		assertEquals(UserConstants.FIRST_NAME_UPDATED, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_UPDATED, user.getLastName());
	}
	
	@Test
	public void testChangeProfile_WithSameUsername_ShouldChangeProfile() throws Exception {
		User userUpdate = new User(UserConstants.USER_ID, UserConstants.USERNAME, UserConstants.EMAIL, UserConstants.PASSWORD_UPDATED, UserConstants.FIRST_NAME_UPDATED, UserConstants.LAST_NAME_UPDATED);
		User user = userService.changeProfile(userUpdate);
		
		verify(userRepository, times(0)).findByUsername(UserConstants.USERNAME);
		assertEquals(UserConstants.EMAIL, user.getEmail());
		assertEquals(UserConstants.USERNAME, user.getUsername());
		assertEquals(UserConstants.FIRST_NAME_UPDATED, user.getFirstName());
		assertEquals(UserConstants.LAST_NAME_UPDATED, user.getLastName());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChangeProfile_WithDifferentExistingUsername_ShouldThrowIllegalArgumentException() throws Exception {
		User userUpdate = new User(UserConstants.USER_ID, UserConstants.USERNAME2, UserConstants.EMAIL, UserConstants.PASSWORD, UserConstants.FIRST_NAME_UPDATED, UserConstants.LAST_NAME_UPDATED);
		userService.changeProfile(userUpdate);
		
		verify(userRepository, times(1)).findByUsername(UserConstants.USERNAME2);
	}
	
	@Test
	public void testForgotPassword_WithAllValues_ShouldChangePassword() throws Exception {
		User user = new User(UserConstants.USER_ID, UserConstants.USERNAME, UserConstants.EMAIL, UserConstants.ENCODED_RANDOM_PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		userService.forgotPassword(UserConstants.EMAIL);
		
		verify(userRepository, times(1)).findByEmail(UserConstants.EMAIL);
		verify(grpService, times(1)).generateRandomPassword();
		verify(userRepository, times(1)).save(user);
		verify(mailService, times(1)).sendMail(UserConstants.EMAIL, UserConstants.SUBJECT_EMAIL, UserConstants.TEXT_EMAIL);
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void testForgotPassword_WithNonExistingEmail_ShouldThrowUsernameNotFoundException() throws Exception {
		User user = new User(UserConstants.USER_ID, UserConstants.USERNAME, UserConstants.NON_EXISTING_EMAIL, UserConstants.ENCODED_RANDOM_PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		userService.forgotPassword(UserConstants.NON_EXISTING_EMAIL);
		
		verify(userRepository, times(1)).findByEmail(UserConstants.NON_EXISTING_EMAIL);
		verify(grpService, times(0)).generateRandomPassword();
		verify(userRepository, times(0)).save(user);
		verify(mailService, times(0)).sendMail(UserConstants.NON_EXISTING_EMAIL, UserConstants.SUBJECT_EMAIL, UserConstants.TEXT_EMAIL);
	}
	
	@Test
	public void testChangePassword_WithAllCorrectValues_ShouldChangePassword() throws Exception {
		User userChangePassword = new User(UserConstants.USER_ID, UserConstants.USERNAME, UserConstants.EMAIL, UserConstants.ENCODED_UPDATED_PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		userService.changePassword(UserConstants.PASSWORD, UserConstants.PASSWORD_UPDATED);
		
		verify(passwordEncoder, times(1)).encode(UserConstants.PASSWORD_UPDATED);
		verify(userRepository, times(1)).save(userChangePassword);
	}
	
	@Test(expected = BadCredentialsException.class)
	public void testChangePassword_WithWrongOldPassword_ShouldThrowBadCredentialsException() throws Exception {
		User userChangePassword = new User(UserConstants.USER_ID, UserConstants.USERNAME, UserConstants.EMAIL, UserConstants.ENCODED_UPDATED_PASSWORD, UserConstants.FIRST_NAME, UserConstants.LAST_NAME);
		userService.changePassword(UserConstants.WRONG_PASSWORD, UserConstants.PASSWORD_UPDATED);
		
		verify(passwordEncoder, times(0)).encode(UserConstants.PASSWORD_UPDATED);
		verify(userRepository, times(0)).save(userChangePassword);
	}
	
}
