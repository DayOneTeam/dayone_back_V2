package dayone.dayone.fixture;

import dayone.dayone.bookloglike.service.BookLogLikeService;
import dayone.dayone.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestBookLogLikeFactory {

    @Autowired
    private BookLogLikeService bookLogLikeService;

    public void createNBookLogLike(final Long bookLogId, final List<User> users) {
        for (User user : users) {
            bookLogLikeService.addLike(bookLogId, user.getId());
        }
    }
}
