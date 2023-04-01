package de.ebamberg.streamline.ml.data.pipeline;

import java.util.Iterator;


public interface IterableStage<I, O extends Iterator<?>> extends Stage<I, O> {

}
