package ittalents.webappsports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WebappsportsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebappsportsApplication.class, args);
    }

}
