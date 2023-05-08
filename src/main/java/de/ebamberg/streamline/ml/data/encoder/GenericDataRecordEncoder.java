package de.ebamberg.streamline.ml.data.encoder;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import de.ebamberg.streamline.ml.data.Record;
import de.ebamberg.streamline.ml.data.Role;

public class GenericDataRecordEncoder implements Encoder<Record> {

	@Override
	public NDArray encode(Record input) {
		var features=input.getSchema().getFeaturesByRole(Role.FEATURE);
		var labels=input.getSchema().getFeaturesByRole(Role.LABEL);

		var x=new float[features.size()];
		var y=new float[labels.size()];
		int i=0;
		for (var f : features) {
			//TODO generic datatype casting 
			x[i++]=(Float)input.getValue(f);
		}
		i=0;
		for (var l : labels) {
			//TODO generic datatype casting 
			x[i++]=(Float)input.getValue(l);
		}

		return NDManager.newBaseManager().create(x);
	}


}
