package net.sinou.patterns.spring.batch.minimal.dummy;

import java.util.Properties;

import org.springframework.batch.core.launch.support.CommandLineJobRunner;

import net.sinou.patterns.spring.batch.minimal.jobs.SimpleBatchConfiguration;

/**
 * Wraps the basic batch command line runner to ease launching from within the
 * Eclipse IDE at dev time
 */
public class CustomRunner {

	public static void main(String[] args) throws Exception {
		// Force the active profile
		Properties properties = System.getProperties();
		properties.setProperty("spring.profiles.active", "test");
		// Give the path to a default folder with content to test:
		String param = "pathToFolder=" + System.getProperty("user.dir") + "/build/libs";

		CommandLineJobRunner.main(new String[] { SimpleBatchConfiguration.class.getName(), "simpleBatchJob", param });
	}
}
