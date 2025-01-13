package dayone.dayone.user.entity.value;

import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @DisplayName("이메일이 null이거나 빈 값일 수 없습니다.")
    @NullAndEmptySource
    @ValueSource(strings = {"", "   "})
    @ParameterizedTest
    void emailShouldNotBeNullOrEmpty(String email) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Email(email))
            .isInstanceOf(UserException.class)
            .hasMessage(UserErrorCode.EMAIL_BLANK_AND_NULL.getMessage());
    }

    @DisplayName("이메일 형식이 올바르지 않으면 예외를 발생합니다.")
    @ValueSource(strings = {"usernameexample.com",
        "usernameexample@com",
        "user@@example.com",
        "user@name@example.com",
        "user@.com",
        "user@com.",
        "user@111.222.333.4444",
        "user@example.c",
        "user name@example.com",
        "user@exa#mple.com",
        "user@exam!ple.com",
        "user@example$.com",
        "@example.com",
        "user@",
        "user@예제.한국"})
    @ParameterizedTest
    void emailShouldHaveValidFormat(String email) {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> new Email(email))
            .isInstanceOf(UserException.class)
            .hasMessage(UserErrorCode.INVALID_EMAIL_FORM.getMessage());
    }
}
