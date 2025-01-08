package dayone.dayone.booklog.ui;

import dayone.dayone.booklog.service.BookLogService;
import dayone.dayone.booklog.service.dto.BookLogCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("api/v1/book-logs")
@RestController
public class BookLogController {

    private final BookLogService bookLogService;

    @PostMapping
    public ResponseEntity<Void> create(final @RequestBody BookLogCreateRequest request) {
        final Long savedId = bookLogService.create(request);
        return ResponseEntity.created(URI.create(String.format("/api/v1/book-logs/%d", savedId))).build();
    }
}
