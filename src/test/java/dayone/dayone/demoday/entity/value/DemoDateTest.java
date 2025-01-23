package dayone.dayone.demoday.entity.value;

import dayone.dayone.demoday.exception.DemoDayErrorCode;
import dayone.dayone.demoday.exception.DemoDayException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class DemoDateTest {


    @DisplayName("데모데이 진행 날짜는 현재 시간보다 이전일 수 없습니다.")
    @Test
    void demoDateShouldBeAfterNow() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime demoDate = now.minusSeconds(1);

        // when
        // then
        Assertions.assertThatThrownBy(() -> new DemoDate(demoDate))
            .isInstanceOf(DemoDayException.class)
            .hasMessage(DemoDayErrorCode.DEMO_DAY_IS_NOT_BEFORE_NOW.getMessage());
    }
}
