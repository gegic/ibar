package com.ktsnwt.project.team9.services.implementations;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktsnwt.project.team9.model.Book;
import com.ktsnwt.project.team9.model.Mark;
import com.ktsnwt.project.team9.model.MarkBook;
import com.ktsnwt.project.team9.model.RegisteredUser;
import com.ktsnwt.project.team9.repositories.IBookRepository;
import com.ktsnwt.project.team9.repositories.IMarkBookRepository;
import com.ktsnwt.project.team9.repositories.IRegisteredUser;
import com.ktsnwt.project.team9.repositories.ITitleRepository;
import com.ktsnwt.project.team9.services.interfaces.IMarkBookService;

import lombok.AllArgsConstructor;

@Service
public class MarkBookService implements IMarkBookService {

	@Autowired
	private IMarkBookRepository markBookRepository;

	@Autowired
	private BookService bookService;

	@Autowired
	private IBookRepository bookRepository;

	@Autowired
	private RegisteredUserService rUserService;

	@Override
	public Iterable<MarkBook> getAll() {
		return markBookRepository.findAll();
	}

	@Override
	public MarkBook getById(Long id) {
		return markBookRepository.findById(id).orElse(null);
	}

	@Override
	public MarkBook create(MarkBook entity) throws Exception {
		RegisteredUser user = rUserService.getById(entity.getGrader().getId());
		if (user == null) {
			throw new NoSuchElementException("Grader doesn't exist.");
		}

		Book book = bookService.getById(entity.getBook().getId());
		if (book == null) {
			throw new NoSuchElementException("Book doesn't exist.");
		}

		List<MarkBook> marks = markBookRepository.findByBookId(book.getId());

		double sum = 0;

		for (MarkBook mark : marks) {
			sum += mark.getValue();
		}

		sum += entity.getValue();
		book.setAverageMark(sum / (marks.size() + 1));

		bookRepository.save(book);

		return markBookRepository.save(entity);
	}

	@Override
	public MarkBook update(Long id, MarkBook entity) throws Exception {
		MarkBook mark = markBookRepository.findByGraderAndBookId(id, entity.getBook().getId());
		if (mark == null) {
			throw new NoSuchElementException("Mark doesn't exist.");
		}

		RegisteredUser user = rUserService.getById(entity.getGrader().getId());
		if (user == null) {
			throw new NoSuchElementException("Grader doesn't exist.");
		}

		Book book = bookService.getById(entity.getBook().getId());

		if (book == null) {
			throw new NoSuchElementException("Book doesn't exist.");
		}

		mark.setValue(entity.getValue());

		List<MarkBook> marks = markBookRepository.findByBookId(book.getId());

		double sum = 0;

		for (Mark m : marks) {
			if (m.getGrader().getId() == mark.getGrader().getId()) {
				sum += entity.getValue();
			} else {
				sum += m.getValue();
			}
		}

		book.setAverageMark(sum / marks.size());

		bookRepository.save(book);

		return markBookRepository.save(mark);
	}

	@Override
	public boolean delete(Long id) throws Exception {
		return false;
	}

	public MarkBook findByUserIdAndBookId(Long userId, Long bookId) {
		return markBookRepository.findByGraderAndBookId(userId, bookId);
	}

}
