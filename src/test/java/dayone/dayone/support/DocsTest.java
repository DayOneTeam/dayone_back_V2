package dayone.dayone.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import dayone.dayone.book.service.BookService;
import dayone.dayone.booklog.service.BookLogService;
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

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;
}
