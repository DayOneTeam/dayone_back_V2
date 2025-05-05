package dayone.dayone.fixture;

import dayone.dayone.demoday.entity.DemoDay;
import dayone.dayone.demoday.entity.Ticket;
import dayone.dayone.demoday.entity.respository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestTicketFactory {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket createNTicket(final DemoDay demoDay, final int capacity) {
        final Ticket ticket = Ticket.forSave(demoDay, capacity);
        return ticketRepository.save(ticket);
    }
}
