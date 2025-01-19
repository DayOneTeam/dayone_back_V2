package dayone.dayone.user.service;

import dayone.dayone.book.entity.Book;
import dayone.dayone.fixture.TestBookFactory;
import dayone.dayone.fixture.TestBookLogFactory;
import dayone.dayone.fixture.TestUserFactory;
import dayone.dayone.support.ServiceTest;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import dayone.dayone.user.service.dto.UserBookListResponse;
import dayone.dayone.user.service.dto.UserBookResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class UserBookServiceTest extends ServiceTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private TestBookFactory testBookFactory;

    @Autowired
    private TestBookLogFactory testBookLogFactory;

    @Autowired
    private UserBookService userBookService;

    @DisplayName("유저의 책장 목록을 조회한다.(책장 목록 내 책 정보는 중복되지 않으며 최근에 bookLog를 작성한 순으로 정렬됩니다.)")
    @MethodSource("BooksUserWriteBookLogAndExceptResponse")
    @ParameterizedTest
    void readUserBooks(List<Integer> bookIds, List<Long> exceptBookIds) {
        // given
        final User user = testUserFactory.createUser("test@test.com", "password", "이름");
        final List<Book> books = testBookFactory.createNBook(10, "책", "작가", "출판사");

        bookIds.forEach(bookId -> {
            final Book book = books.get(bookId);
            testBookLogFactory.createBookLog(book, user);
        });

        // when
        final UserBookListResponse result = userBookService.getUserBooks(user.getId());

        // then
        final List<Long> resultBookIds = result.userBooks().stream()
            .map(UserBookResponse::id)
            .toList();

        assertThat(resultBookIds).usingRecursiveComparison().isEqualTo(exceptBookIds);
    }

    // 사용자가 bookLog를 작성한 책 인덱스 정보와 응답 예측 데이터의 정보
    private static Stream<Arguments> BooksUserWriteBookLogAndExceptResponse() {
        return Stream.of(
            arguments(List.of(0, 1, 2), List.of(3L, 2L, 1L)),
            arguments(List.of(0, 1, 2, 3, 1), List.of(2L, 4L, 3L, 1L)) // 최근에 bookLog를 작성한 순으로 정렬됩니다.
        );
    }

    @DisplayName("존재하지 않은 유저가 자신의 책장 목록을 조회하려고 하면 예외를 발생한다.")
    @Test
    void readUserBooksWithNotExistUser() {
        // given
        final long notExistUserId = Long.MAX_VALUE;

        // when
        // then
        assertThatThrownBy(() -> userBookService.getUserBooks(notExistUserId))
            .isInstanceOf(UserException.class)
            .hasMessage(UserErrorCode.NOT_EXIST_USER.getMessage());
    }
}
