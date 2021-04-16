package com.ktsnwt.project.team9.controllers;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktsnwt.project.team9.dto.UserDTO;
import com.ktsnwt.project.team9.helper.implementations.UserMapper;
import com.ktsnwt.project.team9.services.implementations.UserService;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:4200", maxAge = 3600, allowedHeaders = "*")
public class UserController {
	
	@Autowired
	private UserService userService;
	private UserMapper userMapper;
	
	public UserController() {
		userMapper = new UserMapper();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGISTERED_USER')")
	@PutMapping(value= "/change-profile", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> changeProfile(@Valid @RequestBody UserDTO userDTO) {
		try {
			return new ResponseEntity<>(userMapper.toResDTO(userService.changeProfile(userMapper.toEntity(userDTO))), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
