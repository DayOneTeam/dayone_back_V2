package dayone.dayone.book.service;

import dayone.dayone.book.service.dto.BookSearchListResponse;
import dayone.dayone.book.service.dto.BookSearchResponse;
import dayone.dayone.global.exception.restclient.RestClientErrorCode;
import dayone.dayone.global.exception.restclient.RestClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
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

        try {
            final BookSearchListResponse response = restClient.get()
                .uri(url)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .body(BookSearchListResponse.class);

            if (response == null || response.isEmpty()) {
                return Collections.emptyList();
            }

            return response.items();
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                throw new RestClientException(RestClientErrorCode.READ_TIMEOUT);
            } else if (e.getCause() instanceof ConnectException) {
                throw new RestClientException(RestClientErrorCode.CONNECTION_TIMEOUT);
            } else {
                throw new RestClientException(RestClientErrorCode.UNKNOWN_ERROR);
            }
        }
    }
}
