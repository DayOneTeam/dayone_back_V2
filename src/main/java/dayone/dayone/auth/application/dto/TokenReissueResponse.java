package dayone.dayone.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenReissueResponse(
    @JsonProperty("access_token")
    String accessToken
) {
}
