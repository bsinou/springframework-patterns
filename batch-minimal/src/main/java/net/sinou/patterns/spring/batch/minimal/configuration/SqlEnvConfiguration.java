package net.sinou.patterns.spring.batch.minimal.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableBatchProcessing
@PropertySource("classpath:mysql-application.properties")
public class SqlEnvConfiguration implements InfrastructureConfiguration {

	public static final String KEY_DB_DRIVER_CLASS = "db.driver";
	public static final String KEY_DB_URL = "db.url";
	public static final String KEY_DB_NAME = "db.name";
	public static final String KEY_DB_USER = "db.username";
	public static final String KEY_DB_PASSWORD = "db.password";

	@Autowired
	private Environment env;

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		HikariConfig dataSourceConfig = new HikariConfig();
		dataSourceConfig.setDriverClassName(env.getRequiredProperty(KEY_DB_DRIVER_CLASS));
		String url = env.getRequiredProperty(KEY_DB_URL) + "/" + env.getRequiredProperty(KEY_DB_NAME);
		dataSourceConfig.setJdbcUrl(url);
		dataSourceConfig.setUsername(env.getRequiredProperty(KEY_DB_USER));
		dataSourceConfig.setPassword(env.getRequiredProperty(KEY_DB_PASSWORD));
		return new HikariDataSource(dataSourceConfig);
	}

	/**
	 * Creates a new JDBC template that supports named parameters.
	 * 
	 * @param dataSource
	 *            The datasources that provides database connections.
	 * @return
	 */
	@Bean
	NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
