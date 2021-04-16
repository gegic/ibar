package com.ktsnwt.project.team9.services.implementations;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Book;
import com.ktsnwt.project.team9.model.CommentBook;
import com.ktsnwt.project.team9.repositories.ICommentBookRepository;
import com.ktsnwt.project.team9.services.interfaces.ICommentBookService;

@Service
public class CommentBookService implements ICommentBookService {

	@Autowired
	private ICommentBookRepository commentBookRepository;

	@Autowired
	private BookService bookService;

	@Override
	public Iterable<CommentBook> getAll() {
		return commentBookRepository.findAll();
	}

	@Override
	public CommentBook getById(Long id) {
		Optional<CommentBook> c = commentBookRepository.findById(id);
		if (!c.isPresent()) {
			return null;
		}
		return c.get();
	}

	@Transactional
	public CommentBook create(CommentBook entity) throws Exception {
		Book book = bookService.getById(entity.getBook().getId());
		if (book == null) {
			throw new NoSuchElementException("Book doesn't exist.");
		}

		entity.setBook(book);

		return commentBookRepository.save(entity);
	}

	@Override
	@Transactional
	public boolean delete(Long id) throws Exception {
		CommentBook c = getById(id);
		if (c == null) {
			throw new NoSuchElementException("Comment doesn't exist");
		}

		commentBookRepository.delete(id);

		return true;
	}

	@Override
	public CommentBook update(Long id, CommentBook entity) throws Exception {
		return null;
	}

	public Page<CommentBook> findAllByBookId(Pageable pageable, Long id) {
		return commentBookRepository.findByBookId(id, pageable);
	}
}
