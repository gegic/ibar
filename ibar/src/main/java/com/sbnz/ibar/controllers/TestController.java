package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.ReadingProgressDto;
import com.sbnz.ibar.services.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    private final BookService bookService;

    @PostMapping(path = "/progress")
    public ResponseEntity<ReadingProgressDto> setProgress(@RequestBody ReadingProgressDto readingProgressDto) {
        ReadingProgressDto progress = bookService.setReadingProgress(
                readingProgressDto.getBookId(),
                readingProgressDto.getReaderId(),
                readingProgressDto.getProgress()
        );

        return ResponseEntity.ok(progress);
    }

}
