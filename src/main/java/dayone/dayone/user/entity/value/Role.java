package dayone.dayone.user.entity.value;

import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;

import java.util.Arrays;

public enum Role {

    MEMBER,
    ADMIN;

    public static Role from(final String input) {
        return Arrays.stream(values())
            .filter(role -> role.name().equals(input))
            .findFirst()
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_ROLE));
    }
}
