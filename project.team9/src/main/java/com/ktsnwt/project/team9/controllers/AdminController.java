package com.ktsnwt.project.team9.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktsnwt.project.team9.dto.AdminDTO;
import com.ktsnwt.project.team9.dto.response.UserResDTO;
import com.ktsnwt.project.team9.helper.implementations.AdminMapper;
import com.ktsnwt.project.team9.helper.implementations.CustomPageImplementation;
import com.ktsnwt.project.team9.model.Admin;
import com.ktsnwt.project.team9.services.implementations.AdminService;

@RestController
@RequestMapping(value = "/api/admins", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:4200", maxAge = 3600, allowedHeaders = "*")
public class AdminController {

	@Autowired
	private AdminService adminService;
	private AdminMapper adminMapper;

	public AdminController() {
		adminMapper = new AdminMapper();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<Iterable<AdminDTO>> getAllAdmins() {
		List<AdminDTO> adminsDTO = adminMapper.toDTOList(adminService.getAll());
		return new ResponseEntity<>(adminsDTO, HttpStatus.OK);
  }
  
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/by-page")
	public ResponseEntity<Page<UserResDTO>> getAllAdmins(Pageable pageable) {
		Page<Admin> page = adminService.findAll(pageable);
		return new ResponseEntity<>(createCustomPage(transformListToPage(page)), HttpStatus.OK);
	}

	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/search/{value}")
	public ResponseEntity<Page<UserResDTO>> searchAdmins(Pageable pageable, @PathVariable String value) {
		Page<Admin> page = adminService.searchAdmins(pageable, value);
		return new ResponseEntity<>(createCustomPage(transformListToPage(page)), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserResDTO> getAdmin(@PathVariable Long id) {
		Admin admin = adminService.getById(id);
		if (admin == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(adminMapper.toResDTO(admin), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminDTO adminDTO) {
		try {
			return new ResponseEntity<>(adminMapper.toResDTO(adminService.create(adminMapper.toEntity(adminDTO))),
					HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(adminService.delete(id), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>("Admin doesn't exist", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Admin has cultural offers, so he can't be deleted", HttpStatus.BAD_REQUEST);
		}
	}

	private Page<UserResDTO> transformListToPage(Page<Admin> page) {
		List<UserResDTO> adminsResDTO = adminMapper.toDTOResList(page.toList());
		return new PageImpl<>(adminsResDTO, page.getPageable(), page.getTotalElements());
	}
	
	private CustomPageImplementation<UserResDTO> createCustomPage(Page<UserResDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}

}
