package dayone.dayone.booklog.service;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.booklog.entity.repository.BookLogRepository;
import dayone.dayone.booklog.service.dto.BookLogCreateRequest;
import dayone.dayone.booklog.service.dto.BookLogDetailResponse;
import dayone.dayone.booklog.service.dto.BookLogPaginationListResponse;
import dayone.dayone.booklog.service.dto.BookLogResponse;
import dayone.dayone.booklog.service.dto.BookLogTop4Response;
import dayone.dayone.booklog.service.dto.BookLogWriteActiveResponse;
import dayone.dayone.fixture.TestBookFactory;
import dayone.dayone.fixture.TestBookLogFactory;
import dayone.dayone.fixture.TestUserFactory;
import dayone.dayone.support.ServiceTest;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class BookLogServiceTest extends ServiceTest {

    @Autowired
    private BookLogService bookLogService;

    @Autowired
    private BookLogRepository bookLogRepository;

    @Autowired
    private TestBookFactory testBookFactory;

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private TestBookLogFactory testBookLogFactory;

    @DisplayName("bookLog 생성")
    @Nested
    class CreateBookLog {
        @DisplayName("bookLog 작성시 bookLog가 저장된다.")
        @Test
        void createBookLog() {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름");
            final BookLogCreateRequest request = new BookLogCreateRequest(book.getId(), "의미있는 구절", "내가 느낀 감정");

            // when
            final Long savedId = bookLogService.create(user.getId(), request);

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
            final User user = testUserFactory.createUser("test@test.com", "password", "이름");
            final BookLogCreateRequest request = new BookLogCreateRequest(wrongBookId, "의미있는 구절", "내가 느낀 감정");

            // when, then
            assertThatThrownBy(() -> bookLogService.create(user.getId(), request))
                .isInstanceOf(BookException.class)
                .hasMessage(BookErrorCode.BOOK_NOT_EXIST.getMessage());
        }

        @DisplayName("존재하지 않는 user에 bookLog를 작성하려고 하면 예외가 발생합니다.")
        @Test
        void createBookLogOnNotExistUser() {
            // given
            final long wrongUserId = Long.MAX_VALUE;
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final BookLogCreateRequest request = new BookLogCreateRequest(book.getId(), "의미있는 구절", "내가 느낀 감정");

            // when
            // then
            assertThatThrownBy(() -> bookLogService.create(wrongUserId, request))
                .isInstanceOf(UserException.class)
                .hasMessage(UserErrorCode.NOT_EXIST_USER.getMessage());
        }
    }

    @DisplayName("bookLog 조회")
    @Nested
    class ReadBookLog {
        @DisplayName("BookLog를 cursor기반으로 10개씩 조회한다.")
        @MethodSource("cursorAndExpect")
        @ParameterizedTest
        void readBookLogsByCursor(long cursor, int expectSize, int firstId, int lastId, long nextCursor, boolean next) {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름");
            final List<BookLog> bookLogs = testBookLogFactory.createNBookLog(20, book, user);

            // when
            final BookLogPaginationListResponse response = bookLogService.getAllBookLogs(cursor);

            // then
            int startIndex = 0;
            int lastIndex = response.bookLogs().size() - 1;
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.bookLogs()).hasSize(expectSize);
                softly.assertThat(response.bookLogs().get(startIndex).id()).isEqualTo(firstId);
                softly.assertThat(response.bookLogs().get(lastIndex).id()).isEqualTo(lastId);
                softly.assertThat(response.nextCursor()).isEqualTo(nextCursor);
                softly.assertThat(response.next()).isEqualTo(next);
            });
        }

        private static Stream<Arguments> cursorAndExpect() {
            return Stream.of(
                // 커서, 응답 개수, 응답 레코드의 첫 id, 응답 레코드의 마지막 id, 다음 커서, 다음 데이터 존재 여부
                arguments(11, 10, 10, 1, -1, false),
                arguments(9, 8, 8, 1, -1, false),
                arguments(12, 10, 11, 2, 2, true),
                arguments(21, 10, 20, 11, 11, true)
            );
        }

        @DisplayName("특정 bookLog의 상세 정보를 조회한다.")
        @Test
        void readBookLogDetail() {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름");
            final BookLog bookLog = BookLog.forSave("의미있는 구절", "내가 느낀 감정", book, user);
            bookLogRepository.save(bookLog);

            long requestId = bookLog.getId();

            // when
            final BookLogDetailResponse response = bookLogService.getBookLogById(requestId);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(response.id()).isEqualTo(bookLog.getId());
                softAssertions.assertThat(response.passage()).isEqualTo(bookLog.getPassage());
                softAssertions.assertThat(response.comment()).isEqualTo(bookLog.getComment());
                softAssertions.assertThat(response.likeCnt()).isEqualTo(bookLog.getLikeCount());
                softAssertions.assertThat(response.bookTitle()).isEqualTo(book.getTitle());
                softAssertions.assertThat(response.bookCover()).isEqualTo(book.getThumbnail());
                softAssertions.assertThat(response.createdAt()).isEqualTo(bookLog.getCreatedAt());
            });
        }

        @DisplayName("한 주내 가장 좋아요를 받은 bookLog 4개를 조회한다.")
        @Test
        void getMostLikedAndWrittenRecentlyBookLogsInWeek() {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름");
            final List<BookLog> bookLogWrittenThisWeek = testBookLogFactory.createBookLogWrittenThisWeek(7, book, user);
            final List<BookLog> bookLogWrittenLastWeek = testBookLogFactory.createBookLogWrittenLastWeek(7, book, user);

            // when
            final BookLogTop4Response response = bookLogService.getTop4BookLogs(LocalDateTime.now());

            // then
            int topCount = 4;
            final List<BookLogResponse> expect = bookLogWrittenThisWeek.subList(bookLogWrittenLastWeek.size() - topCount, bookLogWrittenLastWeek.size())
                .stream()
                .map(BookLogResponse::from)
                .toList();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(response.bookLogs()).hasSize(4);
                softAssertions.assertThat(response.bookLogs()).containsExactlyInAnyOrderElementsOf(expect);
            });
        }

        @DisplayName("유저의 일주일간 bookLog를 작성한 날짜의 활성화 정보를 조회한다.")
        @Test
        void getUserBookLogActiveInWeek() {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름");
            final List<BookLog> bookLogWrittenThisWeek = testBookLogFactory.createBookLogWrittenThisWeek(4, book, user);

            // when
            final BookLogWriteActiveResponse result = bookLogService.getBookLogWriteActive(user.getId());

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(result.activeDay().get(0)).isTrue();
                softly.assertThat(result.activeDay().get(1)).isTrue();
                softly.assertThat(result.activeDay().get(2)).isTrue();
                softly.assertThat(result.activeDay().get(3)).isTrue();
                softly.assertThat(result.activeDay().get(4)).isFalse();
                softly.assertThat(result.activeDay().get(5)).isFalse();
                softly.assertThat(result.activeDay().get(6)).isFalse();
            });
        }

        @DisplayName("존재하지 않는 유저가 bookLog를 작성한 날짜의 활성화 정보를 조회하면 예외를 발생한다.")
        @Test
        void getBookLogWriteActiveNotExistUser() {
            // given
            final Long notExistUserId = Long.MAX_VALUE;
            // when
            // then
            assertThatThrownBy(() -> bookLogService.getBookLogWriteActive(notExistUserId))
                .isInstanceOf(UserException.class)
                .hasMessage(UserErrorCode.NOT_EXIST_USER.getMessage());
        }
    }
}
