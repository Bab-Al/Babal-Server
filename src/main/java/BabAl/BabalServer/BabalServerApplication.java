package BabAl.BabalServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BabalServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabalServerApplication.class, args);
	}

}
