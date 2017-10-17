package net.sinou.patterns.spring.batch.minimal.item;

import java.io.File;
import java.nio.file.Path;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class DirReader implements ItemReader<File> {

	private final File[] files;
	private int index = 0;

	public DirReader(Path basePath) {
		File baseFile = basePath.toFile();
		if (!baseFile.exists() || !baseFile.isDirectory())
			throw new IllegalArgumentException();
		files = baseFile.listFiles();
	}

	@Override
	public File read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (index >= files.length)
			return null;
		else
			return files[index++];
	}
}
