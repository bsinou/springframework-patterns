package net.sinou.patterns.spring.batch.minimal.listener;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import net.sinou.patterns.spring.batch.minimal.configuration.InfrastructureConfiguration;
import net.sinou.patterns.spring.batch.minimal.domain.FileInfo;

public class BasicListener implements JobExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(BasicListener.class);

	@Autowired
	public InfrastructureConfiguration infrastructureConfiguration;

	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("=== Job completed");
			DataSource dataSource = infrastructureConfiguration.dataSource();

			JdbcTemplate tmp = new JdbcTemplate(dataSource);
			tmp.execute("SELECT * FROM FILE_INFO ");
			ResultSet rs;
			try {
				rs = dataSource.getConnection().prepareStatement("SELECT * FROM FILE_INFO ").executeQuery();
				logger.info("== Found " + rs.getFetchSize() + " rows in the database.");
				do {
					logger.info("row: " + rs.getString(0));
				} while (rs.next());

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void beforeJob(JobExecution arg0) {
		// nothing to do
	}
}
