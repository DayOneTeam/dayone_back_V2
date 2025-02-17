package dayone.dayone.booklog.entity.repository.dto;

import dayone.dayone.booklog.entity.BookLog;

public interface BookLogInfoWithIsLike {

    BookLog getBookLog();

    boolean getIsLike();
}
