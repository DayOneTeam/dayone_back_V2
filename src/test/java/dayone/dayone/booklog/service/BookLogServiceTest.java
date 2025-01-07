package dayone.dayone.booklog.service;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.booklog.entity.repository.BookLogRepository;
import dayone.dayone.booklog.service.dto.BookLogCreateRequest;
import dayone.dayone.fixture.TestBookFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class BookLogServiceTest {

    @Autowired
    private BookLogService bookLogService;

    @Autowired
    private BookLogRepository bookLogRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestBookFactory testBookFactory;

    @AfterEach
    void delete() {
        bookRepository.deleteAll();
        bookLogRepository.deleteAll();
    }

    @DisplayName("bookLog 작성시 bookLog가 저장된다.")
    @Test
    void createBookLog() {
        // given
        final Book book = testBookFactory.createBook("책", "작가", "출판사");
        final BookLogCreateRequest request = new BookLogCreateRequest(book.getId(), "의미있는 구절", "내가 느낀 감정");

        // when
        final Long savedId = bookLogService.create(request);

        // then
        final BookLog bookLog = bookLogRepository.findById(savedId).get();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(bookLog.getPassage()).isEqualTo(request.passage());
            softAssertions.assertThat(bookLog.getComment()).isEqualTo(request.comment());
        });
    }

    @DisplayName("존재하지 않는 book에 bookLog를 작성하려고 하면 예외가 발생합니다.")
    @Test
    void createBookLogOnNotExistBook() {
        // given
        final long wrongBookId = Long.MAX_VALUE;
        final BookLogCreateRequest request = new BookLogCreateRequest(wrongBookId, "의미있는 구절", "내가 느낀 감정");

        // when, then
        assertThatThrownBy(() -> bookLogService.create(request))
            .isInstanceOf(BookException.class)
            .hasMessage(BookErrorCode.BOOK_NOT_EXIST.getMessage());
    }
}
