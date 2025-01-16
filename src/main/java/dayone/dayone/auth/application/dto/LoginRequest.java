package dayone.dayone.auth.application.dto;

public record LoginRequest(
    String email,
    String password
) {
}
