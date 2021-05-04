package com.ktsnwt.project.team9.services.implementations;

import java.io.IOException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ktsnwt.project.team9.helper.implementations.FileService;
import com.ktsnwt.project.team9.model.Author;
import com.ktsnwt.project.team9.model.CommentAuthor;
import com.ktsnwt.project.team9.repositories.IAuthorRepository;
import com.ktsnwt.project.team9.services.interfaces.IAuthorService;

import javassist.NotFoundException;
import lombok.AllArgsConstructor;

@Service
public class AuthorService implements IAuthorService {

	@Autowired
	private IAuthorRepository authorRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private CommentAuthorService commentAuthorService;

	public Page<Author> findAll(Pageable pageable) {
		return authorRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Iterable<Author> getAll() {
		return authorRepository.findAll();
	}

	@Override
	public Author getById(Long id) {
		Optional<Author> author = authorRepository.findById(id);
		if (!author.isPresent()) {
			return null;
		}

		return author.get();
	}

	@Override
	public Author create(Author entity) throws Exception {
		return null;
	}

	@Transactional
	@Override
	public boolean delete(Long id) throws Exception {
		Author existingAuthor = getById(id);
		if (existingAuthor == null) {
			throw new NotFoundException("Author with given id doesn't exist.");
		}

		if (existingAuthor.getWrittenBooks().size() > 0) {
			throw new Error("Can't delete author with written books.");
		}
		fileService.deleteImageFromFile(existingAuthor.getImage());

		for (CommentAuthor comment : existingAuthor.getCommentAuthors()) {
			commentAuthorService.delete(comment.getId());
		}

		authorRepository.deleteById(id);

		return true;
	}

	@Override
	public Author update(Long id, Author entity) throws NotFoundException {
		return null;
	}

	@Transactional
	public Author update(Long id, Author entity, MultipartFile newImage) throws NotFoundException, IOException {
		Author existingAuthor = getById(id);
		if (existingAuthor == null) {
			throw new NotFoundException("Author with given id doesn't exist.");
		}

		existingAuthor.setDescription(entity.getDescription());
		existingAuthor.setDateOfBirth(entity.getDateOfBirth());
		existingAuthor.setDateOfDeath(entity.getDateOfDeath());

		if (!newImage.isEmpty()) {
			fileService.uploadNewImage(newImage, existingAuthor.getImage());
		}

		return authorRepository.save(existingAuthor);
	}

	@Transactional
	public Author create(Author entity, MultipartFile file) throws Exception {
		String imagePath = fileService.saveImage(file, entity.getName());

		entity.setImage(imagePath);

		return authorRepository.save(entity);

	}

	public Page<Author> findByNameContains(String name, Pageable pageable) {
		return authorRepository.findByNameContainingIgnoreCase(name, pageable);
	}
}
