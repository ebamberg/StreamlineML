package de.ebamberg.streamline.ml.data;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

public class Record {

	private Schema scheme;
	private List<Object> values;

	
	
	public Record(Schema scheme) {
		super();
		this.scheme = scheme;
		this.values=Lists.newArrayList();
	}


	public void addValue(Feature feature, Object value) {
			values.add(value);
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
	
	
	
	
}
