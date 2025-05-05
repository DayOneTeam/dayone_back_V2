package dayone.dayone.demoday.entity.respository;

import dayone.dayone.demoday.entity.DemoDayUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DemoDayUserRepository extends JpaRepository<DemoDayUser, Long> {
    Optional<DemoDayUser> findByDemoDayIdAndUserId(final Long demoDayId, final Long userId);

    List<DemoDayUser> findByDemoDayId(final Long demoDayId);
}
