package dayone.dayone.user.service.dto;

import java.time.LocalDate;
import java.util.Optional;

public record UsersBookLogCountRequest(
    int generation,
    LocalDate startDate,
    LocalDate endDate
) {

    public UsersBookLogCountRequest {
        isValidGeneration(generation);
        isValidDate(startDate, endDate);

    }

    public static UsersBookLogCountRequest of(final int generation, final String startDate, final String endDate) {
        final LocalDate start = parseDate(startDate)
            .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 시작 날짜가 잘못되었습니다."));
        final LocalDate end = parseDate(endDate)
            .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 끝 날짜가 잘못되었습니다."));
        return new UsersBookLogCountRequest(generation, start, end);
    }

    private void isValidGeneration(final int generation) {
        if (generation < 1) {
            throw new IllegalArgumentException("특정 기수가 잘못되었습니다.");
        }
    }

    private void isValidDate(final LocalDate startDate, final LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("조회하고자 하는 시작 날짜가 끝 날짜보다 작으면 안됩니다.");
        }
    }

    private static Optional<LocalDate> parseDate(final String date) {
        try {
            return Optional.of(LocalDate.parse(date));
        } catch (final Exception e) {
            return Optional.empty();
        }
    }
}
