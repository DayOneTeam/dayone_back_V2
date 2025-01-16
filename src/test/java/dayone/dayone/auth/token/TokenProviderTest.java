package dayone.dayone.auth.token;

import dayone.dayone.auth.exception.TokenErrorCode;
import dayone.dayone.auth.exception.TokenException;
import io.jsonwebtoken.Claims;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenProviderTest {

    @DisplayName("access token을 생성한다.")
    @Test
    void createAccessToken() {
        // given
        final TokenProvider tokenProvider = new TokenProvider(10000, 100000, "secretCodeaesb231dbdsd");

        // when
        final String accessToken = tokenProvider.createAccessToken(1);

        // then
        final Claims claims = tokenProvider.parseClaims(accessToken);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(claims.get("memberId")).isEqualTo(1);
            softAssertions.assertThat(claims.getExpiration()).isAfter(new Date());
        });
    }

    @DisplayName("refresh token을 생성한다.")
    @Test
    void createRefreshToken() {
        // given
        final TokenProvider tokenProvider = new TokenProvider(10000, 100000, "secretCodeaesb231dbdsd");

        // when
        final String refreshToken = tokenProvider.createRefreshToken(1);

        // then
        final Claims claims = tokenProvider.parseClaims(refreshToken);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(claims.get("memberId")).isEqualTo(1);
            softAssertions.assertThat(claims.getExpiration()).isAfter(new Date());
        });
    }

    @DisplayName("잘못된 토큰을 분해하면 MalformedTokenException 예외를 발생한다.")
    @Test
    void MalformedTokenException() {
        // given
        final TokenProvider tokenProvider = new TokenProvider(10000, 100000, "secretCodeaesb231dbdsd");
        final String wrongToken = "wrongToken.wrongToken.wrongToken";

        // when
        // then
        assertThatThrownBy(() -> tokenProvider.parseClaims(wrongToken))
            .isInstanceOf(TokenException.class)
            .hasMessage(TokenErrorCode.NOT_ISSUED_TOKEN.getMessage());
    }

    @DisplayName("토큰 기간이 만료되면 ExpiredTokenException 예외를 발생한다.")
    @Test
    void ExpiredTokenException() {
        // given
        final TokenProvider expiredTokenProvider = new TokenProvider(0, 0, "secretCodeaesb231dbdsd");
        final String expiredAccessToken = expiredTokenProvider.createAccessToken(1);

        // when
        // then
        assertThatThrownBy(() -> expiredTokenProvider.parseClaims(expiredAccessToken))
            .isInstanceOf(TokenException.class)
            .hasMessage(TokenErrorCode.EXPIRED_TOKEN.getMessage());
    }
}
