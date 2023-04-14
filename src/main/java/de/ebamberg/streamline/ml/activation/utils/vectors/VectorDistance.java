package de.ebamberg.streamline.ml.activation.utils.vectors;

import ai.djl.ndarray.NDArray;

public class VectorDistance {


    public static float cosineSimilarity(NDArray vectorA, NDArray vectorB) {
        // NDArray dotProduct = vectorA.dot(vectorB); // not implemented in tensorflow
        NDArray dotProduct = vectorA.mul(vectorB).sum();    
      //  NDArray dotProduct = vectorA.dot(vectorB);    
        NDArray normA = vectorA.pow(2).sum().sqrt();
        NDArray normB = vectorB.pow(2).sum().sqrt(); 
        NDArray cosineSim=dotProduct.div (normA.mul(normB));
        return cosineSim.getFloat();        
    }

    public static float cosineDistance(NDArray vectorA, NDArray vectorB) {
    	return 1-cosineSimilarity(vectorA,vectorB);
    }
    
}
