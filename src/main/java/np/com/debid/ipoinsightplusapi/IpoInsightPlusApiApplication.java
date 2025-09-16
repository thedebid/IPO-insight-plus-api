package np.com.debid.ipoinsightplusapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IpoInsightPlusApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpoInsightPlusApiApplication.class, args);
    }

}
