package dayone.dayone.auth.application;

import dayone.dayone.auth.application.dto.LoginRequest;
import dayone.dayone.auth.application.dto.TokenInfo;
import dayone.dayone.auth.exception.AuthErrorCode;
import dayone.dayone.auth.exception.AuthException;
import dayone.dayone.auth.token.TokenProvider;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public TokenInfo login(final LoginRequest loginRequest) {
        final User user = userRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
            .orElseThrow(() -> new AuthException(AuthErrorCode.FAIL_LOGIN));

        final String accessToken = tokenProvider.createAccessToken(user.getId());
        final String refreshToken = tokenProvider.createRefreshToken(user.getId());

        return new TokenInfo(accessToken, refreshToken);
    }
}
