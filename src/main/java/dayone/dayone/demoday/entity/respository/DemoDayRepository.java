package dayone.dayone.demoday.entity.respository;

import dayone.dayone.demoday.entity.DemoDay;
import dayone.dayone.demoday.entity.value.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemoDayRepository extends JpaRepository<DemoDay, Long> {
    List<DemoDay> findAllByStatus(Status status);
}
