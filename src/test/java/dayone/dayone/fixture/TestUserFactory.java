package dayone.dayone.fixture;

import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestUserFactory {

    @Autowired
    private UserRepository userRepository;

    public User createUser(final String email, final String password, final String name) {
        final User user = User.forSave(email, password, name);
        userRepository.save(user);
        return user;
    }
}
