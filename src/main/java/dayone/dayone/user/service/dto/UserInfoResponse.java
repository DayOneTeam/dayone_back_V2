package dayone.dayone.user.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayone.dayone.user.entity.User;

public record UserInfoResponse(
    String name,
    @JsonProperty("profile_image")
    String profileImage
) {
    public static UserInfoResponse from(final User user) {
        return new UserInfoResponse(user.getName(), user.getProfileImage());
    }
}
