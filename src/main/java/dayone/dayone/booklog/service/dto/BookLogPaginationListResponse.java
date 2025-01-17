package dayone.dayone.booklog.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayone.dayone.booklog.entity.BookLog;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public record BookLogPaginationListResponse(
    @JsonProperty("book_logs")
    List<BookLogResponse> bookLogs,
    boolean next,
    @JsonProperty("next_cursor")
    long nextCursor
) {
    public static BookLogPaginationListResponse of(final Slice<BookLog> bookLogs, final Boolean next) {
        final List<BookLogResponse> bookLogResponses = bookLogs.stream()
            .map(BookLogResponse::from)
            .collect(Collectors.toList());

        if (next) {
            final long nextCursor = bookLogs.getContent().get(bookLogs.getSize() - 1).getId();
            return new BookLogPaginationListResponse(bookLogResponses, true, nextCursor);
        }
        return new BookLogPaginationListResponse(bookLogResponses, next, -1);
    }
}
