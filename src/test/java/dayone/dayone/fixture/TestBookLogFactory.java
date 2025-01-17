package dayone.dayone.fixture;

import dayone.dayone.book.entity.Book;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.booklog.entity.repository.BookLogRepository;
import dayone.dayone.booklog.entity.value.Comment;
import dayone.dayone.booklog.entity.value.Passage;
import dayone.dayone.support.DateConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestBookLogFactory {

    @Autowired
    private BookLogRepository bookLogRepository;

    public List<BookLog> createNBookLog(final int cnt, final Book book) {
        List<BookLog> bookLogs = new ArrayList<>();
        for (int i = 1; i <= cnt; i++) {
            final BookLog bookLog = BookLog.forSave("의미있는 구절" + i, "내가 느낀 감정" + i, book);
            bookLogRepository.save(bookLog);
            bookLogs.add(bookLog);
        }
        return bookLogs;
    }

    public BookLog createBookLog(final Book book) {
        BookLog bookLog = BookLog.forSave("의미있는 구절", "내가 느낀 감정", book);
        bookLogRepository.save(bookLog);
        return bookLog;
    }

    public List<BookLog> createBookLogWrittenThisWeek(final int cnt, final Book book) {
        List<BookLog> bookLogs = new ArrayList<>();
        for (int i = 1; i <= cnt; i++) {
            final BookLog bookLog = new BookLog(null, new Passage("의미있는 구절" + i), new Comment("내가 느낀 감정" + i), book, 1L, i, DateConstant.THIS_MONDAY.plusSeconds(i));
            bookLogs.add(bookLog);
        }
        bookLogRepository.saveAll(bookLogs);
        return bookLogs;
    }

    public List<BookLog> createBookLogWrittenLastWeek(final int cnt, final Book book) {
        List<BookLog> bookLogs = new ArrayList<>();
        for (int i = 1; i <= cnt; i++) {
            final BookLog bookLog = new BookLog(null, new Passage("의미있는 구절" + i), new Comment("내가 느낀 감정" + i), book, 1L, i, DateConstant.LAST_MONDAY.plusSeconds(i));
            bookLogs.add(bookLog);
        }
        bookLogRepository.saveAll(bookLogs);
        return bookLogs;
    }
}
