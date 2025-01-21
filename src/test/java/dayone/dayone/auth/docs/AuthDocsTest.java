package dayone.dayone.auth.docs;

import dayone.dayone.auth.application.dto.LoginRequest;
import dayone.dayone.auth.application.dto.TokenInfo;
import dayone.dayone.auth.exception.AuthErrorCode;
import dayone.dayone.auth.exception.AuthException;
import dayone.dayone.support.DocsTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthDocsTest extends DocsTest {

    @DisplayName("로그인 요청")
    @Nested
    class Login {
        @DisplayName("로그인 성공시 accessToken과 refreshToken을 반환한다.")
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

    @DisplayName("로그아웃 요청")
    @Nested
    class Logout {
        @DisplayName("로그 아웃 시 refreshToken 쿠키를 삭제한다.")
        @Test
        void successLogout() throws Exception {
            // given
            final Cookie deletedCookie = new Cookie("refreshToken", null);
            given(cookieProvider.deleteCookie(any(), any()))
                .willReturn(deletedCookie);
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(delete("/api/v1/auth/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"));

            // then
            result.andExpect(status().isNoContent())
                .andDo(document("success-logout",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.SET_COOKIE).description("유효기간이 0인 refreshToken 쿠키")
                    )
                ));
        }

        @DisplayName("비로그인 사용자가 로그아웃 시도 시 401 예외를 발생한다.")
        @Test
        void failLogoutWithUnAuthenticatedUser() throws Exception {
            // given
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(delete("/api/v1/auth/logout")
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보"));

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("fail-logout",
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

    @DisplayName("토큰 재발급 요청")
    @Nested
    class ReissueToken {
        @DisplayName("refreshToken을 통해 accessToken을 재발급한다.")
        @Test
        void reissueTokenWithRefreshToken() throws Exception {
            // given
            given(authService.reissueToken(anyLong(), anyString()))
                .willReturn(new TokenInfo("reissuedAccessToken", "reissueRefreshToken"));
            given(cookieProvider.createAuthCookie(any(), any()))
                .willReturn(new Cookie("refreshToken", "reissuedRefreshTokenValue"));

            // when
            final ResultActions result = mockMvc.perform(post("/api/v1/auth/reissue-token")
                .header(HttpHeaders.COOKIE, "refreshToken=refreshTokenValue"));

            // then
            result.andExpect(status().isOk())
                .andDo(document("reissue-token",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.COOKIE).description("refreshToken 정보")
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.SET_COOKIE).description("재발급된 refreshToken 정보")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("성공 코드 ex) 1"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지 ex) 토큰 재발급 성공"),
                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("재발급된 accessToken 정보")

                    )
                ));
        }
    }

    @DisplayName("refresh Token 없이 토큰 재발급 요청 시 400 예외를 발생한다.")
    @Test
    void reissueTokenWithoutRefreshToken() throws Exception {
        // given
        given(authService.reissueToken(anyLong(), anyString()))
            .willThrow(new AuthException(AuthErrorCode.HAVE_NOT_REFRESH_TOKEN));

        // when
        final ResultActions result = mockMvc.perform(post("/api/v1/auth/reissue-token")
            .header(HttpHeaders.COOKIE, "비어있거나 혹은 잘못된 refreshToken 정보"));


        // then
        result.andExpect(status().isBadRequest())
            .andDo(document("reissue-token-without-refresh-token",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.COOKIE).description("비어 있거나 존재하지 않는 refreshToken 정보")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패 코드 ex) 4004"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지 ex) 잘못된 refresh 토큰을 가지고 있습니다."),
                    fieldWithPath("data").type(null).description("null")
                )
            ));
    }
}
