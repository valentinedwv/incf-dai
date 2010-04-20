/**
 * <p>Title: SmartAtlas Data Access Servlet</p>
 * <p>Description: SmartAtlas Data Access Servlet</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: INCF-CC, UCSD</p>
 * @author Haiyun He
 * @version 1.0
 */
package edu.ucsd.crbs.incf.components.services.emage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

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
public class EmageServiceController extends HttpServlet {

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

			INCFLogger.logDebug(EmageServiceController.class,
					"Welcome to the EMAGE Servlet");

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
		System.out.println(" Request is - " + functionName);

		String outputType = request.getParameter("output");
		System.out.println(" Output Type is - " + outputType);

		try {

			if (functionName.equalsIgnoreCase("GetGenesByPOI")) {

				System.out.println(" Inside Get Genes By POI at server side");
				responseString = getGenesByPOI(request, response);

			} else if (functionName.equalsIgnoreCase("GetCapabilities")) {

				System.out
						.println(" Inside GetCapabilities WPS at server side");
				getCapabilitiesWPS(response);

			} else if (functionName.equalsIgnoreCase("DescribeProcess")) {

				System.out
						.println(" Inside Describe Process WPS at server side");
				describeProcessWPS(response);

			} else if (functionName.equalsIgnoreCase("GetGenesByStructure")) {

				System.out.println(" Inside Get Genes By POI at server side");
				responseString = getGenesByStructure(request, response);

			} else if (functionName.equalsIgnoreCase("spaceTransformation")) {
				spaceTransformation();
			} else if (functionName.equalsIgnoreCase("openMap")) {
				openMap();
			}

			if (outputType == null || outputType.trim().equalsIgnoreCase("")) {
				System.out.println("OutputType is NULL");
				out.println("Output Type is missing. Please provide output type.");
			} else if (outputType.trim().equalsIgnoreCase("txt")
					|| outputType.trim().equalsIgnoreCase("xml")) {
				System.out.println("OutputType is TEXT");
				out.println(responseString);
			} else if (outputType.trim().equalsIgnoreCase("html")) {
				System.out.println("OutputType is HTML");

				if (responseString.trim().startsWith("http:")) {
					response.sendRedirect(responseString.toString().trim());
				} else {
					request.setAttribute("response", responseString);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher(nextJSP);
					dispatcher.forward(request, response);
				}
			}

			System.out.println("Ends redirectiing on the webpage...");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

	public static void main(String args[]) {

		String[] abbrevs = { "M1, M2, M3" };
		String[] rels = { "SELF, SIBLING, PARENT, CHILD" };
		String category = "MOUSE";
		EmageServiceController helper = new EmageServiceController();

		try {
			// helper.getLabelSet( );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Rest Service -
	// http://mouse.brain-map.org/agea/GeneFinder.html?seedPoint=8400,4200,3800
	public String getGenesByPOI(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - GetGenesByPOI Method...");

		// Set the content type
		response.setContentType("text/html");

		// http://mouse.brain-map.org/agea/all_coronal?correlation&seedPoint=8375,4150,3800
		// 1) Define and Get parameters from URL
		// Define config Properties
		String emageHostName = incfConfig.getValue("incf.emage.host.name");
		String emagePortNumber = incfConfig.getValue("incf.emage.port.number");
		String emageServicePath = incfConfig
				.getValue("incf.emage.service.path");

		System.out.println(" Parameters... ");
		String fromSpaceName = request.getParameter("SRSCode");
		String coordinateX = incfUtil.getRoundCoordinateValue(request
				.getParameter("x"));
		String coordinateY = incfUtil.getRoundCoordinateValue(request
				.getParameter("y"));
		String coordinateZ = incfUtil.getRoundCoordinateValue(request
				.getParameter("z"));

		String fineStructureName = "";
		String anatomicStructureName = "";
		String structureName = "";

		StringBuffer responseString = new StringBuffer();

		// Start - Exception Handling
		if (fromSpaceName == null || fromSpaceName.trim().equals("")) {
			responseString
					.append("SRS Code is missing. Please provide the srs code");
			return responseString.toString();
		}

		if (coordinateX == null || coordinateX.trim().equals("")) {
			responseString
					.append("Coordinate X is missing. Please provide Coordinate X");
			return responseString.toString();
		}

		if (coordinateY == null || coordinateY.trim().equals("")) {
			responseString
					.append("Coordinate Y is missing. Please provide Coordinate Y");
			return responseString.toString();
		}

		if (coordinateZ == null || coordinateZ.trim().equals("")) {
			responseString
					.append("Coordinate Z is missing. Please provide Coordinate Z");
			return responseString.toString();
		}

		System.out.println("CoordinateX - " + coordinateX);
		System.out.println("CoordinateY - " + coordinateY);
		System.out.println("CoordinateZ - " + coordinateZ);

		if (fromSpaceName.equals("whs")) {

			System.out
					.println("Inside WHS original coordinates transformation");
			INCFUtil util1 = new INCFUtil();
			CommonServiceVO vo = new CommonServiceVO();
			vo.setFromSRSCode(fromSpaceName);
			vo.setToSRSCode("abavoxel");
			vo.setFromSRSCodeOne(fromSpaceName);
			vo.setToSRSCodeOne("abavoxel");
			vo.setOriginalCoordinateX(coordinateX);
			vo.setOriginalCoordinateY(coordinateY);
			vo.setOriginalCoordinateZ(coordinateZ);

			// Special step to translate coordinates from whs to abavoxel
			String transformedCoordinatesString = util1
					.spaceTransformation(vo);

			String[] transformedCoordinates = util1
					.getTabDelimNumbers(transformedCoordinatesString);

			coordinateX = transformedCoordinates[0];
			coordinateY = transformedCoordinates[1];
			coordinateZ = transformedCoordinates[2];

		}

		try {

			// 2) Get structure names from EMAGE that can be used to get Genes
			// from EMAGE
			System.out.println("Starts Structure Lookup process...");

			INCFUtil util = new INCFUtil();

			// Cannot say fromSpaceName as the structure look up is supported
			// only for abavoxel
			String structureNamesString = util.getStructureNameLookup(
					"abavoxel", coordinateX, coordinateY, coordinateZ);

			String[] structureNames = util
					.getTabDelimNumbers(structureNamesString);

			fineStructureName = structureNames[1];
			anatomicStructureName = structureNames[0];

			System.out.println("Fine Structure - " + fineStructureName);
			System.out.println("Anatomic Structure - " + anatomicStructureName);

			String outOfRange = structureNames[2];

			// Start - Changes
			if (outOfRange != null && outOfRange.trim().equalsIgnoreCase("out")) {
				responseString.append("Out of Range");
				return responseString.toString();
			}

			if ((!fineStructureName.trim().equals("") || fineStructureName != null)
					&& !fineStructureName.trim().equals("-")) {
				System.out.println("Inside Fine Structure");
				structureName = fineStructureName;
			} else if ((!anatomicStructureName.trim().equals("") || anatomicStructureName != null)
					&& !anatomicStructureName.trim().equals("-")) {
				System.out.println("Inside Anatomic Structure");
				structureName = anatomicStructureName;
			} else if (fineStructureName.trim().equals("-")) {
				System.out.println("Inside Fine Structure that is \"-\" ");
				responseString.append("No Structure found");
				return responseString.toString();
			} else if (anatomicStructureName.trim().equals("-")) {
				System.out.println("Inside Anatomic Structure that is \"-\" ");
				responseString.append("No Structure found");
				return responseString.toString();
			}

			System.out.println("Structure Name - " + structureName);

			// End
			System.out.println("Ends ABA Structure Lookup matrix...");

			// Start - Database call to convert structure abbrev into structure
			// name
			EmageServiceBean serviceBean = new EmageServiceBean();
			EmageServiceVO vo = new EmageServiceVO();
			vo.setStructureAbbrev(structureName);
			vo = serviceBean.getStructureNameForStructureAbbrev(vo);
			System.out.println("ABA Full Structure Name - "
					+ vo.getStructureName());
			// End

			if (vo.getStructureName().trim().equals("")
					|| vo.getStructureName() == null) {
				responseString.append("No matching ABA structure found.");
				return responseString.toString();
			}

			// Convert ABA Structure to EMAGEID or EMAGEStructure - Make a
			// webservice call to Albert's program
			INCFClient ic = new INCFClient();
			String emageStructureName = ic.getEmageStructureName(vo
					.getStructureName());
			System.out.println("Emage StructureName after conversion - "
					+ emageStructureName);

			if (emageStructureName.trim().equalsIgnoreCase("")
					|| emageStructureName == null) {
				responseString.append("No matching ABA structure found.");
				return responseString.toString();
			}

			// 3) Construct URL to get Genes By StructureName
			System.out.println("Start - Running EMAGE get GenesByPOI URL...");
			String imageURLPrefix = "http://" + emageHostName + emagePortNumber
					+ emageServicePath + "?strengths=detected&structures=";
			responseString.append(imageURLPrefix).append(emageStructureName);

			System.out.println(" Complete URL for EMAGE after conversion is - "
					+ responseString.toString());

			System.out.println("Ends running EMAGE getGenesByPOI URL...");

		} catch (Exception e) {

			e.printStackTrace();
			responseString
					.append("Please contact the administrator to resolve this issue");

		} finally {

		}

		System.out.println("End - GetGeneByPOI Method...");

		// 4) Return response back to the cllient in a text/xml format
		return responseString.toString();

	}


	public String getGenesByStructure(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - GetGenesByStructure Method...");

		// Set the content type
		response.setContentType("text/html");

		// http://mouse.brain-map.org/agea/all_coronal?correlation&seedPoint=8375,4150,3800
		// 1) Define and Get parameters from URL
		// Define config Properties
		String emageHostName = incfConfig.getValue("incf.emage.host.name");
		String emagePortNumber = incfConfig.getValue("incf.emage.port.number");
		String emageServicePath = incfConfig
				.getValue("incf.emage.service.path");

		System.out.println(" Parameters... ");
		// String fromSpaceName = request.getParameter("SRSCode");
		String vocabulary = request.getParameter("vocabulary");
		String term = request.getParameter("term");

		StringBuffer responseString = new StringBuffer();

		// Start - Exception Handling
		if (vocabulary == null || vocabulary.trim().equals("")) {
			responseString
					.append("Vocabulary is missing. Please provide vocabulary");
			return responseString.toString();
		}

		if (term == null || term.trim().equals("")) {
			responseString.append("Term is missing. Please provide term");
			return responseString.toString();
		}

		try {

			if (vocabulary.equalsIgnoreCase("aba")) {

				// Convert aba to emage structure name
				INCFClient ic = new INCFClient();
				String emageStructureName = ic.getEmageStructureName(term);
				System.out.println("Emage Structure Name :"
						+ emageStructureName);

				if (emageStructureName.trim().equalsIgnoreCase("")
						|| emageStructureName == null) {
					responseString.append("No matching ABA structure found.");
					return responseString.toString();
				}

				// 2) Construct URL to get Genes By StructureName
				// Convert the aba structure name into emapID or emage structure
				// name using Albert's API
				System.out
						.println("Start - Running EMAGE getGenesByStructure URL...");
				String imageURLPrefix = "http://" + emageHostName
						+ emagePortNumber + emageServicePath
						+ "?strengths=detected&structures=";

				responseString.append(imageURLPrefix)
						.append(emageStructureName);

				System.out
						.println(" Complete URL for EMAGE GetGenesByStructure is - "
								+ responseString.toString());

				System.out.println("Ends running EMAGE getGenesByPOI URL...");

			} else if (vocabulary.equalsIgnoreCase("emap")) {

				String imageURLPrefix = "http://" + emageHostName
						+ emagePortNumber + emageServicePath
						+ "?strengths=detected&structures=";
				responseString.append(imageURLPrefix).append(term);

			}

		} catch (Exception e) {

			e.printStackTrace();
			responseString
					.append("Please contact the administrator to resolve this issue");

		} finally {

		}

		System.out.println("End - GetGeneByPOI Method...");

		// 4) Return response back to the cllient in a text/xml format
		return responseString.toString();

	}

	public void getCapabilitiesWPS(HttpServletResponse response) {

		StringBuffer sb = new StringBuffer();

		try {

			System.out.println("Inside getCapabilities Method...");

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();

			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

			out.println("<wps:Capabilities xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps\" xmlns:wps=\"http://www.opengis.net/wps\" xmlns:ows=\"http://www.opengis.net/ows\" version=\"1.0.0\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 ../wpsGetCapabilities_request.xsd\">\n");

			out.println("<ows:ServiceIdentification xmlns:ows=\"http://www.opengis.net/ows\">\n");
			out.println("<ows:Title>Emage Services</ows:Title>\n");
			out.println("<ows:Abstract>Emage Services are created to access various emage atlas space features that are offered by UCSD to its clients.</ows:Abstract>\n");
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
			out.println("<ows:Identifier>GetGenesByPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get genes by POI</ows:Title> \n");
			out.println("<ows:Description>Returns genes segmented for the given point of interest on the map</ows:Description>\n");
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
			System.out.println("Get Capabilities - " + sb.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void describeProcessWPS(HttpServletResponse response) {

		StringBuffer sb = new StringBuffer();

		try {

			sb = new StringBuffer();

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();

			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			out
					.println("<wps:ProcessDescriptions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps\" xmlns:wps=\"http://www.opengis.net/wps\" xmlns:ows=\"http://www.opengis.net/ows\" version=\"1.0.0\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 ../wpsGetCapabilities_request.xsd\">\n");

			out.println("<ProcessDescription>\n");
			out
					.println("<ows:Identifier>GetGenesByStructureName</ows:Identifier>\n");
			out.println("<ows:Title>Get genes by Structure Name</ows:Title>\n");
			out
					.println("<ows:Description>Returns genes segmented for the structure name</ows:Description>\n");
			out.println("<DataInputs>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>StructureName</ows:Identifier>\n");
			out.println("<ows:Title>Structure Name</ows:Title>\n");
			out.println("<ows:Abstract>Structure Name</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:double\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>SRSCODE</ows:Identifier>\n");
			out.println("<ows:Title>Atlas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as EMAGE</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>Vocabulary</ows:Identifier>\n");
			out.println("<ows:Title>Space name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as ABARef, or ABAVox, or WHS, or UCSD</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("</DataInputs>\n");
			out.println("<ProcessOutputs>\n");
			out.println("<Output>\n");
			out.println("<ows:Identifier>Genes</ows:Identifier>\n");
			out.println("<ows:Title>Genes</ows:Title>\n");
			out
					.println("<ows:Abstract>Genes segmented for the given point of interest</ows:Abstract>\n");
			out.println("<ComplexOutput>\n");
			out.println("<Default>\n");
			out.println("<Format>\n");
			out.println("<MimeType>text/ascii</MimeType>\n");
			out.println("<MimeType>http/ascii</MimeType>\n");
			out.println("</Format>\n");
			out.println("</Default>\n");
			out.println("</ComplexOutput>\n");
			out.println("</Output>\n");
			out.println("</ProcessOutputs>\n");
			out.println("</ProcessDescription>\n");

			out.println("</wps:ProcessDescriptions>\n");

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Get Capabilities - " + sb.toString());

	}

	public String xmlQueryStringToGetEmageStructure(String abaStructureName) {

		StringBuffer sb = new StringBuffer();

		try {
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb
					.append("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">");
			sb.append("<S:Header/>");
			sb.append("<S:Body>");
			sb
					.append("<ns2:getGenesByStructure xmlns:ns2=\"http://service.demo.incf.bisel/\">");
			sb.append("<source>aba</source>");
			sb.append("<structureName>").append(abaStructureName).append(
					"</structureName>");
			sb.append("<vocabulary>aba</vocabulary>");
			sb.append("<output>html</output>");
			sb.append("</ns2:getGenesByStructure>");
			sb.append("</S:Body>");
			sb.append("</S:Envelope>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();

	}

	public String convertStructureNameWSClient(
			String xmlStringToConvertStructure) {

		String responseString = "";

		try {

			String endpoint = "http://lxbisel.macs.hw.ac.uk:8080/INCFService/INCF";
			Service service = new Service();
			Call call = (Call) service.createCall();
			QName operationName = new QName(endpoint, "getGenesByStructure");
			call.setOperationName(operationName);
			call.setTargetEndpointAddress(endpoint);

			StringBuffer sb = new StringBuffer();
			sb.append(xmlStringToConvertStructure);

			responseString = (String) call
					.invoke(new Object[] { sb.toString() });

			System.out.println("ResponseString is - " + responseString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseString;

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
