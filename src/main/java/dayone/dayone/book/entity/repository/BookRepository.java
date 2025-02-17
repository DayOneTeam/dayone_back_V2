package dayone.dayone.book.entity.repository;

import dayone.dayone.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(final String isbn);
}
