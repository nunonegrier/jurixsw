package br.com.jurix;

import br.com.jurix.querydsql.factory.JoinCapableRepositoryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan("br.com.jurix")
@EnableJpaRepositories(repositoryFactoryBeanClass = JoinCapableRepositoryFactory.class, basePackages = {"br.com.jurix.**.repository"})
public class JurixApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JurixApiApplication.class, args);
	}
}
