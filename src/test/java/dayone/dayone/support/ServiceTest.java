package dayone.dayone.support;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private DataCleaner dataCleaner;

    @AfterEach
    void cleanUp() {
        dataCleaner.clear();
    }
}
