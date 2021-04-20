package com.ktsnwt.project.team9.services.implementations;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.RegisteredUser;
import com.ktsnwt.project.team9.model.Title;
import com.ktsnwt.project.team9.repositories.IRegisteredUser;
import com.ktsnwt.project.team9.repositories.ITitleRepository;
import com.ktsnwt.project.team9.services.interfaces.ITitleService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TitleService implements ITitleService {

	private ITitleRepository titleRepository;

	private IRegisteredUser registeredUserRepository;

	@Override
	public Iterable<Title> getAll() {
		return titleRepository.findAll();
	}

	@Override
	public Title getById(Long id) {
		Optional<Title> title = titleRepository.findById(id);

		if (!title.isPresent()) {
			return null;
		}

		return title.get();
	}

	@Override
	public Title create(Title entity) throws Exception {
		Title title = titleRepository.findByName(entity.getName());

		if (title != null) {
			throw new EntityExistsException("Geolocation with given lat and lon already exists.");
		}

		title = titleRepository.save(title);

		return title;
	}

	@Override
	public boolean delete(Long id) throws Exception {
		List<RegisteredUser> usersWithTitle = (List<RegisteredUser>) registeredUserRepository.findbyTitle(id);

		if (usersWithTitle.size() == 0) {
			throw new Exception("Can't delete title while users exist with that title.");
		}

		titleRepository.deleteById(id);

		return true;
	}

	@Override
	public Title update(Long id, Title entity) throws Exception {
		Title title = getById(id);

		if (title == null) {
			throw new NotFoundException("Title with given id doesn't exist.");
		}

		title.setName(entity.getName());

		titleRepository.save(title);

		return null;
	}

}
