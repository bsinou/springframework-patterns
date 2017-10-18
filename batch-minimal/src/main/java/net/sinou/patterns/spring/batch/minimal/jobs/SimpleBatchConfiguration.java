package net.sinou.patterns.spring.batch.minimal.jobs;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.sinou.patterns.spring.batch.minimal.configuration.InMemoryTestConfig;
import net.sinou.patterns.spring.batch.minimal.configuration.InfrastructureConfiguration;
import net.sinou.patterns.spring.batch.minimal.domain.FileInfo;
import net.sinou.patterns.spring.batch.minimal.item.DirReader;
import net.sinou.patterns.spring.batch.minimal.item.FileItemProcessor;
import net.sinou.patterns.spring.batch.minimal.listener.BasicListener;

@Configuration
@Import({ InMemoryTestConfig.class })
public class SimpleBatchConfiguration {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// Empty place holder that will be dynamically overwritten on method call
	private static final String OVERRIDDEN_BY_EXPRESSION = null;

	private static final String SQL_PERSIST = "INSERT INTO FILE_INFO ("
			+ "file_name, file_path, file_created, file_last_modified, file_owner, file_size" + ") VALUES ("
			+ ":fileName, :filePath, :created, :lastModified, :owner, :size)";

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public InfrastructureConfiguration infrastructureConfiguration;

	@Bean
	@StepScope
	public ItemReader<File> reader(@Value("#{jobParameters[pathToFolder]}") String pathToFolder) {
		return new DirReader(Paths.get(pathToFolder));
	}

	@Bean
	public FileItemProcessor processor() {
		return new FileItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<FileInfo> writer() {
		JdbcBatchItemWriter<FileInfo> writer = new JdbcBatchItemWriter<FileInfo>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<FileInfo>());
		if (logger.isTraceEnabled())
			logger.trace("Creating writer with SQL: \n" + SQL_PERSIST);
		writer.setSql(SQL_PERSIST);
		writer.setDataSource(infrastructureConfiguration.dataSource());
		return writer;
	}

	@Bean
	public Job simpleBatchJob(JobExecutionListener listener) {
		return jobBuilderFactory.get("simpleBatchJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step1()).end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<File, FileInfo>chunk(10).reader(reader(OVERRIDDEN_BY_EXPRESSION))
				.processor(processor()).writer(writer()).build();
	}

	@Bean
	public BasicListener basicListener() {
		return new BasicListener();
	}
}
