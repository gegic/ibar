package com.ktsnwt.project.team9.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.ktsnwt.project.team9.repositories.IAuthorRepository;
import com.ktsnwt.project.team9.repositories.IReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ktsnwt.project.team9.helper.FileService;
import com.ktsnwt.project.team9.model.Author;
import com.ktsnwt.project.team9.model.Book;
import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.repositories.IBookRepository;

import javassist.NotFoundException;

@Service
public class BookService {

	@Autowired
	private IBookRepository bookRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private FileService fileService;

	@Autowired
	private IReviewRepository reviewRepository;

	@Autowired
	private IAuthorRepository authorRepository;

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
