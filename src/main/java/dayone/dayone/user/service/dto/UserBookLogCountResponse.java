package dayone.dayone.user.service.dto;

import dayone.dayone.user.entity.repository.dto.UserBookLogCountInfo;

public record UserBookLogCountResponse(
    String username,
    int count
) {
    public static UserBookLogCountResponse from(UserBookLogCountInfo userBookLogCountInfo) {
        return new UserBookLogCountResponse(userBookLogCountInfo.getName(), userBookLogCountInfo.getCount());
    }
}
