package dayone.dayone.book.service;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.book.service.dto.BookCreateRequest;
import dayone.dayone.book.service.dto.BookSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final ExternalBookSearch bookSearch;
    private final BookRepository bookRepository;

    public List<BookSearchResponse> searchBooks(final String name) {
        return bookSearch.searchBooks(name);
    }

    public Long create(final BookCreateRequest request) {
        Book book = Book.forSave(request.title(), request.author(), request.publisher(), request.thumbnail(), request.isbn());
        bookRepository.save(book);
        return book.getId();
    }
}
