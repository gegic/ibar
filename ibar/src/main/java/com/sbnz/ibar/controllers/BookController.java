package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.*;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.FileService;
import com.sbnz.ibar.services.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAll();

        return ResponseEntity.ok(books);
    }

	@GetMapping(value = "/{id}")
	public ResponseEntity<BookDto> getBook(@PathVariable UUID id) throws EntityDoesNotExistException {
		return ResponseEntity.ok(bookService.getById(id));
	}

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<BookDto> create(@RequestBody BookDto bookDto) throws EntityDoesNotExistException {
        BookDto saved = this.bookService.create(bookDto);
        return ResponseEntity.created(URI.create(saved.getId().toString())).body(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    ResponseEntity<BookDto> update(@RequestBody BookDto bookDto) throws EntityDoesNotExistException {
        BookDto saved = this.bookService.update(bookDto);
        return ResponseEntity.ok(saved);
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path="/add-cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CoverDto> setPhoto(@RequestParam("cover") MultipartFile photoFile){
        UUID savedPath = this.bookService.setCover(photoFile);
        return ResponseEntity.created(URI.create(savedPath.toString())).body(new CoverDto(savedPath));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path="/add-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ContentFileDto> setPdf(@RequestParam("pdf") MultipartFile pdfFile) {
        ContentFileDto saved = this.bookService.setPdf(pdfFile);
        return ResponseEntity.created(URI.create(saved.getPath().toString())).body(saved);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteBook(@PathVariable UUID id) throws IOException {
        boolean statusOfDeletingBook = bookService.delete(id);

        return ResponseEntity.ok(statusOfDeletingBook);
    }
}
