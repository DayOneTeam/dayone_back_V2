package dayone.dayone.demoday.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record DemoDayCreateRequest(
    String title,
    String description,
    String thumbnail,
    LocalDate demoDate,
    LocalTime demoTime,
    int capacity,
    String location
) {
}
