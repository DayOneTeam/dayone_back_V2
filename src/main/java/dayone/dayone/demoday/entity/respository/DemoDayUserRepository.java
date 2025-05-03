package dayone.dayone.demoday.entity.respository;

import dayone.dayone.demoday.entity.DemoDayUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoDayUserRepository extends JpaRepository<DemoDayUser, Long> {
}
