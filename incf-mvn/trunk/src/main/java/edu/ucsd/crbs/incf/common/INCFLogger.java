/*
 * @(#)BIRNLogger.java, February 14, 2006, 5:04 PM
 *
 * Company/Project - UCSD/BIRN
 * 9500 Gilman Drive, Bldg - Holly, San Diego, CA 92093 U.S.A.
 * All rights reserved.
 *
 */
package edu.ucsd.crbs.incf.common;

import java.net.URL;

//import net.INCF.common.INCFConfigurator;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Logger is a utility class used for Logging messages/errors to a datastore.
 * There are 5 levels of logging enabled - INFO, DEBUG, WARNING, ERROR, FATAL
 * 
 * @version 2.0
 * 
 * Date 					Who 				Description 
 * 14-FEB-2009 		amemon 			Initial Version
 * 
 */ 
public class INCFLogger { 
	
	//private INCFConfigurator configurator = null; 
	public static INCFConfigurator configPropertiesPath = null;
	
	
// ---------------------------------Data Members---------------------------------


	private static Logger logger = null;

	// Constant Strings
	private static final String DEBUG = "DEBUG"; 

	private static final String INFO = "INFO";

	private static final String WARN = "WARN";

	private static final String ERROR = "ERROR";

	private static final String FATAL = "FATAL";

	// DEBUG < INFO < WARN < ERROR < FATAL - This is for information only

	public static final boolean dbgOn = true;
 
	//static initializer
	static { 
		
		configPropertiesPath = INCFConfigurator.INSTANCE;
		String path = configPropertiesPath.getValue("atlas.windowslogger.filepath");
		System.out.println("INCFLogger Path is - " + path);

        PropertyConfigurator.configure( path );

	}

	// --------------------------------Public Methods--------------------------------

	/**
	 * Log this DEBUG message according to the properties set up in the Log4J
	 * configuration file.
	 * 
	 * @param debugInfo -
	 *            Message to log
	 */
	public static void logDebug(Class className, String message) {
		logger = Logger.getLogger(className);
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	} 

	/**
	 * Log this INFO message according to the properties set up in the Log4J
	 * configuration file.
	 * 
	 * @param msg
	 *            Message to log
	 */
	public static void logInfo(Class className, String message) {
		logger = Logger.getLogger(className);
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}

	/**
	 * Log this WARNING message according to the properties set up in the Log4J
	 * configuration file.
	 * 
	 * @param msg
	 *            Message to log
	 */
	public static void logWarn(Class className, String message) {
		logger = Logger.getLogger(className);
		logger.warn(message);
	}

	/**
	 * Log this ERROR message according to the properties set up in the Log4J
	 * configuration file.
	 * 
	 * @param msg
	 *            Message to log
	 */
	public static void logError(Class className, String message) {
		logger = Logger.getLogger(className);
		logger.error(message);
	}

	/**
	 * Log this ERROR message according to the properties set up in the Log4J
	 * configuration file.
	 * 
	 * @param msg
	 *            Message to log
	 * @param e
	 *            Exception to log
	 */
	public static void logError(Class className, String message, Exception e) {
		logger = Logger.getLogger(className);
		logger.error(message, e);
	}

	/**
	 * Log this FATAL message according to the properties set up in the Log4J
	 * configuration file.
	 * 
	 * @param msg Message to log
	 */
	public static void logFatal(Class className, String debugInfo) {
		logger = Logger.getLogger(className);
		logger.fatal(debugInfo);
	}

} // end of Qlogger.java
