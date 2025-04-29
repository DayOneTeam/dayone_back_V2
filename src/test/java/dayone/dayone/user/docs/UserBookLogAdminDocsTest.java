package dayone.dayone.user.docs;

import dayone.dayone.support.DocsTest;
import dayone.dayone.user.service.dto.UserBookLogCountInWeekResponse;
import dayone.dayone.user.service.dto.UserBookLogCountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserBookLogAdminDocsTest extends DocsTest {

    @DisplayName("특정 기수 유저들이 작성한 bookLog 개수를 조회한다.")
    @Test
    void readUserBookLogCountInWeek() throws Exception {
        // given
        final UserBookLogCountInWeekResponse response = new UserBookLogCountInWeekResponse(List.of(new UserBookLogCountResponse("회원1", 4), new UserBookLogCountResponse("회원2", 2)));

        given(userBookService.getUserBookLogCountInWeek(anyInt(), anyString(), anyString()))
            .willReturn(response);
        successAuthAdmin();

        // when
        final ResultActions result = mockMvc.perform(get("/api/v1/admin/users/book-logs/count?generation=1&startDate=2025-01-01&endDate=2025-12-31")
            .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"));

        // then
        result.andExpect(status().isOk())
            .andDo(document("read-user-book-log-count-in-week",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 admin의 accessToken")
                ),
                queryParameters(
                    parameterWithName("generation").description("특정 기수"),
                    parameterWithName("startDate").description("시작 날짜"),
                    parameterWithName("endDate").description("종료 날짜")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("성공 코드 ex) 1"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("성공 메시지 ex) 특정 기수의 특정 기간의 bookLog 작성 횟수 조회"),
                    fieldWithPath("data.user_book_log_counts[].username").type(JsonFieldType.STRING).description("회원 이름"),
                    fieldWithPath("data.user_book_log_counts[].count").type(JsonFieldType.NUMBER).description("작성한 bookLog 개수")
                )
            ));
    }
}
