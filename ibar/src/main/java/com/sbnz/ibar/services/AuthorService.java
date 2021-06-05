package com.sbnz.ibar.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.sbnz.ibar.dto.RatingIntervalDto;
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

import javassist.NotFoundException;

@Service
@AllArgsConstructor
public class AuthorService {


    private final AuthorRepository authorRepository;

    private final FileService fileService;

    private final KieService kieService;

    public Page<Author> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Transactional
    public Iterable<Author> getAll() {
        return authorRepository.findAll();
    }

    public Author getById(UUID id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.orElse(null);

    }

    public ArrayList<Author> findAllByRatingInterval(RatingIntervalDto ratingIntervalDTO) throws FileNotFoundException {
        ArrayList<Author> result = new ArrayList<>();

        InputStream template = new FileInputStream("../drools/src/main/resources/rules.templates/authorRatingSearch.drt");

        ObjectDataCompiler converter = new ObjectDataCompiler();

        List<RatingIntervalDto> data = new ArrayList<>();

        data.add(ratingIntervalDTO);

        String drl = converter.compile(data, template);

        KieSession kieSession = kieService.createKieSessionFromDRL(drl);

        for (Author author : getAll()) {
            kieSession.insert(author);
        }

        kieSession.setGlobal("result", result);

        kieSession.fireAllRules();

        return result;
    }

    public Author create(Author entity) throws Exception {
        return null;
    }

    @Transactional
    public boolean delete(UUID id) throws Exception {
        Author existingAuthor = getById(id);
        if (existingAuthor == null) {
            throw new NotFoundException("Author with given id doesn't exist.");
        }

        // TODO
//		if (existingAuthor.getWrittenBooks().size() > 0) {
//			throw new Error("Can't delete author with written books.");
//		}

        fileService.deleteImageFromFile(existingAuthor.getImage());

        authorRepository.deleteById(id);

        return true;
    }

    public Author update(UUID id, Author entity) throws NotFoundException {
        return null;
    }

    @Transactional
    public Author update(UUID id, Author entity, MultipartFile newImage) throws NotFoundException, IOException {
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
