package de.ebamberg.streamline.ml.data;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

public class Record {

	private Schema schema;
	private List<Object> values;

	
	
	public Record(Schema schema) {
		super();
		this.schema = schema;
		this.values=Lists.newArrayList();
	}


	public void addValue(Feature feature, Object value) {
			values.add(value);
	}

	public void updateValue(String featureName, Object value) {
		int idx=schema.indexOf(featureName);
		values.set(idx, value);
}
	
	public Object getValue(Feature feature) {
		return getValue(feature.getName());
	}
	
	public Object getValue(String featureName) {
		int idx=schema.indexOf(featureName);
		if (idx<0) 
			return null;
		return values.get(idx);
	}



	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		values.forEach(v-> {	
								String s;
								if (v!=null) {
									s=v.toString();
									s=s.substring(0, Math.min(s.length(), 20));
								} else {
									s="";
								}
								sb.append(String.format("%20s", s));
								sb.append("|");
							});
		return sb.toString();
	}


	public Schema getSchema() {
		return schema;
	}



	/**
	 * @param scheme the scheme to set
	 */
	public void setSchema(Schema scheme) {
		this.schema = scheme;
	}
	
	
	
	
}
