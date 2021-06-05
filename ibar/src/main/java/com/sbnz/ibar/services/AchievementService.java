package com.sbnz.ibar.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityExistsException;

import com.sbnz.ibar.model.Achievement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.repositories.ReaderRepository;
import com.sbnz.ibar.repositories.AchievementRepository;

@Service
public class AchievementService {


	private AchievementRepository achevementRepository;


	private ReaderRepository regsteredUserRepository;

	public Iterable<Achievement> getAll() {
		return achevementRepository.findAll();
	}

	public Achievement getById(UUID id) {
		Optional<Achievement> achievement = achevementRepository.findById(id);

		return achievement.orElse(null);
	}

	public Achievement create(Achievement entity) throws Exception {
		Achievement achievement = achevementRepository.findByName(entity.getName());

		if (achievement != null) {
			throw new EntityExistsException("Geolocation with given lat and lon already exists.");
		}

		achievement = achevementRepository.save(entity);

		return achievement;
	}

	public boolean delete(UUID id) throws Exception {
		List<Reader> usersWithAchievement = (List<Reader>) regsteredUserRepository.findByAchievementId(id);

		if (usersWithAchievement.size() == 0) {
			throw new Exception("Can't delete achievement while users exist with that achievement.");
		}

		achevementRepository.deleteById(id);

		return true;
	}

	public Achievement update(UUID id, Achievement entity) throws Exception {
		Achievement achievement = getById(id);

		if (achievement == null) {
			throw new Exception("Achievement with given id doesn't exist."); // todo
		}

		achievement.setName(entity.getName());

		achevementRepository.save(achievement);

		return null;
	}

}
