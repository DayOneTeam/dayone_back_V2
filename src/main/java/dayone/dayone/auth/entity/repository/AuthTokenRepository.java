package dayone.dayone.auth.entity.repository;

import dayone.dayone.auth.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findByUserIdAndRefreshToken(final Long userId, final String refreshToken);
}
