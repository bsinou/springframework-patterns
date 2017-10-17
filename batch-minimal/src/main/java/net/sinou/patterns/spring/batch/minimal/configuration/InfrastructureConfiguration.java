package net.sinou.patterns.spring.batch.minimal.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;

/** Abstract the infra config to ease switching between profiles */
public interface InfrastructureConfiguration {

	@Bean
	public abstract DataSource dataSource();

}