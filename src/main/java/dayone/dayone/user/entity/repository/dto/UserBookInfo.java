package dayone.dayone.user.entity.repository.dto;

import java.time.LocalDateTime;

public interface UserBookInfo {

    Long getId();

    String getThumbnail();

    String getTitle();

    LocalDateTime getRecentlyCreatedAt();
}
