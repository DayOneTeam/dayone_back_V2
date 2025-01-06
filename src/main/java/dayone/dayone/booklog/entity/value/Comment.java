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
            throw new BookLogException(BookLogErrorCode.COMMENT_BLANK_AND_NULL);
        }
        if (value.length() > COMMENT_MAX_LENGTH) {
            throw new BookLogException(BookLogErrorCode.COMMENT_LENGTH_OVER);
        }
    }
}
