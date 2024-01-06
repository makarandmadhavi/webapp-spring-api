package karate;

import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import tech.stark.webapp.WebappApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {WebappApplication.class})
class KarateTests {

    @Karate.Test
    Karate healthTest() {
        return Karate.run("classpath:karate/health/health.feature");
    }

}