package dayone.dayone.booklog.entity;

import dayone.dayone.book.entity.Book;
import dayone.dayone.booklog.entity.value.Comment;
import dayone.dayone.booklog.entity.value.Passage;
import dayone.dayone.global.entity.BaseEntity;
import dayone.dayone.user.entity.User;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BookLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Passage passage;

    @Embedded
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    private int likeCount;

    // TODO : 테스트 용 생성자 비즈니스 로직에 같이 있어도 괜찮을까?
    public BookLog(final Long id, final Passage passage, final Comment comment, final Book book, final User user, final int likeCount, final LocalDateTime createdAt) {
        this.id = id;
        this.passage = passage;
        this.comment = comment;
        this.book = book;
        this.user = user;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }

    // TODO : 추후에 user 객체도 고려하기
    public BookLog(final Long id, final Passage passage, final Comment comment, final Book book, final User user) {
        this.id = id;
        this.passage = passage;
        this.comment = comment;
        this.book = book;
        this.user = user;
        this.likeCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static BookLog forSave(final String passage, final String comment, final Book book, final User user) {
        return new BookLog(null, new Passage(passage), new Comment(comment), book, user);
    }

    public String getPassage() {
        return this.passage.getValue();
    }

    public String getComment() {
        return this.comment.getValue();
    }
}
