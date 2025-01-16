package dayone.dayone.auth.ui;

import dayone.dayone.auth.application.AuthService;
import dayone.dayone.auth.application.dto.LoginRequest;
import dayone.dayone.auth.application.dto.LoginResponse;
import dayone.dayone.auth.application.dto.TokenInfo;
import dayone.dayone.global.response.CommonResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> logout(final HttpServletRequest request, final HttpServletResponse response) {
        final Cookie cookie = cookieProvider.deleteCookie(request.getCookies(), REFRESH_TOKEN_COOKIE_KEY);
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }
}
