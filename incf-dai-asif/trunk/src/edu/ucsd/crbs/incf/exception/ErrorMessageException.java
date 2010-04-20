/**
 * @(#) ErrorMessageException.java
 * 
 * Copyright (C) INCF 
 *
 */

package edu.ucsd.crbs.incf.exception;

import java.util.ArrayList;

/**
 * BIRNException is an implementation of Exception that enables Root Cause
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
public class ErrorMessageException extends Exception {


//-------------------------------Data Members-----------------------------------


    protected ArrayList	stack = new ArrayList();
	protected boolean	showExceptionChain = false;
	protected String	exceptionName = "ErrorMessageException";
    protected ArrayList errorList = null;
    private String exceptionMessage = "";
    

//------------------------------Constructors------------------------------------


    /**
     * Constructs a ErrorMessageException object.
     * 
     */
	public ErrorMessageException() {
	    super();
	}


    /**
     * Constructs a ErrorMessageException object with an exception message
	 *
     * @param msg - An Exception message
     */
	public ErrorMessageException( String message ) {
	    super( message );
 	    setExceptionMessage( message );
	}


//----------------------------public Methods---------------------------------


    /**
     * Gets the exception Message.
     */
    public String getExceptionMessage() {
        return this.exceptionMessage;
    }

    
    /**
     * Sets the exception name to the name of the class.
     */
    public void setExceptionMessage( String message ) {
        this.exceptionMessage = message;
    }

    
}