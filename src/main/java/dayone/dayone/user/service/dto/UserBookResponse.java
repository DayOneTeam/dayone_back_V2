package dayone.dayone.user.service.dto;

import dayone.dayone.user.entity.repository.dto.UserBookInfo;

public record UserBookResponse(
    Long id,
    String thumbnail
) {

    public static UserBookResponse from(final UserBookInfo bookInfo) {
        return new UserBookResponse(bookInfo.getId(), bookInfo.getThumbnail());
    }
}
