package de.ebamberg.streamline.ml.data;

import de.ebamberg.streamline.ml.data.Role.ClassLabelRole;
import de.ebamberg.streamline.ml.data.Role.FeatureRole;
import de.ebamberg.streamline.ml.data.Role.IgnoreRole;
import de.ebamberg.streamline.ml.data.Role.LabelRole;

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

	public static Feature<IgnoreRole> ignore(String name, Class<?> datatype) {
		return new Feature<IgnoreRole>(name,Role.IGNORE,datatype);
	}

	public static Feature<ClassLabelRole> classlabel(String name, Class<?> datatype) {
		return new Feature<ClassLabelRole>(name,Role.CLASSLABEL,datatype);
	}

	public static Feature<FeatureRole> feature(String name, Class<?> datatype) {
		return new Feature<FeatureRole>(name,Role.FEATURE,datatype);
	}

	public static Feature<LabelRole> label(String name, Class<?> datatype) {
		return new Feature<LabelRole>(name,Role.LABEL,datatype);
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
