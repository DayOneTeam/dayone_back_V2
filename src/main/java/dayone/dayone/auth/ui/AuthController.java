package dayone.dayone.auth.ui;

import dayone.dayone.auth.application.AuthService;
import dayone.dayone.auth.application.dto.LoginRequest;
import dayone.dayone.auth.application.dto.LoginResponse;
import dayone.dayone.auth.application.dto.TokenInfo;
import dayone.dayone.auth.application.dto.TokenReissueResponse;
import dayone.dayone.auth.ui.argumentresolver.AuthUser;
import dayone.dayone.global.response.CommonResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private static final String REFRESH_TOKEN_COOKIE_KEY = "refreshToken";
    private static final String EMPTY_REFRESH_TOKEN = "none";

    private final CookieProvider cookieProvider;
    private final AuthService authService;

    @PostMapping("/login")
    public CommonResponseDto<LoginResponse> login(@RequestBody final LoginRequest loginRequest, final HttpServletResponse response) {
        final TokenInfo tokenInfo = authService.login(loginRequest);

        final Cookie refreshTokenCookie = cookieProvider.createAuthCookie(tokenInfo.refreshToken(), REFRESH_TOKEN_COOKIE_KEY);
        response.addCookie(refreshTokenCookie);

        return CommonResponseDto.forSuccess(1, "로그인 성공", new LoginResponse(tokenInfo.accessToken()));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
        @AuthUser final Long userId,
        @CookieValue(value = REFRESH_TOKEN_COOKIE_KEY, defaultValue = EMPTY_REFRESH_TOKEN) final String refreshToken,
        final HttpServletRequest request, final HttpServletResponse response
    ) {
        authService.deleteToken(userId, refreshToken);
        final Cookie cookie = cookieProvider.deleteCookie(request.getCookies(), REFRESH_TOKEN_COOKIE_KEY);
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reissue-token")
    public CommonResponseDto<TokenReissueResponse> reissueToken(@AuthUser final Long userId, @CookieValue(value = REFRESH_TOKEN_COOKIE_KEY, defaultValue = EMPTY_REFRESH_TOKEN) final String refreshToken, final HttpServletResponse response) {
        final TokenInfo tokenInfo = authService.reissueToken(userId, refreshToken);

        final Cookie reissuedRefreshToken = cookieProvider.createAuthCookie(tokenInfo.refreshToken(), REFRESH_TOKEN_COOKIE_KEY);
        response.addCookie(reissuedRefreshToken);

        return CommonResponseDto.forSuccess(1, "토큰 재발급 성공", new TokenReissueResponse(tokenInfo.accessToken()));
    }
}
