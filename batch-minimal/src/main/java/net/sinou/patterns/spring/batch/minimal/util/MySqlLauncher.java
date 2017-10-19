package net.sinou.patterns.spring.batch.minimal.util;

import org.springframework.batch.core.launch.support.CommandLineJobRunner;

import net.sinou.patterns.spring.batch.minimal.jobs.SimpleBatchConfiguration;

/**
 * Wraps the basic batch command line runner to ease launching from within the
 * Eclipse IDE at dev time
 */
public class MySqlLauncher {

	public static void main(String[] args) throws Exception {
		// Force the active profile, useless for default
		// Properties properties = System.getProperties();
		// properties.setProperty("spring.profiles.active", "default");
		// Path to a default folder with content to test:
		String param = "pathToFolder=" + System.getProperty("user.dir") + "/build/libs";

		CommandLineJobRunner.main(new String[] { SimpleBatchConfiguration.class.getName(), "simpleBatchJob", param });
	}
}
