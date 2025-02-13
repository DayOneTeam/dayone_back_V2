package dayone.dayone.user.ui;

import dayone.dayone.auth.ui.argumentresolver.AuthUser;
import dayone.dayone.global.response.CommonResponseDto;
import dayone.dayone.user.service.UserService;
import dayone.dayone.user.service.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
