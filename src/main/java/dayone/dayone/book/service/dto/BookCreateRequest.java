package dayone.dayone.book.service.dto;

public record BookCreateRequest(
    String title,
    String author,
    String publisher,
    String thumbnail,
    String isbn
) {
}
