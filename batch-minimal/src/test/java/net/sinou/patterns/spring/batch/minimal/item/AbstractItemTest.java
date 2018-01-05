package net.sinou.patterns.spring.batch.minimal.item;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Centralise setup for basic tests */
public class AbstractItemTest {
	private final static Logger log = LoggerFactory.getLogger(AbstractItemTest.class);

	private Path parent;
	private final int NB_OF_FILE = 10;

	public int getNbOfFile() {
		return NB_OF_FILE;
	}

	protected Path getParent() {
		return parent;
	}

	@Before
	public void setUp() throws Exception {
		Path path = Paths.get(System.getProperty("user.dir"), "exec");
		log.info("User dir: " + path.toString());

		parent = path.resolve("tmp");
		if (Files.exists(parent)) {
			DeleteDir dd = new DeleteDir();
			Files.walkFileTree(parent, dd);
		}
		parent = Files.createDirectory(parent);

		for (int i = 0; i < NB_OF_FILE; i++) {
			Path child = Files.createFile(parent.resolve("testFile_" + i + ".txt"));
			try {
				String content = "Some content for the file #" + i;
				Files.write(child, content.getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				throw new RuntimeException("Unable to setup the files", e);
			}
		}
	}

	@After
	public void tearDown() throws IOException {
		DeleteDir dd = new DeleteDir();
		Files.walkFileTree(parent, dd);
	}

	class DeleteDir extends SimpleFileVisitor<Path> {

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			FileVisitResult res = super.visitFile(file, attrs);
			Files.delete(file);
			return res;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			FileVisitResult res = super.postVisitDirectory(dir, exc);
			Files.delete(dir);
			return res;
		}
	}
}
