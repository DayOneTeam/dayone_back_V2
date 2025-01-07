package dayone.dayone.book.entity.value;

import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {

    @DisplayName("책 제목은 null이거나 빈 값일 수 없습니다,")
    @NullAndEmptySource
    @ValueSource(strings = {"", "   "})
    @ParameterizedTest
    void titleShouldNotBeNullOrEmpty(String title) {
        // give
        // when
        // then
        assertThatThrownBy(() -> new Title(title))
            .isInstanceOf(BookException.class)
            .hasMessage(BookErrorCode.TITLE_BLANK_AND_NULL.getMessage());
    }

    @DisplayName("책 제목은 100자를 넘기면 예외를 발생합니다.")
    @Test
    void titleShouldBeLessThan100() {
        // given
        String wrongTitle = "글".repeat(101);
        // when
        // then
        assertThatThrownBy(() -> new Title(wrongTitle))
            .isInstanceOf(BookException.class)
            .hasMessage(BookErrorCode.TITLE_LENGTH_OVER.getMessage());
    }
}
