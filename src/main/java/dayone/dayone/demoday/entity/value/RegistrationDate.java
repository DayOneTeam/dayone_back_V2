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
public class RegistrationDate {

    private LocalDateTime startRegistrationDate;

    private LocalDateTime endRegistrationDate;

    public RegistrationDate(final LocalDateTime startRegistrationDate, final LocalDateTime endRegistrationDate) {
        validateEndRegistrationDateIsBeforeStartRegistrationDate(startRegistrationDate, endRegistrationDate);
        this.startRegistrationDate = startRegistrationDate;
        this.endRegistrationDate = endRegistrationDate;
    }

    private void validateEndRegistrationDateIsBeforeStartRegistrationDate(final LocalDateTime startRegistrationDate,
                                                                          final LocalDateTime endRegistrationDate) {
        if (endRegistrationDate.isBefore(startRegistrationDate)) {
            throw new DemoDayException(DemoDayErrorCode.DEMO_DAY_REGISTRATION_PERIOD_ERROR);
        }
    }

    public static RegistrationDate of(final LocalDate demoDate, final LocalTime demoTime) {
        final LocalDateTime startRegistrationDate = LocalDateTime.now();
        final LocalDateTime endRegistrationDate = LocalDateTime.of(demoDate, demoTime);
        return new RegistrationDate(startRegistrationDate, endRegistrationDate);
    }
}
