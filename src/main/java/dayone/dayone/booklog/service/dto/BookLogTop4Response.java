package dayone.dayone.booklog.service.dto;

import dayone.dayone.booklog.entity.BookLog;

import java.util.List;

public record BookLogTop4Response(
    List<BookLogResponse> bookLogs
) {

    public static BookLogTop4Response from(final List<BookLog> bookLogs) {
        final List<BookLogResponse> response = bookLogs.stream()
            .map(BookLogResponse::from)
            .toList();

        return new BookLogTop4Response(response);
    }
}
