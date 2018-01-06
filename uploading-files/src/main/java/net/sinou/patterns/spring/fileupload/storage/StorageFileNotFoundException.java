package net.sinou.patterns.spring.fileupload.storage;

public class StorageFileNotFoundException extends StorageException {
	private static final long serialVersionUID = 9180961176584702359L;

	public StorageFileNotFoundException(String message) {
		super(message);
	}

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
