package dayone.dayone.user.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayone.dayone.user.entity.repository.dto.UserBookLogCountInfo;

import java.util.List;

public record UserBookLogCountInWeekResponse(

    @JsonProperty("user_book_log_counts")
    List<UserBookLogCountResponse> userBookLogCounts
) {

    public static UserBookLogCountInWeekResponse from(final List<UserBookLogCountInfo> userBookLogCountInfos) {
        final List<UserBookLogCountResponse> userBookLogCountResponses = userBookLogCountInfos.stream()
            .map(UserBookLogCountResponse::from)
            .toList();
        return new UserBookLogCountInWeekResponse(userBookLogCountResponses);
    }
}
