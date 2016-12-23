package edu.ucsd.dbmi.drugtarget.main;

import java.util.HashMap;
import java.util.HashSet;

public interface Prediction {
	public HashMap predict(HashSet queries,HashMap associations
			,HashSet allDrug, HashSet allTarget) throws Exception;
	
	public HashMap predict(String query,HashMap associations
			,HashSet allDrug, HashSet allTarget) throws Exception;
}
