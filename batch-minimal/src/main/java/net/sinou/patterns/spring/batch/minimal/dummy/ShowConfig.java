package net.sinou.patterns.spring.batch.minimal.dummy;

import java.util.Arrays;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class to display some of the environment properties easily when
 * launching from the command line
 */
public class ShowConfig {
	private static Logger log = LoggerFactory.getLogger(ShowConfig.class);

	public static void main(String[] args) {
		log.info("=== System properties ");
		Properties props = System.getProperties();
		props.list(System.out);

		log.info("=== Current Runtime ClassPath: ");
		Arrays.stream(System.getProperty("java.class.path").split(":")).forEach(System.out::println);
	}
}
