package dayone.dayone.booklog.entity.repository;

import dayone.dayone.booklog.entity.BookLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLogRepository extends JpaRepository<BookLog, Long> {

    Slice<BookLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Slice<BookLog> findAllByIdLessThanOrderByCreatedAtDesc(Long id, Pageable pageable);
}
