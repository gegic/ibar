package com.sbnz.ibar.mapper;

import com.sbnz.ibar.dto.BookDto;
import com.sbnz.ibar.model.Author;
import com.sbnz.ibar.model.Book;
import com.sbnz.ibar.model.Reader;
import com.sbnz.ibar.model.ReadingListItem;
import com.sbnz.ibar.repositories.ReadingListItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookMapper {

	private final ReadingListItemRepository listItemRepository;

	public BookDto toBookDto(Book entity) {
		BookDto dto = new BookDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setQuantity(entity.getQuantity());
		dto.setCategoryId(entity.getCategory().getId());
		dto.setCategoryName(entity.getCategory().getName());
		dto.setCover(entity.getCover());
		dto.setNumReviews(entity.getNumReviews());
		dto.setAverageRating(entity.getAverageRating());
		dto.setAuthorIds(entity.getAuthors().stream().map(Author::getId).collect(Collectors.toSet()));
		dto.setAuthorNames(entity.getAuthors().stream().map(Author::getName).collect(Collectors.toSet()));
		dto.setPdf(entity.getPdf());
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (o instanceof Reader) {
			Reader r = (Reader) o;
			Optional<ReadingListItem> itemOptional = listItemRepository.findByBookIdAndReaderId(entity.getId(), r.getId());
			dto.setInReadingList(itemOptional.isPresent());
		}
		return dto;
	}

	public Book toEntity(BookDto dto) {
		Book book = new Book();
		book.setId(dto.getId());
		book.setName(dto.getName());
		book.setDescription(dto.getDescription());
		book.setQuantity(dto.getQuantity());
		book.setCover(dto.getCover());
		book.setPdf(dto.getPdf());
		return book;
	}

}
