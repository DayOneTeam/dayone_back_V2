package dayone.dayone.book.service;

import dayone.dayone.book.service.dto.BookSearchResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
class BookServiceTest {

    @MockBean
    private ExternalBookSearch externalBookSearch;

    @Autowired
    private BookService bookService;

    @DisplayName("책 검색에 대한 결과를 반환한다.")
    @Test
    void getBookSearchResult() {
        // given
        final String searchInput = "책 이름";
        final List<BookSearchResponse> searchResult = List.of(new BookSearchResponse("책 제목",
            "이미지",
            "작가",
            "출판사",
            "발행일",
            "ISBN"));
        given(externalBookSearch.searchBooks(searchInput))
            .willReturn(searchResult);

        // when
        final List<BookSearchResponse> result = bookService.searchBooks(searchInput);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0).title()).isEqualTo(searchResult.get(0).title());
            softly.assertThat(result.get(0).image()).isEqualTo(searchResult.get(0).image());
            softly.assertThat(result.get(0).author()).isEqualTo(searchResult.get(0).author());
            softly.assertThat(result.get(0).publisher()).isEqualTo(searchResult.get(0).publisher());
            softly.assertThat(result.get(0).pubdate()).isEqualTo(searchResult.get(0).pubdate());
            softly.assertThat(result.get(0).isbn()).isEqualTo(searchResult.get(0).isbn());
        });
    }
}
