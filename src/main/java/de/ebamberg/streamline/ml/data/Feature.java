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
	
	private Class<?> datatype;
	
	private boolean isCollection;
	
	private boolean categorical;
		
	public Feature(String name, R role, Class<?> datatype) {
		super();
		this.name  		= name;
		this.role		= role;
		this.datatype	= datatype;
		isCollection	= false;
		categorical		= false;
	}
	
	protected Feature(Feature<R> cloneFrom) {
		super();
		this.name  		= cloneFrom.name;
		this.role		= cloneFrom.role;
		this.datatype 	= cloneFrom.datatype;
		isCollection	= cloneFrom.isCollection;
		categorical		= cloneFrom.categorical;
	}
	
	public static <R> Feature<R> of(String name, R role, Class<?> datatype) {
		return new Feature<R>(name,role,datatype);
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



	public Feature<R> setCategorical(boolean b) {
		var nf=new Feature<R>(this);
		nf.categorical=true;
		return nf;
	}

	/**
	 * @return the categorical
	 */
	public boolean isCategorical() {
		return categorical;
	}
	
	
	
}
