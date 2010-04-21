/**
 * This class is used to deal with the database like create connection, query, etc.
 */
package edu.ucsd.crbs.incf.components.services.emage; 


import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.ucsd.crbs.incf.common.INCFConfigurator;
import edu.ucsd.crbs.incf.common.INCFLogger;
import edu.ucsd.crbs.incf.common.BaseDAO;
import edu.ucsd.crbs.incf.exception.UserDefinedException;
import edu.ucsd.crbs.incf.components.services.ucsd.UCSDServiceVO;
import edu.ucsd.crbs.incf.exception.ErrorMessageException;
 

/**
 * @author Asif Memon
 *
 */
public class EmageServiceDAOImpl extends BaseDAO {
	
	private INCFConfigurator configurator = INCFConfigurator.INSTANCE;

	
	/**
	 * This method retrieves the complete name for the structure based on the Structure Abbreviation 
	 * 
	 * @param Value object with the data
	 * 
	 * @return void 
	 */
	public EmageServiceVO getStructureNameForStructureAbbrev( EmageServiceVO vo ) 
		throws SQLException, Exception {

	INCFLogger.logDebug( EmageServiceDAOImpl.class,
	 					 " Start - EmageServiceDAOImpl.getStructureNameForStructureAbbrev");

	Connection conn = getStandAloneConnection();
	Statement stmt = conn.createStatement();
	StringBuffer query = new StringBuffer();

	try { 

		query.append( " select distinct(STRUCTURE_NAME) from STRUCTURES_ABA " )
			 .append( " where ABBREVIATION = '"+vo.getStructureAbbrev()+"' " );

		INCFLogger.logDebug(EmageServiceDAOImpl.class,
			"getDataForCreateImageService - Query is - " + query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 

		while ( rs.next() ) {

			vo.setStructureName( rs.getString("STRUCTURE_NAME") );

			INCFLogger.logDebug( EmageServiceDAOImpl.class,
			         "Structure Name is : " + vo.getStructureName());

		}

	} catch ( SQLException sqle ) {
		
        throw new SQLException( "SQLException thrown " +
        "From getStatusReport(): " + sqle.getMessage() );
    
	} catch ( Exception e ) {
	
		INCFLogger.logDebug(EmageServiceDAOImpl.class,
				"Generic Exception: " + e.getStackTrace() );
    
	} finally {

		stmt.close();
		conn.close();

	}

	INCFLogger.logDebug( EmageServiceDAOImpl.class,
						" End - EmageServiceDAOImpl.getStructureNameForStructureAbbrev() " );

	return vo;

	}

	
}
