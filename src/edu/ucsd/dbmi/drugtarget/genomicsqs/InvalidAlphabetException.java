package edu.ucsd.dbmi.drugtarget.genomicsqs;


public class InvalidAlphabetException extends RuntimeException {
	
	public InvalidAlphabetException (char alphabet, String message) {
		super ("Alphabet " + alphabet + ": " + message) ;
	}	

	public InvalidAlphabetException (String message) {
		super (message) ;
	}	

}
