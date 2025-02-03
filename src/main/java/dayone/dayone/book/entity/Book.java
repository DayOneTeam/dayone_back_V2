package dayone.dayone.book.entity;

import dayone.dayone.book.entity.value.Author;
import dayone.dayone.book.entity.value.Publisher;
import dayone.dayone.book.entity.value.Title;
import dayone.dayone.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Title title;

    private Author author;

    private Publisher publisher;

    private String thumbnail;

    private String isbn;

    public Book(final Long id, final Title title, final Author author, final Publisher publisher, final String thumbnail, final String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
        this.isbn = isbn;
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        this.updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

    public static Book forSave(final String title, final String author, final String publisher, final String thumbnail, final String isbn) {
        return new Book(null, new Title(title), new Author(author), new Publisher(publisher), thumbnail, isbn);
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getAuthor() {
        return author.getValue();
    }

    public String getPublisher() {
        return publisher.getValue();
    }
}
