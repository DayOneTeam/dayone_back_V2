package dayone.dayone.bookloglike.ui;

import dayone.dayone.auth.ui.argumentresolver.AuthUser;
import dayone.dayone.bookloglike.service.BookLogLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<Void> addLike(@AuthUser final Long userId, @PathVariable("bookLogId") final Long bookLogId) {
        bookLogLikeService.addLike(bookLogId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLike(@AuthUser final Long userId, @PathVariable("bookLogId") final Long bookLogId) {
        bookLogLikeService.deleteLike(bookLogId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
