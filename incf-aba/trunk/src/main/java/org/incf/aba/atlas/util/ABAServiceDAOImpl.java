/**
 * This class is used to deal with the database like create connection, query, etc.
 */
package org.incf.aba.atlas.util; 

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * @author Asif Memon
 *
 */
public class ABAServiceDAOImpl {
	
	private ABAConfigurator configurator = ABAConfigurator.INSTANCE;

	public ArrayList getSRSsData() {
		
		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();
		String srsName = "'"+configurator.getValue("srsname.abareference.10")+"','"+configurator.getValue("srsname.abavoxel.10")+"','"+configurator.getValue("srsname.agea.10")+"'";

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgres();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from srs where srs_name in (" + srsName + ") " );

		System.out.println("getSRSData - Query is - " + query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		ABAServiceVO vo = null;

		while ( rs.next() ) {

			vo = new ABAServiceVO();

			vo.setSrsCode(rs.getString("SRS_CODE"));
			vo.setSrsName(rs.getString("SRS_Name"));
			vo.setSrsDescription(rs.getString("SRS_DESCRIPTION"));
			vo.setSrsAuthorCode(rs.getString("SRS_AUTHOR_CODE"));
			vo.setSrsDateSubmitted(rs.getString("SRS_DATE_SUBMITTED"));
			vo.setOrigin(rs.getString("ORIGIN"));
			vo.setUnitsAbbreviation(rs.getString("UNITS_ABBREVIATION"));
			vo.setUnitsName(rs.getString("UNITS_NAME"));
			vo.setNeuroPlusXCode(rs.getString("NEURO_PLUS_X_CODE"));
			vo.setNeuroMinusXCode(rs.getString("NEURO_MINUS_X_CODE"));
			vo.setNeuroPlusYCode(rs.getString("NEURO_PLUS_Y_CODE"));
			vo.setNeuroMinusYCode(rs.getString("NEURO_MINUS_Y_CODE"));
			vo.setNeuroPlusZCode(rs.getString("NEURO_PLUS_Z_CODE"));
			vo.setNeuroMinusZCode(rs.getString("NEURO_MINUS_Z_CODE"));
			vo.setSourceURI(rs.getString("SOURCE_URI"));
			vo.setSourceFileFormat(rs.getString("SOURCE_FILE_FORMAT"));
			vo.setSrsAbstract(rs.getString("ABSTRACT"));
			vo.setDerivedFromSRSCode(rs.getString("DERIVED_FROM_SRS_CODE"));
			vo.setDerivedMethod(rs.getString("DERIVED_METHOD"));
			vo.setSpecies(rs.getString("SPECIES"));
			vo.setSrsBase(rs.getString("SRS_BASE"));
			vo.setRegionOfValidity(rs.getString("REGION_OF_VALIDITY"));
			vo.setRegionURI(rs.getString("REGION_URI"));
			vo.setSrsVersion(rs.getString("SRS_VERSION"));
			vo.setDimensionMinX(rs.getString("DIMENSION_MIN_X"));
			vo.setDimensionMaxX(rs.getString("DIMENSION_MAX_X"));
			vo.setDimensionMinY(rs.getString("DIMENSION_MIN_Y"));
			vo.setDimensionMaxY(rs.getString("DIMENSION_MAX_Y"));
			vo.setDimensionMinZ(rs.getString("DIMENSION_MIN_Z"));
			vo.setDimensionMaxZ(rs.getString("DIMENSION_MAX_Z"));

			list.add(vo);
			
		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList getDescribeSRSData(String srsName) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();
		//String srsName = "'"+configurator.getValue("srsname.abareference.10")+"','"+configurator.getValue("srsname.abavoxel.10")+"','"+configurator.getValue("srsname.agea.10")+"'";

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgres();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from srs where srs_name in ('" + srsName + "') " ); 

		System.out.println("getSRSData - Query is - " + query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		ABAServiceVO vo = null;

		while ( rs.next() ) {

			vo = new ABAServiceVO();

			vo.setSrsCode(rs.getString("SRS_CODE"));
			vo.setSrsName(rs.getString("SRS_Name"));
			vo.setSrsDescription(rs.getString("SRS_DESCRIPTION"));
			vo.setSrsAuthorCode(rs.getString("SRS_AUTHOR_CODE"));
			vo.setSrsDateSubmitted(rs.getString("SRS_DATE_SUBMITTED"));
			vo.setOrigin(rs.getString("ORIGIN"));
			vo.setUnitsAbbreviation(rs.getString("UNITS_ABBREVIATION"));
			vo.setUnitsName(rs.getString("UNITS_NAME"));
			vo.setNeuroPlusXCode(rs.getString("NEURO_PLUS_X_CODE"));
			vo.setNeuroMinusXCode(rs.getString("NEURO_MINUS_X_CODE"));
			vo.setNeuroPlusYCode(rs.getString("NEURO_PLUS_Y_CODE"));
			vo.setNeuroMinusYCode(rs.getString("NEURO_MINUS_Y_CODE"));
			vo.setNeuroPlusZCode(rs.getString("NEURO_PLUS_Z_CODE"));
			vo.setNeuroMinusZCode(rs.getString("NEURO_MINUS_Z_CODE"));
			vo.setSourceURI(rs.getString("SOURCE_URI"));
			vo.setSourceFileFormat(rs.getString("SOURCE_FILE_FORMAT"));
			vo.setSrsAbstract(rs.getString("ABSTRACT"));
			vo.setDerivedFromSRSCode(rs.getString("DERIVED_FROM_SRS_CODE"));
			vo.setDerivedMethod(rs.getString("DERIVED_METHOD"));
			vo.setSpecies(rs.getString("SPECIES"));
			vo.setSrsBase(rs.getString("SRS_BASE"));
			vo.setRegionOfValidity(rs.getString("REGION_OF_VALIDITY"));
			vo.setRegionURI(rs.getString("REGION_URI"));
			vo.setSrsVersion(rs.getString("SRS_VERSION"));
			vo.setDimensionMinX(rs.getString("DIMENSION_MIN_X"));
			vo.setDimensionMaxX(rs.getString("DIMENSION_MAX_X"));
			vo.setDimensionMinY(rs.getString("DIMENSION_MIN_Y"));
			vo.setDimensionMaxY(rs.getString("DIMENSION_MAX_Y"));
			vo.setDimensionMinZ(rs.getString("DIMENSION_MIN_Z"));
			vo.setDimensionMaxZ(rs.getString("DIMENSION_MAX_Z"));

			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public ArrayList getOrientationData() {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgres();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from orientation " );

		System.out.println("getOrientation - Query is - " + query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		ABAServiceVO vo = null;

		while ( rs.next() ) {

			vo = new ABAServiceVO();

			vo.setOrientationCode(rs.getString("ORIENTATION_CODE"));
			vo.setOrientationName(rs.getString("ORIENTATION_NAME"));
			vo.setOrientationDescription(rs.getString("ORIENTATION_DESCRIPTION"));
			vo.setOrientationAuthor(rs.getString("AUTHOR"));
			vo.setOrientationDateSubmitted(rs.getString("DATE_SUBMITTED"));

			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public ArrayList getSliceData( ABAServiceVO vo ) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgres();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from slice where space_code = '"+vo.getSpaceCode()+"' " );

		System.out.println("getSliceData - Query is - " + query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		//ABAServiceVO vo = null;

		while ( rs.next() ) {

			vo = new ABAServiceVO();

			vo.setSpaceCode(rs.getString("space_code"));
			vo.setSlideValueOrigin(rs.getString("slide_value_origin"));
			vo.setValueDirection(rs.getString("value_direction"));
			vo.setRightDirection(rs.getString("right_direction"));
			vo.setUpDirection(rs.getString("up_direction"));
			vo.setPlusX(rs.getString("plus_x"));
			vo.setPlusY(rs.getString("plus_y"));
			vo.setPlusZ(rs.getString("plus_z"));
			vo.setSliceID(rs.getString("slice_id"));
			vo.setSlideValue(rs.getString("slide_value"));

			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String[] getCoordinateRangeForSRS( String srsName ) { 

		BaseDAO dao = new BaseDAO();
		String[] coordinatesRange = new String[6];
		
		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgres();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select dimension_min_x, dimension_max_x, dimension_min_y, dimension_max_y, dimension_min_z, dimension_max_z " )
		.append(" from srs ") 
		.append(" where srs_name = '"+srsName+"' "); 

		System.out.println("Query is - " + query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 

		while ( rs.next() ) {

			coordinatesRange[0] = rs.getString("dimension_min_x");
			coordinatesRange[1] = rs.getString("dimension_max_x");
			coordinatesRange[2] = rs.getString("dimension_min_y");
			coordinatesRange[3] = rs.getString("dimension_max_y");
			coordinatesRange[4] = rs.getString("dimension_min_z");
			coordinatesRange[5] = rs.getString("dimension_max_z");

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return coordinatesRange;
	}

}
