package dayone.dayone.book.service;

import dayone.dayone.book.service.dto.BookSearchListResponse;
import dayone.dayone.book.service.dto.BookSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class NaverBookSearch implements ExternalBookSearch {

    private static final int RESULT_DATA_COUNT = 10;

    @Value("${naver.book-search-url}")
    private String bookSearchUrl;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    private final RestClient restClient;

    @Override
    public List<BookSearchResponse> searchBooks(final String name) {
        String url = String.format("%s?query=%s&display=%d", bookSearchUrl, name, RESULT_DATA_COUNT);

        // TODO: 요청이 실패한 경우에 대해서는 어떻게 처리할지 고민하기
        final BookSearchListResponse response = restClient.get()
            .uri(url)
            .header("X-Naver-Client-Id", clientId)
            .header("X-Naver-Client-Secret", clientSecret)
            .retrieve()
            .body(BookSearchListResponse.class);

        if (response == null || response.items() == null) {
            return Collections.emptyList();
        }

        return response.items();
    }
}
