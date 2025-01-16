package dayone.dayone.auth.docs;

import dayone.dayone.auth.application.dto.LoginRequest;
import dayone.dayone.auth.application.dto.TokenInfo;
import dayone.dayone.support.DocsTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthDocsTest extends DocsTest {


    @DisplayName("로그인 시 accessToken과 refreshToken을 반환한다.")
    @Test
    void successLogin() throws Exception {
        // given
        final LoginRequest request = new LoginRequest("이메일", "비밀번호");
        TokenInfo response = new TokenInfo("accessToken", "refreshToken");
        given(authService.login(any())).willReturn(response);

        final Cookie cookie = new Cookie("refreshToken", "refreshToken value");
        given(cookieProvider.createAuthCookie(any(), any())).willReturn(cookie);

        // when
        final ResultActions result = mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
            .andDo(document("success-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.SET_COOKIE).description("refreshToken 정보")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("성공 코드 ex) 1"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지 ex) 로그인 성공"),
                    fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("accessToken")
                )
            ));
    }

}
