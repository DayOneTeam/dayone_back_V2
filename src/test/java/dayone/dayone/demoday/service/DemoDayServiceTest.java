package dayone.dayone.demoday.service;

import dayone.dayone.demoday.entity.DemoDay;
import dayone.dayone.demoday.entity.respository.DemoDayRepository;
import dayone.dayone.demoday.entity.value.Status;
import dayone.dayone.demoday.service.dto.DemoDayCreateRequest;
import dayone.dayone.demoday.service.dto.DemoDayListResponse;
import dayone.dayone.demoday.service.dto.DemoDayResponse;
import dayone.dayone.fixture.TestDemoDayFactory;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DemoDayServiceTest extends ServiceTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private TestDemoDayFactory testDemoDayFactory;

    @Autowired
    private DemoDayRepository demoDayRepository;

    @Autowired
    private DemoDayService demoDayService;

    @DisplayName("데모데이 생성")
    @Nested
    class Create {
        @DisplayName("정상적으로 데모데이를 생성한다.")
        @Test
        void createDemoDay() {
            // given
            final User user = testUserFactory.createUser("test@test.com", "test", "test");
            final LocalDate today = LocalDate.now();
            final LocalDate tomorrow = today.plusDays(1);
            final DemoDayCreateRequest request = new DemoDayCreateRequest("title", "description", "thumbnail", tomorrow, LocalTime.now(), 1, "location");

            // when
            final Long savedId = demoDayService.create(user.getId(), request);

            // then
            final DemoDay demoDay = demoDayRepository.findById(savedId).get();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(demoDay.getTitle()).isEqualTo(request.title());
                softAssertions.assertThat(demoDay.getDescription()).isEqualTo(request.description());
                softAssertions.assertThat(demoDay.getThumbnail()).isEqualTo(request.thumbnail());
                softAssertions.assertThat(demoDay.getUserId()).isEqualTo(user.getId());
                softAssertions.assertThat(demoDay.getLocation()).isEqualTo(request.location());
                softAssertions.assertThat(demoDay.getCapacity().getValue()).isEqualTo(request.capacity());
                softAssertions.assertThat(demoDay.getRegistrationDate().getStartRegistrationDate().toLocalDate()).isEqualTo(today);
                softAssertions.assertThat(demoDay.getRegistrationDate().getEndRegistrationDate().toLocalDate()).isEqualTo(tomorrow);
                softAssertions.assertThat(demoDay.getDemoDate().toLocalDate()).isEqualTo(tomorrow);
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
}
