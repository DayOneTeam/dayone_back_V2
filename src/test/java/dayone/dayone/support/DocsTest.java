package dayone.dayone.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import dayone.dayone.auth.application.AuthService;
import dayone.dayone.auth.ui.CookieProvider;
import dayone.dayone.book.service.BookService;
import dayone.dayone.booklog.service.BookLogService;
import dayone.dayone.bookloglike.service.BookLogLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;
}
