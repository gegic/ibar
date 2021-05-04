package com.ktsnwt.project.team9.services.implementations;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.repositories.IRegisteredUser;
import com.ktsnwt.project.team9.repositories.ITitleRepository;
import com.ktsnwt.project.team9.repositories.IUserRepository;
import com.ktsnwt.project.team9.services.interfaces.IUserService;

import lombok.AllArgsConstructor;

@Service
public class UserService implements IUserService, UserDetailsService {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MailService mailService;

	@Autowired
	private GenerateRandomPasswordService grpService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
		} else {
			return user;
		}
	}

	@Transactional
	public void changePassword(String oldPassword, String newPassword) throws IllegalAccessException {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String username = "";
		try {
			username = ((User) currentUser.getPrincipal()).getEmail();
		} catch (Exception e) {
			throw new IllegalAccessException("Invalid token.");
		}
		if (authenticationManager != null) {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		} else {
			return;
		}
		User user = (User) loadUserByUsername(username);
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setLastPasswordResetDate(new Date().getTime());
		userRepository.save(user);
	}

	@Override
	@Transactional
	public User changeProfile(User entity) throws Exception {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!entity.getUsername().equals(user.getUsername())) {
			User usernameUser = userRepository.findByUsername(entity.getUsername());
			if (usernameUser != null) {
				throw new IllegalArgumentException("Username already taken");
			}
		}
		user.setUsername(entity.getUsername());
		user.setFirstName(entity.getFirstName());
		user.setLastName(entity.getLastName());
		return userRepository.save(user);
	}

	@Override
	public Iterable<User> getAll() {
		return null;
	}

	@Override
	public User getById(Long id) {
		return null;
	}

	@Override
	public User create(User entity) throws Exception {
		return null;
	}

	@Override
	public boolean delete(Long id) throws Exception {
		return false;
	}

	@Override
	public User update(Long id, User entity) throws Exception {
		return null;
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Transactional
	public void forgotPassword(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
		} else {
			String newPassword = grpService.generateRandomPassword();
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setLastPasswordResetDate(new Date().getTime());
			userRepository.save(user);
			mailService.sendMail(user.getEmail(), "Password reset",
					"Hi " + user.getFirstName() + ",\n\nYour new password is: " + newPassword + ".\n\n\nTeam 9");
		}
	}

}
