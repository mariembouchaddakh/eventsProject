package tn.fst.eventsproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, 
                properties = {
                    "spring.datasource.url=jdbc:h2:mem:testdb",
                    "spring.datasource.driver-class-name=org.h2.Driver",
                    "spring.jpa.hibernate.ddl-auto=create-drop",
                    "spring.jpa.show-sql=false"
                })
class EventsProjectApplicationTests {

    @Test
    void contextLoads() {
        // Test que le contexte Spring se charge correctement
    }

}
