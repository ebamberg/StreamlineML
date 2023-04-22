package de.ebamberg.streamline.ml.api.results;

public class TokenPropability {

	private float score;
	private long token;
	private String sequence;
	/**
	 * @return the score
	 */
	public float getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(float score) {
		this.score = score;
	}
	/**
	 * @return the token
	 */
	public long getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(long token) {
		this.token = token;
	}
	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	@Override
	public String toString() {
		return "TokenPropability [score=" + score + ", token=" + token + ", sequence=" + sequence + "]";
	}

	
	
	
}
