package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.BookDto;
import com.sbnz.ibar.dto.PlanDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.CategoryMapper;
import com.sbnz.ibar.services.CategoryService;
import com.sbnz.ibar.services.PlanService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/plans", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlanController {
	private final PlanService planService;

	@PreAuthorize("hasAuthority('ROLE_READER')")
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

}
