package dayone.dayone.auth.ui.interceptor;

import dayone.dayone.auth.application.AuthService;
import dayone.dayone.auth.exception.AuthErrorCode;
import dayone.dayone.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        final Optional<String> token = TokenExtractor.extractToken(request);
        if (token.isEmpty()) {
            throw new AuthException(AuthErrorCode.NOT_LOGIN_USER);
        }

        if (!authService.validUserIsAdmin(token.get())) {
            throw new AuthException(AuthErrorCode.NOT_ADMIN_USER);
        }

        return true;
    }
}
