package net.sinou.patterns.spring.batch.minimal.item;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;

import org.springframework.batch.item.ItemProcessor;

import net.sinou.patterns.spring.batch.minimal.domain.FileInfo;

/**
 * Read info from the passed Path and compute a MD5 digest to store in the
 * domain FileInfo object
 */
public class FileItemProcessor implements ItemProcessor<File, FileInfo> {

	@Override
	public FileInfo process(final File file) throws Exception {
		Path path = Paths.get(file.getPath());
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFileName(file.getName());
		fileInfo.setFilePath(file.getAbsolutePath());
		fileInfo.setSize(file.length());
		fileInfo.setLastModified(file.lastModified());
		FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
		fileInfo.setOwner(ownerAttributeView.getOwner().getName());
		BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
		fileInfo.setCreated(attributes.creationTime().toMillis());

		// Generates the MD5 digest
		return fileInfo;
	}

}