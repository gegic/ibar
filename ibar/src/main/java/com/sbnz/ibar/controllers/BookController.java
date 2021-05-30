package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.BookDto;
import com.sbnz.ibar.dto.RatingIntervalDto;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.sbnz.ibar.mapper.FileService;
import com.sbnz.ibar.services.BookService;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class BookController {

    private final BookService bookService;
    private final FileService fileService;

    @PreAuthorize("permitAll()")
	@GetMapping(value = "/{id}")
	public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
		return ResponseEntity.ok(bookService.getById(id));
	}

    @PreAuthorize("permitAll()")
    @PostMapping(value = "/ratings-interval")
    public ResponseEntity<List<BookDto>> getAllBooksByRatingInterval(@RequestBody RatingIntervalDto ratingIntervalDTO)
            throws FileNotFoundException {

            List<BookDto> books = bookService.findAllByRatingInterval(ratingIntervalDTO);
            return ResponseEntity.ok(books);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/author/{authorName}")
    public ResponseEntity<List<BookDto>> getAllBooksByAuthorsName(@PathVariable String authorName)
            throws FileNotFoundException {
        List<BookDto> books = bookService.findAllByAuthorsName(authorName);
        return ResponseEntity.ok(books);
    }

    @PreAuthorize("hasAuthority('ROLE_READER')")
    @GetMapping(value = "top-rated")
    public ResponseEntity<List<BookDto>> topRated() {
        return ResponseEntity.ok(bookService.getTopRated());
    }

    @PreAuthorize("hasAuthority('ROLE_READER')")
    @GetMapping(value = "recommended")
    public ResponseEntity<List<BookDto>> recommended() {
        return ResponseEntity.ok(bookService.getRecommended());
    }

//	@PreAuthorize("permitAll()")
//	@GetMapping(value = "/by-page")
//	public ResponseEntity<Page<BookResDTO>> getAllBooks(Pageable pageable) {
//		Page<Book> page = bookService.findAll(pageable);
//		return new ResponseEntity<>(createCustomPage(transformFromListToPage(page)), HttpStatus.OK);
//	}
//
//	@PreAuthorize("permitAll()")
//	@GetMapping(value = "/category/{id}")
//	public ResponseEntity<Page<BookResDTO>> getBooksByCategoryId(Pageable pageable, @PathVariable Long id) {
//		Page<Book> page = bookService.getByCategoryId(id, pageable);
//		return new ResponseEntity<>(createCustomPage(transformFromListToPage(page)), HttpStatus.OK);
//	}
//
//	@PreAuthorize("permitAll()")
//	@GetMapping(value = "/category/{id}/find-by-name/{name}")
//	public ResponseEntity<Page<BookResDTO>> findBookByCategoryIdAndName(Pageable pageable, @PathVariable Long id,
//			@PathVariable String name) {
//		Page<Book> page = bookService.findByCategoryIdAndNameContains(id, name, pageable);
//
//		return new ResponseEntity<>(createCustomPage(transformFromListToPage(page)), HttpStatus.OK);
//	}
//
//	@PreAuthorize("permitAll()")
//	@GetMapping(value = "/find-by-name/{name}")
//	public ResponseEntity<Page<BookResDTO>> findBookByName(Pageable pageable, @PathVariable String name) {
//		Page<Book> page = bookService.findByNameContains(name, pageable);
//
//		return new ResponseEntity<>(createCustomPage(transformFromListToPage(page)), HttpStatus.OK);
//	}
//

//
//	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<BookResDTO> createBook(@RequestPart("bookDTO") @Valid @NotNull BookDTO bookDTO,
//			@RequestPart("file") MultipartFile file) {
//		try {
//			if (file == null || file.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//			}
//
//			BookResDTO bookResDTO = bookMapper.toDTORes(bookService.create(bookMapper.toEntity(bookDTO), file));
//
//			bookResDTO.setImage(fileService.uploadImageAsBase64(bookResDTO.getImage()));
//
//			return new ResponseEntity<>(bookResDTO, HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
//
//	@PutMapping(value = "/{id}")
//	public ResponseEntity<BookResDTO> updateBook(@PathVariable Long id,
//			@RequestPart("bookDTO") @Valid @NotNull BookDTO bookDTO, @RequestPart("file") MultipartFile file) {
//
//		try {
//			BookResDTO bookResDTO = bookMapper.toDTORes(bookService.update(id, bookMapper.toEntity(bookDTO), file));
//
//			bookResDTO.setImage(fileService.uploadImageAsBase64(bookResDTO.getImage()));
//
//			return new ResponseEntity<>(bookResDTO, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
//
//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<Boolean> deleteBook(@PathVariable Long id) {
//		try {
//			return new ResponseEntity<>(bookService.delete(id), HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//	private Page<BookResDTO> transformFromListToPage(Page<Book> page) {
//		List<BookResDTO> booksResDTO = bookMapper.toDTOResList(page.toList());
//		booksResDTO.stream().forEach(i -> {
//			try {
//				i.setImage(fileService.uploadImageAsBase64(i.getImage()));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
//		return new PageImpl<>(booksResDTO, page.getPageable(), page.getTotalElements());
//	}
//
//	private CustomPageImplementation<BookResDTO> createCustomPage(Page<BookResDTO> page) {
//		return new CustomPageImplementation<>(page.getContent(), page.getNumber(), page.getSize(),
//				page.getTotalElements(), null, page.isLast(), page.getTotalPages(), null, page.isFirst(),
//				page.getNumberOfElements());
//	}
}
