package dayone.dayone.auth.ui.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;


@RequestScope
@Component
public class AuthContext {

    private static final long ANONYMOUS_ID = -1L;

    private Long userId = ANONYMOUS_ID;

    public void setMemberId(final Long userId) {
        this.userId = userId;
    }

    public Long getMemberId() {
        return this.userId;
    }
}
