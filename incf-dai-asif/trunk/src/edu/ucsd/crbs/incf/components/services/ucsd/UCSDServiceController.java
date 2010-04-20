package edu.ucsd.crbs.incf.components.services.ucsd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

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
public class UCSDServiceController extends HttpServlet {

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

			INCFLogger.logDebug(UCSDServiceController.class,
					"Welcome to the UCSD Servlet");

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
		UCSDServiceVO vo = new UCSDServiceVO();

		// Get the function name
		String functionName = request.getParameter("request");
		System.out.println(" Request is - " + functionName);

		String outputType = request.getParameter("output");
		System.out.println(" Output Type is is - " + outputType);

		try {

			if (functionName.equalsIgnoreCase("get2DImageAtPOI")
					&& outputType.equalsIgnoreCase("html")
					|| functionName.equalsIgnoreCase("get2DImageAtPOI")
					&& outputType.equalsIgnoreCase("txt")) {

				System.out
						.println(" ***Inside get2DImageAtPOI at server side - html and txt*** ");
				get2DImageListAtPOI(request, response);
				System.out.println(" Get2DImageAtPOI - " + responseString);

			} else if (functionName.equalsIgnoreCase("get2DImageAtPOI")
					&& outputType.equalsIgnoreCase("xml")) {

				System.out
						.println(" ***Inside get2DImageAtPOI with xml output at server side - only xml*** ");
				responseString = get2DImageListAtPOIXMLResponse(request,
						response);
				System.out.println(" Get2DImageAtPOI - " + responseString);

			} else if (functionName.equalsIgnoreCase("get2DImage")) {

				System.out.println(" Inside Get2DImage at server side");
				// Go to mapserver-services webapp to get the data for this
				// get2DImage( request, response );

			} else if (functionName.equalsIgnoreCase("GetCapabilities")
					&& outputType.equalsIgnoreCase("xml")) {

				System.out
						.println(" Inside GetCapabilities WPS at server side");
				getCapabilitiesWPS(response);

			} else if (functionName.equalsIgnoreCase("DescribeProcess")
					&& outputType.equalsIgnoreCase("xml")) {

				System.out
						.println(" Inside Describe Process WPS at server side");
				describeProcessWPS(response);

			} else if (functionName.equalsIgnoreCase("getStructure")) {
				getStructure();
			} else if (functionName.equalsIgnoreCase("spaceTransformation")
					&& outputType.equalsIgnoreCase("html")
					|| functionName.equalsIgnoreCase("spaceTransformation")
					&& outputType.equalsIgnoreCase("txt")) {
				responseString = spaceTransformation(request, response);
			} else if (functionName.equalsIgnoreCase("spaceTransformation")
					&& outputType.equalsIgnoreCase("xml")) {
				responseString = spaceTransformationXMLResponse(request,
						response);
			} else if (functionName
					.equalsIgnoreCase("getSpaceTransformationInfo")
					&& outputType.equalsIgnoreCase("xml")) {
				responseString = getSpaceTransformationInfoXMLResponse(request,
						response);
			} else if (functionName.equalsIgnoreCase("spaceTransformationForm")) {
				CommonServiceVO cvo = spaceTransformationForm(request, response);
				//FIXME - This is due to a different model object. It is CommonServiceVO and not UCSDServiceVO
				if (cvo.getErrorMessage() != null) {
					System.out.println("Error in spacetransformation FORM");
					request.setAttribute("responseVO", cvo);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher(nextJSP);
					dispatcher.forward(request, response);
				} else {
					System.out
							.println("OutputType is spacetransformation FORM");
					request.setAttribute("response", cvo);
					String nextJSP = "/pages/imageviewer/spacetransformation.jsp";
					RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher(nextJSP);
					dispatcher.forward(request, response);
				}
			} else if (functionName.equalsIgnoreCase("spaceTransformationView")) {
				CommonServiceVO cvo = spaceTransformationView(request, response);
				//FIXME - This is due to a different model object. It is CommonServiceVO and not UCSDServiceVO
				if (cvo.getErrorMessage() != null) {
					System.out.println("Error in spacetransformation FORM");
					request.setAttribute("responseVO", cvo);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher(nextJSP);
					dispatcher.forward(request, response);
				} else {
					System.out
							.println("OutputType is spacetransformation FORM");
					request.setAttribute("response", cvo);
					String nextJSP = "/pages/imageviewer/spacetransformation.jsp";
					RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher(nextJSP);
					dispatcher.forward(request, response);
				}
			} else if (functionName.equalsIgnoreCase("openMap")) {
				openMap();
			}

			//FIXME - OutputType
			if (outputType == null || outputType.trim().equalsIgnoreCase("")) {
				System.out.println("OutputType is NULL");
				out.println("Output Type is missing. Please provide output type.");
			} else if (outputType.trim().equalsIgnoreCase("txt")) {
				System.out.println("OutputType is TEXT");
				out.println(responseString);
			} else if (outputType.trim().equalsIgnoreCase("form")) {

				System.out.println("OutputType is FORM");
				request.setAttribute("response", vo);
				String nextJSP = "/pages/imageviewer/transformationview.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);

		    /*
			} else if (outputType.trim().equalsIgnoreCase(
					"spacetransformationform")) {

				if (vo.getErrorMessage() != null) {
					System.out.println("Error in spacetransformation FORM");
					request.setAttribute("responseVO", vo);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher(nextJSP);
					dispatcher.forward(request, response);
				} else {
					System.out
							.println("OutputType is spacetransformation FORM");
					request.setAttribute("response", vo);
					String nextJSP = "/pages/imageviewer/spacetransformation.jsp";
					RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher(nextJSP);
					dispatcher.forward(request, response);
				}
             */
			} else if (outputType.trim().equalsIgnoreCase("xml")) {

				System.out.println("OutputType is XML");
				out.println(responseString);

			} else if (outputType.trim().equalsIgnoreCase("html")) {
				System.out.println("OutputType is HTML");

				if (responseString.trim().startsWith("http:")) {
					response.sendRedirect(responseString.toString().trim());
				} else if (responseString.trim().equalsIgnoreCase(
						"/mapserver/pages/imageviewer/listofimages.jsp")) {
					System.out.println("HTML List of Images is - "
							+ responseString);
					request.setAttribute("response", responseString);
					String nextJSP = responseString;
					RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher(nextJSP);
					dispatcher.forward(request, response);
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
		UCSDServiceController helper = new UCSDServiceController();

		try {
			// helper.getLabelSet( );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void get2DImageListAtPOI(HttpServletRequest request,
			HttpServletResponse response) {

		response.setContentType("text/html");

		// 1) Define and Get parameters from URL
		System.out.println(" Parameters... ");
		String fromSpaceName = request.getParameter("SRSCode");
		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");
		String filter = request.getParameter("filter");
		String filterValue = filter.replaceAll("maptype:", "").trim();
		System.out.println("Map Type - " + filterValue);

		// Define config Properties
		String jspImagePath = "";

		StringBuffer responseString = new StringBuffer();

		// Start - Exception Handling
		if (fromSpaceName == null || fromSpaceName.trim().equals("")) {
			responseString
					.append("SRS Code is missing. Please provide the srs code");
			// return responseString.toString();
		}

		if (coordinateX == null || coordinateX.trim().equals("")) {
			responseString
					.append("Coordinate X is missing. Please provide Coordinate X");
			// return responseString.toString();
		}

		if (coordinateY == null || coordinateY.trim().equals("")) {
			responseString
					.append("Coordinate Y is missing. Please provide Coordinate Y");
			// return responseString.toString();
		}

		if (coordinateZ == null || coordinateZ.trim().equals("")) {
			responseString
					.append("Coordinate Z is missing. Please provide Coordinate Z");
			// return responseString.toString();
		}
		// End - Exception Handling

		// Change from ABAVoxel to Paxinos
		// Change from Paxinos to ABARef

		try {

			System.out.println("Before getPaxinosImageList");
			ArrayList completeImageList = getPaxinosImageList(fromSpaceName,
					coordinateX, coordinateY, coordinateZ, filterValue);

			System.out.println("Before getABAReferenceImageList");
			completeImageList = getABAReferenceImageList(completeImageList,
					fromSpaceName, coordinateX, coordinateY, coordinateZ,
					filterValue);

			// 4) Return response back to the cllient in a text/xml format
			if (completeImageList.size() != 0) {
				request.setAttribute("completeImageList", completeImageList);
				jspImagePath = "/pages/imageviewer/listofspatialimages.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(jspImagePath);
				dispatcher.forward(request, response);
			} else {
				String errorMessage = "No images available within 3 mm of given coordinates";
				request.setAttribute("response", errorMessage);
				jspImagePath = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(jspImagePath);
				dispatcher.forward(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String get2DImageListAtPOIXMLResponse(HttpServletRequest request,
			HttpServletResponse response) {

		// 1) Define and Get parameters from URL
		System.out.println(" Parameters... ");
		String fromSpaceName = request.getParameter("SRSCode");
		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");
		String filter = request.getParameter("filter");
		String filterValue = filter.replaceAll("maptype:", "").trim();
		System.out.println("Map Type - " + filterValue);

		// Define config Properties
		String jspImagePath = "";

		String xmlResponseString = "";
		String errorString = "";
		String message = "";
		INCFUtil util = new INCFUtil();

		// Start - Exception Handling
		if (fromSpaceName == null || fromSpaceName.trim().equals("")) {
			message = "SRS Code is missing. Please provide the srs code";
			errorString = util.error2DImageFromUCSDXMLResponse(message,
					fromSpaceName, coordinateX, coordinateY, coordinateZ,
					filterValue);
			return errorString;
		}

		if (coordinateX == null || coordinateX.trim().equals("")) {
			message = "Coordinate X is missing. Please provide Coordinate X";
			errorString = util.error2DImageFromUCSDXMLResponse(message,
					fromSpaceName, coordinateX, coordinateY, coordinateZ,
					filterValue);
			return errorString;
		}

		if (coordinateY == null || coordinateY.trim().equals("")) {
			message = "Coordinate Y is missing. Please provide Coordinate Y";
			errorString = util.error2DImageFromUCSDXMLResponse(message,
					fromSpaceName, coordinateX, coordinateY, coordinateZ,
					filterValue);
			return errorString;
		}

		if (coordinateZ == null || coordinateZ.trim().equals("")) {
			message = "Coordinate Z is missing. Please provide Coordinate Z";
			errorString = util.error2DImageFromUCSDXMLResponse(message,
					fromSpaceName, coordinateX, coordinateY, coordinateZ,
					filterValue);
			return errorString;
		}
		// End - Exception Handling

		// Change from ABAVoxel to Paxinos
		// Change from Paxinos to ABARef

		try {

			System.out.println("Before getPaxinosImageList");
			ArrayList completeImageList = getPaxinosImageList(fromSpaceName,
					coordinateX, coordinateY, coordinateZ, filterValue);

			System.out.println("Before getABAReferenceImageList");
			completeImageList = getABAReferenceImageList(completeImageList,
					fromSpaceName, coordinateX, coordinateY, coordinateZ,
					filterValue);

			// Convert the results into an xml output string
			xmlResponseString = util.get2DImageFromUCSDXMLResponse(
					completeImageList, fromSpaceName, coordinateX, coordinateY,
					coordinateZ, filterValue);

			// Return response back to the cllient in a text/xml format
			if (completeImageList.size() != 0) {
				return xmlResponseString;
			} else {
				System.out.println("Inside completeImageList size is 0 - "
						+ completeImageList.size());
				// Add a method for no data in an xml string
				message = "No images available within 3 mm of given coordinates";
				errorString = util.error2DImageFromUCSDXMLResponse(message,
						fromSpaceName, coordinateX, coordinateY, coordinateZ,
						filterValue);
				return errorString;
			}

		/*
		 * request.setAttribute("completeImageList", completeImageList);
		 * jspImagePath = "/pages/imageviewer/listofspatialimages.jsp";
		 * RequestDispatcher dispatcher =
		 * getServletContext().getRequestDispatcher(jspImagePath);
		 * dispatcher.forward(request,response);
		 */
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlResponseString;
	}


	public void getCapabilitiesWPS(HttpServletResponse response) {

		StringBuffer sb = new StringBuffer();

		try {

			System.out.println("Inside getCapabilities Method...");

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();

			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

			out
					.println("<wps:Capabilities xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps\" xmlns:wps=\"http://www.opengis.net/wps\" xmlns:ows=\"http://www.opengis.net/ows\" version=\"1.0.0\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 ../wpsGetCapabilities_request.xsd\">\n");

			out
					.println("<ows:ServiceIdentification xmlns:ows=\"http://www.opengis.net/ows\">\n");
			out.println("<ows:Title>UCSD Services</ows:Title>\n");
			out
					.println("<ows:Abstract>UCSD Services are created to access various UCSD atlas space features that are offered by UCSD to its clients.</ows:Abstract>\n");
			out.println("<ows:ServiceType>WPS</ows:ServiceType>\n");
			out
					.println("<ows:ServiceTypeVersion>0.2.4</ows:ServiceTypeVersion>\n");
			out.println("<ows:Fees>NONE</ows:Fees> \n");
			out
					.println("<ows:AccessConstraints>NONE</ows:AccessConstraints>\n");
			out.println(" </ows:ServiceIdentification>\n");

			out.println("<ows:ServiceProvider>\n");
			out.println("<ows:ProviderName>Asif Memon</ows:ProviderName>\n");
			out
					.println("<ows:ServiceContact>amemon@ncmir.ucsd.edu</ows:ServiceContact>\n");
			out.println("</ows:ServiceProvider>\n");

			out.println("<ows:OperationsMetadata>\n");
			out.println("<ows:Operation name=\"GetCapabilities\" />\n");
			out.println("</ows:OperationsMetadata> \n");

			out.println("<wps:ProcessOfferings>\n");

			out.println("<wps:Process>\n");
			out.println("<ows:Identifier>Get2DImageAtPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get 2D Image at POI</ows:Title> \n");
			out
					.println("<ows:Description>Returns 2 Dimensional image based on point of interest on the map</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out
					.println("<ows:Identifier>SpaceTransformation</ows:Identifier>\n");
			out
					.println("<ows:Title>Atlas Space Transformation from Paxinos to WHS, and WHS to Paxinos</ows:Title> \n");
			out
					.println("<ows:Description>Finds, transforms and executes the best transformation available from Paxinos to WHS, and WHS to Paxinos</ows:Description>\n");
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
			out.println("<ows:Identifier>Get2DImageAtPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get 2D Image at POI</ows:Title>\n");
			out
					.println("<ows:Description>Returns 2 Dimensional image based on point of interest on the map</ows:Description>\n");
			out.println("<DataInputs>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>MapPoint</ows:Identifier>\n");
			out.println("<ows:Title>Map Point</ows:Title>\n");
			out
					.println("<ows:Abstract>Point of interest on the map</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:double\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>SRSCODE</ows:Identifier>\n");
			out.println("<ows:Title>Altas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as UCSD, Paxinos,  or ABARef</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("</DataInputs>\n");
			out.println("<ProcessOutputs>\n");
			out.println("<Output>\n");
			out.println("<ows:Identifier>SiteURL</ows:Identifier>\n");
			out.println("<ows:Title>SiteURL</ows:Title>\n");
			out.println("<ows:Abstract>Site URL of an Image</ows:Abstract>\n");
			out.println("<ComplexOutput>\n");
			out.println("<Default>\n");
			out.println("<Format>\n");
			out.println("<MimeType>ascii/text</MimeType>\n");
			out.println("<MimeType>http/ascii</MimeType>\n");
			out.println("</Format>\n");
			out.println("</Default>\n");
			out.println("</ComplexOutput>\n");
			out.println("</Output>\n");
			out.println("</ProcessOutputs>\n");
			out.println("</ProcessDescription>\n");

			out.println("<ProcessDescription>\n");
			out
					.println("<ows:Identifier>AtlasSpaceTransformation</ows:Identifier>\n");
			out.println("<ows:Title>Atlas Space Transformation</ows:Title>\n");
			out
					.println("<ows:Description>Finds, transforms and executes the best transformation available in the space based on the requirement</ows:Description>\n");
			out.println("<DataInputs>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>MapPoint</ows:Identifier>\n");
			out.println("<ows:Title>Map Point</ows:Title>\n");
			out
					.println("<ows:Abstract>Coordinates to form a map point</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>FromSRSCODE</ows:Identifier>\n");
			out.println("<ows:Title>Source Atlas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as ABA, or Paxinos</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>ToSRSCODE</ows:Identifier>\n");
			out
					.println("<ows:Title>Destination Atlas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as ABA, or Paxinos</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("</DataInputs>\n");
			out.println("<ProcessOutputs>\n");
			out.println("<Output>\n");
			out
					.println("<ows:Identifier>TransformedGeometry</ows:Identifier>\n");
			out.println("<ows:Title>Transformed Geometry</ows:Title>\n");
			out
					.println("<ows:Abstract>Returns transformed geometries</ows:Abstract>\n");
			out.println("<ComplexOutput>\n");
			out.println("<Default>\n");
			out.println("<Format>\n");
			out.println("<MimeType>text/ascii</MimeType>\n");
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

	
	public void getStructure() {
	}

	
	// These are for the transformations done by Steve Lamont
	// http://132.239.131.188:8080/incf-services/service/ABAServiceController?request=SpaceTransformation&srcsrscode=ABA&destsrscode&x=263&y=159&z=227
	public String spaceTransformation(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - UCSD spaceTransformation Method...");

		// Set the content type
		response.setContentType("text/html");

		// 1) Define and Get parameters from URL
		// Define Properties
		System.out.println(" Parameters... ");
		String fromSpaceName = request.getParameter("fromSRSCode");
		String toSpaceName = request.getParameter("toSRSCode");

		System.out.println("From Space Name - " + fromSpaceName);

		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");

		// As paxinos coordinates are in double value, so we dont round that up
		if (!fromSpaceName.equalsIgnoreCase("paxinos")) {
			coordinateX = incfUtil.getRoundCoordinateValue(coordinateX);
			coordinateY = incfUtil.getRoundCoordinateValue(coordinateY);
			coordinateZ = incfUtil.getRoundCoordinateValue(coordinateZ);
		}

		StringBuffer responseString = new StringBuffer();

		// Start - Exception Handling
		if (fromSpaceName == null || fromSpaceName.trim().equals("")) {
			responseString
					.append("From SRS Code is missing. Please provide the srs code");
			return responseString.toString();
		}

		if (toSpaceName == null || toSpaceName.trim().equals("")) {
			responseString
					.append("To SRS Code is missing. Please provide the srs code");
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

		StringBuffer transformedCoordinates = new StringBuffer();

		try {

			System.out.println("Start - transformation matrix process...");

			// 2) Get the transformed coordinates from Steve's program
			// http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/atlas_lookup.cgi?atlas=aba&direction=forward&x=263&y=159&z=227

			INCFUtil util = new INCFUtil();

			CommonServiceVO vo = new CommonServiceVO();
			vo.setFromSRSCode(fromSpaceName);
			vo.setToSRSCode(toSpaceName);
			vo.setFromSRSCodeOne(fromSpaceName);
			vo.setToSRSCodeOne(toSpaceName);
			vo.setOriginalCoordinateX(coordinateX);
			vo.setOriginalCoordinateY(coordinateY);
			vo.setOriginalCoordinateZ(coordinateZ);

			String transformedCoordinatesRawString = util
					.spaceTransformation(vo);

			String[] arrayOfTransformedCoordinates = util
					.getTabDelimNumbers(transformedCoordinatesRawString);

			// Exception Handling
			if (arrayOfTransformedCoordinates[0].trim().equalsIgnoreCase("out")) {
				responseString.append("Out of Range");
				return responseString.toString();
			}

			transformedCoordinates.append("Transformed CoordinateX: ").append(
					arrayOfTransformedCoordinates[0]).append("\n").append(
					"Transformed CoordinateY: ").append(
					arrayOfTransformedCoordinates[1]).append("\n").append(
					"Transformed CoordinateZ: ").append(
					arrayOfTransformedCoordinates[2]).append("\n");
			// End

			System.out.println("Ends running transformation  matrix...");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformation Method...");

		// 4) Return response back to the cllient in a text/xml format
		return transformedCoordinates.toString();

	}

	// These are for the transformations done by Steve Lamont
	// http://132.239.131.188:8080/incf-services/service/ABAServiceController?request=SpaceTransformation&srcsrscode=ABA&destsrscode&x=263&y=159&z=227
	public CommonServiceVO spaceTransformationView(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - spaceTransformationView Method...");

		// Set the content type
		response.setContentType("text/html");
		CommonServiceVO vo = new CommonServiceVO();

		// 1) Define and Get parameters from URL
		// Define Properties
		System.out.println(" Parameters... ");

		vo.setFromSRSCode(request.getParameter("optone"));
		vo.setToSRSCode(request.getParameter("opttwo"));

		vo.setFromSRSCodeOne(request.getParameter("optone"));
		vo.setToSRSCodeOne(request.getParameter("opttwo"));
		vo.setOriginalCoordinateX(request.getParameter("coordinateX"));
		vo.setOriginalCoordinateY(request.getParameter("coordinateY"));
		vo.setOriginalCoordinateZ(request.getParameter("coordinateZ"));

		/*
		 * String hostName = incfConfig.getValue("ucsd.host.name"); String
		 * servicePath = incfConfig.getValue("ucsd.ucsd.service.path"); String
		 * portNumber = incfConfig.getValue("ucsd.port.number"); String
		 * transformationMatrixURLPrefix = hostName + portNumber + servicePath;
		 */
		StringBuffer responseString = new StringBuffer();

		// Start - Exception Handling
		if (vo.getFromSRSCodeOne() == null
				|| vo.getFromSRSCodeOne().trim().equals("")) {
			System.out.println(" Inside If. ");
			responseString
					.append("Source SRS Code is missing. Please provide the srs code");
			try {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vo;
		}

		if (vo.getToSRSCodeOne() == null
				|| vo.getToSRSCodeOne().trim().equals("")) {
			responseString
					.append("Destination SRS Code is missing. Please provide the srs code");

			try {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vo;

			// return responseString.toString();
		}

		if (vo.getOriginalCoordinateX() == null
				|| vo.getOriginalCoordinateX().trim().equals("")) {
			System.out.println("X is missing");
			responseString
					.append("Coordinate X is missing. Please provide Coordinate X");

			try {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vo;

			// return responseString.toString();
		}

		if (vo.getOriginalCoordinateY() == null
				|| vo.getOriginalCoordinateY().trim().equals("")) {
			System.out.println("Y is missing");
			responseString
					.append("Coordinate Y is missing. Please provide Coordinate Y");

			try {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vo;

			// return responseString.toString();
		}

		if (vo.getOriginalCoordinateZ() == null
				|| vo.getOriginalCoordinateZ().trim().equals("")) {
			System.out.println("Z is missing");
			responseString
					.append("Coordinate Z is missing. Please provide Coordinate Z");

			try {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vo;

			// return responseString.toString();
		}

		// As paxinos coordinates are in double value, so we dont round that up
		if (!vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos")) {
			vo.setOriginalCoordinateX(incfUtil.getRoundCoordinateValue(vo
					.getOriginalCoordinateX()));
			vo.setOriginalCoordinateY(incfUtil.getRoundCoordinateValue(vo
					.getOriginalCoordinateY()));
			vo.setOriginalCoordinateZ(incfUtil.getRoundCoordinateValue(vo
					.getOriginalCoordinateZ()));
		}

		try {

			System.out.println("Start - transformation matrix process...");

			// 2) Get the transformed coordinates from Steve's program
			// http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/atlas_lookup.cgi?atlas=aba&direction=forward&x=263&y=159&z=227

			INCFUtil util = new INCFUtil();

			vo = util.spaceTransformationView(vo);

			System.out.println("Ends running transformation  matrix...");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformation Method...");

		// 4) Return response back to the cllient in a text/xml format
		return vo;

	}

	// These are for the transformations done by Steve Lamont
	// http://132.239.131.188:8080/incf-services/service/ABAServiceController?request=SpaceTransformation&srcsrscode=ABA&destsrscode&x=263&y=159&z=227
	public String getSpaceTransformationInfoXMLResponse(
			HttpServletRequest request, HttpServletResponse response) {

		System.out
				.println("Start - getSpaceTransformationInfoXMLResponse Method...");

		// Set the content type
		response.setContentType("text/xml");
		CommonServiceVO vo = new CommonServiceVO();

		// 1) Define and Get parameters from URL
		// Define Properties
		System.out.println(" Parameters... ");
		vo.setFromSRSCode(request.getParameter("fromSRSCode"));
		vo.setToSRSCode(request.getParameter("toSRSCode"));

		vo.setFromSRSCodeOne(request.getParameter("fromSRSCode"));
		vo.setToSRSCodeOne(request.getParameter("toSRSCode"));

		StringBuffer responseString = new StringBuffer();
		String errorString = "";
		String message = "";
		INCFUtil util = new INCFUtil();

		// Start - Exception Handling
		if (vo.getFromSRSCode() == null
				|| vo.getFromSRSCode().trim().equals("")) {
			vo.setErrorMessage("From SRS Code is missing. Please provide the srs code");
			errorString = util.errorSpaceTransformationInfoXMLResponse(vo);
			return errorString;
		}

		if (vo.getToSRSCode() == null || vo.getToSRSCode().trim().equals("")) {
			vo.setErrorMessage("To SRS Code is missing. Please provide the srs code");
			errorString = util.errorSpaceTransformationInfoXMLResponse(vo);
			return errorString;
		}

		// As paxinos coordinates are in double value, so we dont round that up
		/*
		 * if ( !vo.getFromSRSCode().equalsIgnoreCase("paxinos")) {
		 * vo.setOriginalCoordinateX(incfUtil.getRoundCoordinateValue(vo.getOriginalCoordinateX()));
		 * vo.setOriginalCoordinateY(incfUtil.getRoundCoordinateValue(vo.getOriginalCoordinateY()));
		 * vo.setOriginalCoordinateZ(incfUtil.getRoundCoordinateValue(vo.getOriginalCoordinateZ())); }
		 */
		String xmlResponseString = "";

		try {

			vo = util.spaceTransformationInfoXML(vo);
			xmlResponseString = vo.getXmlStringForTransformationInfo();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

		}

		System.out
				.println("End - getSpaceTransformationInfoXMLResponse Method...");

		// 4) Return response back to the cllient in a text/xml format
		return xmlResponseString;

	}

	// These are for the transformations done by Steve Lamont
	// http://132.239.131.188:8080/incf-services/service/ABAServiceController?request=SpaceTransformation&srcsrscode=ABA&destsrscode&x=263&y=159&z=227
	public String spaceTransformationXMLResponse(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - spaceTransformationXMLResponse Method...");

		// Set the content type
		response.setContentType("text/xml");

		// 1) Define and Get parameters from URL
		// Define Properties
		System.out.println(" Parameters... ");
		String fromSpaceName = request.getParameter("fromSRSCode");
		String toSpaceName = request.getParameter("toSRSCode");

		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");

		String originalCoordinatesString = request.getParameter("x") + ", "
				+ request.getParameter("y") + ", " + request.getParameter("z");

		StringBuffer responseString = new StringBuffer();
		String errorString = "";
		String message = "";
		INCFUtil util = new INCFUtil();

		// Start - Exception Handling
		if (fromSpaceName == null || fromSpaceName.trim().equals("")) {
			message = "From SRS Code is missing. Please provide the srs code";
			errorString = util.errorSpaceTransformationXMLResponse(
					fromSpaceName, toSpaceName, originalCoordinatesString,
					message);
			return errorString;
		}

		if (toSpaceName == null || toSpaceName.trim().equals("")) {
			message = "To SRS Code is missing. Please provide the srs code";
			errorString = util.errorSpaceTransformationXMLResponse(
					fromSpaceName, toSpaceName, originalCoordinatesString,
					message);
			return errorString;
		}

		if (coordinateX == null || coordinateX.trim().equals("")) {
			message = "Coordinate X is missing. Please provide Coordinate X";
			errorString = util.errorSpaceTransformationXMLResponse(
					fromSpaceName, toSpaceName, originalCoordinatesString,
					message);
			return errorString;
		}

		if (coordinateY == null || coordinateY.trim().equals("")) {
			message = "Coordinate Y is missing. Please provide Coordinate Y";
			errorString = util.errorSpaceTransformationXMLResponse(
					fromSpaceName, toSpaceName, originalCoordinatesString,
					message);
			return errorString;
		}

		if (coordinateZ == null || coordinateZ.trim().equals("")) {
			message = "Coordinate Z is missing. Please provide Coordinate Z";
			errorString = util.errorSpaceTransformationXMLResponse(
					fromSpaceName, toSpaceName, originalCoordinatesString,
					message);
			return errorString;
		}

		// As paxinos coordinates are in double value, so we dont round that up
		if (!fromSpaceName.equalsIgnoreCase("paxinos")) {
			coordinateX = incfUtil.getRoundCoordinateValue(coordinateX);
			coordinateY = incfUtil.getRoundCoordinateValue(coordinateY);
			coordinateZ = incfUtil.getRoundCoordinateValue(coordinateZ);
		}

		StringBuffer transformedCoordinates = new StringBuffer();
		String xmlResponseString = "";

		try {

			System.out.println("Start - transformation matrix process...");

			// 2) Get the transformed coordinates from Steve's program
			// http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/atlas_lookup.cgi?atlas=aba&direction=forward&x=263&y=159&z=227
			CommonServiceVO vo = new CommonServiceVO();
			
			String transformedCoordinatesRawString = util.spaceTransformation(vo);

			String[] arrayOfTransformedCoordinates = util
					.getTabDelimNumbers(transformedCoordinatesRawString);

			// Exception Handling
			if (arrayOfTransformedCoordinates[0].equalsIgnoreCase("out")) {
				// responseString.append("Out of Range");
				message = "OUT OF BOUND";
				System.out.println("Out of Bound");
				errorString = util.errorSpaceTransformationXMLResponse(
						fromSpaceName, toSpaceName, originalCoordinatesString,
						message);
				return errorString;
			}

			transformedCoordinates.append("Transformed CoordinateX: ").append(
					arrayOfTransformedCoordinates[0]).append("\n").append(
					"Transformed CoordinateY: ").append(
					arrayOfTransformedCoordinates[1]).append("\n").append(
					"Transformed CoordinateZ: ").append(
					arrayOfTransformedCoordinates[2]).append("\n");
			// End

			// Write the xml response back to the user
			String transformedCoordinatesString = arrayOfTransformedCoordinates[0]
					+ ", "
					+ arrayOfTransformedCoordinates[1]
					+ ", "
					+ arrayOfTransformedCoordinates[2];

			xmlResponseString = util.getSpaceTransformationXMLResponse(
					fromSpaceName, toSpaceName, originalCoordinatesString,
					transformedCoordinatesString);
			System.out.println("XML Response String - " + xmlResponseString);
			System.out.println("Ends running transformation  matrix...");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationXMLResponse Method...");

		// 4) Return response back to the cllient in a text/xml format
		return xmlResponseString;

	}

	// These are for the transformations done by Steve Lamont
	public CommonServiceVO spaceTransformationForm(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - spaceTransformationForm Method...");

		// Set the content type
		response.setContentType("text/html");
		CommonServiceVO vo = new CommonServiceVO();

		// 1) Define and Get parameters from URL
		// Define Properties
		System.out.println(" Parameters... ");
		String fromSpaceName = request.getParameter("optone");
		String toSpaceName = request.getParameter("opttwo");

		String coordinateX = request.getParameter("coordinateX");
		String coordinateY = request.getParameter("coordinateY");
		String coordinateZ = request.getParameter("coordinateZ");
		System.out.println(" X... " + coordinateX);
		System.out.println(" Y... " + coordinateY);
		System.out.println(" Z... " + coordinateZ);
		System.out.println(" fromSpaceName... " + fromSpaceName);
		System.out.println(" toSpaceName... " + toSpaceName);

		StringBuffer responseString = new StringBuffer();

		// Start - Exception Handling
		if (fromSpaceName == null || fromSpaceName.trim().equals("")) {
			System.out.println(" Inside If. ");
			responseString
					.append("Source SRS Code is missing. Please provide the srs code");
			try {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vo;
		}

		if (toSpaceName == null || toSpaceName.trim().equals("")) {
			responseString
					.append("Destination SRS Code is missing. Please provide the srs code");

			try {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vo;

			// return responseString.toString();
		}

		if (coordinateX == null || coordinateX.trim().equals("")) {
			System.out.println("X is missing");
			responseString
					.append("Coordinate X is missing. Please provide Coordinate X");

			try {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vo;

			// return responseString.toString();
		}

		if (coordinateY == null || coordinateY.trim().equals("")) {
			System.out.println("Y is missing");
			responseString
					.append("Coordinate Y is missing. Please provide Coordinate Y");

			try {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vo;

			// return responseString.toString();
		}

		if (coordinateZ == null || coordinateZ.trim().equals("")) {
			System.out.println("Z is missing");
			responseString
					.append("Coordinate Z is missing. Please provide Coordinate Z");

			try {
				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vo;

			// return responseString.toString();
		}

		// As paxinos coordinates are in double value, so we dont round that up
		if (!fromSpaceName.equalsIgnoreCase("paxinos")) {
			coordinateX = incfUtil.getRoundCoordinateValue(coordinateX);
			coordinateY = incfUtil.getRoundCoordinateValue(coordinateY);
			coordinateZ = incfUtil.getRoundCoordinateValue(coordinateZ);
		}

		StringBuffer transformedCoordinates = new StringBuffer();
		String xmlResponseString = "";

		try {

			System.out.println("Start - transformation matrix process...");

			// 2) Get the transformed coordinates from Steve's program
			// http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/atlas_lookup.cgi?atlas=aba&direction=forward&x=263&y=159&z=227

			INCFUtil util = new INCFUtil();

			String transformedCoordinatesRawString = util
					.spaceTransformation(vo);

			String[] arrayOfTransformedCoordinates = util
					.getTabDelimNumbers(transformedCoordinatesRawString);

			// Exception Handling
			if (arrayOfTransformedCoordinates[0].trim().equalsIgnoreCase("out")) {
				responseString.append("Out of Range");

				request.setAttribute("response", responseString);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(nextJSP);
				dispatcher.forward(request, response);
				return vo;

				// return responseString.toString();
			}

			transformedCoordinates.append("Transformed CoordinateX: ").append(
					arrayOfTransformedCoordinates[0]).append("\n").append(
					"Transformed CoordinateY: ").append(
					arrayOfTransformedCoordinates[1]).append("\n").append(
					"Transformed CoordinateZ: ").append(
					arrayOfTransformedCoordinates[2]).append("\n");

			String transformationXMLResponseString = spaceTransformationXMLResponse(
					request, response);
			System.out.println(" XML Response String - "
					+ transformationXMLResponseString);

			// Write the xml response back to the user
			String originalCoordinatesString = request.getParameter("x") + ", "
					+ request.getParameter("y") + ", "
					+ request.getParameter("z");
			String transformedCoordinatesString = arrayOfTransformedCoordinates[0]
					+ ", "
					+ arrayOfTransformedCoordinates[1]
					+ ", "
					+ arrayOfTransformedCoordinates[2];
			xmlResponseString = util.getSpaceTransformationXMLResponse(
					fromSpaceName, toSpaceName, originalCoordinatesString,
					transformedCoordinatesString);

			vo.setTransformedCoordinateX(arrayOfTransformedCoordinates[0]);
			vo.setTransformedCoordinateY(arrayOfTransformedCoordinates[1]);
			vo.setTransformedCoordinateZ(arrayOfTransformedCoordinates[2]);
			vo.setOriginalCoordinateX(coordinateX);
			vo.setOriginalCoordinateY(coordinateY);
			vo.setOriginalCoordinateZ(coordinateZ);
			vo.setSrcSRSCode(fromSpaceName);
			vo.setDestSRSCode(toSpaceName);
			vo.setTransformationXMLResponseString(xmlResponseString);
			// End

			System.out.println("XML Response String - " + xmlResponseString);
			System.out.println("Ends running transformation  matrix...");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		// 4) Return response back to the cllient in a text/xml format
		return vo;

	}

	// Creates an xml string with buch of parameters to be passed in a
	// webservice
	private String createXMLQueryStringForImageList(String polygonString,
			String srsCode, String filterValue) {

		StringBuffer xmlString = new StringBuffer();

		// Read the proxy from a file
		try {

			xmlString.append("<request>");

			xmlString.append("<category>").append("mouse")
					.append("</category>");
			xmlString.append("<regionofinterest>").append(polygonString)
					.append("</regionofinterest>");
			xmlString.append("<maptype>").append(filterValue).append(
					"</maptype>");
			xmlString.append("<srscode>").append(srsCode).append("</srscode>");

			xmlString.append("</request>");

		} catch (Exception e) {

			e.getStackTrace();

		}

		System.out.println(" Query String for getImageMetaDataForROI - "
				+ xmlString.toString());
		return xmlString.toString();

	}

	public ArrayList getPaxinosImageList(String fromSpaceName,
			String originalCoordinateX, String originalCoordinateY,
			String originalCoordinateZ, String filterValue) {

		INCFConfigurator config = INCFConfigurator.INSTANCE;
		String hostName = config.getValue("ucsd.webservice.host.name");
		String port = config.getValue("ucsd.webservice.port.number");
		System.out.println(" - hostName - " + hostName);
		System.out.println(" - PortNumber - " + port);
		StringBuffer responseString = new StringBuffer();
		ArrayList list = new ArrayList();

		// FIXME - It is used for database "SPACE" value
		String srscode = "PAXINOS";
		CommonServiceVO vo = new CommonServiceVO();

		try {

			String coordinateX = "";
			String coordinateY = "";
			String coordinateZ = "";

			vo.setFromSRSCodeOne(fromSpaceName);
			vo.setToSRSCodeOne("paxinos");
			vo.setOriginalCoordinateX(originalCoordinateX);
			vo.setOriginalCoordinateY(originalCoordinateY);
			vo.setOriginalCoordinateZ(originalCoordinateZ);
			// Convert the coordinates ABAVOXEL into PAXINOS
			String tempCoordinateString2 = incfUtil.spaceTransformation(vo);
			String[] tempArray2 = incfUtil
					.getTabDelimNumbers(tempCoordinateString2);

			coordinateX = incfUtil.getRoundCoordinateValue(tempArray2[0]);
			coordinateY = incfUtil.getRoundCoordinateValue(tempArray2[1]);
			coordinateZ = incfUtil.getRoundCoordinateValue(tempArray2[2]);

			System.out.println("***Coordinate X for Polygon String - "
					+ coordinateX);
			System.out.println("***Coordinate Y for Polygon String - "
					+ coordinateY);
			System.out.println("***Coordinate Z for Polygon String - "
					+ coordinateZ);

			String webserviceName = "ImageMetadataForROI";
			String methodName = "get2DImageListForROI";

			// Create an arbitrary polygon with + or - 3
			String x1 = coordinateX;
			String x2 = String.valueOf(Integer.parseInt(coordinateX) + 20);
			String x3 = String.valueOf(Integer.parseInt(coordinateX) + 20);
			String x4 = coordinateX;
			String y1 = coordinateY;
			String y2 = coordinateY;
			String y3 = String.valueOf(Integer.parseInt(coordinateY) - 20);
			String y4 = String.valueOf(Integer.parseInt(coordinateY) - 20);

			String polygonString = x1 + "," + y1 + "," + x2 + "," + y2 + ","
					+ x3 + "," + y3 + "," + x4 + "," + y4 + "," + x1 + "," + y1;
			System.out.println("PAXINOS - Made up polygon string - "
					+ polygonString);

			String xmlQueryString = createXMLQueryStringForImageList(
					polygonString, srscode, filterValue);

			// Starts - Webserviceclient code
			String endpoint = "http://" + hostName + port + "/axis/services/"
					+ webserviceName;
			Service service = new Service();
			Call call = (Call) service.createCall();
			QName operationName = new QName(endpoint, methodName);
			call.setOperationName(operationName);
			call.setTargetEndpointAddress(endpoint);
			StringBuffer sb = new StringBuffer();
			sb.append(xmlQueryString);

			responseString.append((String) call.invoke(new Object[] { sb
					.toString() }));

			System.out.println("Response String - " + responseString);
			// Ends

			list = parseXMLResponseStringForImageList(list, responseString
					.toString());
			Iterator iterator = list.iterator();
			vo = new CommonServiceVO();

			while (iterator.hasNext()) {
				vo = (CommonServiceVO) iterator.next();
				System.out.println("WMS - " + vo.getWms());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public ArrayList getABAReferenceImageList(ArrayList completeImageList,
			String fromSpaceName, String originalCoordinateX,
			String originalCoordinateY, String originalCoordinateZ,
			String filterValue) {

		INCFConfigurator config = INCFConfigurator.INSTANCE;
		String hostName = config.getValue("ucsd.webservice.host.name");
		String port = config.getValue("ucsd.webservice.port.number");
		System.out.println(" - hostName - " + hostName);
		System.out.println(" - PortNumber - " + port);
		StringBuffer responseString = new StringBuffer();

		try {

			String coordinateX = incfUtil
					.getRoundCoordinateValue(originalCoordinateX);
			String coordinateY = incfUtil
					.getRoundCoordinateValue(originalCoordinateY);
			String coordinateZ = incfUtil
					.getRoundCoordinateValue(originalCoordinateZ);

			System.out.println("***Coordinate X before Transformation - "
					+ coordinateX);
			System.out.println("***Coordinate Y before Transformation - "
					+ coordinateY);
			System.out.println("***Coordinate Z before Transformation - "
					+ coordinateZ);
			
			CommonServiceVO vo = new CommonServiceVO();
			vo.setFromSRSCode(fromSpaceName);
			vo.setToSRSCode("abareference");
			vo.setFromSRSCodeOne(fromSpaceName);
			vo.setToSRSCodeOne("abareference");
			vo.setOriginalCoordinateX(coordinateX);
			vo.setOriginalCoordinateY(coordinateY);
			vo.setOriginalCoordinateZ(coordinateZ);

			// Convert the coordinates ABAVoxel into ABAReference
			String tempCoordinateString1 = incfUtil.spaceTransformation(vo);
			String[] tempArray1 = incfUtil
					.getTabDelimNumbers(tempCoordinateString1);

			coordinateX = incfUtil.getRoundCoordinateValue(tempArray1[0]);
			coordinateY = incfUtil.getRoundCoordinateValue(tempArray1[1]);
			coordinateZ = incfUtil.getRoundCoordinateValue(tempArray1[2]);

			System.out.println("***Coordinate X for Polygon String - "
					+ coordinateX);
			System.out.println("***Coordinate Y for Polygon String - "
					+ coordinateY);
			System.out.println("***Coordinate Z for Polygon String - "
					+ coordinateZ);

			// FIXME
			String webserviceName = "ImageMetadataForROI";
			String methodName = "get2DImageListForROI";
			String srscode = "ABA_REFERENCE";

			// Create an arbitrary polygon
			String x1 = coordinateX;
			String x2 = String.valueOf(Integer.parseInt(coordinateX) + 20);
			String x3 = String.valueOf(Integer.parseInt(coordinateX) + 20);
			String x4 = coordinateX;
			String y1 = coordinateY;
			String y2 = coordinateY;
			String y3 = String.valueOf(Integer.parseInt(coordinateY) - 20);
			String y4 = String.valueOf(Integer.parseInt(coordinateY) - 20);

			String polygonString = x1 + "," + y1 + "," + x2 + "," + y2 + ","
					+ x3 + "," + y3 + "," + x4 + "," + y4 + "," + x1 + "," + y1;
			System.out.println("ABAREFERENCE - Made up polygon string - "
					+ polygonString);

			String xmlQueryString = createXMLQueryStringForImageList(
					polygonString, srscode, filterValue);

			// Starts - Webserviceclient code
			String endpoint = "http://" + hostName + port + "/axis/services/"
					+ webserviceName;
			Service service = new Service();
			Call call = (Call) service.createCall();
			QName operationName = new QName(endpoint, methodName);
			call.setOperationName(operationName);
			call.setTargetEndpointAddress(endpoint);
			StringBuffer sb = new StringBuffer();
			sb.append(xmlQueryString);

			responseString.append((String) call.invoke(new Object[] { sb
					.toString() }));

			System.out.println("Response String - " + responseString);
			// Ends

			completeImageList = parseXMLResponseStringForImageList(
					completeImageList, responseString.toString());

			// It is a complete list containing paxinos as well as abaref images
			Iterator iterator = completeImageList.iterator();
			vo = new CommonServiceVO();
			while (iterator.hasNext()) {
				vo = (CommonServiceVO) iterator.next();
				System.out.println("WMS - " + vo.getWms());
				System.out
						.println("Image Base Name - " + vo.getImageBaseName());
				System.out.println("Image service Name - "
						+ vo.getImageServiceName());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return completeImageList;

	}

	private ArrayList parseXMLResponseStringForImageList(ArrayList list,
			String xmlData) {

		// Make the xml document from the xml string
		org.jdom.Document document;

		try {

			SAXBuilder builder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser", false);

			document = builder.build(new InputSource(new ByteArrayInputStream(
					xmlData.getBytes())));

			// Get all the childrens
			List structuresList = document.getRootElement().getChildren();

			// Start - xml parsing
			Element responseElement;
			// Element responseElement =
			// document.getRootElement().getChild("imageinfo");
			CommonServiceVO vo;
			INCFUtil util = new INCFUtil();

			for (int i = 0; i < structuresList.size(); i++) {

				responseElement = (Element) structuresList.get(i);
				vo = new CommonServiceVO();

				vo = util.getSimpleImageEnvelope("smartatlas.crbs.ucsd.edu",
						responseElement.getChildText("imageServiceName"),
						"MAPSERVER");

				// vo.setWms(responseElement.getChildText("wmsURL"));
				vo.setImageBaseName(responseElement.getChildText("imageName"));
				vo.setImageServiceName(responseElement
						.getChildText("imageServiceName"));
				vo.setTfw1(responseElement.getChildText("tfw1"));
				vo.setTfw2(responseElement.getChildText("tfw2"));
				vo.setTfw3(responseElement.getChildText("tfw3"));
				vo.setTfw4(responseElement.getChildText("tfw4"));
				vo.setTfw5(responseElement.getChildText("tfw5"));
				vo.setTfw6(responseElement.getChildText("tfw6"));

				list.add(vo);// Since data for multiimageservice is the same
								// for all the records based on sliceID, so we
								// are commenting this line

			}
			// End - xml parsing

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

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
