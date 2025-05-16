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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//        final Optional<BookLogLike> alreadyLike = bookLogLikeRepository.findBookLogLikeByUserIdAndBookLogIdForUpdate(userId,bookLogId);
//        if (alreadyLike.isPresent()) {
//            throw new BookLogLikeException(BookLogLikeErrorCode.ALREADY_LIKE_BOOK_LOG);
//        }
//
//        final BookLogLike bookLogLike = BookLogLike.forSave(userId, bookLogId);
//        bookLogLikeRepository.save(bookLogLike);
//        bookLogRepository.plusLike(bookLogId);
        try {
            // 중복 여부 확인 없이 바로 삽입
            BookLogLike like = BookLogLike.forSave(userId, bookLogId);
            bookLogLikeRepository.save(like);

            // 좋아요 수 증가 (Optional: 이벤트로 분리 가능)
            bookLogRepository.plusLike(bookLogId);
        } catch (DataIntegrityViolationException e) {
            // 유니크 키 제약 위반 시 중복 좋아요로 판단
            throw new BookLogLikeException(BookLogLikeErrorCode.ALREADY_LIKE_BOOK_LOG);
        }
    }

    @Transactional
    public void deleteLike(final Long bookLogId, final Long userId) {
        bookLogRepository.findById(bookLogId)
            .orElseThrow(() -> new BookLogException(BookLogErrorCode.NOT_EXIST_BOOK_LOG));

        userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));

        final BookLogLike bookLogLike = bookLogLikeRepository.findBookLogLikeByUserIdAndBookLogIdForUpdate(userId, bookLogId)
            .orElseThrow(() -> new BookLogLikeException(BookLogLikeErrorCode.NOT_LIKE_BOOK_LOG));


        bookLogLikeRepository.delete(bookLogLike);
        bookLogRepository.minusLike(bookLogId);
    }
}
