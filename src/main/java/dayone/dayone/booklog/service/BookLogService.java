package dayone.dayone.booklog.service;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.booklog.entity.repository.BookLogRepository;
import dayone.dayone.booklog.exception.BookLogException;
import dayone.dayone.booklog.service.dto.BookLogCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookLogService {

    private final BookLogRepository bookLogRepository;
    private final BookRepository bookRepository;

    public Long create(final BookLogCreateRequest request) {
        final Book book = bookRepository.findById(request.bookId())
            .orElseThrow(() -> new BookException(BookErrorCode.BOOK_NOT_EXIST));

        final BookLog bookLog = BookLog.forSave(request.passage(), request.comment(), book);
        bookLogRepository.save(bookLog);
        return bookLog.getId();
    }
}
