package dayone.dayone.booklog.entity.repository;

import dayone.dayone.booklog.entity.BookLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookLogRepository extends JpaRepository<BookLog, Long> {

    @Query("""
        SELECT bl as bookLog, CASE WHEN bll.id IS NOT NULL THEN true ELSE false END AS isLike
        FROM BookLog bl
        JOIN FETCH bl.user
        JOIN FETCH bl.book
        LEFT JOIN BookLogLike bll ON bll.bookLogId = bl.id AND bll.userId = bl.user.id
        WHERE bl.id = :id
        """)
    Optional<BookLogInfoWithIsLike> findByIdWithUserAndBook(Long id);

    @Query("""
        SELECT bl
        FROM BookLog bl
        JOIN FETCH bl.user
        JOIN FETCH bl.book
        ORDER BY bl.createdAt DESC
        """)
    Slice<BookLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("""
        SELECT bl
        FROM BookLog bl
        JOIN FETCH bl.user
        JOIN FETCH bl.book
        WHERE bl.id < :id
        ORDER BY bl.createdAt DESC
        """)
    Slice<BookLog> findAllByIdLessThanOrderByCreatedAtDesc(Long id, Pageable pageable);

    @Query("""
        UPDATE BookLog bl
        SET bl.likeCount = bl.likeCount + 1
        WHERE bl.id = :id
        """)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void plusLike(@Param("id") final Long bookLogId);

    @Query("""
        UPDATE BookLog bl
        SET bl.likeCount = bl.likeCount -1
        WHERE bl.id = :id
        """)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void minusLike(@Param("id") final Long bookLogId);

    @Query("""
        SELECT bl
        FROM BookLog bl
        JOIN FETCH bl.user
        JOIN FETCH bl.book
        WHERE bl.createdAt BETWEEN :monDay AND :sunDay
        """)
    List<BookLog> findAllByCreatedAtBetween(@Param("monDay") final LocalDateTime monDay, @Param("sunDay") final LocalDateTime sunDay);

    @Query("""
        SELECT bl
        FROM BookLog bl
        JOIN FETCH bl.user
        JOIN FETCH bl.book
        WHERE bl.user.id = :id
        AND bl.createdAt between :monDay and :sunDay
        """)
    List<BookLog> findAllByUserIdAndCreatedAtBetween(@Param("id") final Long userId, @Param("monDay") final LocalDateTime monDay, @Param("sunDay") final LocalDateTime sunDay);

    long countByUserId(final Long userId);
}
