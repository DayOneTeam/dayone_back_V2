package dayone.dayone.booklog.service;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.booklog.entity.BookLogs;
import dayone.dayone.booklog.entity.repository.BookLogRepository;
import dayone.dayone.booklog.exception.BookLogErrorCode;
import dayone.dayone.booklog.exception.BookLogException;
import dayone.dayone.booklog.service.dto.BookLogCreateRequest;
import dayone.dayone.booklog.service.dto.BookLogDetailResponse;
import dayone.dayone.booklog.service.dto.BookLogPaginationListResponse;
import lombok.RequiredArgsConstructor;
import dayone.dayone.booklog.service.dto.BookLogTop4Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookLogService {

    private final static int BOOK_LOG_PAGE_SIZE = 10;

    private final BookLogRepository bookLogRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Long create(final BookLogCreateRequest request) {
        final Book book = bookRepository.findById(request.bookId())
            .orElseThrow(() -> new BookException(BookErrorCode.BOOK_NOT_EXIST));

        final BookLog bookLog = BookLog.forSave(request.passage(), request.comment(), book);
        bookLogRepository.save(bookLog);
        return bookLog.getId();
    }

    public BookLogPaginationListResponse getAllBookLogs(final Long cursor) {
        Pageable pageable = PageRequest.of(0, BOOK_LOG_PAGE_SIZE);

        // 초기 요청인 경우
        if (cursor == -1L) {
            final Slice<BookLog> bookLogs = bookLogRepository.findAllByOrderByCreatedAtDesc(pageable);
            return BookLogPaginationListResponse.of(bookLogs, bookLogs.hasNext());
        }

        final Slice<BookLog> bookLogs = bookLogRepository.findAllByIdLessThanOrderByCreatedAtDesc(cursor, pageable);
        return BookLogPaginationListResponse.of(bookLogs, bookLogs.hasNext());
    }

    public BookLogDetailResponse getBookLogById(final Long bookLogId) {
        final BookLog bookLog = bookLogRepository.findById(bookLogId)
            .orElseThrow(() -> new BookLogException(BookLogErrorCode.NOT_EXIST_BOOK_LOG));
        return BookLogDetailResponse.of(bookLog);
    }

    public BookLogTop4Response getTop4BookLogs(final LocalDateTime now) {
        LocalDateTime monDay = now.with(DayOfWeek.MONDAY);
        LocalDateTime sunDay = now.with(DayOfWeek.SUNDAY);
        final List<BookLog> bookLogsWrittenThisWeek = bookLogRepository.findAllByCreatedAtBetween(monDay, sunDay);

        BookLogs bookLogs = new BookLogs(bookLogsWrittenThisWeek);
        return BookLogTop4Response.from(bookLogs.getMostLikedBookLogs());
    }
}
