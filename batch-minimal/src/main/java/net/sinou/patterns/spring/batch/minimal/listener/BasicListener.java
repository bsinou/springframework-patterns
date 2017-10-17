package net.sinou.patterns.spring.batch.minimal.listener;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import net.sinou.patterns.spring.batch.minimal.configuration.InfrastructureConfiguration;
import net.sinou.patterns.spring.batch.minimal.util.JdbcUtils;

public class BasicListener implements JobExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(BasicListener.class);

	@Autowired
	public InfrastructureConfiguration infrastructureConfiguration;

	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("=== Job completed");
			DataSource dataSource = infrastructureConfiguration.dataSource();

			if (logger.isTraceEnabled())
				JdbcUtils.listAllDbTables(dataSource);

			if (logger.isDebugEnabled())
				JdbcUtils.listDbTableFirstLines(dataSource, "FILE_INFO");
		}
	}

	public void beforeJob(JobExecution arg0) {
		// nothing to do
	}
}
