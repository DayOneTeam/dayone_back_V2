package dayone.dayone.user.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayone.dayone.user.entity.repository.dto.UserBookInfo;

import java.util.List;

public record UserBookListResponse(
    @JsonProperty("user_books")
    List<UserBookResponse> userBooks
) {

    public static UserBookListResponse from(final List<UserBookInfo> bookInfos) {
        final List<UserBookResponse> userBookResponses = bookInfos.stream()
            .map(UserBookResponse::from)
            .toList();

        return new UserBookListResponse(userBookResponses);
    }
}
