package dayone.dayone.demoday.entity.value;

import dayone.dayone.demoday.exception.DemoDayErrorCode;
import dayone.dayone.demoday.exception.DemoDayException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class DemoDate {

    private LocalDateTime demoDate;

    public DemoDate(final LocalDateTime demoDate) {
        validateDemoDateIsAfterNow(demoDate);
        this.demoDate = demoDate;
    }

    private void validateDemoDateIsAfterNow(final LocalDateTime demoDate) {
        if (demoDate.isBefore(LocalDateTime.now())) {
            throw new DemoDayException(DemoDayErrorCode.DEMO_DAY_IS_NOT_BEFORE_NOW);
        }
    }

    public static DemoDate of(final LocalDate demoDate, final LocalTime demoTime) {
        return new DemoDate(LocalDateTime.of(demoDate, demoTime));
    }
}
