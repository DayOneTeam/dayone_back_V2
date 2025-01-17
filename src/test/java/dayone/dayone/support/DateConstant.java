package dayone.dayone.support;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DateConstant {

    private DateConstant() {
    }

    public static final LocalDateTime NOW = LocalDateTime.now();

    public static final LocalDateTime THIS_MONDAY = NOW
        .with(DayOfWeek.MONDAY)
        .toLocalDate()
        .atStartOfDay();

    public static final LocalDateTime THIS_SUNDAY = NOW.with(DayOfWeek.SUNDAY)
        .toLocalDate()
        .atTime(23, 59, 59, 99999);

    public static final LocalDateTime LAST_MONDAY = NOW.with(DayOfWeek.MONDAY)
        .minusWeeks(1)
        .toLocalDate()
        .atStartOfDay();
}
