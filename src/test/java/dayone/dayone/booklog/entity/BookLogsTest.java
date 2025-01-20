package dayone.dayone.booklog.entity;

import dayone.dayone.booklog.entity.value.Comment;
import dayone.dayone.booklog.entity.value.Passage;
import dayone.dayone.support.DateConstant;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class BookLogsTest {

    @DisplayName("좋아요 수가 많은 bookLog 4개를 반환한다.")
    @Test
    void getMostLikedBookLogs() {
        // given
        final BookLog bookLogWithFiveLike = new BookLog(1L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 5, LocalDateTime.now());
        final BookLog bookLogWithFourLike = new BookLog(2L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 4, LocalDateTime.now());
        final BookLog bookLogWithThreeLike = new BookLog(3L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 3, LocalDateTime.now());
        final BookLog bookLogWithOneLike = new BookLog(4L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 1, LocalDateTime.now());
        final BookLog bookLogWithZeroLike = new BookLog(5L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 0, LocalDateTime.now());

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
        final BookLog bookLogWithFiveLikeCreatedAtFirst = new BookLog(1L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 5, LocalDateTime.now());
        final BookLog bookLogWithFiveLikeCreatedAtSecond = new BookLog(2L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 5, LocalDateTime.now());
        final BookLog bookLogWithFiveLikeCreatedAtThird = new BookLog(3L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 5, LocalDateTime.now());
        final BookLog bookLogWithFiveLikeCreatedAtFourth = new BookLog(4L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 5, LocalDateTime.now());
        final BookLog bookLogWithFiveLikeCreatedAtFifth = new BookLog(5L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 5, LocalDateTime.now());

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
        final BookLog bookLogWithFiveLike = new BookLog(1L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 5, LocalDateTime.now());
        final BookLog bookLogWithOneLike = new BookLog(2L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 1, LocalDateTime.now());

        final BookLogs bookLogs = new BookLogs(List.of(bookLogWithFiveLike, bookLogWithOneLike));

        // when
        final List<BookLog> result = bookLogs.getMostLikedBookLogs();

        // then
        final List<BookLog> expected = List.of(bookLogWithFiveLike, bookLogWithOneLike);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("BookLog의 생성 날짜 정보를 통해서 작성한 날을 활성화한 정보를 조회한다.")
    @Test
    void getActiveDateOnWriteBookLogInWeek() {
        // given
        final LocalDateTime tuesDay = DateConstant.THIS_MONDAY.plusDays(1);
        final LocalDateTime friDay = DateConstant.THIS_MONDAY.plusDays(4);
        final BookLog bookLogWriteTuesDay = new BookLog(1L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 5, tuesDay);
        final BookLog bookLogWriteFriDay = new BookLog(1L, new Passage("의미있는 구절"), new Comment("내가 느낀 감정"), null, null, 5, friDay);

        BookLogs bookLogs = new BookLogs(List.of(bookLogWriteTuesDay, bookLogWriteFriDay));

        // when
        final boolean[] result = bookLogs.getBookLogWriteActive();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result[0]).isFalse();
            softAssertions.assertThat(result[1]).isTrue();
            softAssertions.assertThat(result[2]).isFalse();
            softAssertions.assertThat(result[3]).isFalse();
            softAssertions.assertThat(result[4]).isTrue();
            softAssertions.assertThat(result[5]).isFalse();
            softAssertions.assertThat(result[6]).isFalse();
        });
    }
}
