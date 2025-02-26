package dayone.dayone.support;

import dayone.dayone.user.entity.repository.UserRepository;
import dayone.dayone.user.service.StaticFileService;
import dayone.dayone.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Autowired
    UserRepository userRepository;

    @Bean
    UserService userService() {
        return new UserService(userRepository, new StaticFileService());
    }
}
