package dayone.dayone.book.ui;

import dayone.dayone.book.service.BookService;
import dayone.dayone.book.service.dto.BookCreateRequest;
import dayone.dayone.book.service.dto.BookSearchResponse;
import dayone.dayone.global.response.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/search")
    public CommonResponseDto<List<BookSearchResponse>> searchBooks(@RequestParam("name") String name) {
        final List<BookSearchResponse> response = bookService.searchBooks(name);
        return CommonResponseDto.forSuccess(1, "책 검색 정보 응답", response);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody BookCreateRequest request) {
        final Long savedBookId = bookService.create(request);
        return ResponseEntity.created(URI.create(String.format("/books/%d", savedBookId))).build();
    }
}
