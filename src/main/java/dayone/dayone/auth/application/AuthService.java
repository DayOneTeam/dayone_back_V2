package dayone.dayone.auth.application;

import dayone.dayone.auth.application.dto.LoginRequest;
import dayone.dayone.auth.application.dto.TokenInfo;
import dayone.dayone.auth.exception.AuthErrorCode;
import dayone.dayone.auth.exception.AuthException;
import dayone.dayone.auth.token.TokenProvider;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.UserRepository;
import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import io.jsonwebtoken.Claims;
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

    public Long validUserByAccessToken(final String accessToken) {
        final Claims claims = tokenProvider.parseClaims(accessToken);
        final Long userId = claims.get("memberId", Long.class);
        userRepository.findById(userId).
            orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));

        return userId;
    }
}
