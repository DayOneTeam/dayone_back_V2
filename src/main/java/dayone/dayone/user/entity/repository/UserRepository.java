package dayone.dayone.user.entity.repository;

import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.dto.UserBookInfo;
import dayone.dayone.user.entity.repository.dto.UserBookLogCountInfo;
import dayone.dayone.user.entity.repository.dto.UserBookLogInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(final String email, final String password);

    @Query("""
        SELECT distinct b.id as id, b.thumbnail as thumbnail, b.title.value as title, MAX(bl.createdAt) as recentlyCreatedAt
        FROM Users u
        JOIN BookLog bl ON bl.user = u
        JOIN Book b ON bl.book = b
        WHERE u.id = :userId
        GROUP BY b.id
        ORDER BY MAX(bl.createdAt) DESC
        """)
    List<UserBookInfo> findUserBookInfo(@Param("userId") final Long userId);

    @Query("""
        SELECT bl.id as id, bl.passage.value as passage, bl.comment.value as comment, bl.createdAt as createdAt
        FROM Users u
        JOIN BookLog bl ON bl.user = u
        WHERE u.id = :userId
        AND bl.book.id = :bookId
        ORDER BY bl.createdAt DESC
        """)
    List<UserBookLogInfo> findUserBookLogInfo(@Param("userId") final Long userId, @Param("bookId") final Long bookId);

    @Query("""
        SELECT u.name as name, COUNT(bl.id) as count
        FROM Users u
        LEFT JOIN BookLog bl ON bl.user = u
        AND bl.createdAt BETWEEN :startDate AND :endDate
        WHERE u.generation = :generation
        GROUP BY u.name
        """)
    List<UserBookLogCountInfo> findByGenerationAndStartDateAndEndDate(@Param("generation") final int generation, @Param("startDate") final LocalDateTime startDate, @Param("endDate") final LocalDateTime endDate);
}
