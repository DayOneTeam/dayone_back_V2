package dayone.dayone.user.entity.value;

import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @Column(name = "email")
    private String value;

    public Email(final String value) {
        validate(value);
        this.value = value;
    }

    public void validate(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new UserException(UserErrorCode.EMAIL_BLANK_AND_NULL);
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new UserException(UserErrorCode.INVALID_EMAIL_FORM);
        }
    }
}
