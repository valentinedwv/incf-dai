/**
 * <p>Title: SmartAtlas Data Access Servlet</p>
 * <p>Description: SmartAtlas Data Access Servlet</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: INCF-CC, UCSD</p>
 * @author Haiyun He
 * @version 1.0
 */
package edu.ucsd.crbs.incf.components.services.whs;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucsd.crbs.incf.common.CommonServiceVO;
import edu.ucsd.crbs.incf.common.INCFConfigurator;
import edu.ucsd.crbs.incf.common.INCFLogger;
import edu.ucsd.crbs.incf.util.INCFUtil;

/**
 * AtlasTableInfoObject
 * 
 * The main servlet class to handle atlas related database access function (the
 * main purpose is to hide the direct connection to database)
 */ 
public class WHSServiceController extends HttpServlet {
	
	private INCFConfigurator incfConfig = INCFConfigurator.INSTANCE;
	INCFUtil incfUtil = new INCFUtil();

	/**
	 * Get configuration parameters and initiliaze
	 * 
	 * @param config
	 *            ServletConfig
	 * @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String servletName = getServletName();

		try {

			INCFLogger.logDebug(WHSServiceController.class,
					"Welcome to the WHS Servlet");
	
		} catch (Exception ex) {
			log("Exception in init()...", ex);
			throw new ServletException("Wrapped init() Exception", ex);
		}

	}


	/**
	 * The main service method of the servlet (Handles all the requests)
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		java.util.Vector v = new java.util.Vector();

		PrintWriter out = response.getWriter();
		
		String responseString = "";

		// Get the function name
		String functionName = request.getParameter("request");
		System.out.println(" Request is - " + functionName );

		String outputType = request.getParameter("output");
		System.out.println(" Output Type is - " + outputType );

		try {
		
			if (functionName.equalsIgnoreCase("GetCapabilities")) {
			
				System.out.println(" Inside GetCapabilities WPS at server side" );
				getCapabilitiesWPS( response );
			
			} else if (functionName.equalsIgnoreCase("DescribeProcess")) {
			
				System.out.println(" Inside Describe Process WPS at server side" );
				describeProcessWPS( response );
			
			} else if (functionName.equalsIgnoreCase("GetStructureNameByPOI")) {

				System.out.println(" WHS - Inside GetStructureName By POI at server side" );
				responseString = getStructureNameByPOI( request, response );

/*				String filter = request.getParameter("filter");
				String filterValue = filter.replaceAll("structureset:", "").trim();
				System.out.println("Structure Set - " + filterValue);
				if (filterValue.equalsIgnoreCase("Fine")) {
					System.out.println(" WHS - Inside GetStructureName By POI at server side" );
					responseString = getStructureNameByPOI( request, response );
				} else if (filterValue.equalsIgnoreCase("Anatomic")) {
					System.out.println(" WHS - Inside Get Anatomic StructureName By POI at server side" );
					//responseString = getAnatomicStructureNameByPOI( request, response );
				}
*/
			} else if (functionName.equalsIgnoreCase("spaceTransformation")) {
				spaceTransformation();
			} else if (functionName.equalsIgnoreCase("openMap")) {
				openMap();
			}

			if ( outputType == null || outputType.trim().equalsIgnoreCase("") ) {
				System.out.println("OutputType is NULL");
				out.println("Output Type is missing. Please provide output type.");
			} else if ( outputType.trim().equalsIgnoreCase("txt") ||
					    outputType.trim().equalsIgnoreCase("xml") ) {
				System.out.println("OutputType is TEXT");
				out.println(responseString);
			} else if ( outputType.trim().equalsIgnoreCase("html") ) {
				System.out.println("OutputType is HTML");

				if (responseString.trim().startsWith("http:")) { 
					response.sendRedirect(responseString.toString().trim());
				} else {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
				dispatcher.forward(request,response);
				}
			}
			System.out.println("Ends redirectiing on the webpage...");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}


	public static void main( String args[] ) {
		
		String[] abbrevs = {"M1, M2, M3"};
		String[] rels = {"SELF, SIBLING, PARENT, CHILD"}; 
		String category = "MOUSE";
		WHSServiceController helper = new WHSServiceController();
		
		try {
			//helper.getLabelSet( );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
		//http://132.239.131.188:8080/incf-services/service/WHSServiceController?request=GetStructureNameByPOI&srscode=WHS&x=255&y=511&z=255&vocabulary=WHS&output=html
		public String getStructureNameByPOI( HttpServletRequest request, HttpServletResponse response ) {

		System.out.println("Start - getStructureNameByPOI Method...");

		//Set the content type
		response.setContentType("text/html");

		//1) Define and Get parameters from URL
		System.out.println(" Parameters... " );
		String fromSpaceName = request.getParameter("SRSCode");
		String coordinateX = incfUtil.getRoundCoordinateValue(request.getParameter("x"));
		String coordinateY = incfUtil.getRoundCoordinateValue(request.getParameter("y"));
		String coordinateZ = incfUtil.getRoundCoordinateValue(request.getParameter("z"));
		String vocabulary = request.getParameter("vocabulary");

		String structureName = "";
		StringBuffer responseString = new StringBuffer();

		//Start - Exception Handling 
		if ( fromSpaceName == null || fromSpaceName.trim().equals("") ) {
			responseString.append("SRS Code is missing. Please provide the srs code");
			return responseString.toString();
		}
		
/*		if ( !fromSpaceName.trim().equalsIgnoreCase("whs") && !fromSpaceName.trim().equalsIgnoreCase("abavoxel")) {
			responseString.append("Incorrect SRS Code. Please provide correct srs code");
			return responseString.toString();
		}
*/
		if ( vocabulary == null || vocabulary.trim().equals("") ) {
			responseString.append("Vocabulary is missing. Please provide the vocabulary");
			return responseString.toString();
		}
		
		if ( !vocabulary.trim().equalsIgnoreCase("whs") && !vocabulary.trim().equalsIgnoreCase("abavoxel")) {
			responseString.append("Incorrect Vocabulary. Please provide correct vocabulary");
			return responseString.toString();
		}

		if ( coordinateX == null || coordinateX.trim().equals("") ) {
			responseString.append("Coordinate X is missing. Please provide Coordinate X");
			return responseString.toString();
		}

		if ( coordinateY == null || coordinateY.trim().equals("") ) {
			responseString.append("Coordinate Y is missing. Please provide Coordinate Y");
			return responseString.toString();
		}

		if ( coordinateZ == null || coordinateZ.trim().equals("") ) {
			responseString.append("Coordinate Z is missing. Please provide Coordinate Z");
			return responseString.toString();
		}

		if ( fromSpaceName.equalsIgnoreCase("abavoxel") ) {
			
			System.out.println("Inside ABAVoxel original coordinates transformation");
			INCFUtil util1 = new INCFUtil();
			
			CommonServiceVO vo = new CommonServiceVO();
			vo.setFromSRSCode(fromSpaceName);
			vo.setToSRSCode("whs");
			vo.setFromSRSCodeOne(fromSpaceName);
			vo.setToSRSCodeOne("whs");
			vo.setOriginalCoordinateX(coordinateX);
			vo.setOriginalCoordinateY(coordinateY);
			vo.setOriginalCoordinateZ(coordinateZ);

			//Special step to translate coordinates from abavoxel to whs
			String transformedCoordinatesString = util1.spaceTransformation(vo );

			String[] transformedCoordinates = util1.getTabDelimNumbers(transformedCoordinatesString);

			System.out.println("Value X - " + transformedCoordinates[0] );

			//Exception Handling
			if ( transformedCoordinates[0].trim().equalsIgnoreCase("out") ) { 
				System.out.println("Inside Value X - " + transformedCoordinates[0] );
				responseString.append("Out of Range");
				return responseString.toString();
			}

			coordinateX = transformedCoordinates[0];
			coordinateY = transformedCoordinates[1];
			coordinateZ = transformedCoordinates[2];

			System.out.println("Inside WHS Coordinate X - " + coordinateX );
			System.out.println("Inside WHS Coordinate Y - " + coordinateY );
			System.out.println("Inside WHS Coordinate Z - " + coordinateZ );

		}

		try { 

			System.out.println("Starts Transformation matrix process...");

			//2) Get the transformed coordinates from Steve's program
			//http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/structure_lookup.cgi?atlas=aba&x=264&y=160&z=228
			//http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/canon_lookup.cgi?x=264&y=160&z=228
			INCFUtil util = new INCFUtil();
			
			System.out.println("Coordinate X - " + coordinateX );
			System.out.println("Coordinate Y - " + coordinateY );
			System.out.println("Coordinate Z - " + coordinateZ );
			
			String structureNamesString = util.getStructureNameLookup("whs", coordinateX, coordinateY, coordinateZ );

			String[] structureNames = util.getTabDelimNumbers(structureNamesString);

			structureName = structureNames[0];
			String outOfRange = structureNames[2];

			//Start - "out of range"
			if ( outOfRange != null && outOfRange.trim().equalsIgnoreCase("of") ) {
				responseString.append("Out of Range");
			} else if ( structureName.trim().equals("-") ) {
				responseString.append("No data found");
			} else if ( !structureName.trim().equals("") || structureName != null ) {
				responseString.append("Structure Name: ".concat(structureName));
			} 

			System.out.println("Structure Name - " + responseString);

			//End
			System.out.println("Ends running transformation  matrix...");
			
		} catch ( Exception e ) {

			e.printStackTrace();
			responseString.append("Please contact the administrator to resolve this issue");

		} finally {

		}

		System.out.println("End - getStructureNameByPOI Method...");

		//4) Return response back to the client in a text/xml format
		return responseString.toString();

	}
	
	
	public void getCapabilitiesWPS( HttpServletResponse response ) {

		StringBuffer sb = new StringBuffer();

		try {

			System.out.println("Inside getCapabilities Method...");

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();

			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"); 

			out.println("<wps:Capabilities xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps\" xmlns:wps=\"http://www.opengis.net/wps\" xmlns:ows=\"http://www.opengis.net/ows\" version=\"1.0.0\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 ../wpsGetCapabilities_request.xsd\">\n");

			out.println("<ows:ServiceIdentification xmlns:ows=\"http://www.opengis.net/ows\">\n");
			out.println("<ows:Title>WHS Services</ows:Title>\n"); 
			out.println("<ows:Abstract>WHS Services are created to access various WHS atlas space features that are offered by UCSD to its clients.</ows:Abstract>\n"); 
			out.println("<ows:ServiceType>WPS</ows:ServiceType>\n"); 
			out.println("<ows:ServiceTypeVersion>0.2.4</ows:ServiceTypeVersion>\n"); 
			out.println("<ows:Fees>NONE</ows:Fees> \n"); 
			out.println("<ows:AccessConstraints>NONE</ows:AccessConstraints>\n"); 
			out.println(" </ows:ServiceIdentification>\n"); 

			out.println("<ows:ServiceProvider>\n"); 
			out.println("<ows:ProviderName>Asif Memon</ows:ProviderName>\n");  
			out.println("<ows:ServiceContact>amemon@ncmir.ucsd.edu</ows:ServiceContact>\n");  
			out.println("</ows:ServiceProvider>\n"); 

			out.println("<ows:OperationsMetadata>\n"); 
			out.println("<ows:Operation name=\"GetCapabilities\" />\n");  
			out.println("</ows:OperationsMetadata> \n"); 

			out.println("<wps:ProcessOfferings>\n"); 

			out.println("<wps:Process>\n"); 
			out.println("<ows:Identifier>GetStructureNameByPOI</ows:Identifier>\n");  
			out.println("<ows:Title>Get structure name by POI</ows:Title> \n"); 
			out.println("<ows:Description>Returns structures segmented for the point of interest on map</ows:Description>\n");  
			out.println("</wps:Process>\n"); 
 
			out.println("<wps:Process>\n"); 
			out.println("<ows:Identifier>SpaceTransformation</ows:Identifier>\n");  
			out.println("<ows:Title>Atlas Space Transformation from WHS to AGEA, and AGEA to WHS</ows:Title> \n"); 
			out.println("<ows:Description>Finds, transforms and executes the best transformation available from WHS to AGEA, and AGEA to WHS</ows:Description>\n");  
			out.println("</wps:Process>\n"); 

			out.println("</wps:ProcessOfferings>\n"); 

			out.println("<wps:Languages>\n"); 
			out.println("<wps:Default>\n"); 
			out.println("<ows:Language>en-CA</ows:Language>\n");  
			out.println("</wps:Default>\n"); 
			out.println("<wps:Supported>\n"); 
			out.println("<ows:Language>en-CA</ows:Language>\n");  
			out.println("</wps:Supported>\n"); 
			out.println("</wps:Languages>\n"); 

			out.println("</wps:Capabilities>\n");
			System.out.println("Get Capabilities - " + sb.toString() );

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}


	private void describeProcessWPS( HttpServletResponse response ) {

		StringBuffer sb = new StringBuffer();

		try {

			sb = new StringBuffer();

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();

			out.println( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			out.println( "<wps:ProcessDescriptions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps\" xmlns:wps=\"http://www.opengis.net/wps\" xmlns:ows=\"http://www.opengis.net/ows\" version=\"1.0.0\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 ../wpsGetCapabilities_request.xsd\">\n");

			out.println( "<ProcessDescription>\n");
			out.println( "<ows:Identifier>GetStructureNameByPOI</ows:Identifier>\n"); 
			out.println( "<ows:Title>Get structure name by POI</ows:Title>\n"); 
			out.println( "<ows:Description>Returns structures segmented for the point of interest</ows:Description>\n"); 
			out.println( "<DataInputs>\n");
			out.println( "<Input>\n");
			  out.println( "<ows:Identifier>MapPoint</ows:Identifier>\n");
			  out.println( "<ows:Title>Map point</ows:Title>\n");
			  out.println( "<ows:Abstract>Point of interest on the map</ows:Abstract>\n");
			  out.println( "<LiteralData>\n");
			  out.println( "<ows:DataType ows:reference=\"xs:double\"/>\n");
			  out.println( "</LiteralData>\n");
			out.println( "</Input>\n");
			out.println( "<Input>\n");
				out.println( "<ows:Identifier>SRSCODE</ows:Identifier>\n");
				out.println( "<ows:Title>Atlas Space Name</ows:Title>\n");
				out.println( "<ows:Abstract>Name of an atlas space such as WHS</ows:Abstract>\n");
				out.println( "<LiteralData>\n");
				out.println( "<ows:DataType ows:reference=\"xs:string\"/>\n");
			    out.println( "</LiteralData>\n");
			out.println( "</Input>\n");  
			out.println( "</DataInputs>\n");
			out.println( "<ProcessOutputs>\n");
			out.println( "<Output>\n");
			out.println( "<ows:Identifier>StructureName</ows:Identifier>\n");
			out.println( "<ows:Title>Structure Name</ows:Title>\n");
			out.println( "<ows:Abstract>Structures segmented for the given point of interest</ows:Abstract>\n");
			out.println( "<ComplexOutput>\n");
			out.println( "<Default>\n");
			out.println( "<Format>\n");
			out.println( "<MimeType>text/ascii</MimeType>\n");
			out.println( "<MimeType>http/ascii</MimeType>\n");
			out.println( "</Format>\n");
			out.println( "</Default>\n");
			out.println( "</ComplexOutput>\n");
			out.println( "</Output>\n");
			out.println( "</ProcessOutputs>\n");
			out.println( "</ProcessDescription>\n");

			out.println( "<ProcessDescription>\n");
			  out.println( "<ows:Identifier>AtlasSpaceTransformation</ows:Identifier>\n"); 
			  out.println( "<ows:Title>Atlas Space Transformation</ows:Title>\n"); 
			  out.println( "<ows:Description>Finds, transforms and executes the best transformation available in the space based on the requirement</ows:Description>\n"); 
			  out.println( "<DataInputs>\n");
			  out.println( "<Input>\n");
			  out.println( "<ows:Identifier>MapPoint</ows:Identifier>\n");
			  out.println( "<ows:Title>Map Point</ows:Title>\n");
			  out.println( "<ows:Abstract>Coordinates to form a map point</ows:Abstract>\n");
			  out.println( "<LiteralData>\n");
			  out.println( "<ows:DataType ows:reference=\"xs:string\"/>\n");
			  out.println( "</LiteralData>\n");
			  out.println( "</Input>\n"); 
			  out.println( "<Input>\n");
			  out.println( "<ows:Identifier>FromSRSCODE</ows:Identifier>\n");
			out.println( "<ows:Title>Source Atlas Space Name</ows:Title>\n");
			out.println( "<ows:Abstract>Name of an atlas space such as WHS, or AGEA</ows:Abstract>\n");
			out.println( "<LiteralData>\n");
			out.println( "<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println( "</LiteralData>\n");
			out.println( "</Input>\n");  
			out.println( "<Input>\n");
			  out.println( "<ows:Identifier>ToSRSCODE</ows:Identifier>\n");
			out.println( "<ows:Title>Destination Atlas Space Name</ows:Title>\n");
			out.println( "<ows:Abstract>Name of an atlas space such as AGEA, or WHS</ows:Abstract>\n");
			out.println( "<LiteralData>\n");
			out.println( "<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println( "</LiteralData>\n");
			out.println( "</Input>\n");  
			out.println( "</DataInputs>\n");
			 out.println( "<ProcessOutputs>\n");
			out.println( "<Output>\n");
			out.println( "<ows:Identifier>TransformedGeometry</ows:Identifier>\n");
			out.println( "<ows:Title>Transformed Geometry</ows:Title>\n");
			out.println( "<ows:Abstract>Returns transformed geometries</ows:Abstract>\n");
			out.println( "<ComplexOutput>\n");
			out.println( "<Default>\n");
			out.println( "<Format>\n");
			out.println( "<MimeType>text/ascii</MimeType>\n");
			out.println( "</Format>\n");
			out.println( "</Default>\n");
			out.println( "</ComplexOutput>\n");
			out.println( "</Output>\n");
			out.println( "</ProcessOutputs>\n");
			out.println( "</ProcessDescription>\n");

			out.println( "</wps:ProcessDescriptions>\n");

			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	    System.out.println( "Get Capabilities - " + sb.toString() );
	    
	}


	public void getStructure() {
	}
	
	public void spaceTransformation() {
	}

	public void openMap() {
	}


	/**
	 * Clean up the resources
	 * 
	 * @throws ServletException
	 */
	public void destory() throws ServletException {
		try {
		} catch (Exception ex) {
			throw new ServletException("Wrapped service() Exception", ex);
		}
	}
}
