package net.sinou.patterns.spring.batch.minimal.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Simple configuration for tests with in memory DB. The data structure is
 * cleaned at each start.
 * 
 * Note that the creation of the data source is not very clean: is this
 * singleton pattern really the best way to go?
 */
@EnableBatchProcessing
@Profile("test")
@PropertySource("classpath:testData.properties")
public class InMemoryTestConfig implements InfrastructureConfiguration {

	@Value("${data.batch.clean}")
	private String batchClean;

	@Value("${data.batch.schema}")
	private String batchSchema;

	@Value("${data.business.reset}")
	private String businessReset;

	private DataSource dataSource;

	@Bean
	public DataSource dataSource() {
		if (dataSource == null) {
			EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
			dataSource = embeddedDatabaseBuilder.addScript(batchClean).addScript(batchSchema).addScript(businessReset)
					.setType(EmbeddedDatabaseType.HSQL).build();
		}
		return dataSource;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
