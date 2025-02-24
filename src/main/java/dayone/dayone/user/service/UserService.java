package dayone.dayone.user.service;

import dayone.dayone.global.service.FileService;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.UserRepository;
import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import dayone.dayone.user.service.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileService filesService;

    public UserInfoResponse getUserInfo(final Long userId) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));

        return UserInfoResponse.from(user);
    }

    @Transactional
    public void updateUserProfileImage(final Long userId, final MultipartFile file) throws IOException {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));

        final String newProfileImage = filesService.uploadFile(file);
        user.updateProfileImage(newProfileImage);
    }
}
