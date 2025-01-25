package dayone.dayone.fixture;

import dayone.dayone.demoday.entity.DemoDay;
import dayone.dayone.demoday.entity.respository.DemoDayRepository;
import dayone.dayone.demoday.entity.value.Capacity;
import dayone.dayone.demoday.entity.value.DemoDate;
import dayone.dayone.demoday.entity.value.RegistrationDate;
import dayone.dayone.demoday.entity.value.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestDemoDayFactory {

    @Autowired
    private DemoDayRepository demoDayRepository;

    public List<DemoDay> createNDemoDaysWithStatus(final int n, final String title, final String description, final Status status) {
        List<DemoDay> demoDays = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            LocalDateTime localDateTime = LocalDateTime.now();
            final DemoDay demoDay = new DemoDay(null,
                title,
                description,
                "이미지",
                new Capacity(10),
                new RegistrationDate(localDateTime, localDateTime.plusDays(1)),
                new DemoDate(localDateTime.plusDays(1)),
                "장소",
                status,
                1L);
            demoDays.add(demoDayRepository.save(demoDay));
        }

        return demoDays;
    }
}
