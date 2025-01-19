package dayone.dayone.user.entity.repository;

import dayone.dayone.book.entity.Book;
import dayone.dayone.book.entity.repository.BookRepository;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.booklog.entity.repository.BookLogRepository;
import dayone.dayone.support.RepositoryTest;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.dto.UserBookInfo;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


class UserRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookLogRepository bookLogRepository;

    @DisplayName("유저가 bookLog를 작성한 책을 불러온다.")
    @Test
    void getUserBookInfo() {
        // given
        final User user = userRepository.save(User.forSave("test@test.com", "password", "이름"));
        final List<Book> books = createNBook(10);
        final BookLog bookLogWrittenByUser1 = bookLogRepository.save(BookLog.forSave("의미있는 구절", "내가 느낀 감정", books.get(0), user));
        final BookLog bookLogWrittenByUser2 = bookLogRepository.save(BookLog.forSave("의미있는 구절", "내가 느낀 감정", books.get(1), user));
        final BookLog bookLogWrittenByUser3 = bookLogRepository.save(BookLog.forSave("의미있는 구절", "내가 느낀 감정", books.get(1), user));

        // when
        final List<UserBookInfo> userBookInfos = userRepository.findUserBookInfo(user.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(userBookInfos).hasSize(2);
            softAssertions.assertThat(userBookInfos.get(0).getId()).isEqualTo(bookLogWrittenByUser3.getBook().getId());
            softAssertions.assertThat(userBookInfos.get(0).getThumbnail()).isEqualTo(bookLogWrittenByUser3.getBook().getThumbnail());
            softAssertions.assertThat(userBookInfos.get(1).getId()).isEqualTo(bookLogWrittenByUser1.getBook().getId());
            softAssertions.assertThat(userBookInfos.get(1).getThumbnail()).isEqualTo(bookLogWrittenByUser1.getBook().getThumbnail());
        });

    }

    private List<Book> createNBook(final int cnt) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            books.add(Book.forSave("책" + i, "작가" + i, "출판사" + i, "이미지" + i, "ISBN" + i));
        }
        bookRepository.saveAll(books);
        return books;
    }
}
