package dayone.dayone.booklog.entity.value;

import dayone.dayone.booklog.exception.BookLogErrorCode;
import dayone.dayone.booklog.exception.BookLogException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PassageTest {

    @DisplayName("구절은 null이거나 빈 값일 수 없습니다,")
    @NullAndEmptySource
    @ValueSource(strings = {"", "   "})
    @ParameterizedTest
    void passageShouldNotBeNullOrEmpty(String passage) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Passage(passage))
            .isInstanceOf(BookLogException.class)
            .hasMessage(BookLogErrorCode.PASSAGE_BLANK_AND_NULL.getMessage());
    }

    @DisplayName("구절은 1000자를 넘기면 예외를 발생합니다.")
    @Test
    void passageShouldBeLessThan1000() {
        // given
        String wrongPassage = "글".repeat(1001);
        // when
        // then
        assertThatThrownBy(() -> new Passage(wrongPassage))
            .isInstanceOf(BookLogException.class)
            .hasMessage(BookLogErrorCode.PASSAGE_LENGTH_OVER.getMessage());
    }
}
