package dayone.dayone.user.service;

import dayone.dayone.fixture.TestUserFactory;
import dayone.dayone.support.ServiceTest;
import dayone.dayone.support.TestConfig;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TestConfig.class})
class UserServiceTest extends ServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private UserService userService;

    @DisplayName("사용자의 프로필 사진을 수정한다.")
    @Test
    void updateUserProfileImage() throws IOException {
        // given
        final User user = testUserFactory.createUser("test@test.com", "password", "이름");

        // when
        userService.updateUserProfileImage(user.getId(), new MockMultipartFile("test", "test".getBytes()));

        // then
        User updateUser = userRepository.findById(user.getId()).get();
        assertThat(updateUser.getProfileImage()).isEqualTo("updatedFile"); // 이미지 업로드 테스트 필요
    }
}
