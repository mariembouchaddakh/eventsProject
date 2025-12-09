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
                    "spring.jpa.show-sql=false",
                    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
                    "spring.jpa.properties.hibernate.format_sql=false"
                })
class EventsProjectApplicationTests {

    @Test
    void contextLoads() {
        // Test que le contexte Spring se charge correctement
    }

}
