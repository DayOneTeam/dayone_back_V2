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
public class Publisher {

    private static final int PUBLISHER_MAX_LENGTH = 100;

    @Column(name = "publisher")
    private String value;

    public Publisher(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new BookException(BookErrorCode.PUBLISHER_BLANK_AND_NULL);
        }

        if (value.length() > PUBLISHER_MAX_LENGTH) {
            throw new BookException(BookErrorCode.PUBLISHER_LENGTH_OVER);
        }
    }
}
