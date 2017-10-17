package net.sinou.patterns.spring.batch.minimal.jobs;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import net.sinou.patterns.spring.batch.minimal.util.JdbcUtils;

/**
 * Test retrieval of the necessary beans via the Spring Autowire mechanism
 */
public class SimpleAutoConfigurationTest {
	private final static Logger logger = LoggerFactory.getLogger(SimpleAutoConfigurationTest.class);

	private ConfigurableApplicationContext context = null;

	private JobLauncher launcher;
	private DataSource dataSource;
	private Job job;

	/**
	 * Injection setter for the {@link JobLauncher}.
	 *
	 * @param launcher
	 *            the launcher to set
	 */
	public void setLauncher(JobLauncher launcher) {
		this.launcher = launcher;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setSimpleBatchJob(Job simpleBatchJob) {
		this.job = simpleBatchJob;
	}

	@Before
	public void setUp() throws Exception {
		context = new AnnotationConfigApplicationContext(SimpleBatchConfiguration.class);
		context.getAutowireCapableBeanFactory().autowireBeanProperties(this,
				AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
	}

	@After
	public void tearDown() throws Exception {
		context.close();
	}

	@Test
	public void testSimpleJob() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		Assert.notNull(launcher, "A JobLauncher must be provided.  Please add one to the configuration.");
		Assert.notNull(dataSource, "No datasource found in context");
		Assert.notNull(job, "No job with name 'simpleBatchJob' found in context");

		JobExecution jobExecution = launcher.run(job, new JobParameters());
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("=== Job completed");

			if (logger.isTraceEnabled())
				JdbcUtils.listAllDbTables(dataSource);

			if (logger.isDebugEnabled())
				JdbcUtils.listDbTableFirstLines(dataSource, "FILE_INFO");
		}

	}
}
