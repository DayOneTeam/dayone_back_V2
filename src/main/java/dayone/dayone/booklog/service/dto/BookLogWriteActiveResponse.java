package dayone.dayone.booklog.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.IntStream;

public record BookLogWriteActiveResponse(
    @JsonProperty("active_day")
    List<Boolean> activeDay
) {

    public static BookLogWriteActiveResponse from(boolean[] isWrite) {
        final List<Boolean> response = IntStream.range(0, isWrite.length)
            .mapToObj(i -> isWrite[i]) // auto-boxing
            .toList();
        return new BookLogWriteActiveResponse(response);
    }
}
