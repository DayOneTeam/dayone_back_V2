package dayone.dayone.booklog.entity;

import dayone.dayone.booklog.entity.value.Comment;
import dayone.dayone.booklog.entity.value.Passage;
import dayone.dayone.global.entity.BaseEntity;
import jakarta.persistence.Embedded;
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
public class BookLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Passage passage;

    @Embedded
    private Comment comment;

    // TODO: 추후에 BOOK 객체를 참조하는 것 고려하기
    private Long bookId;

    // TODO : 추후에 USER 객체를 참조하는 것 고려하기
    private Long userId;

    private int likeCount;
}
