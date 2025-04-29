package dayone.dayone.user.ui;

import dayone.dayone.global.response.CommonResponseDto;
import dayone.dayone.user.service.UserBookService;
import dayone.dayone.user.service.dto.UserBookLogCountInWeekResponse;
import dayone.dayone.user.service.dto.UsersBookLogCountRequest;
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

    @GetMapping("/count")
    public CommonResponseDto<UserBookLogCountInWeekResponse> getUsersBookLogCountInWeek(@RequestParam("generation") final int generation, @RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate) {
        final UsersBookLogCountRequest request = UsersBookLogCountRequest.of(generation, startDate, endDate);
        final UserBookLogCountInWeekResponse response = userBookService.getUserBookLogCountInWeek(request);
        return CommonResponseDto.forSuccess(1, "특정 기수의 특정 기간의 bookLog 작성 횟수 조회", response);
    }
}
