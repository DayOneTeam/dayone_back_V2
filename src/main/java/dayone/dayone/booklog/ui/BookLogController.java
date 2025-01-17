package dayone.dayone.booklog.ui;

import dayone.dayone.booklog.service.BookLogService;
import dayone.dayone.booklog.service.dto.BookLogCreateRequest;
import dayone.dayone.booklog.service.dto.BookLogDetailResponse;
import dayone.dayone.booklog.service.dto.BookLogPaginationListResponse;
import dayone.dayone.booklog.service.dto.BookLogTop4Response;
import dayone.dayone.global.response.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/api/v1/book-logs")
@RestController
public class BookLogController {

    private final BookLogService bookLogService;

    @PostMapping
    public ResponseEntity<Void> create(final @RequestBody BookLogCreateRequest request) {
        final Long savedId = bookLogService.create(request);
        return ResponseEntity.created(URI.create(String.format("/api/v1/book-logs/%d", savedId))).build();
    }

    @GetMapping
    public CommonResponseDto<BookLogPaginationListResponse> getBookLogs(@RequestParam(value = "cursor", defaultValue = "-1") String cursor) {
        final BookLogPaginationListResponse response = bookLogService.getAllBookLogs(Long.parseLong(cursor));
        return CommonResponseDto.forSuccess(1, "BookLog 조회 성공", response);
    }

    @GetMapping("/{id}")
    public CommonResponseDto<BookLogDetailResponse> getBookLogById(@PathVariable("id") Long bookLogId) {
        final BookLogDetailResponse response = bookLogService.getBookLogById(bookLogId);
        return CommonResponseDto.forSuccess(1, "BookLog 상세 조회 성공", response);
    }

    @GetMapping("/top4")
    public CommonResponseDto<BookLogTop4Response> getTop4BookLogs() {
        final BookLogTop4Response response = bookLogService.getTop4BookLogs(LocalDateTime.now());
        return CommonResponseDto.forSuccess(1, "Top4 BookLog 조회 성공", response);
    }
}
