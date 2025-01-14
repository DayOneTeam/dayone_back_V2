package dayone.dayone.bookloglike.entity.repository;

import dayone.dayone.bookloglike.entity.BookLogLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookLogLikeRepository extends JpaRepository<BookLogLike, Long> {

    Optional<BookLogLike> findAllByUserIdAndBookLogId(final Long userId, final Long bookLogId);

    List<BookLogLike> findAllByBookLogId(final Long bookLogId);
}
