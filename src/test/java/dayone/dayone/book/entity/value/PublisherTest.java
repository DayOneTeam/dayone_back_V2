package dayone.dayone.book.entity.value;

import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PublisherTest {

    @DisplayName("출판사명은 null이거나 빈 값일 수 없습니다,")
    @NullAndEmptySource
    @ValueSource(strings = {"", "   "})
    @ParameterizedTest
    void publisherShouldNotBeNullOrEmpty(String publisher) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Publisher(publisher))
            .isInstanceOf(BookException.class)
            .hasMessage(BookErrorCode.PUBLISHER_BLANK_AND_NULL.getMessage());
    }

    @DisplayName("출판사명은 100자를 넘기면 예외를 발생합니다.")
    @Test
    void publisherShouldBeLessThan100() {
        // given
        String wrongPublisher = "글".repeat(101);
        // when
        // then
        assertThatThrownBy(() -> new Publisher(wrongPublisher))
            .isInstanceOf(BookException.class)
            .hasMessage(BookErrorCode.PUBLISHER_LENGTH_OVER.getMessage());
    }
}
