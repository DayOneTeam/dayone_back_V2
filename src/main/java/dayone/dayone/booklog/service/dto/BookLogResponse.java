package dayone.dayone.booklog.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayone.dayone.booklog.entity.BookLog;

import java.time.LocalDateTime;

public record BookLogResponse(
    long id,
    String passage,
    String comment,
    @JsonProperty("like_count")
    int likeCnt,
    @JsonProperty("book_title")
    String bookTitle,
    @JsonProperty("created_at")
    LocalDateTime createdAt
    // TODO 유저 정보 추가하기
) {
    public static BookLogResponse of(final BookLog bookLog) {
        return new BookLogResponse(bookLog.getId(),
            bookLog.getPassage(),
            bookLog.getComment(),
            bookLog.getLikeCount(),
            bookLog.getBook().getTitle(),
            bookLog.getCreatedAt());
    }
}
