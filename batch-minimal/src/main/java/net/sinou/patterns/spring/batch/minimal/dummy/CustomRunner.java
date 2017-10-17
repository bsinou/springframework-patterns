package net.sinou.patterns.spring.batch.minimal.dummy;

import org.springframework.batch.core.launch.support.CommandLineJobRunner;

import net.sinou.patterns.spring.batch.minimal.jobs.SimpleBatchConfiguration;

/**
 * Wraps the basic batch command line runner to ease launching from within the
 * Eclipse IDE at dev time
 */
public class CustomRunner {

	public static void main(String[] args) throws Exception {
		CommandLineJobRunner.main(new String[] { SimpleBatchConfiguration.class.getName(), "simpleBatchJob" });
	}

}
