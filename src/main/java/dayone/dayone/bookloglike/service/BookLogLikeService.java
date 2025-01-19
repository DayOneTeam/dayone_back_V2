package dayone.dayone.bookloglike.service;

import dayone.dayone.booklog.entity.repository.BookLogRepository;
import dayone.dayone.booklog.exception.BookLogErrorCode;
import dayone.dayone.booklog.exception.BookLogException;
import dayone.dayone.bookloglike.entity.BookLogLike;
import dayone.dayone.bookloglike.entity.repository.BookLogLikeRepository;
import dayone.dayone.bookloglike.exception.BookLogLikeErrorCode;
import dayone.dayone.bookloglike.exception.BookLogLikeException;
import dayone.dayone.user.entity.repository.UserRepository;
import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookLogLikeService {

    private final BookLogLikeRepository bookLogLikeRepository;
    private final BookLogRepository bookLogRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addLike(final Long bookLogId, final Long userId) {
        bookLogRepository.findById(bookLogId)
            .orElseThrow(() -> new BookLogException(BookLogErrorCode.NOT_EXIST_BOOK_LOG));

        userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));

        final Optional<BookLogLike> alreadyLike = bookLogLikeRepository.findAllByUserIdAndBookLogId(userId, bookLogId);
        if (alreadyLike.isPresent()) {
            throw new BookLogLikeException(BookLogLikeErrorCode.ALREADY_LIKE_BOOK_LOG);
        }

        final BookLogLike bookLogLike = BookLogLike.forSave(userId, bookLogId);
        bookLogLikeRepository.save(bookLogLike);
        bookLogRepository.plusLike(bookLogId);
    }

    @Transactional
    public void deleteLike(final Long bookLogId, final long userId) {
        bookLogRepository.findById(bookLogId)
            .orElseThrow(() -> new BookLogException(BookLogErrorCode.NOT_EXIST_BOOK_LOG));

        userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));

        final BookLogLike bookLogLike = bookLogLikeRepository.findAllByUserIdAndBookLogId(userId, bookLogId)
            .orElseThrow(() -> new BookLogLikeException(BookLogLikeErrorCode.NOT_LIKE_BOOK_LOG));

        bookLogLikeRepository.delete(bookLogLike);
        bookLogRepository.minusLike(bookLogId);
    }
}
