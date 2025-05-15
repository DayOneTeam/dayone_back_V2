package dayone.dayone.demoday.entity.respository;

import dayone.dayone.demoday.entity.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByDemoDayId(final Long demoDayId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT T FROM Ticket T WHERE T.demoDay.id = :demoDayId")
    Optional<Ticket> findByDemoDayIdForUpdate(final @Param("demoDayId") Long demoDayId);
}
