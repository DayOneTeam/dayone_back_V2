package dayone.dayone.booklog.entity.repository;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.support.RepositoryTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

class BookLogRepositoryTest extends RepositoryTest {

    @Autowired
    private BookLogRepository bookLogRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("BookLog 중 최신 10개 정보를 불러온다.")
    @Test
    void getBookLogsRecently10() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        final Book book = Book.forSave("책 제목", "작가", "출판사", "이미지", "ISBN");
        bookRepository.save(book);

        createBookLog(20, book);
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

        createBookLog(20, book);
        // when
        final Slice<BookLog> result = bookLogRepository.findAllByIdLessThanOrderByCreatedAtDesc(11L, pageable);


        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).hasSize(10);
            softAssertions.assertThat(result.getContent().get(0).getId()).isEqualTo(10L);
            softAssertions.assertThat(result.getContent().get(9).getId()).isEqualTo(1L);
        });
    }

    private void createBookLog(int cnt, Book book) {
        for (int i = 1; i <= cnt; i++) {
            final BookLog bookLog = BookLog.forSave("의미있는 구절" + i, "내가 느낀 감정" + i, book);
            bookLogRepository.save(bookLog);
        }
    }
}
