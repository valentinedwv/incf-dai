/**
 * This is a service bean to write the business logic
 */
package edu.ucsd.crbs.incf.components.services.emage;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.ucsd.crbs.incf.exception.UserDefinedException;
import edu.ucsd.crbs.incf.common.INCFLogger;


/**
 * @author Asif Memon
 *
 */
public class EmageServiceBean {


	/**
	 * This method retrieves the data from the database which was entered 
	 * before running the image service
	 * 
	 * @param Value object with the data
	 * 
	 * @return void 
	 */
	public EmageServiceVO getStructureNameForStructureAbbrev( EmageServiceVO vo ) 
		throws UserDefinedException, Exception {
	
		INCFLogger.logDebug( EmageServiceBean.class,
		 " Start - EmageServiceBean.getDataForCreateImageService");
		
		try { 
		
			EmageServiceDAOImpl impl = new EmageServiceDAOImpl();
			vo = impl.getStructureNameForStructureAbbrev(vo);
			
		} catch ( Exception e ) {
			
			e.getStackTrace();
		
		}

		return vo;
		
	}


}
