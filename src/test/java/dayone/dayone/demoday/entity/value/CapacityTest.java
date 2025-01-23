package dayone.dayone.demoday.entity.value;

import dayone.dayone.demoday.exception.DemoDayErrorCode;
import dayone.dayone.demoday.exception.DemoDayException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CapacityTest {

    @DisplayName("데모데이 수용 인원은 1명 이상이어야 합니다.")
    @ValueSource(ints = {0, -1})
    @ParameterizedTest
    void demoDayCapacityShouldBeGreaterThanZero(int capacity) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Capacity(capacity))
            .isInstanceOf(DemoDayException.class)
            .hasMessage(DemoDayErrorCode.DEMO_DAY_CAPACITY_UNDER_ZERO.getMessage());
    }

}
