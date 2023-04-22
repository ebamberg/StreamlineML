package de.ebamberg.streamline.ml.api;

public class ExternalServiceException extends PipelineException {

	public ExternalServiceException() {
	}

	public ExternalServiceException(String message) {
		super(message);
	}

	public ExternalServiceException(Throwable cause) {
		super(cause);
	}

	public ExternalServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExternalServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
