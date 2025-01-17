package dayone.dayone.booklog.entity;

import dayone.dayone.booklog.entity.value.Comment;
import dayone.dayone.booklog.entity.value.Passage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class BookLogsTest {

    @DisplayName("좋아요 수가 많은 bookLog 4개를 반환한다.")
    @Test
    void getMostLikedBookLogs() {
        // given
        final BookLog bookLogWithFiveLike = new BookLog(1L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 5, LocalDateTime.now());
        final BookLog bookLogWithFourLike = new BookLog(2L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 4, LocalDateTime.now());
        final BookLog bookLogWithThreeLike = new BookLog(3L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 3, LocalDateTime.now());
        final BookLog bookLogWithOneLike = new BookLog(4L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 1, LocalDateTime.now());
        final BookLog bookLogWithZeroLike = new BookLog(5L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 0, LocalDateTime.now());

        final BookLogs bookLogs = new BookLogs(List.of(bookLogWithFiveLike, bookLogWithFourLike, bookLogWithThreeLike, bookLogWithOneLike, bookLogWithZeroLike));

        // when
        final List<BookLog> result = bookLogs.getMostLikedBookLogs();

        // then
        final List<BookLog> expected = List.of(bookLogWithFiveLike, bookLogWithFourLike, bookLogWithThreeLike, bookLogWithOneLike);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("좋아요 수가 같은 BookLog들의 경우 최근에 작성된 순으로 반환한다.")
    @Test
    void getMostLikedAndWrittenRecentlyBookLogs() {
        // given
        final BookLog bookLogWithFiveLikeCreatedAtFirst = new BookLog(1L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 5, LocalDateTime.now());
        final BookLog bookLogWithFiveLikeCreatedAtSecond = new BookLog(2L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 5, LocalDateTime.now());
        final BookLog bookLogWithFiveLikeCreatedAtThird = new BookLog(3L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 5, LocalDateTime.now());
        final BookLog bookLogWithFiveLikeCreatedAtFourth = new BookLog(4L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 5, LocalDateTime.now());
        final BookLog bookLogWithFiveLikeCreatedAtFifth = new BookLog(5L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 5, LocalDateTime.now());

        final BookLogs bookLogs = new BookLogs(List.of(bookLogWithFiveLikeCreatedAtFirst, bookLogWithFiveLikeCreatedAtSecond, bookLogWithFiveLikeCreatedAtThird, bookLogWithFiveLikeCreatedAtFourth, bookLogWithFiveLikeCreatedAtFifth));

        // when
        final List<BookLog> result = bookLogs.getMostLikedBookLogs();

        // then
        final List<BookLog> expected = List.of(bookLogWithFiveLikeCreatedAtFifth, bookLogWithFiveLikeCreatedAtFourth, bookLogWithFiveLikeCreatedAtThird, bookLogWithFiveLikeCreatedAtSecond);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("이번주에 작성된 bookLog들의 개수가 Top Display Count보다 적더라도 좋아요 순과 최근에 작성된 순으로 반환한다.")
    @Test
    void getMostLikedAndWrittenRecentlyBookLogsWithLessThanTopDisplayCount() {
        // given
        final BookLog bookLogWithFiveLike = new BookLog(1L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 5, LocalDateTime.now());
        final BookLog bookLogWithOneLike = new BookLog(1L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, 1L, 1, LocalDateTime.now());

        final BookLogs bookLogs = new BookLogs(List.of(bookLogWithFiveLike, bookLogWithOneLike));

        // when
        final List<BookLog> result = bookLogs.getMostLikedBookLogs();

        // then
        final List<BookLog> expected = List.of(bookLogWithFiveLike, bookLogWithOneLike);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }
}
