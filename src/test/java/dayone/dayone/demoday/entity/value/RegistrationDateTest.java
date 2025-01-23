package dayone.dayone.demoday.entity.value;

import dayone.dayone.demoday.exception.DemoDayErrorCode;
import dayone.dayone.demoday.exception.DemoDayException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RegistrationDateTest {

    @DisplayName("데모데이 신청 마감 기한이 신청 시작 기한보다 이전일 수 없습니다.")
    @Test
    void endRegistrationDateShouldBeAfterStartRegistrationDate() {
        // given
        LocalDateTime startRegistrationDate = LocalDateTime.now();
        LocalDateTime endRegistrationDate = startRegistrationDate.minusDays(1);

        // when
        // then
        assertThatThrownBy(() -> new RegistrationDate(startRegistrationDate, endRegistrationDate))
            .isInstanceOf(DemoDayException.class)
            .hasMessage(DemoDayErrorCode.DEMO_DAY_REGISTRATION_PERIOD_ERROR.getMessage());
    }
}
