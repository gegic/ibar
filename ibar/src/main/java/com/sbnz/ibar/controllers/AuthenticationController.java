package com.sbnz.ibar.controllers;

import java.util.NoSuchElementException;
import javax.validation.Valid;

import com.sbnz.ibar.dto.UserLoginDto;
import com.sbnz.ibar.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbnz.ibar.dto.UserDto;
import com.sbnz.ibar.security.TokenUtils;
import com.sbnz.ibar.services.AuthorityService;
import com.sbnz.ibar.services.MailService;
import com.sbnz.ibar.services.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	private final TokenUtils tokenUtils;
	private final AuthenticationManager authenticationManager;
	private final UserService userDetailsService;
	private final AuthorityService authorityService;
	private final MailService mailService;

//
//	private UserMapper userMapper;

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserLoginDto authenticationRequest) {
//		Authentication authentication;
//		try {
//			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//					authenticationRequest.getEmail(), authenticationRequest.getPassword()));
//		} catch (Exception e) {
//			return new ResponseEntity<>("Incorrect email or password.", HttpStatus.UNAUTHORIZED);
//		}
//
//		User user = (User) authentication.getPrincipal();
//		List<Authority> auth = user.getAuthorities();
//
//		RegisteredUser regUser = registeredUserService.findByEmail(user.getEmail());
//		if (regUser != null && !regUser.isVerified()) {
//			return new ResponseEntity<>("Account is not activated.", HttpStatus.BAD_REQUEST);
//		}
//
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//
//		String jwt = tokenUtils.generateToken(user.getEmail(), auth.get(0).getName()); // prijavljujemo se na sistem sa
//																						// email adresom
//		int expiresIn = tokenUtils.getExpiredIn();
//
//		return ResponseEntity.ok(new AuthTokenDTO(jwt, (long) expiresIn, auth));
		return null;
	}

	@PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addUser(@Valid @RequestBody UserDto userRequest) throws Exception {
//		User existEmail = this.userDetailsService.findByEmail(userRequest.getEmail());
//		if (existEmail != null) {
//			return new ResponseEntity<>("Email already exists.", HttpStatus.CONFLICT);
//		}
//		User existUser = this.userDetailsService.findByUsername(userRequest.getUsername());
//		if (existUser != null) {
//			return new ResponseEntity<>("Username already exists.", HttpStatus.CONFLICT);
//		}
//		RegisteredUser newUser = null;
//		try {
//			newUser = registeredUserService.create(registeredUserMapper.toEntity(userRequest));
//		} catch (Exception e) {
//			return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
//		}
//		String token = UUID.randomUUID().toString();
//		VerificationToken vtoken = new VerificationToken();
//		vtoken.setId(null);
//		vtoken.setToken(token);
//		vtoken.setUser(newUser);
//		verificationTokenService.saveToken(vtoken);
//		String confirmationUrl = "/confirm-registration/" + token;
//		mailService.sendMail(newUser.getEmail(), "Account activation", "Hi " + newUser.getFirstName()
//				+ ",\n\nThanks for getting started with CulturalContentTeam9! Click below to confirm your registration:\n"
//				+ "\nhttps://localhost:4200/auth" + confirmationUrl + "\n\nThanks,\nTeam 9\n");
//		return new ResponseEntity<>(registeredUserMapper.toResDTO(newUser), HttpStatus.CREATED);
		return null;
	}

	@PostMapping(value = "/forgot-password", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> forgotPassword(@RequestBody PasswordReset passwordReset) {
		User user = this.userDetailsService.findByEmail(passwordReset.getEmail());
		if (user == null) {
			return new ResponseEntity<>("User with given email doesn't exist.", HttpStatus.BAD_REQUEST);
		}
		userDetailsService.forgotPassword(passwordReset.getEmail());
		return new ResponseEntity<>("Password reset successfully.", HttpStatus.OK);
	}

	@PostMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> changePassword(@Valid @RequestBody PasswordChanger passwordChanger) {
		try {
			userDetailsService.changePassword(passwordChanger.getOldPassword(), passwordChanger.getNewPassword());
		} catch (Exception e) {
			return new ResponseEntity<>("Incorrect old password", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
	}

	@Getter
	@Setter
	@NoArgsConstructor
	static class PasswordReset {
		private String email;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	static class PasswordChanger {
		private String oldPassword;
		private String newPassword;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGISTERED_USER')")
	@GetMapping(value = "/current-user")
	public ResponseEntity<UserDto> currentUser() {
//		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return new ResponseEntity<>(userMapper.toResDTO(current), HttpStatus.OK);

		return null;
	}

}
