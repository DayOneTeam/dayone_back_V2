package dayone.dayone.bookloglike.ui;

import dayone.dayone.bookloglike.service.BookLogLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/book-logs/{bookLogId}/like")
@RestController
public class BookLogLikeController {

    private final BookLogLikeService bookLogLikeService;

    @PostMapping
    public ResponseEntity<Void> addLike(@PathVariable("bookLogId") final Long bookLogId) {
        // TODO : 유저 인증 처리 후 요청 유저의 id로 변경하기
        bookLogLikeService.addLike(bookLogId, 1L);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
