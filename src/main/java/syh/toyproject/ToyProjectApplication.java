package syh.toyproject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
@SpringBootApplication
public class ToyProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToyProjectApplication.class, args);
	}
}
