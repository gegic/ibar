package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.AuthorDto;
import com.sbnz.ibar.dto.CategoryDto;
import com.sbnz.ibar.dto.PlanDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.services.PlanService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/plans", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlanController {
    private final PlanService planService;

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<PlanDto>> getPlans() {
        return ResponseEntity.ok(planService.getAll());
    }

    @PreAuthorize("hasAuthority('ROLE_READER')")
    @PostMapping(value = "/subscribe")
    public ResponseEntity<Void> subscribe(@RequestBody PlanDto dto)
            throws EntityAlreadyExistsException, EntityDoesNotExistException {
        planService.subscribe(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlanDto> createPlan(@Valid @RequestBody PlanDto planDto)
            throws Exception {
        PlanDto newPlan = planService.create(planDto);

        return ResponseEntity.ok(newPlan);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PlanDto> updatePlan(@PathVariable UUID id, @Valid @RequestBody PlanDto planDto)
            throws Exception {
        PlanDto updatedPlan = planService.update(id, planDto);

        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Boolean> deletePlan(@PathVariable UUID id) throws IOException {
        boolean result = planService.delete(id);

        return ResponseEntity.ok(result);
    }

}
