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
public class Passage {

    private static final int PASSAGE_MAX_LENGTH = 1000;

    @Column(name = "passage", columnDefinition = "TEXT")
    private String value;

    public Passage(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            // TODO: 추후에 커스텀 예외 처리로 변경하기
            throw new IllegalArgumentException("구절은 비어있어나 null 일 수 없습니다.");
        }

        if (value.length() > PASSAGE_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("구절은 %d자를 넘을 수 없습니다.", PASSAGE_MAX_LENGTH));
        }
    }
}
