package com.sbnz.ibar.services;

import com.sbnz.ibar.exceptions.EntityAlreadyExistsException;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.model.Rank;
import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.repositories.RankRepository;
import com.sbnz.ibar.repositories.ReaderRepository;
import lombok.AllArgsConstructor;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RankService {

	private final KieService kieService;
	private final RankRepository rankRepository;
	private final ReaderRepository readerRepository;

	public List<Rank> getAll() {
		return rankRepository.findAll();
	}

	public Rank getById(UUID id) {
		Optional<Rank> achievement = rankRepository.findById(id);

		return achievement.orElse(null);
	}

	public Rank getUserRank() {
		Reader reader = (Reader) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return reader.getRank();
	}

	public Rank create(Rank entity) throws Exception {
		Rank rank = this.rankRepository.findByName(entity.getName());
		if (rank != null) {
			throw new EntityExistsException("Geolocation with given lat and lon already exists.");
		}

		entity.setId(null);
		rank = this.rankRepository.save(entity);
		kieService.getRanksSession().insert(rank);

		return rank;
	}

	public boolean delete(UUID id) throws EntityAlreadyExistsException {
		List<Reader> usersWithRank = readerRepository.findByRankId(id);

		if (usersWithRank.size() == 0) {
			throw new EntityAlreadyExistsException(Rank.class.getName(), id);
		}
		Optional<Rank> optionalRank = rankRepository.findById(id);
		if (optionalRank.isEmpty()) {
			return true;
		}
		Rank rank = optionalRank.get();
		if (rank.getHigherRank() != null) {
			throw new EntityAlreadyExistsException(Rank.class.getName(), id);
		}
		FactHandle factHandle = kieService.getRanksSession().getFactHandle(rank);
		kieService.getRanksSession().delete(factHandle);
		rankRepository.delete(rank);
		return true;
	}

	public Rank update(Rank entity) throws EntityDoesNotExistException {
		Optional<Rank> optionalRank = rankRepository.findById(entity.getId());

		if (optionalRank.isEmpty()) {
			throw new EntityDoesNotExistException(Rank.class.getName(), entity.getId());
		}

		Rank rank = optionalRank.get();

		FactHandle factHandle = kieService.getRanksSession().getFactHandle(rank);
		kieService.getRanksSession().delete(factHandle);

		rank.setName(entity.getName());
		rank.setPoints(entity.getPoints());

		rank = this.rankRepository.save(rank);

		kieService.getRanksSession().insert(rank);
		return rank;
	}

}
