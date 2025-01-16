package dayone.dayone.auth.ui;

import dayone.dayone.auth.exception.AuthErrorCode;
import dayone.dayone.auth.exception.AuthException;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieProvider {

    private static final int WEEK = 7 * 24 * 60 * 60;

    public Cookie createAuthCookie(final String token, final String name) {
        final Cookie cookie = new Cookie(name, token);
        cookie.setMaxAge(WEEK);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }

    public Cookie deleteCookie(final Cookie[] cookies, final String name) {
        final Cookie cookie = findCookie(cookies, name);
        if (cookie.getValue().isEmpty()) {
            throw new AuthException(AuthErrorCode.HAVE_NOT_REFRESH_TOKEN);
        }
        cookie.setMaxAge(0);
        cookie.setValue(null);
        return cookie;
    }

    private Cookie findCookie(final Cookie[] cookies, final String name) {
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(name))
            .findFirst()
            .orElse(new Cookie("notExist", ""));
    }
}
