package dayone.dayone.demoday.entity.respository;

import dayone.dayone.demoday.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByDemoDayId(Long demoDayId);
}
