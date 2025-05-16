package dayone.dayone.bookloglike.entity.repository;

import dayone.dayone.booklog.entity.BookLog;
import dayone.dayone.bookloglike.entity.BookLogLike;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookLogLikeRepository extends JpaRepository<BookLogLike, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT bl FROM BookLogLike bl WHERE bl.userId = :userId AND bl.bookLogId = :bookLogId")
    Optional<BookLogLike> findBookLogLikeByUserIdAndBookLogIdForUpdate(final @Param("userId") Long userId, final @Param("bookLogId") Long bookLogId);

    List<BookLogLike> findAllByBookLogId(final Long bookLogId);
}
