package com.ktsnwt.project.team9.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktsnwt.project.team9.dto.MarkAuthorDTO;
import com.ktsnwt.project.team9.dto.MarkBookDTO;
import com.ktsnwt.project.team9.helper.implementations.MarkAuthorMapper;
import com.ktsnwt.project.team9.helper.implementations.MarkBookMapper;
import com.ktsnwt.project.team9.model.MarkAuthor;
import com.ktsnwt.project.team9.model.MarkBook;
import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.services.implementations.MarkAuthorService;
import com.ktsnwt.project.team9.services.implementations.MarkBookService;

@RestController
@RequestMapping(value = "/api/marks", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*")
public class MarkController {

	@Autowired
	private MarkBookService markBookService;

	@Autowired
	private MarkAuthorService markAuthorService;

	@Autowired
	private MarkBookMapper markBookMapper;

	@Autowired
	private MarkAuthorMapper markAuthorMapper;

	public MarkController() {
		markBookMapper = new MarkBookMapper();
		markAuthorMapper = new MarkAuthorMapper();
	}

	@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@GetMapping(value = "author/{id}")
	public ResponseEntity<MarkAuthorDTO> getMarkForAuthor(@PathVariable Long id) {
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MarkAuthor mark = markAuthorService.findByUserIdAndAuthorId(current.getId(), id);
		if (mark == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(markAuthorMapper.toDto(mark), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@GetMapping(value = "book/{id}")
	public ResponseEntity<MarkBookDTO> getMarkForBook(@PathVariable Long id) {
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MarkBook mark = markBookService.findByUserIdAndBookId(current.getId(), id);
		if (mark == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(markBookMapper.toDto(mark), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@PostMapping(value = "/author", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MarkAuthorDTO> createMarkForAuthor(@Valid @RequestBody MarkAuthorDTO markDTO) {
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			return new ResponseEntity<>(
					markAuthorMapper
							.toDto(markAuthorService.create(markAuthorMapper.dtoToEntity(markDTO, current.getId()))),
					HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@PostMapping(value = "/book", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MarkBookDTO> createMarkForBook(@Valid @RequestBody MarkBookDTO markDTO) {
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			return new ResponseEntity<>(
					markBookMapper.toDto(markBookService.create(markBookMapper.dtoToEntity(markDTO, current.getId()))),
					HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@PutMapping(value = "/author", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MarkAuthorDTO> updateMarkForAuthor(@Valid @RequestBody MarkAuthorDTO markDTO) {
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			return new ResponseEntity<>(markAuthorMapper.toDto(
					markAuthorService.update(current.getId(), markAuthorMapper.dtoToEntity(markDTO, current.getId()))),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@PutMapping(value = "/book", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MarkBookDTO> updateMarkForBook(@Valid @RequestBody MarkBookDTO markDTO) {
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			return new ResponseEntity<>(markBookMapper.toDto(
					markBookService.update(current.getId(), markBookMapper.dtoToEntity(markDTO, current.getId()))),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
