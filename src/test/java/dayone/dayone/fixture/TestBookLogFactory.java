package dayone.dayone.fixture;

import dayone.dayone.book.entity.Book;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.booklog.entity.repository.BookLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class  TestBookLogFactory {

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
}
