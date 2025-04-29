package dayone.dayone.user.entity.value;

import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        if (role == null) {
            throw new UserException(UserErrorCode.ROLE_BLANK_AND_NULL);
        }
        return role.name();
    }

    @Override
    public Role convertToEntityAttribute(String roleType) {
        if (roleType == null) {
            throw new UserException(UserErrorCode.ROLE_BLANK_AND_NULL);
        }
        return Role.from(roleType);
    }
}
