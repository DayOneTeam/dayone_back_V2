package dayone.dayone.demoday.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dayone.dayone.demoday.entity.DemoDay;

import java.util.List;

public record DemoDayListResponse(
    @JsonProperty("demo_days")
    List<DemoDayResponse> demoDays
) {

    public static DemoDayListResponse from(final List<DemoDay> demoDays) {
        final List<DemoDayResponse> demoDayResponses = demoDays.stream()
            .map(DemoDayResponse::from)
            .toList();

        return new DemoDayListResponse(demoDayResponses);
    }
}
