package dayone.dayone.book.service.dto;

public record BookSearchResponse(
    String title,
    String image,
    String author,
    String publisher,
    String pubdate,
    String isbn
) {
}
