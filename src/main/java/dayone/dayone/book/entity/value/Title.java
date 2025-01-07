package dayone.dayone.book.entity.value;

import dayone.dayone.book.exception.BookErrorCode;
import dayone.dayone.book.exception.BookException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Title {

    private static final int TITLE_MAX_LENGTH = 100;

    @Column(name = "title")
    private String value;

    public Title(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new BookException(BookErrorCode.TITLE_BLANK_AND_NULL);
        }
        if (value.length() > TITLE_MAX_LENGTH) {
            throw new BookException(BookErrorCode.TITLE_LENGTH_OVER);
        }
    }
}
