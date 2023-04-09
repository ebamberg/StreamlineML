package de.ebamberg.streamline.ml.activation;

import ai.djl.ndarray.NDArray;
import de.ebamberg.streamline.ml.layer.Layer;

public class TanhActivation implements Activation {

    /**
     * 
     * <p>Tanh is defined by: \( y = (e^x - e^{-x}) / (e^x + e^{-x}) \)
     *
     * @param array the input {@link NDArray}
     * @return the {@link NDArray} after applying Tanh activation
     */
    public NDArray forward(NDArray array) {
        return array.getNDArrayInternal().tanh();
    }
	
}
