package dayone.dayone.demoday.entity.respository;

import dayone.dayone.demoday.entity.DemoDayUser;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DemoDayUserRepository extends JpaRepository<DemoDayUser, Long> {

    Optional<DemoDayUser> findByDemoDayIdAndUserId(final Long demoDayId, final Long userId);

    List<DemoDayUser> findByDemoDayId(final Long demoDayId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT D FROM DemoDayUser D WHERE D.demoDayId = :demoDayId")
    List<DemoDayUser> findByDemoDayIdForUpdate(final @Param("demoDayId") Long demoDayId);
}
