package de.ebamberg.streamline.ml.api;

public class PipelineException extends RuntimeException {

	public PipelineException() {
	}

	public PipelineException(String message) {
		super(message);
	}

	public PipelineException(Throwable cause) {
		super(cause);
	}

	public PipelineException(String message, Throwable cause) {
		super(message, cause);
	}

	public PipelineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
