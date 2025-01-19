package dayone.dayone.user.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayone.dayone.user.entity.repository.dto.UserBookLogInfo;

import java.util.List;

public record UserBookLogListResponse(
    @JsonProperty("book_logs")
    List<UserBookLogResponse> bookLogs
) {

    public static UserBookLogListResponse from(final List<UserBookLogInfo> bookLogs) {
        final List<UserBookLogResponse> userBookLogResponses = bookLogs.stream()
            .map(UserBookLogResponse::from)
            .toList();

        return new UserBookLogListResponse(userBookLogResponses);
    }
}
