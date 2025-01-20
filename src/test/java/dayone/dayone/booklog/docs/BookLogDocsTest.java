package dayone.dayone.booklog.docs;

import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import dayone.dayone.booklog.service.dto.BookLogCreateRequest;
import dayone.dayone.booklog.service.dto.BookLogDetailResponse;
import dayone.dayone.booklog.service.dto.BookLogPaginationListResponse;
import dayone.dayone.booklog.service.dto.BookLogResponse;
import dayone.dayone.booklog.service.dto.BookLogTop4Response;
import dayone.dayone.booklog.service.dto.BookLogWriteActiveResponse;
import dayone.dayone.support.DocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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

    @DisplayName("bookLog 생성 요청")
    @Nested
    class CreateBookLog {
        @DisplayName("bookLog를 생성한다.")
        @Test
        void createBookLog() throws Exception {
            // given
            final BookLogCreateRequest request = new BookLogCreateRequest(1L, "의미있는 구절", "내가 느낀 감정");
            given(bookLogService.create(anyLong(), any(BookLogCreateRequest.class)))
                .willReturn(1L);
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(post("/api/v1/book-logs")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(status().isCreated())
                .andDo(document("book-log-create-success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                    ),
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
            successAuth();

            given(bookLogService.create(anyLong(), any(BookLogCreateRequest.class)))
                .willThrow(new BookException(BookErrorCode.BOOK_NOT_EXIST));

            // when
            final ResultActions result = mockMvc.perform(post("/api/v1/book-logs")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(status().isBadRequest())
                .andDo(document("book-log-create-fail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                    ),
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

        @DisplayName("인증되지 않는 사용자가 bookLog 생성 요청 시 401예외를 발생한다.")
        @Test
        void failCreateBookLogWithUnAuthenticatedUser() throws Exception {
            // given
            final BookLogCreateRequest request = new BookLogCreateRequest(1L, "의미있는 구절", "내가 느낀 감정");
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(post("/api/v1/book-logs")
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON));

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("create-book-log-fail-with-unauthenticated-user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("비어 있거나 존재하지 않는 User의 Token 정보")
                    ),
                    requestFields(
                        fieldWithPath("passage").type(JsonFieldType.STRING).description("구절"),
                        fieldWithPath("comment").type(JsonFieldType.STRING).description("댓글"),
                        fieldWithPath("bookId").type(JsonFieldType.NUMBER).description("작성할 책 id")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패 코드 ex) 4003"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지 ex) 로그인 되지 않은 유저입니다."),
                        fieldWithPath("data").type(null).description("null")
                    )
                ));
        }
    }

    @DisplayName("bookLog 조회 요청")
    @Nested
    class ReadBookLog {
        @DisplayName("BookLog를 cursor기반으로 10개씩 조회한다.")
        @Test
        void readBookLogsByCursor() throws Exception {
            // given
            final List<BookLogResponse> response = List.of(new BookLogResponse(1L, "의미있는 구절", "내가 느낀 감정", 1, "책 제목", LocalDateTime.now()));

            given(bookLogService.getAllBookLogs(anyLong()))
                .willReturn(new BookLogPaginationListResponse(response, false, -1L));
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(get("/api/v1/book-logs")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .param("cursor", "1")
            );

            // then
            result.andExpect(status().isOk())
                .andDo(document("book-log-read-success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                    ),
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

        @DisplayName("인증되지 않은 사용자가 Cursor 기반 bookLog 조회 요청 시 401예외를 발생한다.")
        @Test
        void failReadBookLogsByCursorWithUnAuthenticatedUser() throws Exception {
            // given
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(get("/api/v1/book-logs")
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보")
                .param("cursor", "1")
            );

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("read-book-logs-by-cursor-fail-with-unauthorized-user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("비어 있거나 존재하지 않는 User의 Token 정보")
                    ),
                    queryParameters(
                        parameterWithName("cursor").description("조회를 시작할 bookLog Id")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패 코드 ex) 4003"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지 ex) 로그인 되지 않은 유저입니다."),
                        fieldWithPath("data").type(null).description("null")
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
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(get("/api/v1/book-logs/{book_log_id}", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"));

            // then
            result.andExpect(status().isOk())
                .andDo(document("read-book-log-detail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                    ),
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

        @DisplayName("인증되지 않은 사용자가 BookLog 상세 조회 요청 시 401예외를 발생한다.")
        @Test
        void failReadBookLogDetailWithUnAuthenticatedUser() throws Exception {
            // given
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(get("/api/v1/book-logs/{book_log_id}", 1L)
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보"));

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("read-book-log-detail-fail-with-unauthorized-user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("비어 있거나 존재하지 않는 User의 Token 정보")
                    ),
                    pathParameters(
                        parameterWithName("book_log_id").description("조회할 책 로그의 id")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패 코드 ex) 4003"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지 ex) 로그인 되지 않은 유저입니다."),
                        fieldWithPath("data").type(null).description("null")
                    )
                ));

        }

        @DisplayName("한 주내 가장 좋아요를 받은 bookLog 4개를 조회한다.")
        @Test
        void readMostLikedAndWrittenRecentlyBookLogsInWeek() throws Exception {
            // given
            final List<BookLogResponse> bookLogResponses = List.of(
                new BookLogResponse(1L, "의미있는 구절", "내가 느낀 감정", 5, "책 제목", LocalDateTime.now()),
                new BookLogResponse(3L, "의미있는 구절", "내가 느낀 감정", 4, "책 제목", LocalDateTime.now()),
                new BookLogResponse(2L, "의미있는 구절", "내가 느낀 감정", 4, "책 제목", LocalDateTime.now()),
                new BookLogResponse(4L, "의미있는 구절", "내가 느낀 감정", 2, "책 제목", LocalDateTime.now())
            );

            given(bookLogService.getTop4BookLogs(any()))
                .willReturn(new BookLogTop4Response(bookLogResponses));
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(get("/api/v1/book-logs/top4")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"));

            // then
            result.andExpect(status().isOk())
                .andDo(document("read-most-liked-and-written-recently-book-logs-in-week",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                    ),
                    responseFields(
                        fieldWithPath("code").description("성공 코드 ex) 1"),
                        fieldWithPath("message").description("성공 메시지 ex) 조회된 책 로그 정보"),
                        fieldWithPath("data.book_logs[].id").type(JsonFieldType.NUMBER).description("책 id"),
                        fieldWithPath("data.book_logs[].passage").type(JsonFieldType.STRING).description("구절"),
                        fieldWithPath("data.book_logs[].comment").type(JsonFieldType.STRING).description("댓글"),
                        fieldWithPath("data.book_logs[].book_title").type(JsonFieldType.STRING).description("책 제목"),
                        fieldWithPath("data.book_logs[].like_count").type(JsonFieldType.NUMBER).description("책 로그 좋아요 수"),
                        fieldWithPath("data.book_logs[].created_at").type(JsonFieldType.STRING).description("책 로그 생성 시간")
                    )
                ));
        }

        @DisplayName("인증되지 않은 사용자가 top4BookLog 조회 요청 시 401예외를 발생한다.")
        @Test
        void failReadMostLikedAndWrittenRecentlyBookLogsInWeekWithUnAuthenticatedUser() throws Exception {
            // given
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(get("/api/v1/book-logs/top4")
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보"));

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("read-most-liked-and-written-recently-book-logs-in-week-fail-with-unauthorized-user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("비어 있거나 존재하지 않는 User의 Token 정보")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패 코드 ex) 4003"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지 ex) 로그인 되지 않은 유저입니다."),
                        fieldWithPath("data").type(null).description("null")
                    )
                ));
        }

        @DisplayName("유저가 자신이 한 주간 bookLog를 작성한 날짜를 활성화한 정보를 조회한다.")
        @Test
        void readActiveDateThatWriteBookLogInWeek() throws Exception {
            // given
            given(bookLogService.getBookLogWriteActive(anyLong()))
                .willReturn(new BookLogWriteActiveResponse(List.of(true, false, true, true, false, true, false)));
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(get("/api/v1/book-logs/write-in-week")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"));

            // then
            result.andExpect(status().isOk())
                .andDo(document("read-active-date-that-write-book-log-in-week",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("성공 코드 ex) 1"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("성공 메시지 ex) 일주일간 bookLog 작성 날짜 조회 성공"),
                        fieldWithPath("data.active_day[]").type(JsonFieldType.ARRAY).description("일주일간 bookLog를 작성한 요일 활성화 정보")

                    )
                ));
        }

        @DisplayName("인증되지 않은 유저가 주간 bookLog를 작성한 날짜를 활성한 정보를 조회할 경우 401예외가 발생한다.")
        @Test
        void failReadActiveDateThatWriteBookLogInWeekWithUnauthorizedUser() throws Exception {
            // given
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(get("/api/v1/book-logs/write-in-week")
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보"));

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("read-active-date-that-write-book-log-in-week-fail-with-unauthorized-user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("비어 있거나 존재하지 않는 User의 Token 정보")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패 코드 ex) 4003"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지 ex) 로그인 되지 않은 유저입니다."),
                        fieldWithPath("data").type(null).description("null")
                    )
                ));
        }

    }
}
