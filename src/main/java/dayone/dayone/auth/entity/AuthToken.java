package dayone.dayone.auth.entity;

import dayone.dayone.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class AuthToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String refreshToken;

    public AuthToken(final Long id, final Long userId, final String refreshToken) {
        this.id = id;
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static AuthToken forSave(final Long userId, final String refreshToken) {
        return new AuthToken(null, userId, refreshToken);
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
