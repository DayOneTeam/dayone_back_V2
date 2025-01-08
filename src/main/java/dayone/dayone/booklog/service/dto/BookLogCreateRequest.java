package dayone.dayone.booklog.service.dto;

public record BookLogCreateRequest(
    long bookId,
    String passage,
    String comment
) {
}
