package com.ktsnwt.project.team9.controllers;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.ktsnwt.project.team9.dto.CommentAuthorDTO;
import com.ktsnwt.project.team9.dto.CommentBookDTO;
import com.ktsnwt.project.team9.dto.response.CommentAuthorResDTO;
import com.ktsnwt.project.team9.dto.response.CommentBookResDTO;
import com.ktsnwt.project.team9.helper.implementations.CommentAuthorMapper;
import com.ktsnwt.project.team9.helper.implementations.CommentBookMapper;
import com.ktsnwt.project.team9.helper.implementations.CustomPageImplementation;
import com.ktsnwt.project.team9.model.CommentAuthor;
import com.ktsnwt.project.team9.model.CommentBook;
import com.ktsnwt.project.team9.model.User;
import com.ktsnwt.project.team9.services.implementations.CommentAuthorService;
import com.ktsnwt.project.team9.services.implementations.CommentBookService;

@RestController
@RequestMapping(value = "/api/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:4200", maxAge = 3600, allowedHeaders = "*")
public class CommentController {

	@Autowired
	private CommentBookService commentBookService;

	@Autowired
	private CommentAuthorService commentAuthorService;

	private CommentBookMapper commentBookMapper;

	private CommentAuthorMapper commentAuthorMapper;

	public CommentController() {
		commentBookMapper = new CommentBookMapper();
		commentAuthorMapper = new CommentAuthorMapper();
	}

	@PreAuthorize("permitAll()")
	@GetMapping(value = "/book/{id}")
	public ResponseEntity<Page<CommentBookResDTO>> getAllCommentsForBook(@PathVariable Long id, Pageable pageable) {
		Page<CommentBook> page = commentBookService.findAllByBookId(pageable, id);
		List<CommentBookResDTO> commentDTOs = commentBookMapper.toResDTOList(page.toList());
		Page<CommentBookResDTO> pageCommentDTOs = new PageImpl<>(commentDTOs, page.getPageable(),
				page.getTotalElements());
		return new ResponseEntity<>(createCustomPageBook(pageCommentDTOs), HttpStatus.OK);
	}

	@PreAuthorize("permitAll()")
	@GetMapping(value = "/author/{id}")
	public ResponseEntity<Page<CommentAuthorResDTO>> getAllCommentsForAuthor(@PathVariable Long id, Pageable pageable) {
		Page<CommentAuthor> page = commentAuthorService.findAllByAuthorId(pageable, id);
		List<CommentAuthorResDTO> commentDTOs = commentAuthorMapper.toResDTOList(page.toList());
		Page<CommentAuthorResDTO> pageCommentDTOs = new PageImpl<>(commentDTOs, page.getPageable(),
				page.getTotalElements());
		return new ResponseEntity<>(createCustomPageAuthor(pageCommentDTOs), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@PostMapping(value = "/author")
	public ResponseEntity<?> createCommentForAuthor(
			@RequestPart("commentDTO") @Valid @NotNull CommentAuthorDTO commentDTO) throws Exception {

		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CommentAuthor res = commentAuthorService.create(commentAuthorMapper.dtoToEntity(commentDTO, current.getId()));
		CommentAuthorResDTO resDTO = commentAuthorMapper.toResDTO(commentAuthorService.getById(res.getId()));
		return new ResponseEntity<>(resDTO, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@PostMapping(value = "/book")
	public ResponseEntity<?> createCommentForBook(@RequestPart("commentDTO") @Valid @NotNull CommentBookDTO commentDTO)
			throws Exception {

		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CommentBook res = commentBookService.create(commentBookMapper.dtoToEntity(commentDTO, current.getId()));
		CommentBookResDTO resDTO = commentBookMapper.toResDTO(commentBookService.getById(res.getId()));
		return new ResponseEntity<>(resDTO, HttpStatus.CREATED);
	}

	private Page<CommentBookResDTO> createCustomPageBook(Page<CommentBookResDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}

	private Page<CommentAuthorResDTO> createCustomPageAuthor(Page<CommentAuthorResDTO> page) {
		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
				page.getNumberOfElements());
	}

}
