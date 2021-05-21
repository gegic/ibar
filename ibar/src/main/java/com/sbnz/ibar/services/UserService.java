package com.sbnz.ibar.services;

import java.util.Date;

import javax.transaction.Transactional;

import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sbnz.ibar.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final MailService mailService;

	@Autowired
	public UserService(UserRepository userRepository, MailService mailService) {
		this.userRepository = userRepository;
		this.mailService = mailService;
	}

	@Override
	public User loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No user found with email " + email));
	}

	@Transactional
	public void changePassword(String oldPassword, String newPassword) throws IllegalAccessException {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String username;
		try {
			username = ((User) currentUser.getPrincipal()).getEmail();
		} catch (Exception e) {
			throw new IllegalAccessException("Invalid token.");
		}

		User user = loadUserByUsername(username);
		user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
		user.setLastPasswordResetDate(new Date().getTime());
		userRepository.save(user);
	}

	@Transactional
	public User changeProfile(User entity) throws Exception {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!entity.getEmail().equals(user.getEmail())) {
			if (userRepository.findByEmail(entity.getEmail()).isPresent()) {
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
		return this.loadUserByUsername(email);
	}

	@Transactional
	public void forgotPassword(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No user found with email " + email));

//		TODO

//		String newPassword = grpService.generateRandomPassword();
//		user.setPassword(passwordEncoder.encode(newPassword));
//		user.setLastPasswordResetDate(new Date().getTime());
//		userRepository.save(user);
//		mailService.sendMail(user.getEmail(), "Password reset",
//				"Hi " + user.getFirstName() + ",\n\nYour new password is: " + newPassword + ".\n\n\nTeam 9");
	}
}
