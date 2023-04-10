package de.ebamberg.streamline.ml.data.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPipeline {

	protected static final Logger log=LoggerFactory.getLogger("Pipeline");

	protected Producer<?> firstProducer;
	protected Stage<Object,?> initialStage;

	protected void clonePrivateState(AbstractPipeline parent) {
		this.firstProducer = parent.firstProducer;
		this.initialStage = parent.initialStage;
	}
	
	protected void forward(Object element) {
		if (initialStage!=null && element!=null) {
			initialStage.forward(element);
		}
	}
	
}
