package dayone.dayone.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import dayone.dayone.auth.application.AuthService;
import dayone.dayone.auth.exception.AuthErrorCode;
import dayone.dayone.auth.exception.AuthException;
import dayone.dayone.auth.ui.CookieProvider;
import dayone.dayone.auth.ui.interceptor.AuthContext;
import dayone.dayone.auth.ui.interceptor.AuthInterceptor;
import dayone.dayone.book.service.BookService;
import dayone.dayone.booklog.service.BookLogService;
import dayone.dayone.bookloglike.service.BookLogLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class DocsTest {

    @MockBean
    public BookService bookService;

    @MockBean
    public BookLogService bookLogService;

    @MockBean
    public BookLogLikeService bookLogLikeService;

    @MockBean
    public CookieProvider cookieProvider;

    @MockBean
    public AuthService authService;

    @MockBean
    public AuthInterceptor authInterceptor;

    @MockBean
    public AuthContext authContext;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    public void failAuth() throws Exception {
        given(authInterceptor.preHandle(any(), any(), any()))
            .willThrow(new AuthException(AuthErrorCode.NOT_LOGIN_USER));
        given(authContext.getMemberId()).willReturn(1L);
    }

    public void successAuth() throws Exception {
        given(authInterceptor.preHandle(any(), any(), any()))
            .willReturn(true);
    }
}
