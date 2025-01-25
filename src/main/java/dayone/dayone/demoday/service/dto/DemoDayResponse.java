package dayone.dayone.demoday.service.dto;

import dayone.dayone.demoday.entity.DemoDay;

import java.time.LocalDate;

public record DemoDayResponse(
    Long id,
    String title,
    String thumbnail,
    LocalDate demoDate
) {

    public static DemoDayResponse from(final DemoDay demoDay) {
        return new DemoDayResponse(demoDay.getId(),
            demoDay.getTitle(),
            demoDay.getThumbnail(),
            demoDay.getDemoDate().toLocalDate());
    }
}
