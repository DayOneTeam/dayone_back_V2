package dayone.dayone.demoday.entity;

import dayone.dayone.demoday.entity.value.DemoDate;
import dayone.dayone.demoday.entity.value.RegistrationDate;
import dayone.dayone.demoday.entity.value.Status;
import dayone.dayone.demoday.exception.DemoDayErrorCode;
import dayone.dayone.demoday.exception.DemoDayException;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.value.Role;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DemoDayTest {

    @DisplayName("데모데이를 신청하면 티켓이 하나 감소한다.")
    @Test
    void applyDemoDay() {
        // given
        final LocalDateTime localDateTime = LocalDateTime.now();
        final DemoDay demoDayOpen = new DemoDay(null,
            "title",
            "description",
            "이미지",
            new RegistrationDate(localDateTime, localDateTime.plusDays(1)),
            new DemoDate(localDateTime.plusDays(1)),
            "장소",
            Status.OPEN,
            1L);
        final Ticket ticket = Ticket.forSave(demoDayOpen, 1);
        final User user = new User(2L, "test@test.com", "test", "test", 1, "프로필", Role.MEMBER);

        // when
        final DemoDayUser apply = demoDayOpen.apply(user, ticket);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(ticket.getCapacity()).isEqualTo(0);
            softly.assertThat(apply.getUserId()).isEqualTo(user.getId());
            softly.assertThat(apply.getDemoDayId()).isEqualTo(demoDayOpen.getId());
        });
    }

    @DisplayName("이미 종료된 데모데이를 신청하면 예외를 발생한다.")
    @Test
    void applyDemoDayWithClosedDemoDay() {
        // given
        final LocalDateTime localDateTime = LocalDateTime.now();
        final DemoDay demoDayClosed = new DemoDay(null,
            "title",
            "description",
            "이미지",
            new RegistrationDate(localDateTime, localDateTime.plusDays(1)),
            new DemoDate(localDateTime.plusDays(1)),
            "장소",
            Status.CLOSED,
            1L);

        final Ticket ticket = Ticket.forSave(demoDayClosed, 1);
        final User user = new User(2L, "test@test.com", "test", "test", 1, "프로필", Role.MEMBER);

        // when
        // then
        assertThatThrownBy(() -> demoDayClosed.apply(user, ticket))
            .isInstanceOf(DemoDayException.class)
            .hasMessage(DemoDayErrorCode.DEMO_DAY_IS_CLOSED.getMessage());
    }

    @DisplayName("데모데이 등록자는 자신의 데모데이를 신청할 수 없다.")
    @Test
    void applyDemoDayWithNotOwner() {
        // given
        final LocalDateTime localDateTime = LocalDateTime.now();
        final DemoDay demoDayOpen = new DemoDay(null,
            "title",
            "description",
            "이미지",
            new RegistrationDate(localDateTime, localDateTime.plusDays(1)),
            new DemoDate(localDateTime.plusDays(1)),
            "장소",
            Status.OPEN,
            1L);
        final Ticket ticket = Ticket.forSave(demoDayOpen, 1);
        final User owner = new User(1L, "test@test.com", "test", "test", 1, "프로필", Role.MEMBER);

        // when
        // then
        assertThatThrownBy(() -> demoDayOpen.apply(owner, ticket))
            .isInstanceOf(DemoDayException.class)
            .hasMessage(DemoDayErrorCode.DEMO_DAY_OWNER_NOT_APPLY_ONESELF.getMessage());
    }

    @DisplayName("참여 인원이 꽉찬 경우 예외를 발생한다.")
    @Test
    void applyDemoDayWithOverCapacity() {
        // given
        final LocalDateTime localDateTime = LocalDateTime.now();
        final DemoDay demoDayOpen = new DemoDay(null,
            "title",
            "description",
            "이미지",
            new RegistrationDate(localDateTime, localDateTime.plusDays(1)),
            new DemoDate(localDateTime.plusDays(1)),
            "장소",
            Status.OPEN,
            1L);
        final Ticket ticket = Ticket.forSave(demoDayOpen, 1);
        final User user = new User(2L, "test@test.com", "test", "test", 1, "프로필", Role.MEMBER);
        ticket.sold();

        // when
        // then
        assertThatThrownBy(() -> demoDayOpen.apply(user, ticket))
            .isInstanceOf(DemoDayException.class)
            .hasMessage(DemoDayErrorCode.DEMO_DAY_IS_FULL.getMessage());
    }
}
