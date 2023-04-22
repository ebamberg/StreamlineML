package de.ebamberg.streamline.ml.modules.huggingface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import de.ebamberg.streamline.ml.api.ExternalServiceException;
import de.ebamberg.streamline.ml.api.Predictor;
import de.ebamberg.streamline.ml.api.ValidationException;

public abstract class HuggingFaceCloudAdapter<I,O> implements Predictor<I,O> {

	private static final Logger log=LoggerFactory.getLogger(HuggingFaceCloudAdapter.class);
	
	private static final int INFERENCE_RETRIES = 3;
	private static final String HUGGINGFACE_APIKEY = "HUGGINGFACE_APIKEY";
	private static final String HUGGINGFACE_HOST = "HUGGINGFACE_HOST";
	private Gson gson;
	private HttpClient client;
	private URI cachedUri=null;
	private String apiToken=System.getenv(HUGGINGFACE_APIKEY);
	private String huggingFaceHost=System.getProperty(HUGGINGFACE_HOST, "https://api-inference.huggingface.co/");
	
	public HuggingFaceCloudAdapter() {
		gson=new Gson();
		initHttpClient();
	}
	
	protected void initHttpClient() {
		client=HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(20))
				.build();
	}
	
	
	abstract protected String inferencePath() ;
	abstract protected Object buildInputOject(I input) ;
	abstract protected O transformOutput(HttpResponse<String> response) ;

	
	public O predict(I input) {
		checkPreconditions();
		var modelInput=buildInputOject(input);
		
		var request = buildRequest(modelInput);
		
		int retryCounter=INFERENCE_RETRIES;
		HttpResponse<String> response;
		try {
			do {
				response=client.send(request, BodyHandlers.ofString());
				if(response.statusCode()==503) {
					var errorResp=gson.fromJson(response.body(), ModelLoadingResponse.class);
					log.info(errorResp.getError());
					try {
						Thread.sleep( (long)errorResp.estimatedTime*1000);
					} catch (InterruptedException e) {
						log.trace("thread waited for model load in cloud interrupted.",e);
					}
				}
			} while (retryCounter-->0 && response.statusCode()>400);
			if (response.statusCode()!=200) {
				throw new ExternalServiceException(response.body());
			}
			
			return transformOutput(response);
		} catch (JsonSyntaxException | IOException | InterruptedException e) {
			throw new ExternalServiceException("error infer data in external hugging face cloud",e);
		}
	}
	
	protected O jsonToOutput(String data, Class<O> type) {
		return gson.fromJson(data,type);
		
	}

	private void checkPreconditions() {
		if (apiToken==null || apiToken.isEmpty()) {
			throw new ValidationException("no API TOKEN for Huggingface inference api set. please set environment variable "+HUGGINGFACE_APIKEY);
		}
		
	}

	protected HttpRequest buildRequest(Object modelInput) {
		if (cachedUri==null) {
			cachedUri=URI.create(huggingFaceHost+inferencePath());
		}
		var request=HttpRequest.newBuilder()
				.uri(cachedUri)
				.header("Authorization","Bearer "+apiToken)
				.POST(BodyPublishers.ofString(gson.toJson(modelInput)))
				.build();
		return request;
	}
	
	
	static class ModelLoadingResponse {
		private String error;
		@SerializedName("estimated_time")
		private float estimatedTime;
		/**
		 * @return the error
		 */
		public String getError() {
			return error;
		}
		/**
		 * @param error the error to set
		 */
		public void setError(String error) {
			this.error = error;
		}
		/**
		 * @return the estimatedTime
		 */
		public float getEstimatedTime() {
			return estimatedTime;
		}
		/**
		 * @param estimatedTime the estimatedTime to set
		 */
		public void setEstimatedTime(float estimatedTime) {
			this.estimatedTime = estimatedTime;
		}
		
		
		
	}
	
	static class SimpleTextInput {
		private String inputs;

		public SimpleTextInput(String inputs) {
			super();
			this.inputs = inputs;
		}

		/**
		 * @return the inputs
		 */
		public String getInputs() {
			return inputs;
		}
		
		
	}

}
