package dayone.dayone.book.service;

import dayone.dayone.book.service.dto.BookSearchResponse;

import java.util.List;

public interface ExternalBookSearch {

    List<BookSearchResponse> searchBooks(String name);
}
