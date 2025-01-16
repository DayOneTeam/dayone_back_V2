package dayone.dayone.auth.application.dto;

public record TokenInfo(
    String accessToken,
    String refreshToken
) {
}
