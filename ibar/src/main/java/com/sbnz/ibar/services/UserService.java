package com.sbnz.ibar.services;

import java.util.Date;

import javax.transaction.Transactional;

import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.model.User;
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

import com.sbnz.ibar.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {


	private UserRepository userRepository;


	private PasswordEncoder passwordEncoder;


	private AuthenticationManager authenticationManager;


	private MailService mailService;
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

	@Transactional
	public User changeProfile(User entity) throws Exception {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!entity.getEmail().equals(user.getEmail())) {
			User usernameUser = userRepository.findByEmail(entity.getEmail());
			if (usernameUser != null) {
				throw new IllegalArgumentException("Username already taken");
			}
		}
		user.setEmail(entity.getEmail());
		user.setFirstName(entity.getFirstName());
		user.setLastName(entity.getLastName());
		return userRepository.save(user);
	}

	public Iterable<User> getAll() {
		return null;
	}

	public User getById(Long id) {
		return null;
	}

	public User create(User entity) throws Exception {
		return null;
	}

	public boolean delete(Long id) throws Exception {
		return false;
	}

	public Reader update(Long id, Reader entity) throws Exception {
		return null;
	}

	public User update(Long id, User entity) throws Exception {
		return null;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User findByUsername(String username) {
		return userRepository.findByEmail(username);
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
