package dayone.dayone.book.service;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.book.service.dto.BookCreateRequest;
import dayone.dayone.book.service.dto.BookSearchResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
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
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @AfterEach
    void cleanUp() {
        bookRepository.deleteAll();
    }

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

    @DisplayName("책을 생성한다.")
    @Test
    void createBook() {
        // given
        final BookCreateRequest bookCreateRequest = new BookCreateRequest("제목", "저자", "판매처", "이미지", "ISBN");

        // when
        final Long savedBookId = bookService.create(bookCreateRequest);

        // then
        final Book book = bookRepository.findById(savedBookId).get();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(book.getTitle()).isEqualTo(bookCreateRequest.title());
            softly.assertThat(book.getAuthor()).isEqualTo(bookCreateRequest.author());
            softly.assertThat(book.getPublisher()).isEqualTo(bookCreateRequest.publisher());
            softly.assertThat(book.getThumbnail()).isEqualTo(bookCreateRequest.thumbnail());
            softly.assertThat(book.getIsbn()).isEqualTo(bookCreateRequest.isbn());
        });
    }
}
