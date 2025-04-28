package dayone.dayone.user.ui;

import dayone.dayone.global.response.CommonResponseDto;
import dayone.dayone.user.service.UserBookService;
import dayone.dayone.user.service.dto.UserBookLogCountInWeekResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users/book-logs")
@RestController
public class UserBookLogAdminController {

    private final UserBookService userBookService;

    @GetMapping("/count?generation={generation}&startDate={startDate}&endDate={endDate}")
    public CommonResponseDto<UserBookLogCountInWeekResponse> getUsersBookLogCountInWeek(@RequestParam final int generation, @RequestParam final String startDate, @RequestParam final String endDate) {
        final UserBookLogCountInWeekResponse response = userBookService.getUserBookLogCountInWeek(generation, startDate, endDate);
        return CommonResponseDto.forSuccess(1, "특정 기수의 특정 기간의 bookLog 작성 횟수 조회", response);
    }
}
