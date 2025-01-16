package dayone.dayone.auth.ui;

import dayone.dayone.auth.application.AuthService;
import dayone.dayone.auth.application.dto.LoginRequest;
import dayone.dayone.auth.application.dto.LoginResponse;
import dayone.dayone.auth.application.dto.TokenInfo;
import dayone.dayone.global.response.CommonResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final CookieProvider cookieProvider;
    private final AuthService authService;

    @PostMapping("/login")
    public CommonResponseDto<LoginResponse> login(@RequestBody final LoginRequest loginRequest, final HttpServletResponse response) {
        final TokenInfo tokenInfo = authService.login(loginRequest);

        final Cookie refreshTokenCookie = cookieProvider.createAuthCookie(tokenInfo.refreshToken(), "refresh_token");
        response.addCookie(refreshTokenCookie);

        return CommonResponseDto.forSuccess(1, "로그인 성공", new LoginResponse(tokenInfo.accessToken()));
    }
}
