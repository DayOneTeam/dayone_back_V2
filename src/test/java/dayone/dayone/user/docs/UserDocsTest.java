package dayone.dayone.user.docs;

import dayone.dayone.support.DocsTest;
import dayone.dayone.user.service.dto.UserInfoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserDocsTest extends DocsTest {

    @DisplayName("유저 정보를 조회한다.")
    @Test
    void readUserInfo() throws Exception {
        // given
        successAuth();
        given(userService.getUserInfo(anyLong()))
            .willReturn(new UserInfoResponse("유저 이름", "유저 프로필 이미지"));

        // when
        final ResultActions result = mockMvc.perform(get("/api/v1/users/info")
            .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"));

        // then
        result.andExpect(status().isOk())
            .andDo(document("read-user-info",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("성공 코드 ex) 1"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 메시지 ex) 조회된 유저 정보"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING).description("유저 이름"),
                    fieldWithPath("data.profile_image").type(JsonFieldType.STRING).description("유저 프로필 이미지")
                )
            ));
    }

    @DisplayName("유저 정보 조회 시 인증되지 않은 사용자가 조회할 경우 401예외를 발생한다.")
    @Test
    void failReadUserInfoWithUnAuthenticatedUser() throws Exception {
        // given
        failAuth();

        // when
        final ResultActions result = mockMvc.perform(get("/api/v1/users/info")
            .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보"));

        // then
        result.andExpect(status().isUnauthorized())
            .andDo(document("fail-read-user-info",
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

    @DisplayName("사용자의 프로필 이미지를 업데이트한다.")
    @Test
    void updateUserProfileImage() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "profile.png",
            "image/png",
            "dummy image content".getBytes()
        );
        successAuth();
        willDoNothing().given(userService).updateUserProfileImage(anyLong(), any());

        // when
        final ResultActions result = mockMvc.perform(multipart("/api/v1/users/update-profile-image")
            .file(file)
            .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
            .with(request -> {
                request.setMethod("PATCH");
                request.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
                return request;
            }));

        // then
        result.andExpect(status().isNoContent())
            .andDo(document("update-user-profile-image",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("인증된 사용자의 accessToken"),
                    headerWithName("Content-Type").description("multipart/form-data 요청")
                ),
                requestParts(
                    partWithName("file").description("업로드할 프로필 이미지 파일")
                )
            ));

    }
}
