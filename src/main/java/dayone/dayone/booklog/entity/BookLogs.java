package dayone.dayone.booklog.entity;

import lombok.Getter;

import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

@Getter
public class BookLogs {

    private static final int BOOK_LOG_TOP_COUNT = 4;

    private final List<BookLog> bookLogs;

    public BookLogs(final List<BookLog> bookLogs) {
        this.bookLogs = bookLogs;
    }

    public List<BookLog> getMostLikedBookLogs() {
        return bookLogs.stream()
            .sorted(Comparator.comparingInt(BookLog::getLikeCount).reversed()
                .thenComparing(comparing(BookLog::getId).reversed())
            )
            .limit(BOOK_LOG_TOP_COUNT)
            .toList();
    }
}
