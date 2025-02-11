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
    @JsonProperty("user_name")
    String userName,
    @JsonProperty("profile_image")
    String profileImage,
    @JsonProperty("created_at")
    LocalDateTime createdAt
) {
    public static BookLogResponse from(final BookLog bookLog) {
        return new BookLogResponse(bookLog.getId(),
            bookLog.getPassage(),
            bookLog.getComment(),
            bookLog.getLikeCount(),
            bookLog.getBook().getTitle(),
            bookLog.getUser().getName(),
            bookLog.getUser().getProfileImage(),
            bookLog.getCreatedAt());
    }
}
