package com.ktsnwt.project.team9.services.implementations;

import java.io.IOException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ktsnwt.project.team9.helper.implementations.FileService;
import com.ktsnwt.project.team9.model.Book;
import com.ktsnwt.project.team9.model.Category;
import com.ktsnwt.project.team9.model.CommentBook;
import com.ktsnwt.project.team9.model.Image;
import com.ktsnwt.project.team9.repositories.IBookRepository;
import com.ktsnwt.project.team9.services.interfaces.IBookService;

import javassist.NotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookService implements IBookService {

	private IBookRepository bookRepository;

	private CategoryService categoryService;

	private ImageService imageService;

	private FileService fileService;

	private CommentBookService commentBookService;

	public Page<Book> findAll(Pageable pageable) {
		return bookRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Iterable<Book> getAll() {
		return bookRepository.findAll();
	}

	@Override
	public Book getById(Long id) {
		Optional<Book> book = bookRepository.findById(id);
		if (!book.isPresent()) {
			return null;
		}

		return book.get();
	}

	@Override
	public Book create(Book entity) throws Exception {
		return null;
	}

	@Transactional
	@Override
	public boolean delete(Long id) throws Exception {
		Book existingBook = getById(id);
		if (existingBook == null) {
			throw new NotFoundException("Book with given id doesn't exist.");
		}

		fileService.deleteImageFromFile(existingBook.getImage().getUrl());

		for (CommentBook comment : existingBook.getComments()) {
			commentBookService.delete(comment.getId());
		}

		bookRepository.deleteById(id);

		return true;
	}

	@Override
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

		if (!newImage.isEmpty()) {
			fileService.uploadNewImage(newImage, existingBook.getImage().getUrl());
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

		Image image = imageService.create(new Image(imagePath));
		entity.setImage(image);

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

}
