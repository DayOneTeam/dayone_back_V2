package dayone.dayone.booklog.docs;

import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import dayone.dayone.booklog.service.dto.BookLogCreateRequest;
import dayone.dayone.booklog.service.dto.BookLogDetailResponse;
import dayone.dayone.booklog.service.dto.BookLogListResponse;
import dayone.dayone.booklog.service.dto.BookLogResponse;
import dayone.dayone.support.DocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
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

    @DisplayName("BookLog를 cursor기반으로 10개씩 조회한다.")
    @Test
    void readBookLogsByCursor() throws Exception {
        // given
        final List<BookLogResponse> response = List.of(new BookLogResponse(1L, "의미있는 구절", "내가 느낀 감정", 1, "책 제목", LocalDateTime.now()));

        given(bookLogService.getAllBookLogs(anyLong()))
            .willReturn(new BookLogListResponse(response, false, -1L));

        // when
        final ResultActions result = mockMvc.perform(get("/api/v1/book-logs")
            .param("cursor", "1")
        );

        // then
        result.andExpect(status().isOk())
            .andDo(document("book-log-read-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("cursor").description("조회를 시작할 bookLog Id")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("성공 코드 ex) 1"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지 ex)책 로그 응답 성공"),
                    fieldWithPath("data.book_logs[].id").type(JsonFieldType.NUMBER).description("책 id"),
                    fieldWithPath("data.book_logs[].passage").type(JsonFieldType.STRING).description("구절"),
                    fieldWithPath("data.book_logs[].comment").type(JsonFieldType.STRING).description("댓글"),
                    fieldWithPath("data.book_logs[].book_title").type(JsonFieldType.STRING).description("책 제목"),
                    fieldWithPath("data.book_logs[].like_count").type(JsonFieldType.NUMBER).description("책 로그 좋아요 수"),
                    fieldWithPath("data.book_logs[].created_at").type(JsonFieldType.STRING).description("책 로그 생성 시간"),
                    fieldWithPath("data.next").type(JsonFieldType.BOOLEAN).description("다음 데이터 존재 여부"),
                    fieldWithPath("data.next_cursor").type(JsonFieldType.NUMBER).description("다음 데이터 커서")
                )
            ));
    }

    @DisplayName("BookLog를 상세 조회한다.")
    @Test
    void readBookLogDetail() throws Exception {
        // given
        final BookLogDetailResponse response = new BookLogDetailResponse(1L,
            "의미 있는 구절",
            "책에 대한 나의 생각",
            0,
            "책 제목",
            "책 표지",
            LocalDateTime.now());
        given(bookLogService.getBookLogById(anyLong()))
            .willReturn(response);

        // when
        final ResultActions result = mockMvc.perform(get("/api/v1/book-logs/{book_log_id}", 1L));

        // then
        result.andExpect(status().isOk())
            .andDo(document("read-book-log-detail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("book_log_id").description("조회할 책 로그의 id")
                ),
                responseFields(
                    fieldWithPath("code").description("성공 코드 ex) 1"),
                    fieldWithPath("message").description("성공 메시지 ex) 조회된 책 로그 정보"),
                    fieldWithPath("data.id").description("조회된 책 로그 id"),
                    fieldWithPath("data.passage").description("의미 있는 구절"),
                    fieldWithPath("data.comment").description("책에 대한 나의 생각"),
                    fieldWithPath("data.book_title").description("책 제목"),
                    fieldWithPath("data.book_cover").description("책 표지"),
                    fieldWithPath("data.like_count").description("책 로그의 좋아요 수"),
                    fieldWithPath("data.created_at").description("책 로그 생성 시간")
                )
            ));

    }
}
