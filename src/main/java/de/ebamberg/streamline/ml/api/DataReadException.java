package de.ebamberg.streamline.ml.api;

public class DataReadException extends PipelineException {

	public DataReadException() {
	}

	public DataReadException(String message) {
		super(message);
	}

	public DataReadException(Throwable cause) {
		super(cause);
	}

	public DataReadException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
