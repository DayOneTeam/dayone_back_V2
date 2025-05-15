package dayone.dayone.demoday.service;

import dayone.dayone.demoday.entity.DemoDay;
import dayone.dayone.demoday.entity.DemoDayUser;
import dayone.dayone.demoday.entity.DemoDayUsers;
import dayone.dayone.demoday.entity.Ticket;
import dayone.dayone.demoday.entity.respository.DemoDayRepository;
import dayone.dayone.demoday.entity.respository.DemoDayUserRepository;
import dayone.dayone.demoday.entity.respository.TicketRepository;
import dayone.dayone.demoday.entity.value.Status;
import dayone.dayone.demoday.exception.DemoDayErrorCode;
import dayone.dayone.demoday.service.dto.DemoDayCreateRequest;
import dayone.dayone.demoday.service.dto.DemoDayListResponse;
import dayone.dayone.user.entity.User;
import dayone.dayone.user.entity.repository.UserRepository;
import dayone.dayone.user.exception.UserErrorCode;
import dayone.dayone.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class DemoDayService {

    private final DemoDayRepository demoDayRepository;
    private final TicketRepository ticketRepository;
    private final DemoDayUserRepository demoDayUserRepository;
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
            request.location(),
            userId);

        demoDayRepository.save(demoDay);
        final Ticket ticket = Ticket.forSave(demoDay, request.capacity());
        ticketRepository.save(ticket);
        return demoDay.getId();
    }

    public DemoDayListResponse getDemoDaysWithStatus(final String status) {
        final List<DemoDay> demoDays = demoDayRepository.findAllByStatus(Status.valueOf(status));
        return DemoDayListResponse.from(demoDays);
    }

    @Transactional
    public void applyDemoDay(final Long userId, final Long demoDayId) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));
        final DemoDay demoDay = demoDayRepository.findById(demoDayId)
            .orElseThrow(() -> new UserException(DemoDayErrorCode.NOT_EXIST_DEMO_DAY));
        final Ticket ticket = ticketRepository.findByDemoDayIdForUpdate(demoDayId)
            .orElseThrow(() -> new UserException(DemoDayErrorCode.NOT_EXIST_DEMO_DAY));
        final List<DemoDayUser> demoDayUsers = demoDayUserRepository.findByDemoDayIdForUpdate(demoDayId);

        final DemoDayUser apply = demoDay.apply(user, ticket, new DemoDayUsers(demoDayUsers));
        demoDayUserRepository.save(apply);
    }
}
