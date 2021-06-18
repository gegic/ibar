package com.sbnz.ibar.controllers;

import com.sbnz.ibar.dto.PlanDto;
import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.model.Rank;
import com.sbnz.ibar.services.PlanService;
import com.sbnz.ibar.services.RankService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/ranks", produces = MediaType.APPLICATION_JSON_VALUE)
public class RankController {
	private final RankService rankService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Rank>> getRanks() {
		return ResponseEntity.ok(rankService.getAll());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Rank> create(@RequestBody Rank rank) throws Exception {
		return ResponseEntity.ok(rankService.create(rank));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping
	public ResponseEntity<Rank> update(@RequestBody Rank rank) throws EntityDoesNotExistException {
		return ResponseEntity.ok(rankService.update(rank));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) throws EntityAlreadyExistsException {
		rankService.delete(id);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('READER')")
	@GetMapping(value = "/reader")
	public ResponseEntity<Rank> getUserRank() {
		return ResponseEntity.ok(rankService.getUserRank());
	}

}
