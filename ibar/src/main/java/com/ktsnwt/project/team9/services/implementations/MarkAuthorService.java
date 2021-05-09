package com.ktsnwt.project.team9.services.implementations;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Author;
import com.ktsnwt.project.team9.model.Mark;
import com.ktsnwt.project.team9.model.MarkAuthor;
import com.ktsnwt.project.team9.model.RegisteredUser;
import com.ktsnwt.project.team9.repositories.IAuthorRepository;
import com.ktsnwt.project.team9.repositories.IMarkAuthorRepository;
import com.ktsnwt.project.team9.repositories.IRegisteredUser;
import com.ktsnwt.project.team9.repositories.ITitleRepository;
import com.ktsnwt.project.team9.services.interfaces.IMarkAuthorService;

import lombok.AllArgsConstructor;

@Service
public class MarkAuthorService implements IMarkAuthorService {

	@Autowired
	private IMarkAuthorRepository markAuthorRepository;

	@Autowired
	private AuthorService authorService;

	@Autowired
	private IAuthorRepository authorRepository;

	@Autowired
	private RegisteredUserService rUserService;

	@Override
	public Iterable<MarkAuthor> getAll() {
		return markAuthorRepository.findAll();
	}

	@Override
	public MarkAuthor getById(Long id) {
		return markAuthorRepository.findById(id).orElse(null);
	}

	@Override
	public MarkAuthor create(MarkAuthor entity) throws Exception {
		RegisteredUser user = rUserService.getById(entity.getGrader().getId());
		if (user == null) {
			throw new NoSuchElementException("Grader doesn't exist.");
		}

		Author author = authorService.getById(entity.getAuthor().getId());
		if (author == null) {
			throw new NoSuchElementException("Author doesn't exist.");
		}

		List<MarkAuthor> marks = markAuthorRepository.findByAuthorId(author.getId());

		double sum = 0;

		for (MarkAuthor mark : marks) {
			sum += mark.getValue();
		}

		sum += entity.getValue();
		author.setAverageMark(sum / (marks.size() + 1));

		authorRepository.save(author);

		return markAuthorRepository.save(entity);
	}

	@Override
	public MarkAuthor update(Long id, MarkAuthor entity) throws Exception {
		MarkAuthor mark = markAuthorRepository.findByGraderAndAuthorId(id, entity.getAuthor().getId());
		if (mark == null) {
			throw new NoSuchElementException("Mark doesn't exist.");
		}

		RegisteredUser user = rUserService.getById(entity.getGrader().getId());
		if (user == null) {
			throw new NoSuchElementException("Grader doesn't exist.");
		}

		Author author = authorService.getById(entity.getAuthor().getId());

		if (author == null) {
			throw new NoSuchElementException("Author doesn't exist.");
		}

		mark.setValue(entity.getValue());

		List<MarkAuthor> marks = markAuthorRepository.findByAuthorId(author.getId());

		double sum = 0;

		for (Mark m : marks) {
			if (m.getGrader().getId() == mark.getGrader().getId()) {
				sum += entity.getValue();
			} else {
				sum += m.getValue();
			}
		}

		author.setAverageMark(sum / marks.size());

		authorRepository.save(author);

		return markAuthorRepository.save(mark);
	}

	@Override
	public boolean delete(Long id) throws Exception {
		return false;
	}

	public MarkAuthor findByUserIdAndAuthorId(Long userId, Long authorId) {
		return markAuthorRepository.findByGraderAndAuthorId(userId, authorId);
	}

}
