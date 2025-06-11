package dayone.dayone.bookloglike.service;

import dayone.dayone.book.entity.Book;
import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.booklog.entity.repository.BookLogRepository;
import dayone.dayone.booklog.exception.BookLogErrorCode;
import dayone.dayone.booklog.exception.BookLogException;
import dayone.dayone.bookloglike.entity.BookLogLike;
import dayone.dayone.bookloglike.entity.repository.BookLogLikeRepository;
import dayone.dayone.bookloglike.exception.BookLogLikeErrorCode;
import dayone.dayone.bookloglike.exception.BookLogLikeException;
import dayone.dayone.fixture.TestBookFactory;
import dayone.dayone.fixture.TestBookLogFactory;
import dayone.dayone.fixture.TestBookLogLikeFactory;
import dayone.dayone.fixture.TestUserFactory;
import dayone.dayone.support.ServiceTest;
import dayone.dayone.user.entity.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookLogLikeServiceTest extends ServiceTest {

    @Autowired
    private TestBookFactory testBookFactory;

    @Autowired
    private TestBookLogFactory testBookLogFactory;

    @Autowired
    private TestUserFactory testUserFactory;

    @Autowired
    private TestBookLogLikeFactory testBookLogLikeFactory;

    @Autowired
    private BookLogRepository bookLogRepository;

    @Autowired
    private BookLogLikeRepository bookLogLikeRepository;

    @Autowired
    private BookLogLikeService bookLogLikeService;

    @DisplayName("BookLog 좋아요 추가")
    @Nested
    class AddLikeOnBookLog {

        @DisplayName("bookLog에 좋아요를 추가한다.")
        @Test
        void addLikeOnBookLog() {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름", 1);
            final BookLog bookLog = testBookLogFactory.createBookLog(book, user);

            // when
            bookLogLikeService.addLike(bookLog.getId(), 1L);

            // then
            final BookLog likedBookLog = bookLogRepository.findById(bookLog.getId()).get();
            final List<BookLogLike> bookLogLikeByBookLogId = bookLogLikeRepository.findAllByBookLogId(bookLog.getId());

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(likedBookLog.getLikeCount()).isEqualTo(1);
                softAssertions.assertThat(bookLogLikeByBookLogId).hasSize(1);
            });
        }

        @DisplayName("존재하지 않는 BookLog에 좋아요를 요청하면 예외를 발생한다.")
        @Test
        void addLikeOnNotExistBookLog() {
            // given
            final Long notExistBookLogId = 99999L;
            // when
            // then
            assertThatThrownBy(() -> bookLogLikeService.addLike(notExistBookLogId, 1L))
                .isInstanceOf(BookLogException.class)
                .hasMessage(BookLogErrorCode.NOT_EXIST_BOOK_LOG.getMessage());
        }

        @DisplayName("이미 좋아요를 누른 BookLog에 또 좋아요를 요청하면 예외가 발생한다.")
        @Test
        void addLikeOnBookLogThatIsAlreadyLiked() {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름", 1);
            final BookLog bookLog = testBookLogFactory.createBookLog(book, user);
            bookLogLikeService.addLike(bookLog.getId(), 1L);

            // when
            // then
            assertThatThrownBy(() -> bookLogLikeService.addLike(bookLog.getId(), 1L))
                .isInstanceOf(BookLogLikeException.class)
                .hasMessage(BookLogLikeErrorCode.ALREADY_LIKE_BOOK_LOG.getMessage());
        }

        @DisplayName("BookLog에 10개의 좋아요 요청이 동시다발적으로 발생했을 때 올바르게 동작한다.")
        @Test
        void addLikeOnBookLogWithManyUserSimultaneously() throws InterruptedException {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final List<User> users = testUserFactory.createNUser(10, "test@test.com", "password", "이름", 1);
            final BookLog bookLog = testBookLogFactory.createBookLog(book, users.get(0));

            final int threadCount = 10;
            final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

            // when
            for (int i = 0; i < threadCount; i++) {
                final User user = users.get(i);
                executorService.submit(() -> {
                    bookLogLikeService.addLike(bookLog.getId(), user.getId());
                    countDownLatch.countDown();
                });
            }

            countDownLatch.await();

            // then
            final BookLog likedBookLog = bookLogRepository.findById(bookLog.getId()).get();
            final List<BookLogLike> bookLogLikeByBookLogId = bookLogLikeRepository.findAllByBookLogId(bookLog.getId());
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(likedBookLog.getLikeCount()).isEqualTo(10);
                softAssertions.assertThat(bookLogLikeByBookLogId).hasSize(10);
            });
        }

        @DisplayName("같은 유저가 같은 BookLog에 좋아요를 동시에 2번 추가할 경우 1번만 추가된다.")
        @Test
        void addLikeOnBookLogWithConcurrentSameUser() throws InterruptedException {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름", 1);
            final BookLog bookLog = testBookLogFactory.createBookLog(book, user);

            final User anotherUser = testUserFactory.createUser("test2@test.com", "password", "이름", 1);

            final int threadCount = 2;
            final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

            // when
            final AtomicInteger errorCount = new AtomicInteger(0);
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        bookLogLikeService.addLike(bookLog.getId(), anotherUser.getId());
                    } catch (Exception ignored) {
                        errorCount.incrementAndGet();
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }
            countDownLatch.await();

            // then
            final BookLog likedBookLog = bookLogRepository.findById(bookLog.getId()).get();
            final List<BookLogLike> bookLogLikeByBookLogId = bookLogLikeRepository.findAllByBookLogId(bookLog.getId());
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(likedBookLog.getLikeCount()).isEqualTo(1);
                softAssertions.assertThat(bookLogLikeByBookLogId).hasSize(1);
                softAssertions.assertThat(errorCount.get()).isEqualTo(threadCount - 1);
            });
        }
    }

    @DisplayName("BookLog 좋아요 취소")
    @Nested
    class DeleteLikeOnBookLog {
        @DisplayName("BookLog에 좋아요를 삭제한다.")
        @Test
        void deleteLikeOnBookLog() {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름", 1);
            final BookLog bookLog = testBookLogFactory.createBookLog(book, user);
            final long userId = 1L;
            bookLogLikeService.addLike(bookLog.getId(), userId);

            // when
            bookLogLikeService.deleteLike(bookLog.getId(), userId);


            // then
            final BookLog notLikedBookLog = bookLogRepository.findById(bookLog.getId()).get();
            final List<BookLogLike> bookLogLikeByBookLogId = bookLogLikeRepository.findAllByBookLogId(bookLog.getId());

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(notLikedBookLog.getLikeCount()).isEqualTo(0);
                softAssertions.assertThat(bookLogLikeByBookLogId).hasSize(0);
            });
        }

        @DisplayName("존재하지 않는 BookLog에 좋아요 취소를 요청하면 예외를 발생한다.")
        @Test
        void deleteLikeOnNotExistBookLog() {
            // given
            final long notExistBookLogId = 99999L;
            final long userId = 1L;

            // when
            // then
            assertThatThrownBy(() -> bookLogLikeService.deleteLike(notExistBookLogId, userId))
                .isInstanceOf(BookLogException.class)
                .hasMessage(BookLogErrorCode.NOT_EXIST_BOOK_LOG.getMessage());
        }

        @DisplayName("좋아요를 누르지 않은 BookLog에 좋아요 취소를 요청하면 예외를 발생한다.")
        @Test
        void deleteLikeOnBookLogThatIsNotLiked() {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름", 1);
            final BookLog bookLog = testBookLogFactory.createBookLog(book, user);
            final long userId = 1L;

            // when
            // then
            assertThatThrownBy(() -> bookLogLikeService.deleteLike(bookLog.getId(), userId))
                .isInstanceOf(BookLogLikeException.class)
                .hasMessage(BookLogLikeErrorCode.NOT_LIKE_BOOK_LOG.getMessage());
        }

        @DisplayName("BookLog에 10개의 좋아요 취소 요청이 동시다발적으로 발생했을 때 올바르게 동작한다.")
        @Test
        void deleteLikeOnBookLogWithManyUserSimultaneously() throws InterruptedException {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final List<User> users = testUserFactory.createNUser(10, "test@test.com", "password", "이름", 1);
            final BookLog bookLog = testBookLogFactory.createBookLog(book, users.get(0));
            testBookLogLikeFactory.createNBookLogLike(bookLog.getId(), users);

            final int threadCount = 10;
            final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

            // when
            for (int i = 0; i < threadCount; i++) {
                User user = users.get(i);
                executorService.submit(() -> {
                    bookLogLikeService.deleteLike(bookLog.getId(), user.getId());
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
            // then
            final BookLog notLikedBookLog = bookLogRepository.findById(bookLog.getId()).get();
            final List<BookLogLike> bookLogLikeByBookLogId = bookLogLikeRepository.findAllByBookLogId(bookLog.getId());
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(notLikedBookLog.getLikeCount()).isEqualTo(0);
                softAssertions.assertThat(bookLogLikeByBookLogId).hasSize(0);
            });
        }

        @DisplayName("같은 유저가 BookLog에 좋아요 취소를 동시에 두번 하더라도 1번만 취소 처리된다.")
        @Test
        void deleteLikeOnBookLogWithConcurrentSameUser() throws InterruptedException {
            // given
            final Book book = testBookFactory.createBook("책", "작가", "출판사");
            final User user = testUserFactory.createUser("test@test.com", "password", "이름", 1);
            final BookLog bookLog = testBookLogFactory.createBookLog(book, user);

            final User anotherUser = testUserFactory.createUser("test2@test.com", "password", "이름", 1);
            testBookLogLikeFactory.createNBookLogLike(bookLog.getId(), List.of(anotherUser));

            final int threadCount = 2;
            final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

            // when
            final AtomicInteger errorCount = new AtomicInteger(0);
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        bookLogLikeService.deleteLike(bookLog.getId(), anotherUser.getId());
                    } catch (Exception ignored) {
                        errorCount.incrementAndGet();
                    } finally {
                        countDownLatch.countDown();
                    }

                });
            }
            countDownLatch.await();

            // then
            final BookLog notLikedBookLog = bookLogRepository.findById(bookLog.getId()).get();
            final List<BookLogLike> bookLogLikeByBookLogId = bookLogLikeRepository.findAllByBookLogId(bookLog.getId());
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(notLikedBookLog.getLikeCount()).isEqualTo(0);
                softAssertions.assertThat(bookLogLikeByBookLogId).hasSize(0);
                softAssertions.assertThat(errorCount.get()).isEqualTo(threadCount - 1);
            });
        }
    }
}
