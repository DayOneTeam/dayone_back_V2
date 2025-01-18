package dayone.dayone.bookloglike.docs;

import dayone.dayone.support.DocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookLogLikeDocsTest extends DocsTest {

    @DisplayName("like 생성 요청")
    @Nested
    class CreateLikeOnBookLog {
        @DisplayName("bookLog에 like을 추가한다.")
        @Test
        void addLikeOnBookLog() throws Exception {
            // given
            willDoNothing().given(bookLogLikeService).addLike(anyLong(), anyLong());
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(post("/api/v1/book-logs/{book_log_id}/like", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"));

            // then
            result.andExpect(status().isCreated())
                .andDo(document("book-log-like-create-success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                    ),
                    pathParameters(
                        parameterWithName("book_log_id").description("좋아요를 추가할 bookLog의 id")
                    ))
                );
        }

        @DisplayName("인증되지 않은 유저가 bookLog에 like 생성 요청 시 401예외를 발생한다.")
        @Test
        void failCreateLikeOnBookLogWithUnAuthenticatedUser() throws Exception {
            // given
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(post("/api/v1/book-logs/{book_log_id}/like", 1L)
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보"));

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("create-like-on-book-log-fail-with-unauthenticated-user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("비어 있거나 존재하지 않는 User의 Token 정보")
                    ),
                    pathParameters(
                        parameterWithName("book_log_id").description("좋아요를 추가할 bookLog의 id")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패 코드 ex) 4003"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지 ex) 로그인 되지 않은 유저입니다."),
                        fieldWithPath("data").type(null).description("null")
                    )
                ));
        }
    }

    @DisplayName("like 삭제 요청")
    @Nested
    class DeleteLike {
        @DisplayName("bookLog에 좋아요를 삭제한다.")
        @Test
        void deleteLikeOnBookLog() throws Exception {
            // given
            willDoNothing().given(bookLogLikeService).deleteLike(anyLong(), anyLong());
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(delete("/api/v1/book-logs/{book_log_id}/like", 1L, 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"));

            // then
            result.andExpect(status().isNoContent())
                .andDo(document("book-log-like-delete-success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                    ),
                    pathParameters(
                        parameterWithName("book_log_id").description("좋아요를 삭제할 bookLog의 id")
                    ))
                );
        }

        @DisplayName("인증되지 않은 유저가 bookLog에 like 삭제 요청 시 401예외를 발생한다.")
        @Test
        void failDeleteLikeOnBookLogWithUnAuthenticatedUser() throws Exception {
            // given
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(delete("/api/v1/book-logs/{book_log_id}/like", 1L, 1L)
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보"));

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("delete-like-on-book-log-fail-with-unauthenticated-user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("비어 있거나 존재하지 않는 User의 Token 정보")
                    ),
                    pathParameters(
                        parameterWithName("book_log_id").description("좋아요를 삭제할 bookLog의 id")
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
