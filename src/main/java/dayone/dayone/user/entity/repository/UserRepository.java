package dayone.dayone.user.entity.repository;

import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.dto.UserBookInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(final String email, final String password);

    @Query("""
        SELECT distinct b.id as id, b.thumbnail as thumbnail, MAX(bl.createdAt) as recentlyCreatedAt
        FROM Users u
        JOIN BookLog bl ON bl.user = u
        JOIN Book b ON bl.book = b
        WHERE u.id = :userId
        GROUP BY b.id
        ORDER BY MAX(bl.createdAt) DESC
        """)
    List<UserBookInfo> findUserBookInfo(@Param("userId") final Long userId);
}
