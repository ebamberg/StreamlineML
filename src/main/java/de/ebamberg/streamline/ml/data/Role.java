package de.ebamberg.streamline.ml.data;


/**
 * 
 * defined the role of a feature or label in a dataset
 * 
 * @author erik.bamberg@web.de
 *
 */
public interface Role {

	public static final FeatureRole FEATURE= new FeatureRole();

	public static final LabelRole LABEL= new LabelRole();

	public static final ClassLabelRole CLASSLABEL= new ClassLabelRole();

	
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
