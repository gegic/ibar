package com.ktsnwt.project.team9.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import com.ktsnwt.project.team9.model.Achievement;
import org.openqa.selenium.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Reader;
import com.ktsnwt.project.team9.repositories.IReaderRepository;
import com.ktsnwt.project.team9.repositories.IAchievementRepository;

@Service
public class AchievementService {

	@Autowired
	private IAchievementRepository achievementRepository;

	@Autowired
	private IReaderRepository registeredUserRepository;

	public Iterable<Achievement> getAll() {
		return achievementRepository.findAll();
	}

	public Achievement getById(Long id) {
		Optional<Achievement> achievement = achievementRepository.findById(id);

		return achievement.orElse(null);
	}

	public Achievement create(Achievement entity) throws Exception {
		Achievement achievement = achievementRepository.findByName(entity.getName());

		if (achievement != null) {
			throw new EntityExistsException("Geolocation with given lat and lon already exists.");
		}

		achievement = achievementRepository.save(entity);

		return achievement;
	}

	public boolean delete(Long id) throws Exception {
		List<Reader> usersWithAchievement = (List<Reader>) registeredUserRepository.findByAchievementId(id);

		if (usersWithAchievement.size() == 0) {
			throw new Exception("Can't delete achievement while users exist with that achievement.");
		}

		achievementRepository.deleteById(id);

		return true;
	}

	public Achievement update(Long id, Achievement entity) throws Exception {
		Achievement achievement = getById(id);

		if (achievement == null) {
			throw new NotFoundException("Achievement with given id doesn't exist.");
		}

		achievement.setName(entity.getName());

		achievementRepository.save(achievement);

		return null;
	}

}
