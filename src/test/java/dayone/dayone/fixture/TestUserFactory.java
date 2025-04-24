package dayone.dayone.fixture;

import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestUserFactory {

    @Autowired
    private UserRepository userRepository;

    public User createUser(final String email, final String password, final String name, final int generation) {
        final User user = User.forSave(email, password, name, generation);
        userRepository.save(user);
        return user;
    }

    public List<User> createNUser(final int cnt, final String email, final String password, final String name, final int generation) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < cnt; i++) {
            final User user = User.forSave(email + i, password, name + i, generation);
            userRepository.save(user);
            users.add(user);
        }
        return users;
    }
}
