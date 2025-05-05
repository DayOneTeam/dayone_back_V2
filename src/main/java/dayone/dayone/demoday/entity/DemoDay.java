package dayone.dayone.demoday.entity;

import dayone.dayone.demoday.entity.value.DemoDate;
import dayone.dayone.demoday.entity.value.RegistrationDate;
import dayone.dayone.demoday.entity.value.Status;
import dayone.dayone.demoday.exception.DemoDayErrorCode;
import dayone.dayone.demoday.exception.DemoDayException;
import dayone.dayone.global.entity.BaseEntity;
import dayone.dayone.user.entity.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DemoDay extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String thumbnail;

    @Embedded
    private RegistrationDate registrationDate;

    @Embedded
    private DemoDate demoDate;

    private String location;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long userId;

    public DemoDay(
        final Long id,
        final String title,
        final String description,
        final String thumbnail,
        final RegistrationDate registrationDate,
        final DemoDate demoDate,
        final String location,
        final Status status,
        final Long userId
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.registrationDate = registrationDate;
        this.demoDate = demoDate;
        this.location = location;
        this.status = status;
        this.userId = userId;
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        this.updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

    public static DemoDay forSave(
        final String title,
        final String description,
        final String thumbnail,
        final LocalDate demoDate,
        final LocalTime demoTime,
        final String location,
        final Long userId
    ) {
        return new DemoDay(
            null,
            title,
            description,
            thumbnail,
            RegistrationDate.of(demoDate, demoTime),
            DemoDate.of(demoDate, demoTime),
            location,
            Status.OPEN,
            userId
        );
    }

    public DemoDayUser apply(final User user, final Ticket ticket, final DemoDayUsers demoDayUsers) {
        validateApply(user, ticket, demoDayUsers);
        ticket.sold();
        return new DemoDayUser(null, this.id, user.getId());
    }

    private void validateApply(final User user, final Ticket ticket, final DemoDayUsers demoDayUsers) {
        if (this.status == Status.CLOSED) {
            throw new DemoDayException(DemoDayErrorCode.DEMO_DAY_IS_CLOSED);
        }

        if (Objects.equals(user.getId(), this.userId)) {
            throw new DemoDayException(DemoDayErrorCode.DEMO_DAY_OWNER_NOT_APPLY_ONESELF);
        }

        if (ticket.getCapacity() == 0) {
            throw new DemoDayException(DemoDayErrorCode.DEMO_DAY_IS_FULL);
        }

        if (demoDayUsers.isAlreadyApply(user.getId())) {
            throw new DemoDayException(DemoDayErrorCode.DEMO_DAY_ALREADY_APPLY);
        }
    }

    public LocalDateTime getDemoDate() {
        return demoDate.getDemoDate();
    }
}
