package com.ktsnwt.project.team9.services.implementations;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Author;
import com.ktsnwt.project.team9.model.CommentAuthor;
import com.ktsnwt.project.team9.repositories.ICommentAuthorRepository;
import com.ktsnwt.project.team9.services.interfaces.ICommentAuthorService;

@Service
public class CommentAuthorService implements ICommentAuthorService {

	@Autowired
	private ICommentAuthorRepository commentAuthorRepository;

	@Autowired
	private AuthorService authorService;

	@Override
	public Iterable<CommentAuthor> getAll() {
		return commentAuthorRepository.findAll();
	}

	@Override
	public CommentAuthor getById(Long id) {
		Optional<CommentAuthor> c = commentAuthorRepository.findById(id);
		if (!c.isPresent()) {
			return null;
		}
		return c.get();
	}

	@Transactional
	public CommentAuthor create(CommentAuthor entity) throws Exception {
		Author author = authorService.getById(entity.getAuthor().getId());
		if (author == null) {
			throw new NoSuchElementException("Author doesn't exist.");
		}

		entity.setAuthor(author);

		return commentAuthorRepository.save(entity);
	}

	@Override
	@Transactional
	public boolean delete(Long id) throws Exception {
		CommentAuthor c = getById(id);
		if (c == null) {
			throw new NoSuchElementException("Comment doesn't exist");
		}

		commentAuthorRepository.delete(id);

		return true;
	}

	@Override
	public CommentAuthor update(Long id, CommentAuthor entity) throws Exception {
		return null;
	}

	public Page<CommentAuthor> findAllByAuthorId(Pageable pageable, Long id) {
		return commentAuthorRepository.findByAuthorId(id, pageable);
	}
}
