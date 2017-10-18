package net.sinou.patterns.spring.batch.minimal.jobs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

/**
 * Test explicit retrieval of the necessary beans via the Spring application
 * context
 */
public class SimpleConfigurationTest {
	private ConfigurableApplicationContext context = null;

	@BeforeClass
	public static void setSystemProperty() {
		// Force the active profile with no Spring magic
		Properties properties = System.getProperties();
		properties.setProperty("spring.profiles.active", "test");
	}

	@Before
	public void setUp() throws Exception {
		context = new AnnotationConfigApplicationContext(SimpleBatchConfiguration.class);
	}

	@After
	public void tearDown() throws Exception {
		context.close();
	}

	@Test
	public void testSimpleJob() throws Exception {
		JobLauncher launcher = (JobLauncher) context.getBean("jobLauncher");
		Assert.notNull(launcher, "A JobLauncher must be provided.  Please add one to the configuration.");

		Job job = (Job) context.getBean("simpleBatchJob");
		Assert.notNull(job, "No job with name 'simpleBatchJob' found in context");
		JobParameter path = new JobParameter(System.getProperty("user.dir") + "/build/libs");
		Map<String, JobParameter> map = new HashMap<>();
		map.put("pathToFolder", path);
		JobExecution jobExecution = launcher.run(job, new JobParameters(map));
		assertEquals("Batch status is not completed", jobExecution.getStatus(), BatchStatus.COMPLETED);

		DataSource dataSource = (DataSource) context.getBean("dataSource");
		Assert.notNull(dataSource, "No datasource found in context");

		// Verify the persisted data
		try (Connection con = dataSource.getConnection()) {
			String tableName = "FILE_INFO";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
			int i = 0;
			while (rs.next())
				// Add better tests
				i++;
			assertTrue("No row has been created", i > 0);
		}
	}
}
