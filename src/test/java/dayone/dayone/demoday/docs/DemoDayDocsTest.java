package dayone.dayone.demoday.docs;

import dayone.dayone.demoday.service.dto.DemoDayCreateRequest;
import dayone.dayone.support.DocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DemoDayDocsTest extends DocsTest {

    @DisplayName("데모데이 생성 요청")
    @Nested
    class Create {
        @DisplayName("데모데이를 생성한다.")
        @Test
        void createDemoDay() throws Exception {
            // given
            final DemoDayCreateRequest request = new DemoDayCreateRequest("title", "description", "thumbnail", LocalDate.now(), LocalTime.now(), 1, "location");
            given(demoDayService.create(anyLong(), any(DemoDayCreateRequest.class)))
                .willReturn(1L);
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(post("/api/v1/demo-days")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON));

            // then
            result.andExpect(status().isCreated())
                .andDo(document("demo-day-create",
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("데모데이 제목"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("데모데이 설명"),
                        fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("데모데이 표지 이미지"),
                        fieldWithPath("demoDate").type(JsonFieldType.STRING).description("데모데이 진행 날짜"),
                        fieldWithPath("demoTime").type(JsonFieldType.STRING).description("데모데이 시작 시간"),
                        fieldWithPath("capacity").type(JsonFieldType.NUMBER).description("데모데이 참여 인원"),
                        fieldWithPath("location").type(JsonFieldType.STRING).description("데모데이 장소")
                    )
                ));
        }

        @DisplayName("인증되지 않은 유저가 데모데이 생성 요청 시 401 예외를 발생시킨다.")
        @Test
        void createDemoDayWithUnAuthenticatedUser() throws Exception {
            // given
            final DemoDayCreateRequest request = new DemoDayCreateRequest("title", "description", "thumbnail", LocalDate.now(), LocalTime.now(), 1, "location");
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(post("/api/v1/demo-days")
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("fail-create-demo-day-with-unauthenticated-user",
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("비어 있거나 존재하지 않는 User의 Token 정보")
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("데모데이 제목"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("데모데이 설명"),
                        fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("데모데이 표지 이미지"),
                        fieldWithPath("demoDate").type(JsonFieldType.STRING).description("데모데이 진행 날짜"),
                        fieldWithPath("demoTime").type(JsonFieldType.STRING).description("데모데이 시작 시간"),
                        fieldWithPath("capacity").type(JsonFieldType.NUMBER).description("데모데이 참여 인원"),
                        fieldWithPath("location").type(JsonFieldType.STRING).description("데모데이 장소")
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
