package dayone.dayone.user.entity.value;

import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @DisplayName("존재하지 않는 role일 때 예외를 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"존재하지 않는 role"})
    @ParameterizedTest
    void notExistRole(String role) {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> Role.from(role))
            .isInstanceOf(UserException.class)
            .hasMessage(UserErrorCode.NOT_EXIST_ROLE.getMessage());
    }

    @DisplayName("올바른 role을 불러온다.")
    @ValueSource(strings = {"ADMIN", "MEMBER"})
    @ParameterizedTest
    void existRole(String role) {
        // given
        // when
        // then
        assertThat(Role.from(role)).isEqualTo(Role.valueOf(role));
    }
}
