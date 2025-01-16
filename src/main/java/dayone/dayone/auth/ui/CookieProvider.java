package dayone.dayone.auth.ui;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

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
}
