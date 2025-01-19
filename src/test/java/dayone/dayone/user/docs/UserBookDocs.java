package dayone.dayone.user.docs;

import dayone.dayone.support.DocsTest;
import dayone.dayone.user.service.dto.UserBookListResponse;
import dayone.dayone.user.service.dto.UserBookResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserBookDocs extends DocsTest {

    @DisplayName("유저가 자신의 책장 목록을 조회한다.")
    @Test
    void readUserBooks() throws Exception {
        // given
        given(userBookService.getUserBooks(anyLong()))
            .willReturn(new UserBookListResponse(List.of(new UserBookResponse(1L, "책 표지 사진"))));
        successAuth();

        // when
        final ResultActions result = mockMvc.perform(get("/api/v1/users/books")
            .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"));

        // then
        result.andExpect(status().isOk())
            .andDo(document("read-user-books",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("성공 코드 ex) 1"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 메시지 ex) 조회된 책 목록"),
                    fieldWithPath("data.user_books[].id").type(JsonFieldType.NUMBER).description("책 id"),
                    fieldWithPath("data.user_books[].thumbnail").type(JsonFieldType.STRING).description("책 표지 사진 url")
                )
            ));
    }

    @DisplayName("인증되지 않은 사용자가 책장 목록을 조회할 경우 401예외를 발생한다.")
    @Test
    void failReadUserBooksWithUnAuthenticatedUser() throws Exception {
        // given
        failAuth();

        // when
        final ResultActions result = mockMvc.perform(get("/api/v1/users/books")
            .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보"));

        // then
        result.andExpect(status().isUnauthorized())
            .andDo(document("fail-read-user-books",
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
