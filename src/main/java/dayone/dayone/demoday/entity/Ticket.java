package dayone.dayone.demoday.entity;

import dayone.dayone.demoday.entity.value.Capacity;
import dayone.dayone.global.entity.BaseEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Ticket extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "demo_day_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private DemoDay demoDay;

    @Embedded
    private Capacity capacity;

    public Ticket(final Long id, final DemoDay demoDay, final Capacity capacity) {
        this.id = id;
        this.demoDay = demoDay;
        this.capacity = capacity;
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        this.updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

    public static Ticket forSave(final DemoDay demoDay, final int capacity) {
        return new Ticket(null, demoDay, new Capacity(capacity));
    }

    public void sold() {
        this.capacity.minus();
    }

    public int getCapacity() {
        return capacity.getValue();
    }
}
