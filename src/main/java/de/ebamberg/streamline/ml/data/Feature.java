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
	
	private boolean isCategory;
		
	public Feature(String name, R role) {
		super();
		this.name  = name;
		this.role	= role;
		isCollection=false;
		isCategory=false;
	}
	
	protected Feature(Feature<R> cloneFrom) {
		super();
		this.name  = cloneFrom.name;
		this.role	= cloneFrom.role;
		isCollection=cloneFrom.isCollection;
		isCategory=cloneFrom.isCategory;
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



	public Feature<R> setCategorize(boolean b) {
		var nf=new Feature<R>(this);
		nf.isCategory=true;
		return nf;
	}
	
	
	
}
