package dayone.dayone.demoday.service;

import dayone.dayone.demoday.entity.DemoDay;
import dayone.dayone.demoday.entity.respository.DemoDayRepository;
import dayone.dayone.demoday.service.dto.DemoDayCreateRequest;
import dayone.dayone.user.entity.repository.UserRepository;
import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class DemoDayService {

    private final DemoDayRepository demoDayRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(final Long userId, final DemoDayCreateRequest request) {
        userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));

        final DemoDay demoDay = DemoDay.forSave(request.title(),
            request.description(),
            request.thumbnail(),
            request.demoDate(),
            request.demoTime(),
            request.capacity(),
            request.location(),
            userId);

        demoDayRepository.save(demoDay);
        return demoDay.getId();
    }
}
