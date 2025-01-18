package dayone.dayone.book.docs;

import dayone.dayone.book.service.dto.BookCreateRequest;
import dayone.dayone.book.service.dto.BookSearchResponse;
import dayone.dayone.support.DocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookDocsTest extends DocsTest {

    @DisplayName("책 검색 요청")
    @Nested
    class SearchBook {
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
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(get("/api/v1/books/search")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .param("name", searchParam));

            // then
            result.andExpect(status().isOk())
                .andDo(document("book-search",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("name").description("검색할 책 제목 ex) 책")
                    ),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
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

        @DisplayName("인증되지 않는 사용자가 책 검색 요청 시 401예외를 발생한다.")
        @Test
        void failSearchBookWithUnAuthenticatedUser() throws Exception {
            // given
            final String searchParam = "책";
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(get("/api/v1/books/search")
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보")
                .param("name", searchParam));

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("fail-search-book",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("비어 있거나 존재하지 않는 User의 Token 정보")
                    ),
                    queryParameters(
                        parameterWithName("name").description("검색할 책 제목 ex) 책")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("실패 코드 ex) 4003"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지 ex) 로그인 되지 않은 유저입니다."),
                        fieldWithPath("data").type(null).description("null")
                    )
                ));
        }
    }

    @DisplayName("책 생성 요청")
    @Nested
    class CreateBook {
        @DisplayName("책을 생성한다.")
        @Test
        void createBook() throws Exception {
            // given
            final BookCreateRequest request = new BookCreateRequest("책 제목", "작가", "출판사", "이미지", "ISBN");
            given(bookService.create(any(BookCreateRequest.class)))
                .willReturn(1L);
            successAuth();

            // when
            final ResultActions result = mockMvc.perform(post("/api/v1/books")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(status().isCreated())
                .andDo(document("book-create",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증된 사용자의 accessToken")
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("책 제목"),
                        fieldWithPath("author").type(JsonFieldType.STRING).description("책 작가"),
                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("책 출판사"),
                        fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("책 표지 이미지"),
                        fieldWithPath("isbn").type(JsonFieldType.STRING).description("책 ISBN")
                    ))
                );
        }

        @DisplayName("인증되지 않은 사용자가 책 생성 요청 시 401예외를 발생한다.")
        @Test
        void failCreateBookWithUnAuthenticatedUser() throws Exception {
            // given
            final BookCreateRequest request = new BookCreateRequest("책 제목", "작가", "출판사", "이미지", "ISBN");
            failAuth();

            // when
            final ResultActions result = mockMvc.perform(post("/api/v1/books")
                .header(HttpHeaders.AUTHORIZATION, "비어있거나 혹은 존재하지 않는 UserToken 정보")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
            );

            // then
            result.andExpect(status().isUnauthorized())
                .andDo(document("fail-create-book",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("비어 있거나 존재하지 않는 User의 Token 정보")
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("책 제목"),
                        fieldWithPath("author").type(JsonFieldType.STRING).description("책 작가"),
                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("책 출판사"),
                        fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("책 표지 이미지"),
                        fieldWithPath("isbn").type(JsonFieldType.STRING).description("책 ISBN")
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
