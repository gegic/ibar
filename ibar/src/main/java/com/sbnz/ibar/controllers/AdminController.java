package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.UserDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.services.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/admins", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:4200", maxAge = 3600)
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllAdmins() {
        Iterable<UserDto> author = adminService.getAll();

        return ResponseEntity.ok(author);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> createAdmin(@Valid @RequestBody UserDto userDto)
            throws EntityAlreadyExistsException {
        UserDto newAdmin = adminService.create(userDto);

        return ResponseEntity.ok(newAdmin);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteAdmin(@PathVariable UUID id) {
        boolean deleteStatus = adminService.delete(id);

        return ResponseEntity.ok(deleteStatus);
    }
}
