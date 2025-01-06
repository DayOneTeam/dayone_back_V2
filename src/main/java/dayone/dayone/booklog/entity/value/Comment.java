package dayone.dayone.booklog.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Comment {

    private static final int COMMENT_MAX_LENGTH = 5000;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String value;

    public Comment(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            // TODO: 추후에 커스텀 예외 처리로 변경하기
            throw new IllegalArgumentException("자신의 생각은 비어있어나 null 일 수 없습니다.");
        }
        if (value.length() > COMMENT_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("책에 대한 자신의 생각은 %d을 넘길 수 없습니다.", COMMENT_MAX_LENGTH));
        }
    }
}
