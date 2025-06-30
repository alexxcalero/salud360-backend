package pe.edu.pucp.salud360;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "pe.edu.pucp.salud360")
@EntityScan(basePackages = "pe.edu.pucp.salud360")  // Escanea TODAS las entidades en subpaquetes
@EnableJpaRepositories(basePackages = "pe.edu.pucp.salud360")  // Escanea TODOS los repositorios
@EnableAspectJAutoProxy//Para los logs

public class Salud360Application {
	public static void main(String[] args) {
		SpringApplication.run(Salud360Application.class, args);
	}
}
