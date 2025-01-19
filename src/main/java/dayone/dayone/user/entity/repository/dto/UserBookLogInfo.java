package dayone.dayone.user.entity.repository.dto;

import java.time.LocalDateTime;

public interface UserBookLogInfo {
    Long getId();

    String getPassage();

    String getComment();

    LocalDateTime getCreatedAt();
}
