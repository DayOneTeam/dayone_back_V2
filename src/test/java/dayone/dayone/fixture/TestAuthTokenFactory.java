package dayone.dayone.fixture;

import dayone.dayone.auth.entity.AuthToken;
import dayone.dayone.auth.entity.repository.AuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestAuthTokenFactory {

    @Autowired
    private AuthTokenRepository authTokenRepository;

    public AuthToken createAuthToken(final Long userId, final String refreshToken) {
        final AuthToken authToken = AuthToken.forSave(userId, refreshToken);
        authTokenRepository.save(authToken);
        return authToken;
    }
}
