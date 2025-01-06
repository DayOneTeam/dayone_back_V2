package dayone.dayone.booklog.entity.value;

import dayone.dayone.booklog.exception.BookLogErrorCode;
import dayone.dayone.booklog.exception.BookLogException;
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
            throw new BookLogException(BookLogErrorCode.PASSAGE_BLANK_AND_NULL);
        }

        if (value.length() > PASSAGE_MAX_LENGTH) {
            throw new BookLogException(BookLogErrorCode.PASSAGE_LENGTH_OVER);
        }
    }
}
