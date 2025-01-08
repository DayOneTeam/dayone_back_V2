package dayone.dayone.book.entity.repository;

import dayone.dayone.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
