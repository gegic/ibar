package com.ktsnwt.project.team9.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ktsnwt.project.team9.helper.implementations.CustomPageImplementation;
import com.ktsnwt.project.team9.helper.implementations.FileService;
import com.ktsnwt.project.team9.model.Author;
import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.services.implementations.AuthorService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/authors", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@CrossOrigin(origins = "https://localhost:4200", maxAge = 3600)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AuthorController {

	private AuthorService authorService;

	private AuthorMapper authorMapper;

	private FileService fileService;

	@PreAuthorize("permitAll()")
	@GetMapping(value = "/by-page")
	public ResponseEntity<Page<AuthorResDTO>> getAllAuthors(Pageable pageable) {
		Page<Author> page = authorService.findAll(pageable);
		return new ResponseEntity<>(createCustomPage(transformFromListToPage(page)), HttpStatus.OK);
	}

	@PreAuthorize("permitAll()")
	@GetMapping(value = "/category/{id}")
	public ResponseEntity<Page<AuthorResDTO>> getAuthorsByCategoryId(Pageable pageable, @PathVariable Long id) {
		Page<Author> page = authorService.getByCategoryId(id, pageable);
		return new ResponseEntity<>(createCustomPage(transformFromListToPage(page)), HttpStatus.OK);
	}

	@PreAuthorize("permitAll()")
	@GetMapping(value = "/category/{id}/find-by-name/{name}")
	public ResponseEntity<Page<AuthorResDTO>> findAuthorByCategoryIdAndName(Pageable pageable, @PathVariable Long id,
			@PathVariable String name) {
		Page<Author> page = authorService.findByCategoryIdAndNameContains(id, name, pageable);

		return new ResponseEntity<>(createCustomPage(transformFromListToPage(page)), HttpStatus.OK);
	}

	@PreAuthorize("permitAll()")
	@GetMapping(value = "/find-by-name/{name}")
	public ResponseEntity<Page<AuthorResDTO>> findAuthorByName(Pageable pageable, @PathVariable String name) {
		Page<Author> page = authorService.findByNameContains(name, pageable);

		return new ResponseEntity<>(createCustomPage(transformFromListToPage(page)), HttpStatus.OK);
	}

	@PreAuthorize("permitAll()")
	@GetMapping(value = "/{id}")
	public ResponseEntity<AuthorResDTO> getAuthor(@PathVariable Long id) {

		Author author = authorService.getById(id);
		if (author == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		AuthorResDTO authorResDTO = authorMapper.toDTORes(author);
		try {
			authorResDTO.setImage(fileService.uploadImageAsBase64(authorResDTO.getImage()));
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(authorResDTO, HttpStatus.OK);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<AuthorResDTO> createAuthor(@RequestPart("authorDTO") @Valid @NotNull AuthorDTO authorDTO,
			@RequestPart("file") MultipartFile file) {
		try {
			if (file == null || file.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			authorDTO.setAdmin(current.getId());
			AuthorResDTO authorResDTO = authorMapper.toDTORes(authorService.create(authorMapper.toEntity(authorDTO), file));

			authorResDTO.setImage(fileService.uploadImageAsBase64(authorResDTO.getImage()));

			return new ResponseEntity<>(authorResDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<AuthorResDTO> updateAuthor(@PathVariable Long id,
			@RequestPart("authorDTO") @Valid @NotNull AuthorDTO authorDTO, @RequestPart("file") MultipartFile file) {

		try {
			AuthorResDTO authorResDTO = authorMapper.toDTORes(authorService.update(id, authorMapper.toEntity(authorDTO), file));

			authorResDTO.setImage(fileService.uploadImageAsBase64(authorResDTO.getImage()));

			return new ResponseEntity<>(authorResDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Boolean> deleteAuthor(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(authorService.delete(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private Page<AuthorResDTO> transformFromListToPage(Page<Author> page) {
		List<AuthorResDTO> authorsResDTO = authorMapper.toDTOResList(page.toList());
		authorsResDTO.stream().forEach(i -> {
			try {
				i.setImage(fileService.uploadImageAsBase64(i.getImage()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return new PageImpl<>(authorsResDTO, page.getPageable(), page.getTotalElements());
	}

	private Page<AuthorResDTO> customTransform(Page<Author> page) {
		List<AuthorResDTO> authorsResDTO = authorMapper.toDTOResList(page.toList());
		return new PageImpl<>(authorsResDTO, page.getPageable(), page.getTotalElements());
	}

	private CustomPageImplementation<AuthorResDTO> createCustomPage(Page<AuthorResDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}
}
