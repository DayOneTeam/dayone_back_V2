package dayone.dayone.user.ui;

import dayone.dayone.auth.ui.argumentresolver.AuthUser;
import dayone.dayone.global.response.CommonResponseDto;
import dayone.dayone.user.service.UserBookService;
import dayone.dayone.user.service.dto.UserBookListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users/books")
@RestController
public class UserBookController {

    private final UserBookService userBookService;

    @GetMapping
    public CommonResponseDto<UserBookListResponse> getUserBooks(@AuthUser final Long userId) {
        final UserBookListResponse response = userBookService.getUserBooks(userId);
        return CommonResponseDto.forSuccess(1, "유저 책장 목로 조회 성공", response);
    }
}
