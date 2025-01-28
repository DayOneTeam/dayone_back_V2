package dayone.dayone.auth.application;

import dayone.dayone.auth.application.dto.LoginRequest;
import dayone.dayone.auth.application.dto.TokenInfo;
import dayone.dayone.auth.entity.AuthToken;
import dayone.dayone.auth.entity.repository.AuthTokenRepository;
import dayone.dayone.auth.exception.AuthErrorCode;
import dayone.dayone.auth.exception.AuthException;
import dayone.dayone.auth.token.TokenProvider;
import dayone.dayone.fixture.TestAuthTokenFactory;
import dayone.dayone.fixture.TestUserFactory;
import dayone.dayone.support.ServiceTest;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.UserRepository;
import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import io.jsonwebtoken.Claims;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest extends ServiceTest {

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private TestAuthTokenFactory testAuthTokenFactory;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthService authService;

    @DisplayName("로그인")
    @Nested
    class Login {
        @DisplayName("로그인 시 accessToken과 refreshToken을 발급한다.")
        @Test
        void successLogin() {
            // given
            TokenProvider tokenProvider = new TokenProvider(10000, 100000, "secretCodeaesb231dbdsd");
            AuthService authService = new AuthService(tokenProvider, authTokenRepository, userRepository);

            final User user = testUserFactory.createUser("test@test.com", "test", "test");
            final LoginRequest loginRequest = new LoginRequest(user.getEmail(), user.getPassword());

            // when
            final TokenInfo tokenInfo = authService.login(loginRequest);

            // then
            final Claims accessTokenClaims = tokenProvider.parseClaims(tokenInfo.accessToken());
            final Claims refreshTokenClaims = tokenProvider.parseClaims(tokenInfo.refreshToken());
            Optional<AuthToken> authToken = authTokenRepository.findByUserIdAndRefreshToken(user.getId(), tokenInfo.refreshToken());

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(accessTokenClaims.get("memberId", Long.class)).isEqualTo(user.getId());
                softAssertions.assertThat(refreshTokenClaims.get("memberId", Long.class)).isEqualTo(user.getId());
                softAssertions.assertThat(authToken.isPresent()).isTrue();
            });
        }

        @DisplayName("존재하지 않는 회원 정보로 로그인 시도 시 예외를 발생한다.")
        @MethodSource("provideInvalidLoginRequest")
        @ParameterizedTest
        void failLoginWithNotExistMember(LoginRequest wrongLoginRequest) {
            // given
            TokenProvider tokenProvider = new TokenProvider(10000, 100000, "secretCodeaesb231dbdsd");
            AuthService authService = new AuthService(tokenProvider, authTokenRepository, userRepository);
            testUserFactory.createUser("test@test.com", "test", "test");

            // when
            // then
            assertThatThrownBy(() -> authService.login(wrongLoginRequest))
                .isInstanceOf(AuthException.class)
                .hasMessage(AuthErrorCode.FAIL_LOGIN.getMessage());
        }

        private static Stream<Arguments> provideInvalidLoginRequest() {
            return Stream.of(
                Arguments.of(new LoginRequest("test1@test.com", "test")),
                Arguments.of(new LoginRequest("test@test.com", "test1"))
            );
        }

        @DisplayName("이미 로그인되 계정으로 로그인 시도 시 예외를 발생한다.")
        @Test
        void failLoginWithAlreadyLoginedUser() {
            // given
            final User user = testUserFactory.createUser("test@test.com", "test", "test");
            testAuthTokenFactory.createAuthToken(user.getId(), "refreshToken");
            final LoginRequest wrongLoginRequest = new LoginRequest(user.getEmail(), user.getPassword());

            // when
            // then
            assertThatThrownBy(() -> authService.login(wrongLoginRequest))
                .isInstanceOf(AuthException.class)
                .hasMessage(AuthErrorCode.ALREADY_LOGIN.getMessage());
        }
    }

    @DisplayName("token 삭제")
    @Nested
    class DeleteToken {
        @DisplayName("로그아웃시 refreshToken을 삭제한다.")
        @Test
        void deleteToken() {
            // given
            final User user = testUserFactory.createUser("test@test.com", "test", "test");
            final String existingRefreshToken = "refreshToken";
            testAuthTokenFactory.createAuthToken(user.getId(), existingRefreshToken);

            // when
            authService.deleteToken(user.getId(), existingRefreshToken);

            // then
            Optional<AuthToken> authToken = authTokenRepository.findByUserIdAndRefreshToken(user.getId(), existingRefreshToken);
            Assertions.assertThat(authToken.isPresent()).isFalse();
        }

        @DisplayName("잘못된 유저 혹은 refreshToken 정보로 토큰 삭제 요청 시 예외가 발생한다.")
        @Test
        void deleteTokenWithNotExistUserOrWrongRefreshToken() {
            // given
            final User user = testUserFactory.createUser("test@test.com", "test", "test");
            final String existingRefreshToken = "refreshToken";
            testAuthTokenFactory.createAuthToken(user.getId(), existingRefreshToken);

            final Long nonExistUserId = Long.MAX_VALUE;
            final String wrongRefreshToken = "wrongRefreshToken";

            // when
            // then
            assertThatThrownBy(() -> authService.deleteToken(nonExistUserId, existingRefreshToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(AuthErrorCode.HAVE_NOT_REFRESH_TOKEN.getMessage());
        }
    }


    @DisplayName("token 검증")
    @Nested
    class ValidateToken {
        @DisplayName("token 정보를 통해 user를 검증한다.")
        @Test
        void validateUserByAccessToken() {
            // given
            TokenProvider tokenProvider = new TokenProvider(10000, 100000, "secretCodeaesb231dbdsd");
            AuthService authService = new AuthService(tokenProvider, authTokenRepository, userRepository);

            final User user = testUserFactory.createUser("test@test.com", "test", "test");
            final String accessToken = tokenProvider.createAccessToken(user.getId());

            // when
            final Long memberId = authService.validUserByAccessToken(accessToken);


            // then
            Assertions.assertThat(memberId).isEqualTo(user.getId());
        }

        @DisplayName("존재하지 않는 User에 대한 token은 예외를 발생한다.")
        @Test
        void invalidUserByAccessToken() {
            // given
            TokenProvider tokenProvider = new TokenProvider(10000, 100000, "secretCodeaesb231dbdsd");
            AuthService authService = new AuthService(tokenProvider, authTokenRepository, userRepository);

            final long wrongUserId = Long.MAX_VALUE;
            final String accessToken = tokenProvider.createAccessToken(wrongUserId);

            // when
            // then
            assertThatThrownBy(() -> authService.validUserByAccessToken(accessToken))
                .isInstanceOf(UserException.class)
                .hasMessage(UserErrorCode.NOT_EXIST_USER.getMessage());
        }
    }

    @DisplayName("reissue token을 통해 accessToken을 재발급한다.")
    @Test
    void reissueToken() {
        // given
        final User user = testUserFactory.createUser("test@test.com", "test", "test");
        final String existingRefreshToken = "refreshToken";
        testAuthTokenFactory.createAuthToken(user.getId(), existingRefreshToken);

        // when
        final TokenInfo result = authService.reissueToken(user.getId(), existingRefreshToken);

        // then
        final Claims accessTokenClaims = tokenProvider.parseClaims(result.accessToken());
        final Claims refreshTokenClaims = tokenProvider.parseClaims(result.refreshToken());
        AuthToken authToken = authTokenRepository.findByUserIdAndRefreshToken(user.getId(), result.refreshToken()).get();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(accessTokenClaims.get("memberId", Long.class)).isEqualTo(user.getId());
            softAssertions.assertThat(refreshTokenClaims.get("memberId", Long.class)).isEqualTo(user.getId());
            softAssertions.assertThat(authToken.getRefreshToken()).isEqualTo(result.refreshToken());
        });
    }
}
