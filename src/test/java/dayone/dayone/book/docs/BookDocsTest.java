package dayone.dayone.book.docs;

import dayone.dayone.book.service.dto.BookSearchResponse;
import dayone.dayone.support.DocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
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

public class BookDocsTest extends DocsTest {

    @DisplayName("책 제목을 통해 검색을 시도하면 검색 결과를 반환해준다.")
    @Test
    void bookSearch() throws Exception {
        // given
        final String searchParam = "책";
        given(bookService.searchBooks(searchParam))
            .willReturn(List.of(new BookSearchResponse("책 제목",
                "이미지",
                "작가",
                "출판사",
                "발행일",
                "ISBN")));

        // when
        final ResultActions result = mockMvc.perform(get("/api/v1/books/search")
            .param("name", searchParam));

        // then
        result.andExpect(status().isOk())
            .andDo(document("book-search",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("name").description("검색할 책 제목 ex) 책")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("성공 코드 ex) 1"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지 ex)책 검색 응답"),
                    fieldWithPath("data[].title").type(JsonFieldType.STRING).description("책 제목"),
                    fieldWithPath("data[].image").type(JsonFieldType.STRING).description("책 표지 이미지"),
                    fieldWithPath("data[].author").type(JsonFieldType.STRING).description("책 작가"),
                    fieldWithPath("data[].publisher").type(JsonFieldType.STRING).description("책 출판사"),
                    fieldWithPath("data[].pubdate").type(JsonFieldType.STRING).description("책 발행일"),
                    fieldWithPath("data[].isbn").type(JsonFieldType.STRING).description("책 ISBN")

                )));
    }
}
