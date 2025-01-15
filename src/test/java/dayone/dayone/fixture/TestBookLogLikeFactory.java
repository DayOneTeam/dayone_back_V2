package dayone.dayone.fixture;

import dayone.dayone.bookloglike.service.BookLogLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestBookLogLikeFactory {

    @Autowired
    private BookLogLikeService bookLogLikeService;

    public void createNBookLogLike(final int cnt, final Long bookLogId) {
        for (long i = 1; i <= cnt; i++) {
            bookLogLikeService.addLike(bookLogId, i);
        }
    }
}
