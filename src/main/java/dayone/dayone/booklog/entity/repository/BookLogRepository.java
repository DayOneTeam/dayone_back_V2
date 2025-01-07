package dayone.dayone.booklog.entity.repository;

import dayone.dayone.booklog.entity.BookLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLogRepository extends JpaRepository<BookLog, Long> {
}
