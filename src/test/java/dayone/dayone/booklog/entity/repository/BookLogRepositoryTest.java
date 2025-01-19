package dayone.dayone.booklog.entity.repository;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.booklog.entity.value.Comment;
import dayone.dayone.booklog.entity.value.Passage;
import dayone.dayone.support.DateConstant;
import dayone.dayone.support.RepositoryTest;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.UserRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;

class BookLogRepositoryTest extends RepositoryTest {

    @Autowired
    private BookLogRepository bookLogRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("BookLog 중 최신 10개 정보를 불러온다.")
    @Test
    void getBookLogsRecently10() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        final Book book = Book.forSave("책 제목", "작가", "출판사", "이미지", "ISBN");
        bookRepository.save(book);
        final User user = User.forSave("test@test.com", "password", "이름");
        userRepository.save(user);

        final List<BookLog> nBookLogWrittenNow = createNBookLogWrittenNow(20, book, user);
        bookLogRepository.saveAll(nBookLogWrittenNow);

        // when
        final Slice<BookLog> result = bookLogRepository.findAllByOrderByCreatedAtDesc(pageable);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(10);
            softAssertions.assertThat(result.getContent().get(0).getId()).isEqualTo(20L);
            softAssertions.assertThat(result.getContent().get(9).getId()).isEqualTo(11L);
        });
    }

    @DisplayName("특정 id 이후의 최신 BookLog 정보를 불러온다.")
    @Test
    void getBookLogsRecentlyAfterId() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        final Book book = Book.forSave("책 제목", "작가", "출판사", "이미지", "ISBN");
        bookRepository.save(book);
        final User user = User.forSave("test@test.com", "password", "이름");
        userRepository.save(user);

        final List<BookLog> nBookLogWrittenNow = createNBookLogWrittenNow(10, book, user);
        bookLogRepository.saveAll(nBookLogWrittenNow);

        // when
        final Slice<BookLog> result = bookLogRepository.findAllByIdLessThanOrderByCreatedAtDesc(11L, pageable);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(10);
            softAssertions.assertThat(result.getContent().get(0).getId()).isEqualTo(10L);
            softAssertions.assertThat(result.getContent().get(9).getId()).isEqualTo(1L);
        });
    }

    private List<BookLog> createNBookLogWrittenNow(final int cnt, final Book book, final User user) {
        List<BookLog> bookLogs = new ArrayList<>();
        for (int i = 1; i <= cnt; i++) {
            final BookLog bookLog = new BookLog(null, new Passage("의미있는 구절" + i), new Comment("내가 느낀 감정" + i), book, user, 5, DateConstant.NOW.plusSeconds(i));
            bookLogs.add(bookLog);
        }
        return bookLogs;
    }

    @DisplayName("이번주에 작성된 bookLog들을 조회한다.")
    @Test
    void getBookLogsWrittenThisWeek() {
        // given
        final Book book = Book.forSave("책 제목", "작가", "출판사", "이미지", "ISBN");
        bookRepository.save(book);
        final User user = User.forSave("test@test.com", "password", "이름");
        userRepository.save(user);

        final List<BookLog> bookLogsWrittenThisWeek = createNBookLogWrittenThisWeek(10, book, user);
        final List<BookLog> bookLogsWrittenLastWeek = createNBookLogWrittenLastWeek(10, book, user);

        bookLogRepository.saveAll(bookLogsWrittenThisWeek);
        bookLogRepository.saveAll(bookLogsWrittenLastWeek);

        // when
        final List<BookLog> result = bookLogRepository.findAllByCreatedAtBetween(DateConstant.THIS_MONDAY, DateConstant.THIS_SUNDAY);
        for (final BookLog bookLog : result) {
            System.out.println(bookLog.getCreatedAt());
        }

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(10);
            softAssertions.assertThat(result.get(0).getId()).isEqualTo(bookLogsWrittenThisWeek.get(0).getId());
            softAssertions.assertThat(result.get(9).getId()).isEqualTo(bookLogsWrittenThisWeek.get(9).getId());
        });
    }

    private static List<BookLog> createNBookLogWrittenThisWeek(final int cnt, final Book book, final User user) {
        List<BookLog> bookLogs = new ArrayList<>();

        int likeCount = 1;
        for (long i = 1; i <= cnt; i++) {
            final BookLog bookLog = new BookLog(null, new Passage("의미있는 구절" + i), new Comment("내가 느낀 감정" + i), book, user, likeCount++, DateConstant.THIS_MONDAY);
            bookLogs.add(bookLog);
        }
        return bookLogs;
    }

    private static List<BookLog> createNBookLogWrittenLastWeek(final int cnt, final Book book, final User user) {
        List<BookLog> bookLogs = new ArrayList<>();

        int likeCount = 1;
        for (long i = 1; i <= cnt; i++) {
            final BookLog bookLog = new BookLog(null, new Passage("의미있는 구절" + i), new Comment("내가 느낀 감정" + i), book, user, likeCount++, DateConstant.LAST_MONDAY);
            bookLogs.add(bookLog);
        }
        return bookLogs;
    }
}
