package dayone.dayone.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
    @JsonProperty("access_token")
    String accessToken
) {
}
