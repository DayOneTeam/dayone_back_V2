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

public interface BookLogRepository extends JpaRepository<BookLog, Long> {

    Slice<BookLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Slice<BookLog> findAllByIdLessThanOrderByCreatedAtDesc(Long id, Pageable pageable);

    @Query("update BookLog bl set bl.likeCount = bl.likeCount +1 where bl.id = :id")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void plusLike(@Param("id") final Long bookLogId);

    @Query("update BookLog bl set bl.likeCount = bl.likeCount -1 where bl.id = :id")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void minusLike(@Param("id") final Long bookLogId);

    @Query("select bl from BookLog bl where bl.createdAt between :monDay and :sunDay")
    List<BookLog> findAllByCreatedAtBetween(@Param("monDay") final LocalDateTime monDay, @Param("sunDay") final LocalDateTime sunDay);

    @Query("""
        SELECT bl 
        FROM BookLog bl
        JOIN Users u ON bl.user = u
        WHERE u.id = :id
        AND bl.createdAt between :monDay and :sunDay
        """)
    List<BookLog> findAllByUserIdAndCreatedAtBetween(@Param("id") final Long userId, @Param("monDay") final LocalDateTime monDay, @Param("sunDay") final LocalDateTime sunDay);
}
