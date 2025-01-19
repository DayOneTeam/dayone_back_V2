package dayone.dayone.user.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayone.dayone.user.entity.repository.dto.UserBookLogInfo;

import java.time.LocalDateTime;

public record UserBookLogResponse (
    Long id,
    String passage,
    String comment,
    @JsonProperty("created_at")
    LocalDateTime createdAt
) {

    public static UserBookLogResponse from(final UserBookLogInfo bookLogInfo) {
        return new UserBookLogResponse(bookLogInfo.getId(),
            bookLogInfo.getPassage(),
            bookLogInfo.getComment(),
            bookLogInfo.getCreatedAt());
    }
}
