package net.sinou.patterns.spring.batch.minimal.item;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import net.sinou.patterns.spring.batch.minimal.domain.FileInfo;
import net.sinou.patterns.spring.batch.minimal.item.FileItemProcessor;

public class FileItemProcessorTest extends AbstractItemTest {

	/**
	 * Test method for
	 * {@link net.sinou.patterns.spring.batch.minimal.item.FileItemProcessor#process(java.io.File)}.
	 */
	@Test
	public void testProcess() throws Exception {
		FileItemProcessor processor = new FileItemProcessor();

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(getParent())) {
			for (Path path : directoryStream) {
				File curr = path.toFile();
				FileInfo info = processor.process(curr);
				Assert.assertEquals(curr.getName(), info.getFileName());
			}
		}
	}
}
