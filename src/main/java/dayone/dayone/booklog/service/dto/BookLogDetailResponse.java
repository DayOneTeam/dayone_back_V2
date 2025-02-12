package dayone.dayone.booklog.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayone.dayone.booklog.entity.BookLog;

import java.time.LocalDateTime;

public record BookLogDetailResponse(
    long id,
    String passage,
    String comment,
    @JsonProperty("like_count")
    int likeCnt,
    @JsonProperty("book_title")
    String bookTitle,
    @JsonProperty("book_cover")
    String bookCover,
    @JsonProperty("user_name")
    String userName,
    @JsonProperty("profile_image")
    String profileImage,
    @JsonProperty("created_at")
    LocalDateTime createdAt
) {
    public static BookLogDetailResponse of(final BookLog bookLog) {
        return new BookLogDetailResponse(bookLog.getId(),
            bookLog.getPassage(),
            bookLog.getComment(),
            bookLog.getLikeCount(),
            bookLog.getBook().getTitle(),
            bookLog.getBook().getThumbnail(),
            bookLog.getUser().getName(),
            bookLog.getUser().getProfileImage(),
            bookLog.getCreatedAt());
    }
}
