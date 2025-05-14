package dayone.dayone.demoday.service;

import dayone.dayone.demoday.entity.DemoDay;
import dayone.dayone.demoday.entity.DemoDayUser;
import dayone.dayone.demoday.entity.Ticket;
import dayone.dayone.demoday.entity.respository.DemoDayRepository;
import dayone.dayone.demoday.entity.respository.DemoDayUserRepository;
import dayone.dayone.demoday.entity.respository.TicketRepository;
import dayone.dayone.demoday.entity.value.Status;
import dayone.dayone.demoday.service.dto.DemoDayCreateRequest;
import dayone.dayone.demoday.service.dto.DemoDayListResponse;
import dayone.dayone.demoday.service.dto.DemoDayResponse;
import dayone.dayone.fixture.TestDemoDayFactory;
import dayone.dayone.fixture.TestTicketFactory;
import dayone.dayone.fixture.TestUserFactory;
import dayone.dayone.support.ServiceTest;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DemoDayServiceTest extends ServiceTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private TestDemoDayFactory testDemoDayFactory;

    @Autowired
    private TestTicketFactory testTicketFactory;

    @Autowired
    private DemoDayRepository demoDayRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private DemoDayUserRepository demoDayUserRepository;

    @Autowired
    private DemoDayService demoDayService;

    @DisplayName("데모데이 생성")
    @Nested
    class Create {
        @DisplayName("정상적으로 데모데이를 생성한다.")
        @Test
        void createDemoDay() {
            // given
            final User user = testUserFactory.createUser("test@test.com", "test", "test", 1);
            final LocalDate today = LocalDate.now();
            final LocalDate tomorrow = today.plusDays(1);
            final DemoDayCreateRequest request = new DemoDayCreateRequest("title", "description", "thumbnail", tomorrow, LocalTime.now(), 1, "location");

            // when
            final Long savedId = demoDayService.create(user.getId(), request);

            // then
            final DemoDay demoDay = demoDayRepository.findById(savedId).get();
            final Ticket ticket = ticketRepository.findByDemoDayId(savedId).get();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(demoDay.getTitle()).isEqualTo(request.title());
                softAssertions.assertThat(demoDay.getDescription()).isEqualTo(request.description());
                softAssertions.assertThat(demoDay.getThumbnail()).isEqualTo(request.thumbnail());
                softAssertions.assertThat(demoDay.getUserId()).isEqualTo(user.getId());
                softAssertions.assertThat(demoDay.getLocation()).isEqualTo(request.location());
                softAssertions.assertThat(demoDay.getRegistrationDate().getStartRegistrationDate().toLocalDate()).isEqualTo(today);
                softAssertions.assertThat(demoDay.getRegistrationDate().getEndRegistrationDate().toLocalDate()).isEqualTo(tomorrow);
                softAssertions.assertThat(demoDay.getDemoDate().toLocalDate()).isEqualTo(tomorrow);
                softAssertions.assertThat(ticket.getCapacity()).isEqualTo(request.capacity());
            });
        }

        @DisplayName("존재하지 않는 유저가 데모데이를 생성할 시 예외를 발생한다.")
        @Test
        void createDemoDayWithNotExistUser() {
            // given
            final Long nonExistUserId = Long.MAX_VALUE;
            final LocalDate tomorrow = LocalDate.now().plusDays(1);
            final DemoDayCreateRequest request = new DemoDayCreateRequest("title", "description", "thumbnail", tomorrow, LocalTime.now(), 1, "location");

            // when
            // then
            assertThatThrownBy(() -> demoDayService.create(nonExistUserId, request))
                .isInstanceOf(UserException.class)
                .hasMessage(UserErrorCode.NOT_EXIST_USER.getMessage());
        }
    }

    @DisplayName("데모데이 조회")
    @Nested
    class readDemoDay {
        @DisplayName("데모데이 상태 정보를 바탕으로 데모데이를 조회한다.")
        @Test
        void readDemoDayByStatus() {
            // given
            final List<DemoDay> openDemoDay = testDemoDayFactory.createNDemoDaysWithStatus(3, "title", "description", Status.valueOf("OPEN"));
            final List<DemoDay> closedDemoDay = testDemoDayFactory.createNDemoDaysWithStatus(2, "title", "description", Status.valueOf("CLOSED"));
            final String status = "OPEN";

            // when
            final DemoDayListResponse response = demoDayService.getDemoDaysWithStatus(status);

            // then
            final List<DemoDayResponse> expected = openDemoDay.stream()
                .map(DemoDayResponse::from)
                .toList();

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.demoDays()).hasSize(3);
                softly.assertThat(response.demoDays()).usingRecursiveComparison().isEqualTo(expected);
            });
        }
    }

    @DisplayName("데모데이 신청")
    @Nested
    class apply {
        @DisplayName("데모데이를 신청하면 티켓이 하나 감소한다.")
        @Test
        void applyDemoDay() {
            // given
            final User DemoDayOwner = testUserFactory.createUser("test@test.com", "test", "test", 1);
            final User DemoDayUser = testUserFactory.createUser("test2@test.com", "test2", "test2", 1);
            final DemoDay demoDayOpen = testDemoDayFactory.createDemoDayOpen("title", "description", DemoDayOwner.getId());
            testTicketFactory.createNTicket(demoDayOpen, 1);

            // when
            demoDayService.applyDemoDay(DemoDayUser.getId(), demoDayOpen.getId());

            // then
            final Optional<DemoDayUser> byDemoDayIdAndUserId = demoDayUserRepository.findByDemoDayIdAndUserId(demoDayOpen.getId(), DemoDayUser.getId());
            final Ticket ticket = ticketRepository.findByDemoDayId(demoDayOpen.getId()).get();
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(ticket.getCapacity()).isEqualTo(0);
                softly.assertThat(byDemoDayIdAndUserId.isPresent()).isTrue();
            });
        }

        @DisplayName("티켓의 개수가 1개인 데모데이에 10명의 유저가 동시에 참여 요청을 하더라도 1명한 참여 가능하다.")
        @Test
        void applyDemoDayWithConcurrent() throws InterruptedException {
            // given
            final User DemoDayOwner = testUserFactory.createUser("test@test.com", "test", "test", 1);
            final List<User> users = testUserFactory.createNUser(10, "test2@test.com", "test2", "test2", 1);


            final DemoDay demoDayOpen = testDemoDayFactory.createDemoDayOpen("title", "description", DemoDayOwner.getId());
            final Ticket tickets = testTicketFactory.createNTicket(demoDayOpen, 1);

            final ExecutorService executorService = Executors.newFixedThreadPool(users.size());
            final CountDownLatch countDownLatch = new CountDownLatch(users.size());

            // when
            for (final User user : users) {
                executorService.submit(() -> {
                    demoDayService.applyDemoDay(user.getId(), demoDayOpen.getId());
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();

            // then
            final List<DemoDayUser> result = demoDayUserRepository.findByDemoDayId(demoDayOpen.getId());
            final Ticket ticket = ticketRepository.findByDemoDayId(demoDayOpen.getId()).get();
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(result).hasSize(tickets.getCapacity());
                softly.assertThat(ticket.getCapacity()).isEqualTo(0);
            });
        }
    }
}
