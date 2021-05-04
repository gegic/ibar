package com.ktsnwt.project.team9.services.implementations;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.model.Authority;
import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.repositories.IAdminRepository;
import com.ktsnwt.project.team9.repositories.IRegisteredUser;
import com.ktsnwt.project.team9.repositories.ITitleRepository;
import com.ktsnwt.project.team9.repositories.IUserRepository;
import com.ktsnwt.project.team9.services.interfaces.IAdminService;

import lombok.AllArgsConstructor;

@Service
public class AdminService implements IAdminService {

	@Autowired
	private IAdminRepository adminRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthorityService authService;

	@Autowired
	private GenerateRandomPasswordService grpService;

	@Autowired
	private MailService mailService;

	public Page<Admin> findAll(Pageable pageable) {
		return adminRepository.findAll(pageable);
	}

	@Override
	public Iterable<Admin> getAll() {
		return adminRepository.findAll();
	}

	@Override
	public Admin getById(Long id) {
		Optional<Admin> a = adminRepository.findById(id);
		if (!a.isPresent()) {
			return null;
		}
		return a.get();
	}

	@Override
	@Transactional
	public Admin create(Admin entity) throws Exception {
		entity.setActive(true);
		
		User usernameUser = userRepository.findByUsername(entity.getUsername());
		if (usernameUser != null) {
			throw new IllegalArgumentException("User with this username already exists.");
		}
		
		User emailUser = userRepository.findByEmail(entity.getEmail());
		if (emailUser != null) {
			throw new IllegalArgumentException("User with this email already exists.");
		}
		
		List<Authority> auth = authService.findByName("ROLE_ADMIN");
		
		entity.setAuthorities(auth);
		
		String newPassword = grpService.generateRandomPassword();
		entity.setPassword(passwordEncoder.encode(newPassword));
		entity.setLastPasswordResetDate(new Date().getTime());
		
		mailService.sendMail(entity.getEmail(), "Account activation",
				"You are now new administrator. Congratulations!\n Your credentials are: \n\tUsername: "
						+ entity.getUsername() + "\n\tPassoword: " + newPassword);
		
		return adminRepository.save(entity);
	}

	@Override
	@Transactional
	public boolean delete(Long id) throws Exception {
		Admin existingAdmin = getById(id);
		if (existingAdmin == null) {
			throw new NoSuchElementException("Admin with given id doesn't exist.");
		}
		
		existingAdmin.setActive(false);

		adminRepository.deleteById(id);
		
		return true;
	}

	@Override
	public Admin update(Long id, Admin entity) throws Exception {
		return null;
	}

	public Page<Admin> searchAdmins(Pageable pageable, String value) {
		return adminRepository.findByUsernameOrEmailOrFirstNameOrLastNameContainingIgnoreCase(
				'%' + value.toLowerCase() + '%', pageable);
	}

}
