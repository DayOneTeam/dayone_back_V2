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

import java.time.LocalDateTime;

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

    public DemoDay(
        final Long id,
        final String title,
        final String description,
        final String thumbnail,
        final Capacity capacity,
        final RegistrationDate registrationDate,
        final DemoDate demoDate,
        final String location,
        final Status status
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
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
