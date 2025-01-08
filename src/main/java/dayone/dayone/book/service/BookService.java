package dayone.dayone.book.service;

import dayone.dayone.book.service.dto.BookSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final ExternalBookSearch bookSearch;

    public List<BookSearchResponse> searchBooks(final String name) {
        return bookSearch.searchBooks(name);
    }
}
