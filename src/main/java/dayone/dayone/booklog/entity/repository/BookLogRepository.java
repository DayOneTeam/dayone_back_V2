package dayone.dayone.booklog.entity.repository;

import dayone.dayone.booklog.entity.BookLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookLogRepository extends JpaRepository<BookLog, Long> {

    Slice<BookLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Slice<BookLog> findAllByIdLessThanOrderByCreatedAtDesc(Long id, Pageable pageable);

    @Query("update BookLog bl set bl.likeCount = bl.likeCount +1 where bl.id = :id")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void plusLike(@Param("id") final Long bookLogId);
}
