package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.ReadingProgressDto;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.ReadingProgressMapper;
import com.sbnz.ibar.model.Book;
import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.model.ReadingProgress;
import com.sbnz.ibar.model.User;
import com.sbnz.ibar.repositories.BookRepository;
import com.sbnz.ibar.repositories.ReadingProgressRepository;
import com.sbnz.ibar.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ReadingProgressService {

    private final ReadingProgressRepository progressRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReadingProgressMapper progressMapper;

    public ReadingProgressDto save(ReadingProgressDto progressDto) throws EntityDoesNotExistException {

        Book b = bookRepository.findById(progressDto.getBookId())
                .orElseThrow(() -> new EntityDoesNotExistException(Book.class.getName(), progressDto.getBookId()));

        Reader u = (Reader) userRepository.findById(progressDto.getReaderId())
                .orElseThrow(() -> new EntityDoesNotExistException(Reader.class.getName(), progressDto.getReaderId()));

        ReadingProgress rp = progressRepository
                .findByBookIdAndReaderId(progressDto.getBookId(), progressDto.getReaderId())
                .orElse(new ReadingProgress(b, u));

        if (progressDto.getProgress() > rp.getProgress()) {
            rp.setProgress(progressDto.getProgress());
        }

        return progressMapper.toDto(progressRepository.save(rp));
    }

    public ReadingProgressDto get(UUID bookId) throws EntityDoesNotExistException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(user instanceof Reader)) {
            return null;
        }
        Book b = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityDoesNotExistException(Book.class.getName(), bookId));

        Reader r = (Reader) user;

        ReadingProgress rp = progressRepository.
                findByBookIdAndReaderId(bookId, user.getId()).orElse(new ReadingProgress(b, r));

        return progressMapper.toDto(rp);
    }

}
