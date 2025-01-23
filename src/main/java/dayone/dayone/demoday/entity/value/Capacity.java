package dayone.dayone.demoday.entity.value;

import dayone.dayone.demoday.exception.DemoDayErrorCode;
import dayone.dayone.demoday.exception.DemoDayException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Capacity {

    private static final int CAPACITY_MIN_VALUE = 1;

    @Column(name = "capacity")
    private int value;

    public Capacity(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < CAPACITY_MIN_VALUE) {
            throw new DemoDayException(DemoDayErrorCode.DEMO_DAY_CAPACITY_UNDER_ZERO);
        }
    }
}
