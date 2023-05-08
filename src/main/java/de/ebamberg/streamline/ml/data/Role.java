package de.ebamberg.streamline.ml.data;


/**
 * 
 * defined the role of a feature or label in a dataset
 * 
 * @author erik.bamberg@web.de
 *
 */
public interface Role {

	public static final Role FEATURE= new FeatureRole();

	public static final Role LABEL= new LabelRole();

	public static final Role CLASSLABEL= new ClassLabelRole();

	public static final Role IGNORE= new IgnoreRole();

	public static class IgnoreRole implements Role {	
	}

	
	public static class FeatureRole implements Role {	
	}
	
	public static class LabelRole implements Role {
	}

	/**
	 * target for classification training tasks
	 * 
	 * @author erik.bamberg@web.de
	 *
	 */
	public static class ClassLabelRole extends LabelRole {
	}
	
}
