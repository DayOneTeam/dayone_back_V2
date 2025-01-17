package dayone.dayone.booklog.service;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class DateFinder {

    public LocalDateTime getWeekStartDate(final LocalDateTime now) {
        return now.with(DayOfWeek.MONDAY)
            .toLocalDate()
            .atStartOfDay();

    }

    public LocalDateTime getWeekEndDate(final LocalDateTime now) {
        return now.with(DayOfWeek.SUNDAY)
            .toLocalDate()
            .atTime(23, 59, 59, 9999);
    }
}
