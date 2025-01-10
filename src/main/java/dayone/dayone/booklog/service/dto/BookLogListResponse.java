package dayone.dayone.booklog.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayone.dayone.booklog.entity.BookLog;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public record BookLogListResponse(
    @JsonProperty("book_logs")
    List<BookLogResponse> bookLogs,
    boolean next,
    @JsonProperty("next_cursor")
    long nextCursor
) {
    public static BookLogListResponse of(final Slice<BookLog> bookLogs, final Boolean next) {
        final List<BookLogResponse> bookLogResponses = bookLogs.stream()
            .map(BookLogResponse::of)
            .collect(Collectors.toList());

        if (next) {
            final long nextCursor = bookLogs.getContent().get(bookLogs.getSize() - 1).getId();
            return new BookLogListResponse(bookLogResponses, true, nextCursor);
        }
        return new BookLogListResponse(bookLogResponses, next, -1);
    }
}
