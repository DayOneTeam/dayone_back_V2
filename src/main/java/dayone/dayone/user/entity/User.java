package dayone.dayone.user.entity;

import dayone.dayone.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "Users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String profileImage;

    public User(final Long id, final String email, final String password, final String name, final String profileImage) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage;
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        this.updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

    // TODO : 이미지는 추후에 처리하기
    public static User forSave(final String email, final String password, final String name) {
        return new User(null, email, password, name, "기본 이미지");
    }
}
