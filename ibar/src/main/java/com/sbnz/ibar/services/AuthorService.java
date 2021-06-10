package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.AuthorDto;
import com.sbnz.ibar.dto.RatingIntervalDto;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.AuthorMapper;
import com.sbnz.ibar.mapper.FileService;
import com.sbnz.ibar.model.Author;
import com.sbnz.ibar.repositories.AuthorRepository;
import lombok.AllArgsConstructor;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.runtime.KieSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final FileService fileService;

    private final KieService kieService;

    private final AuthorMapper authorMapper;

    @Transactional
    public Iterable<AuthorDto> getAll() {
        List<Author> authors = authorRepository.findAll();

        return authors.stream().map(this::toAuthorDto).collect(Collectors.toList());
    }

    public AuthorDto getById(UUID id)
            throws EntityNotFoundException {
        Optional<Author> author = authorRepository.findById(id);

        return this.toAuthorDto(author.orElseThrow(EntityNotFoundException::new));
    }

    public List<AuthorDto> findAllByRatingInterval(RatingIntervalDto ratingIntervalDTO)
            throws FileNotFoundException {
        ArrayList<Author> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules.templates/authorRatingSearch.drt");

        ObjectDataCompiler converter = new ObjectDataCompiler();

        List<RatingIntervalDto> data = new ArrayList<>();

        data.add(ratingIntervalDTO);

        String drl = converter.compile(data, template);

        KieSession kieSession = kieService.createKieSessionFromDrl(drl);

        List<Author> authors = authorRepository.findAll();

        for (Author author : authors) {
            kieSession.insert(author);
        }

        kieSession.setGlobal("result", result);

        kieSession.fireAllRules();

        return result.stream().map(this::toAuthorDto).collect(Collectors.toList());
    }

    @Transactional
    public AuthorDto create(AuthorDto entity, MultipartFile file)
            throws IOException {
        Author author = new Author(entity);

        author.setId(UUID.randomUUID());

        String imagePath = fileService.saveImage(file, entity.getName());

        author.setImage(imagePath);

        author = authorRepository.save(author);

        return this.toAuthorDto(author);
    }

    @Transactional
    public AuthorDto update(UUID id, AuthorDto entity, MultipartFile newImage)
            throws EntityDoesNotExistException, IOException {
        Optional<Author> existingAuthor = authorRepository.findById(id);

        if (!this.doesAuthorExist(existingAuthor)) {
            throw new EntityDoesNotExistException(entity.getName(), id);
        }

        Author author = existingAuthor.get();

        author.setName(entity.getName());
        author.setDescription(entity.getDescription());
        author.setDateOfBirth(entity.getDateOfBirth().toInstant());
        author.setDateOfDeath(entity.getDateOfDeath().toInstant());

        if (!newImage.isEmpty()) {
            fileService.uploadNewImage(newImage, author.getImage());
        }

        authorRepository.save(author);

        return this.toAuthorDto(author);
    }

    @Transactional
    public boolean delete(UUID id)
            throws IOException {
        Optional<Author> existingAuthor = authorRepository.findById(id);

        if (!this.checkAllValiditiesForDeletingAuthor(existingAuthor)) {
            return false;
        }

        Author author = existingAuthor.get();

        fileService.deleteImageFromFile(author.getImage());

        authorRepository.deleteById(id);

        return true;
    }

    private AuthorDto toAuthorDto(Author author) {
        return authorMapper.toDto(author);
    }

    private boolean checkAllValiditiesForDeletingAuthor(Optional<Author> existingAuthor) {
        if (!this.doesAuthorExist(existingAuthor)) {
            return false;
        }

        Author author = existingAuthor.get();

        if (this.doesAuthorHaveWrittenBooks(author.getId())) {
            return false;
        }

        return true;
    }

    private boolean doesAuthorExist(Optional<Author> author) {
        return author.isPresent();
    }

    private boolean doesAuthorHaveWrittenBooks(UUID id) {
        return authorRepository.getNumberOfBooks(id) > 0;
    }
}
