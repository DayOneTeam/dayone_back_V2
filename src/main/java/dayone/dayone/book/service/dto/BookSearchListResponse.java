package dayone.dayone.book.service.dto;

import java.util.List;

public record BookSearchListResponse(
    List<BookSearchResponse> items
) {
    public boolean isEmpty() {
        return this.items() == null || this.items().isEmpty();
    }
}
