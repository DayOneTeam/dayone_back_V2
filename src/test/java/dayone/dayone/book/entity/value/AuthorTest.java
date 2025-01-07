package dayone.dayone.book.entity.value;

import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthorTest {

    @DisplayName("책 작가명은 null이거나 빈 값일 수 없습니다,")
    @NullAndEmptySource
    @ValueSource(strings = {"", "   "})
    @ParameterizedTest
    void authorShouldNotBeNullOrEmpty(String author) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Author(author))
            .isInstanceOf(BookException.class)
            .hasMessage(BookErrorCode.AUTHOR_BLACK_AND_NULL.getMessage());
    }

    @DisplayName("책 작가명은 100자를 넘기면 예외를 발생합니다.")
    @Test
    void authorShouldBeLessThan100() {
        // given
        String wrongAuthor = "글".repeat(101);
        // when
        // then
        assertThatThrownBy(() -> new Author(wrongAuthor))
            .isInstanceOf(BookException.class)
            .hasMessage(BookErrorCode.AUTHOR_LENGTH_OVER.getMessage());
    }
}
