package dayone.dayone.bookloglike.docs;

import dayone.dayone.support.DocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookLogLikeDocsTest extends DocsTest {


    @DisplayName("bookLog에 like을 추가한다.")
    @Test
    void addLikeOnBookLog() throws Exception {
        // given
        willDoNothing().given(bookLogLikeService).addLike(anyLong(), anyLong());

        // when
        final ResultActions result = mockMvc.perform(post("/api/v1/book-logs/{book_log_id}/like", 1L));

        // then
        result.andExpect(status().isCreated())
            .andDo(document("book-log-like-create-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("book_log_id").description("좋아요를 추가할 bookLog의 id")
                ))
            );
    }
}
