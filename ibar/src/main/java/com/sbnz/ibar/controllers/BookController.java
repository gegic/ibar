package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.BookDto;
import com.sbnz.ibar.dto.FilterDto;
import com.sbnz.ibar.dto.RatingIntervalDto;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.FileService;
import com.sbnz.ibar.services.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    private final BookService bookService;

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAll();

        return ResponseEntity.ok(books);
    }

    @PreAuthorize("permitAll()")
	@GetMapping(value = "/{id}")
	public ResponseEntity<BookDto> getBook(@PathVariable UUID id) throws EntityDoesNotExistException {
		return ResponseEntity.ok(bookService.getById(id));
	}

    @PostMapping(value = "/search")
    public ResponseEntity<List<BookDto>> search(@RequestParam(name = "query", defaultValue = "")  String searchQuery,
                                                @RequestBody FilterDto filterDto) throws FileNotFoundException {
        return ResponseEntity.ok(bookService.search(searchQuery, filterDto));
    }

    @PreAuthorize("hasAuthority('ROLE_READER')")
    @GetMapping(value = "/top-rated")
    public ResponseEntity<List<BookDto>> topRated() {
        return ResponseEntity.ok(bookService.getTopRated());
    }

    @PreAuthorize("hasAuthority('ROLE_READER')")
    @GetMapping(value = "/recommended")
    public ResponseEntity<List<BookDto>> recommended() {
        return ResponseEntity.ok(bookService.getRecommended());
    }

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
//	public ResponseEntity<BookResDTO> updateBook(@PathVariable UUID id,
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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteBook(@PathVariable UUID id) throws IOException {
        boolean statusOfDeletingBook = bookService.delete(id);

        return ResponseEntity.ok(statusOfDeletingBook);
    }

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
