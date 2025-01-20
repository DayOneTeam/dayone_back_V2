package dayone.dayone.booklog.common;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DateFinder {

    public static LocalDateTime getWeekStartDate(final LocalDateTime now) {
        return now.with(DayOfWeek.MONDAY)
            .toLocalDate()
            .atStartOfDay();

    }

    public static LocalDateTime getWeekEndDate(final LocalDateTime now) {
        return now.with(DayOfWeek.SUNDAY)
            .toLocalDate()
            .atTime(23, 59, 59, 9999);
    }

    public static int findDayOfWeek(final LocalDateTime date) {
        final DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getValue() - 1;
    }
}
