package dayone.dayone.book.service;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.book.service.dto.BookCreateRequest;
import dayone.dayone.book.service.dto.BookSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookService {

    private final ExternalBookSearch bookSearch;
    private final BookRepository bookRepository;

    public List<BookSearchResponse> searchBooks(final String name) {
        return bookSearch.searchBooks(name);
    }

    @Transactional
    public Long create(final BookCreateRequest request) {
        final Optional<Book> alreadySavedBook = bookRepository.findByIsbn(request.isbn());
        if (alreadySavedBook.isPresent()) {
            return alreadySavedBook.get().getId();
        }
        Book book = Book.forSave(request.title(), request.author(), request.publisher(), request.thumbnail(), request.isbn());
        bookRepository.save(book);
        return book.getId();
    }
}
