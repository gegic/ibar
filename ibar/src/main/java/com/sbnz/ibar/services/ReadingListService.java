package com.sbnz.ibar.services;

import com.sbnz.ibar.dto.ReadingListItemDto;
import com.sbnz.ibar.exceptions.EntityDoesNotExistException;
import com.sbnz.ibar.mapper.ReadingListItemMapper;
import com.sbnz.ibar.model.Book;
import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.model.ReadingListItem;
import com.sbnz.ibar.model.User;
import com.sbnz.ibar.repositories.BookRepository;
import com.sbnz.ibar.repositories.ReadingListItemRepository;
import com.sbnz.ibar.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class ReadingListService {

    private final ReadingListItemRepository listItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReadingListItemMapper mapper;

    public ReadingListItemDto add(UUID bookId) throws EntityDoesNotExistException {

        Book b = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityDoesNotExistException(Book.class.getName(), bookId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(user instanceof Reader)) {
            throw new EntityDoesNotExistException(Reader.class.getName(), user.getId());
        }

        Optional<ReadingListItem> itemOptional = listItemRepository.findByBookIdAndReaderId(bookId, user.getId());

        if (itemOptional.isPresent()) {
            return mapper.toDto(itemOptional.get());
        }

        ReadingListItem item = new ReadingListItem(b, (Reader) user);

        return mapper.toDto(item);
    }

    public void remove(UUID bookId) throws EntityDoesNotExistException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(user instanceof Reader)) {
            throw new EntityDoesNotExistException(Reader.class.getName(), user.getId());
        }

        Optional<ReadingListItem> itemOptional = listItemRepository.findByBookIdAndReaderId(bookId, user.getId());

        if (itemOptional.isEmpty()) {
            return;
        }

        listItemRepository.deleteByBookIdAndReaderId(bookId, user.getId());
    }

    public ReadingListItemDto get(UUID bookId) throws EntityDoesNotExistException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(user instanceof Reader)) {
            return null;
        }
        Book b = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityDoesNotExistException(Book.class.getName(), bookId));

        Reader r = (Reader) user;

        ReadingListItem rli = listItemRepository
                .findByBookIdAndReaderId(bookId, user.getId())
                .orElseThrow(() -> new EntityDoesNotExistException(ReadingListItem.class.getName(), b.getId()));

        return mapper.toDto(rli);
    }

}
