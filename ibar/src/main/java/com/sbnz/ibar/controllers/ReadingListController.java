package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.ReadingListItemDto;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.services.ReadingListService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/reading-list", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReadingListController {

    private final ReadingListService readingListService;

    @PostMapping(path="book/{bookId}")
    ResponseEntity<ReadingListItemDto> addToReadingList(@PathVariable UUID bookId)
            throws EntityDoesNotExistException {
        return ResponseEntity.ok(readingListService.add(bookId));
    }

    @DeleteMapping(path="book/{bookId}")
    ResponseEntity<Void> removeFromReadingList(@PathVariable UUID bookId)
            throws EntityDoesNotExistException {
        readingListService.remove(bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "book/{bookId}")
    ResponseEntity<ReadingListItemDto> getReadingListItem(@PathVariable UUID bookId) throws EntityDoesNotExistException {
        return ResponseEntity.ok(readingListService.get(bookId));
    }
}
