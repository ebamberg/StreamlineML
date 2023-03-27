package de.ebamberg.streamline.ml.data;

/**
 * 
 * definition of a data value in the schema
 * 
 * @author erik.bamberg@web.de
 *
 * @param <T>
 */
public class Feature<R> {

	private String name;
	
	private R role;
	
	private boolean isCollection;
		
	public Feature(String name, R role) {
		super();
		this.name  = name;
		this.role	= role;
		isCollection=false;
	}


	
	public String getName() {
		return name;
	}



	public R getRole() {
		return role;
	}

	
	/**
	 * returns a string-representation of this feature
	 */
	@Override
	public String toString() {
		return "Feature [name=" + name + ", role=" + role + "]";
	}
	
	
	
}
