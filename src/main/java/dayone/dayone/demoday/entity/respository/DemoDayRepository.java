package dayone.dayone.demoday.entity.respository;

import dayone.dayone.demoday.entity.DemoDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoDayRepository extends JpaRepository<DemoDay, Long> {
}
