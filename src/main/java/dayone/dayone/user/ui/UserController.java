package dayone.dayone.user.ui;

import dayone.dayone.auth.ui.argumentresolver.AuthUser;
import dayone.dayone.global.response.CommonResponseDto;
import dayone.dayone.user.service.UserService;
import dayone.dayone.user.service.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public CommonResponseDto<UserInfoResponse> getUserInfo(@AuthUser final Long userId) {
        final UserInfoResponse response = userService.getUserInfo(userId);
        return CommonResponseDto.forSuccess(1, "유저 정보 조회 성공", response);
    }

    @PatchMapping("/update-profile-image")
    public ResponseEntity<Void> updateUserProfileImage(@AuthUser final Long userId, @RequestPart("file") final MultipartFile file) throws IOException {
        userService.updateUserProfileImage(userId, file);
        return ResponseEntity.noContent().build();
    }
}
