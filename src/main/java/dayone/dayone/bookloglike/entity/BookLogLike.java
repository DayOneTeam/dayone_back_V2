package dayone.dayone.bookloglike.entity;

import dayone.dayone.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BookLogLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long bookLogId;

    public BookLogLike(final Long id, final Long userId, final Long bookLogId) {
        this.id = id;
        this.userId = userId;
        this.bookLogId = bookLogId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static BookLogLike forSave(final Long userId, final Long bookLogId) {
        return new BookLogLike(null, userId, bookLogId);
    }
}
