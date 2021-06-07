package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.AuthorDto;
import com.sbnz.ibar.dto.RatingIntervalDto;
import com.sbnz.ibar.mapper.AuthorMapper;
import com.sbnz.ibar.mapper.FileService;
import com.sbnz.ibar.services.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/authors", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:4200", maxAge = 3600)
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final FileService fileService;

    @PreAuthorize("permitAll()")
    @PostMapping(value = "/ratings-interval")
    public ResponseEntity<?> getAllAuthorsByRatingInterval(@RequestBody RatingIntervalDto ratingIntervalDTO) {
        try {
            List<AuthorDto> authors = authorService.findAllByRatingInterval(ratingIntervalDTO)
                    .stream().map(authorMapper::toDto)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(authors, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//	@PreAuthorize("permitAll()")
//	@GetMapping(value = "/by-page")
//	public ResponseEntity<Page<AuthorDTO>> getAllAuthors(Pageable pageable) {
//		Page<Author> page = authorService.findAll(pageable);
//		return new ResponseEntity<>(createCustomPage(transformFromListToPage(page)), HttpStatus.OK);
//	}
//
//	@PreAuthorize("permitAll()")
//	@GetMapping(value = "/find-by-name/{name}")
//	public ResponseEntity<Page<AuthorResDTO>> findAuthorByName(Pageable pageable, @PathVariable String name) {
//		Page<Author> page = authorService.findByNameContains(name, pageable);
//
//		return new ResponseEntity<>(createCustomPage(transformFromListToPage(page)), HttpStatus.OK);
//	}
//
//	@PreAuthorize("permitAll()")
//	@GetMapping(value = "/{id}")
//	public ResponseEntity<AuthorResDTO> getAuthor(@PathVariable UUID id) {
//
//		Author author = authorService.getById(id);
//		if (author == null) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//		AuthorResDTO authorResDTO = authorMapper.toDTORes(author);
//		try {
//			authorResDTO.setImage(fileService.uploadImageAsBase64(authorResDTO.getImage()));
//		} catch (IOException e) {
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//		return new ResponseEntity<>(authorResDTO, HttpStatus.OK);
//	}
//
//	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<AuthorResDTO> createAuthor(@RequestPart("authorDTO") @Valid @NotNull AuthorDTO authorDTO,
//			@RequestPart("file") MultipartFile file) {
//		try {
//			if (file == null || file.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//			}
//
//			AuthorResDTO authorResDTO = authorMapper
//					.toDTORes(authorService.create(authorMapper.toEntity(authorDTO), file));
//
//			authorResDTO.setImage(fileService.uploadImageAsBase64(authorResDTO.getImage()));
//
//			return new ResponseEntity<>(authorResDTO, HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
//
//	@PutMapping(value = "/{id}")
//	public ResponseEntity<AuthorResDTO> updateAuthor(@PathVariable UUID id,
//			@RequestPart("authorDTO") @Valid @NotNull AuthorDTO authorDTO, @RequestPart("file") MultipartFile file) {
//
//		try {
//			AuthorResDTO authorResDTO = authorMapper
//					.toDTORes(authorService.update(id, authorMapper.toEntity(authorDTO), file));
//
//			authorResDTO.setImage(fileService.uploadImageAsBase64(authorResDTO.getImage()));
//
//			return new ResponseEntity<>(authorResDTO, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
//
//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<Boolean> deleteAuthor(@PathVariable UUID id) {
//		try {
//			return new ResponseEntity<>(authorService.delete(id), HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//	private Page<AuthorResDTO> transformFromListToPage(Page<Author> page) {
//		List<AuthorResDTO> authorsResDTO = authorMapper.toDTOResList(page.toList());
//		authorsResDTO.stream().forEach(i -> {
//			try {
//				i.setImage(fileService.uploadImageAsBase64(i.getImage()));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
//		return new PageImpl<>(authorsResDTO, page.getPageable(), page.getTotalElements());
//	}
//
//	private Page<AuthorResDTO> customTransform(Page<Author> page) {
//		List<AuthorResDTO> authorsResDTO = authorMapper.toDTOResList(page.toList());
//		return new PageImpl<>(authorsResDTO, page.getPageable(), page.getTotalElements());
//	}
//
//	private CustomPageImplementation<AuthorResDTO> createCustomPage(Page<AuthorResDTO> page) {
//		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
//				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
//				page.getNumberOfElements());
//	}
}
