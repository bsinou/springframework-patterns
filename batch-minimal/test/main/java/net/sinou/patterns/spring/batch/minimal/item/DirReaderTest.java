package net.sinou.patterns.spring.batch.minimal.item;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import net.sinou.patterns.spring.batch.minimal.item.DirReader;

/** Simple test for the directory reader */
public class DirReaderTest extends AbstractItemTest {

	@Test
	public void testRead() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		DirReader reader = new DirReader(getParent());
		for (int i = 0; i < getNbOfFile(); i++) {
			File file = reader.read();
			Assert.assertNotNull(file);
		}
		File file = reader.read();
		Assert.assertNull(file);
	}
}
