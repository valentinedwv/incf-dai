/**
 * @(#) BIRNException.java
 * 
 * Copyright (C) BIRN 
 *
 */

package edu.ucsd.crbs.incf.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * INCFException is an implementation of Exception that enables Root Cause
 * Analysis by persisting an exception chain. It is the base class for all
 * exceptions used by the business logic foundation.  It also stores
 * a list of business errors.
 *
 * @author	Asif Memon
 * @version	4
 * 
 * @history	4
 * Date         Who   Description
 * -----------  ----  ---------------------------------------------
 * 10-Apr-2006	AM	  Initial Version
 * 
 */
public class INCFException extends Exception {


//-------------------------------Data Members-----------------------------------


    public static String exceptionMessage = "";
    

//------------------------------Constructors------------------------------------


    /**
     * Constructs a INCFException object.
     * 
     */
	public INCFException() {
	    super();
 	    setExceptionMessage( "" );
	}


    /**
     * Constructs a INCFException object with an exception message
	 *
     * @param msg - An Exception message
     */
	public INCFException( String message ) {
	    super( message );
 	    setExceptionMessage( message );
	}


//------------------------------Public Methods----------------------------------


    /**
     * Gets the name of the exception.
	 *
     * @return Name of the Exception as a String
     */
	public static String getExceptionMessage() {
		return exceptionMessage;
	}


    /**
     * Sets the exception name to the name of the class.
     */
	public static void setExceptionMessage(String exceptionMessage) {
		INCFException.exceptionMessage = exceptionMessage;
	}


}
