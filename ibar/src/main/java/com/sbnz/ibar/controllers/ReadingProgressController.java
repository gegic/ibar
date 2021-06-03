package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.ReadingProgressDto;
import com.sbnz.ibar.dto.ReviewDto;
import com.sbnz.ibar.dto.ReviewNumbersDto;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.services.ReadingProgressService;
import com.sbnz.ibar.services.ReviewService;
import com.sbnz.ibar.utils.PageableExtractor;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/reading-progress", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReadingProgressController {

    private final ReadingProgressService progressService;

    @PostMapping
    ResponseEntity<ReadingProgressDto> newProgress(@RequestBody ReadingProgressDto progressDto)
            throws EntityDoesNotExistException {
        return ResponseEntity.ok(progressService.save(progressDto));
    }

    @GetMapping(path = "book/{bookId}")
    ResponseEntity<ReadingProgressDto> getProgress(@PathVariable long bookId) throws EntityDoesNotExistException {
        return ResponseEntity.ok(progressService.get(bookId));
    }
}
