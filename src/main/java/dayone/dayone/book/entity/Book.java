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
}
