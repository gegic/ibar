package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.AuthorDto;
import com.sbnz.ibar.dto.RatingIntervalDto;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.AuthorMapper;
import com.sbnz.ibar.mapper.FileService;
import com.sbnz.ibar.services.AuthorService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/authors", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:4200", maxAge = 3600)
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("permitAll()")
    @PostMapping(value = "/ratings-interval")
    public ResponseEntity<?> getAllAuthorsByRatingInterval(@RequestBody RatingIntervalDto ratingIntervalDTO)
            throws FileNotFoundException {
        List<AuthorDto> authors = authorService.findAllByRatingInterval(ratingIntervalDTO);

        return ResponseEntity.ok(authors);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<Iterable<AuthorDto>> getAllAuthors() {
        Iterable<AuthorDto> author = authorService.getAll();

        return ResponseEntity.ok(author);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable UUID id)
            throws EntityNotFoundException {
        AuthorDto author = authorService.getById(id);

        return ResponseEntity.ok(author);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthorDto> createAuthor(@RequestPart("authorDTO") @Valid @NotNull AuthorDto authorDTO,
                                                  @RequestPart("file") MultipartFile file)
            throws IOException {
        AuthorDto newAuthor = authorService.create(authorDTO, file);

        return ResponseEntity.ok(newAuthor);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable UUID id,
                                                  @RequestPart("authorDTO") @Valid @NotNull AuthorDto authorDTO,
                                                  @RequestPart("file") MultipartFile file)
            throws IOException, EntityDoesNotExistException {
        AuthorDto updatedAuthor = authorService.update(id, authorDTO, file);

        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteAuthor(@PathVariable UUID id) throws IOException {
        boolean result = authorService.delete(id);

        return ResponseEntity.ok(result);
    }

}
