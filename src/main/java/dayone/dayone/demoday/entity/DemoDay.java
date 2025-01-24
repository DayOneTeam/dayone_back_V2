package dayone.dayone.demoday.entity;

import dayone.dayone.demoday.entity.value.Capacity;
import dayone.dayone.demoday.entity.value.DemoDate;
import dayone.dayone.demoday.entity.value.RegistrationDate;
import dayone.dayone.demoday.entity.value.Status;
import dayone.dayone.global.entity.BaseEntity;
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
    private Capacity capacity;

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
        final Capacity capacity,
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
        this.capacity = capacity;
        this.registrationDate = registrationDate;
        this.demoDate = demoDate;
        this.location = location;
        this.status = status;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static DemoDay forSave(
        final String title,
        final String description,
        final String thumbnail,
        final LocalDate demoDate,
        final LocalTime demoTime,
        final int capacity,
        final String location,
        final Long userId
    ) {
        return new DemoDay(
            null,
            title,
            description,
            thumbnail,
            new Capacity(capacity),
            RegistrationDate.of(demoDate, demoTime),
            DemoDate.of(demoDate, demoTime),
            location,
            Status.OPEN,
            userId
        );
    }
}
