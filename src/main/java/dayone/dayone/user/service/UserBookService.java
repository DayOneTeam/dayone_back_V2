package dayone.dayone.user.service;

import dayone.dayone.user.entity.repository.UserRepository;
import dayone.dayone.user.entity.repository.dto.UserBookInfo;
import dayone.dayone.user.entity.repository.dto.UserBookLogInfo;
import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import dayone.dayone.user.service.dto.UserBookListResponse;
import dayone.dayone.user.service.dto.UserBookLogListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserBookService {

    private final UserRepository userRepository;

    public UserBookListResponse getUserBooks(final Long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));

        List<UserBookInfo> userBookInfos = userRepository.findUserBookInfo(userId);
        return UserBookListResponse.from(userBookInfos);
    }

    public UserBookLogListResponse getUserBookLogs(final Long userId, final Long bookId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));

        List<UserBookLogInfo> userBookLogInfos = userRepository.findUserBookLogInfo(userId, bookId);
        return UserBookLogListResponse.from(userBookLogInfos);
    }
}
