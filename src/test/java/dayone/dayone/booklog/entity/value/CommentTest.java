package dayone.dayone.booklog.entity.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentTest {

    @DisplayName("자신의 생각은 null이거나 빈 값일 수 없습니다,")
    @NullAndEmptySource
    @ValueSource(strings = {"", "   "})
    @ParameterizedTest
    void commentShouldNotBeNullOrEmpty(String comment) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Comment(comment))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("자신의 생각은 5000자를 넘기면 예외를 발생합니다.")
    @Test
    void commentShouldBeLessThan5000() {
        // given
        String wrongComment = "글".repeat(5001);

        // when
        // then
        assertThatThrownBy(() -> new Comment(wrongComment))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
