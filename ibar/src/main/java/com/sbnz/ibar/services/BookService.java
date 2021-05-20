package com.sbnz.ibar.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import com.sbnz.ibar.dto.ReadingProgressDto;
import com.sbnz.ibar.mapper.ReadingProgressMapper;
import com.sbnz.ibar.model.*;
import com.sbnz.ibar.repositories.*;
import com.sbnz.ibar.utils.Constants;
import lombok.AllArgsConstructor;
import org.kie.api.runtime.KieSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sbnz.ibar.mapper.FileService;

import javassist.NotFoundException;

@Service
@AllArgsConstructor
public class BookService {

	private final BookRepository bookRepository;
	private final CategoryService categoryService;
	private final FileService fileService;
	private final ReviewRepository reviewRepository;
	private final AuthorRepository authorRepository;
	private final UserRepository userRepository;
	private final ReadingProgressRepository readingProgressRepository;
	private final ReadingProgressMapper readingProgressMapper;
	private final KieService kieService;

	public Page<Book> findAll(Pageable pageable) {
		return bookRepository.findAll(pageable);
	}

	@Transactional
	public Iterable<Book> getAll() {
		return bookRepository.findAll();
	}

	public Book getById(Long id) {
		Optional<Book> book = bookRepository.findById(id);
		return book.orElse(null);
	}

	public Book create(Book entity) throws Exception {
		return null;
	}

	@Transactional
	public boolean delete(Long id) throws Exception {
		Book existingBook = getById(id);
		if (existingBook == null) {
			throw new NotFoundException("Book with given id doesn't exist.");
		}

		fileService.deleteImageFromFile(existingBook.getImage());

		reviewRepository.deleteAllByBookId(id);

		bookRepository.deleteById(id);

		return true;
	}

	public Book update(Long id, Book entity) throws NotFoundException {
		return null;
	}

	@Transactional
	public Book update(Long id, Book entity, MultipartFile newImage) throws NotFoundException, IOException {
		Book existingBook = getById(id);
		if (existingBook == null) {
			throw new NotFoundException("Book with given id doesn't exist.");
		}

		Category category = categoryService.getById(entity.getCategory().getId());
		if (category == null) {
			throw new NotFoundException("Category doesn't exist.");
		}

		existingBook.setCategory(category);
		existingBook.setDescription(entity.getDescription());
		existingBook.setType(entity.getType());

		Set<Author> writtenBy = getAuthorsOfBook(entity);

		existingBook.setAuthors(writtenBy);

		if (!newImage.isEmpty()) {
			fileService.uploadNewImage(newImage, existingBook.getImage());
		}
		return bookRepository.save(existingBook);
	}

	@Transactional
	public Book create(Book entity, MultipartFile file) throws Exception {
		Category category = categoryService.getById(entity.getCategory().getId());
		if (category == null) {
			throw new NotFoundException("Category doesn't exist.");
		}

		entity.setCategory(category);

		String imagePath = fileService.saveImage(file, entity.getName());

		entity.setImage(imagePath);

		entity.setAuthors(getAuthorsOfBook(entity));

		return bookRepository.save(entity);
	}

	public ReadingProgressDto setReadingProgress(long bookId, long readerId, long progress) {
		Book book = this.bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId));

		User user = this.userRepository.findById(readerId)
				.orElseThrow(() -> new EntityNotFoundException("Reader with id " + readerId));

		if (!(user instanceof Reader)) {
			throw new IllegalArgumentException("Passed id of a non-reader user " + readerId);
		}

		Reader reader = (Reader) user;

		ReadingProgress readingProgress = this.readingProgressRepository.findByBookIdAndReaderId(bookId, readerId)
				.orElse(new ReadingProgress(book, reader));

		if (progress > readingProgress.getProgress()) {
			readingProgress.setProgress(progress);
			KieSession kieSession = kieService.getSession(Constants.BOOKS_SESSION, Constants.BOOKS_AGENDA);
			kieSession.insert(readingProgress);
			kieService.runSession(kieSession);
			readingProgress = readingProgressRepository.save(readingProgress);
		}
		return readingProgressMapper.toDto(readingProgress);
	}

	public Page<Book> getByCategoryId(Long id, Pageable pageable) {
		return bookRepository.getByCategoryId(id, pageable);
	}

	public Page<Book> findByCategoryIdAndNameContains(Long id, String name, Pageable pageable) {
		return bookRepository.findByCategoryIdAndNameContainingIgnoreCase(id, name, pageable);
	}

	public Page<Book> findByNameContains(String name, Pageable pageable) {
		return bookRepository.findByNameContainingIgnoreCase(name, pageable);
	}

	private Set<Author> getAuthorsOfBook(Book entity) throws NotFoundException {
		Set<Author> writtenBy = new HashSet<Author>();

		for (Author author : entity.getAuthors()) {
			author = authorRepository.findById(author.getId()).orElse(null);

			if (author == null)
				throw new NotFoundException("Author with given id doesn't exists.");

			writtenBy.add(author);
		}

		return writtenBy;
	}
}
