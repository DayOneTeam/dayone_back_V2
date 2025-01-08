package dayone.dayone.booklog.docs;

import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import dayone.dayone.booklog.service.dto.BookLogCreateRequest;
import dayone.dayone.support.DocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookLogDocsTest extends DocsTest {

    @DisplayName("bookLog를 생성한다.")
    @Test
    void createBookLog() throws Exception {
        // given
        final BookLogCreateRequest request = new BookLogCreateRequest(1L, "의미있는 구절", "내가 느낀 감정");
        given(bookLogService.create(any(BookLogCreateRequest.class)))
            .willReturn(1L);

        // when
        final ResultActions result = mockMvc.perform(post("/api/v1/book-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isCreated())
            .andDo(document("book-log-create-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("passage").type(JsonFieldType.STRING).description("구절"),
                    fieldWithPath("comment").type(JsonFieldType.STRING).description("댓글"),
                    fieldWithPath("bookId").type(JsonFieldType.NUMBER).description("작성할 책 id")
                )
            ));
    }

    @DisplayName("존재하지 않은 책에 bookLog를 작성하려고 하면 예외가 발생한다.")
    @Test
    void createBookLogOnNotExistBook() throws Exception {
        // given
        final BookLogCreateRequest request = new BookLogCreateRequest(1L, "의미있는 구절", "내가 느낀 감정");
        given(bookLogService.create(any(BookLogCreateRequest.class)))
            .willThrow(new BookException(BookErrorCode.BOOK_NOT_EXIST));

        // when
        final ResultActions result = mockMvc.perform(post("/api/v1/book-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest())
            .andDo(document("book-log-create-fail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("passage").type(JsonFieldType.STRING).description("구절"),
                    fieldWithPath("comment").type(JsonFieldType.STRING).description("댓글"),
                    fieldWithPath("bookId").type(JsonFieldType.NUMBER).description("작성할 책 id")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description(BookErrorCode.BOOK_NOT_EXIST.getCode()),
                    fieldWithPath("message").type(JsonFieldType.STRING).description(BookErrorCode.BOOK_NOT_EXIST.getMessage()),
                    fieldWithPath("data").type(null).description("null")
                )
            ));
    }
}
