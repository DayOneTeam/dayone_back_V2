package dayone.dayone.fixture;

import dayone.dayone.book.entity.Book;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.booklog.entity.repository.BookLogRepository;
import dayone.dayone.booklog.entity.value.Comment;
import dayone.dayone.booklog.entity.value.Passage;
import dayone.dayone.support.DateConstant;
import dayone.dayone.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestBookLogFactory {

    @Autowired
    private BookLogRepository bookLogRepository;

    public List<BookLog> createNBookLog(final int cnt, final Book book, final User user) {
        List<BookLog> bookLogs = new ArrayList<>();
        for (int i = 1; i <= cnt; i++) {
            final BookLog bookLog = BookLog.forSave("의미있는 구절" + i, "내가 느낀 감정" + i, book, user);
            bookLogRepository.save(bookLog);
            bookLogs.add(bookLog);
        }
        return bookLogs;
    }

    public BookLog createBookLog(final Book book, final User user) {
        BookLog bookLog = BookLog.forSave("의미있는 구절", "내가 느낀 감정", book, user);
        bookLogRepository.save(bookLog);
        return bookLog;
    }

    public List<BookLog> createBookLogWrittenThisWeek(final int cnt, final Book book, final User user) {
        List<BookLog> bookLogs = new ArrayList<>();
        for (int i = 1; i <= cnt; i++) {
            final BookLog bookLog = new BookLog(null, new Passage("의미있는 구절" + i), new Comment("내가 느낀 감정" + i), book, user, i, DateConstant.THIS_MONDAY.plusDays(i - 1));
            bookLogs.add(bookLog);
        }
        bookLogRepository.saveAll(bookLogs);
        return bookLogs;
    }

    public List<BookLog> createBookLogWrittenLastWeek(final int cnt, final Book book, final User user) {
        List<BookLog> bookLogs = new ArrayList<>();
        for (int i = 1; i <= cnt; i++) {
            final BookLog bookLog = new BookLog(null, new Passage("의미있는 구절" + i), new Comment("내가 느낀 감정" + i), book, user, i, DateConstant.LAST_MONDAY.plusDays(i - 1));
            bookLogs.add(bookLog);
        }
        bookLogRepository.saveAll(bookLogs);
        return bookLogs;
    }
}
