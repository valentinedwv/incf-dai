/**
 * <p>Title: SmartAtlas Data Access Servlet</p>
 * <p>Description: SmartAtlas Data Access Servlet</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: INCF-CC, UCSD</p>
 * @author Haiyun He
 * @version 1.0
 */
package edu.ucsd.crbs.incf.components.clients.wbc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Clob;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import edu.ucsd.crbs.incf.common.INCFConfigurator;
import edu.ucsd.crbs.incf.common.INCFLogger;

import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * AtlasTableInfoObject
 * 
 * The main servlet class to handle atlas related database access function (the
 * main purpose is to hide the direct connection to database)
 */ 
public class WBCClientServiceController extends HttpServlet {

	private INCFConfigurator configurator = INCFConfigurator.INSTANCE;

	private final String STATUS_OK = "OK";

	private final String STATUS_FAILED = "FAILED";

	INCFConfigurator incfConfig = INCFConfigurator.INSTANCE;

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

			System.out.println("************* " + servletName
					+ " : Some changeInitializing... *************");
			System.out.println("************* " + servletName
					+ " : Initialized... *************");

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
			throws ServletException {
		
		java.util.Vector v = new java.util.Vector();
		
/*			response.setContentType("application/x-java-serialized-object");
			OutputStream os = response.getOutputStream();
			ObjectOutputStream output = new ObjectOutputStream(os);
*/			System.out.println("2");			

			try {

				System.out.println("3");
				// Get the function name
				String functionName = request.getParameter("request");
				System.out.println("4");
				String atlasSpaceName = request.getParameter("srscode");
				System.out.println("5");			

				if ( functionName.equalsIgnoreCase("get2DImageAtPOI") && 
			         atlasSpaceName.equalsIgnoreCase("ABA") ) { 
					System.out.println("Inside get 2D Image");			
					get2DImageAtPOIForABA( request, response, atlasSpaceName, functionName );
				} else if ( functionName.equalsIgnoreCase("GetCorrelationMap") &&
						    atlasSpaceName.equalsIgnoreCase("ABA") ) {
					System.out.println("Inside get Coorelation Map");			
					getCoorelationMapForABA( request, response, atlasSpaceName, functionName );
				} else if ( functionName.equalsIgnoreCase("GetGenesByPOI") && 
						    atlasSpaceName.equalsIgnoreCase("ABA") ) {
					System.out.println("Inside get GetGenesByPOI");
					getGenesByPOIForABA( request, response, atlasSpaceName, functionName );;

				} else if ( functionName.equalsIgnoreCase("GetStructureNameByPOI") && 
						    atlasSpaceName.equalsIgnoreCase("ABA") ) {
					String filter = request.getParameter("filter");
					String filterValue = filter.toLowerCase().replaceAll("structureset:", "").trim();
					System.out.println("Structure Set - " + filterValue);
					if (filterValue.equalsIgnoreCase("Fine")) {
						System.out.println(" ABA - Inside Get Fine StructureName By POI at server side" );
						getFineStructureNameByPOIForABA( request, response, atlasSpaceName, functionName, filter );
					} else if (filterValue.equalsIgnoreCase("Anatomic")) {
						System.out.println(" ABA - Inside Get Anatomic StructureName By POI at server side" );
						getAnatomicStructureNameByPOIForABA( request, response, atlasSpaceName, functionName, filter );
					}
				} else if ( functionName.equalsIgnoreCase("GetGenesByPOI") && 
						    atlasSpaceName.equalsIgnoreCase("EMAGE") ) {
					System.out.println("Inside get GetGenesByPOIForEMAGE");
					getGenesByPOIForEmage( request, response, atlasSpaceName, functionName );;

				} else if ( functionName.equalsIgnoreCase("GetFineStructureNameByPOI") && 
						    atlasSpaceName.equalsIgnoreCase("WHS") ) {
					String filter = request.getParameter("filter");
					String filterValue = filter.replaceAll("structureset:", "").trim();
					System.out.println("Structure Set - " + filterValue);
					if (filterValue.equalsIgnoreCase("Fine")) {
						System.out.println(" WHS - Inside Get Fine StructureName By POI at server side" );
						getFineStructureNameByPOIForWHS( request, response, atlasSpaceName, functionName, filter );
					} else if (filterValue.equalsIgnoreCase("Anatomic")) {
						System.out.println(" WHS - Inside Get Anatomic StructureName By POI at server side" );
						getAnatomicStructureNameByPOIForWHS( request, response, atlasSpaceName, functionName, filter );
					}
				} else if (functionName.equalsIgnoreCase("getCapabilities")) {
					getCapabilities();
				} else if (functionName.equalsIgnoreCase("getStructure")) {
					getStructure();
				} else if (functionName.equalsIgnoreCase("spaceTransformation")) {
					spaceTransformation();
				} 

				System.out.println("6");

			} catch (Exception sqe) {
				sqe.printStackTrace();
			} finally {
			}

	}


	public static void main( String args[] ) {
		
		String[] abbrevs = {"M1, M2, M3"};
		String[] rels = {"SELF, SIBLING, PARENT, CHILD"}; 
		String category = "MOUSE";
		WBCClientServiceController helper = new WBCClientServiceController();
		
		try {
			//helper.getLabelSet( );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void get2DImageAtPOIForABA( HttpServletRequest request, 
									   HttpServletResponse response, String spaceName, String functionName ) {

		//WBC Client Service - http://132.239.131.188:8080/incf-services/service/wbc?request=get2DImageAtPOI&srscode=ABA&x=263&y=159&z=227&width=217&height=152&filter=maptype:coronal&output=html
		String hostName = incfConfig.getValue("ucsd.host.name");
		String portNumber = incfConfig.getValue("ucsd.port.number");
		String servicePath = incfConfig.getValue("ucsd.aba.service.path");

		String width = request.getParameter("width");
		String height = request.getParameter("height");
		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");

		//Start - Changes recommended by Ilya
		String filter = request.getParameter("filter");
		String outputType = request.getParameter("output");
		//End - Changes recommended by Ilya

		try { 
			//http://132.239.131.188:8080/incf-services/service/ABAServiceController?request=get2DImageAtPOI&srscode=ABA&x=263&y=159&z=227&width=217&height=152&filter=maptype:coronal&output=html
			String wbcClientUrlString = "http://" + hostName + portNumber + servicePath + "request="+functionName+ "&srscode="+spaceName + "&x="+coordinateX+"&y="+coordinateY+"&z="+coordinateZ+"&width="+width+"&height="+height+"&filter="+filter+"&output="+outputType; 

			System.out.println(" hostName - " + hostName );
			System.out.println(" Port Number - " + portNumber );
			System.out.println(" ABA Service Path - " + servicePath );
			System.out.println(" WBC Client URL - " + wbcClientUrlString );

			System.out.println("Starts running WBS Client for getImage URL...");

			//Start - Call servlet, and read the string from the website or the servlet
	        URL url = new URL(wbcClientUrlString);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			String outputURL = "";
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				outputURL = outputURL + inputLine;
			}
			System.out.println("outputURL - "+outputURL);
			System.out.println("Ends running WBS Client for getImage URL...");

			System.out.println("Starts redirectiing on the webpage...");
			if ( outputURL.startsWith("http") ) {
			    response.sendRedirect(outputURL.toString().trim());
			} else {
				request.setAttribute("response", outputURL);
				String nextJSP = "/pages/incfGenericError.jsp";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
				dispatcher.forward(request,response);
			}
			System.out.println("Ends redirectiing on the webpage...");
			//End

		} catch ( Exception e ) {

			e.printStackTrace();

		}

	}

	//http://132.239.131.188:8080/incf-services/service/wbc?request=GetCorrelationMap&srscode=ABA&x=263&y=159&z=227&filter=maptype:coronal&output=html
	public void getCoorelationMapForABA( HttpServletRequest request, 
			   HttpServletResponse response, String spaceName, String functionName ) {

		//Parameters
		String hostName = incfConfig.getValue("ucsd.host.name");
		String portNumber = incfConfig.getValue("ucsd.port.number");
		String servicePath = incfConfig.getValue("ucsd.aba.service.path");

		System.out.println(" Parameters... " );
		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");

		//Start - Changes recommended by Ilya
		String filter = request.getParameter("filter");
		String outputType = request.getParameter("output");
		//End - Changes recommended by Ilya

		try { 
		
		String wbcClientUrlString = "http://" + hostName + portNumber + servicePath + "request="+functionName+ "&srscode="+spaceName + "&x=" +coordinateX+"&y="+coordinateY+"&z="+coordinateZ + "&filter=" + filter + "&output=" + outputType; 
		
		System.out.println(" hostName - " + hostName );
		System.out.println(" Port Number - " + portNumber );
		System.out.println(" ABA Service Path - " + servicePath );
		System.out.println(" WBC Client URL - " + wbcClientUrlString );
		
		System.out.println("Starts running WBS Client for getGenesByPOIForEmage...");
		
		//Start - Call servlet, and read the string from the website or the servlet
		URL url = new URL(wbcClientUrlString);
		URLConnection urlCon = url.openConnection();
		urlCon.setUseCaches(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
		.getInputStream()));
		String inputLine;
		String outputURL = "";
		while ((inputLine = in.readLine()) != null) {
		System.out.println("inputLine - "+inputLine);
		outputURL = outputURL + inputLine;
		}
		System.out.println("outputURL - "+outputURL);
		System.out.println("Ends running WBS Client for getCoorelationMap URL...");
		
		System.out.println("Starts redirecting on the webpage...");
		if ( outputURL.startsWith("http") ) {
		    response.sendRedirect(outputURL.toString().trim());
		} else {
			request.setAttribute("response", outputURL);
			String nextJSP = "/pages/incfGenericError.jsp";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
		}
		System.out.println("Ends redirectiing on the webpage...");

		System.out.println("Ends redirecting on the webpage...");
		//End
		
		} catch ( Exception e ) {
		
		e.printStackTrace();
		
		}
		
	}
	
	
	//WBC Client Service - http://132.239.131.188:8080/incf-services/service/wbc?request=GetGenesByPOI&atlasSpaceName=ABA&x=263&y=159&z=227
	public void getGenesByPOIForABA( HttpServletRequest request, 
			   HttpServletResponse response, String spaceName, String functionName ) {

		//http://132.239.131.188:8080/incf-services/service/wbc?func=GetGenesByPOI&srscode=ABA&&x=0&y=4&z=2&output=html
		//Parameters
		String hostName = incfConfig.getValue("ucsd.host.name");
		String portNumber = incfConfig.getValue("ucsd.port.number");
		String servicePath = incfConfig.getValue("ucsd.aba.service.path");

		//Start - Changes recommended by Ilya
		String outputType = request.getParameter("output");
		//End - Changes recommended by Ilya

		System.out.println(" Parameters... " );
		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");
		
		try { 
		
		String wbcClientUrlString = "http://" + hostName + portNumber + servicePath + "request="+functionName+ "&srscode="+spaceName + "&x=" + coordinateX+"&y="+coordinateY+"&z="+coordinateZ+"&output="+outputType;

		System.out.println(" hostName - " + hostName );
		System.out.println(" Port Number - " + portNumber );
		System.out.println(" ABA Service Path - " + servicePath );
		System.out.println(" WBC Client URL - " + wbcClientUrlString );
		
		System.out.println("Starts running WBS Client for getGenesByPOI URL...");
		//Start - Call servlet, and read the string from the website or the servlet
		URL url = new URL(wbcClientUrlString);
		URLConnection urlCon = url.openConnection();
		urlCon.setUseCaches(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
		.getInputStream()));
		String inputLine;
		String outputURL = "";
		while ((inputLine = in.readLine()) != null) {
		System.out.println("inputLine - "+inputLine);
		outputURL = outputURL + inputLine;
		}
		System.out.println("outputURL - "+outputURL);
		System.out.println("Ends running WBS Client for getGenesByPOI URL...");
		
		System.out.println("Starts redirecting on the webpage...");
		if ( outputURL.startsWith("http") ) {
		    response.sendRedirect(outputURL.toString().trim());
		} else {
			request.setAttribute("response", outputURL);
			String nextJSP = "/pages/incfGenericError.jsp";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
		}
		System.out.println("Ends redirecting on the webpage...");
		//End
		
		} catch ( Exception e ) {
		
		e.printStackTrace();
		
		}
		
	}

	//WBC Client Service - http://incf-dev-mapserver.crbs.ucsd.edu:8080/incf-services/service/ABAServiceController?request=GetFineStructureNameByPOI&atlasSpaceName=ABA&x=263&y=159&z=227
	public void getGenesByPOIForEmage( HttpServletRequest request, 
			   HttpServletResponse response, String spaceName, String functionName ) {

		//http://132.239.131.188:8080/incf-services/service/wbc?func=GetFineStructureNameByPOI&atlasSpaceName=ABA&&x=0&y=4&z=2
		//Parameters
		String hostName = incfConfig.getValue("ucsd.host.name");
		String portNumber = incfConfig.getValue("ucsd.port.number");
		String servicePath = incfConfig.getValue("ucsd.emage.service.path");

		System.out.println(" Parameters... " );
		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");
		String outputType = request.getParameter("output");
		
		try { 
		
		String wbcClientUrlString = "http://" + hostName + portNumber + servicePath + "request="+functionName+ "&srscode="+spaceName + "&x=" + coordinateX+"&y="+coordinateY+"&z="+coordinateZ+"&output="+outputType; 
		
		System.out.println(" hostName - " + hostName );
		System.out.println(" Port Number - " + portNumber );
		System.out.println(" ABA Service Path - " + servicePath );
		System.out.println(" WBC Client URL - " + wbcClientUrlString );
		
		System.out.println("Starts running WBS Client for getGenesByPOI URL...");
		//Start - Call servlet, and read the string from the website or the servlet
		URL url = new URL(wbcClientUrlString);
		URLConnection urlCon = url.openConnection();
		urlCon.setUseCaches(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
		.getInputStream()));
		String inputLine;
		String outputURL = "";
		while ((inputLine = in.readLine()) != null) {
		System.out.println("inputLine - "+inputLine);
		outputURL = outputURL + inputLine;
		}
		System.out.println("outputURL - "+outputURL);
		System.out.println("Ends running WBS Client for getGenesByPOI URL...");
		
		System.out.println("Starts redirecting on the webpage...");
		if ( outputURL.startsWith("http") ) {
		    response.sendRedirect(outputURL.toString().trim());
		} else {
			request.setAttribute("response", outputURL);
			String nextJSP = "/pages/incfGenericError.jsp";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
		}
		System.out.println("Ends redirectiing on the webpage...");
		//End
		
		} catch ( Exception e ) {
		
		e.printStackTrace();
		
		}
		
	}

	
	//WBC Client Service - http://incf-dev-mapserver.crbs.ucsd.edu:8080/incf-services/service/ABAServiceController?request=GetAnatomicStructureNameByPOI&atlasSpaceName=ABA&x=263&y=159&z=227
	public void getAnatomicStructureNameByPOIForABA( HttpServletRequest request, 
			   HttpServletResponse response, String spaceName, String functionName, String filter ) {

		//http://132.239.131.188:8080/incf-services/service/wbc?func=GetAnatomicStructureNameByPOI&atlasSpaceName=ABA&&x=0&y=4&z=2
		//Parameters
		String hostName = incfConfig.getValue("ucsd.host.name");
		String portNumber = incfConfig.getValue("ucsd.port.number");
		String servicePath = incfConfig.getValue("ucsd.aba.service.path");

		System.out.println(" Parameters... " );
		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");

		String outputType = request.getParameter("output");

		try { 
		
		String wbcClientUrlString = "http://" + hostName + portNumber + servicePath + "request="+functionName+ "&srscode="+spaceName + "&x=" + coordinateX+"&y="+coordinateY+"&z="+coordinateZ+"&filter="+filter+"&output="+outputType; 
		
		System.out.println(" hostName - " + hostName );
		System.out.println(" Port Number - " + portNumber );
		System.out.println(" ABA Service Path - " + servicePath );
		System.out.println(" WBC Client URL - " + wbcClientUrlString );
		
		System.out.println("Starts running WBS Client for for getAnatomicStructureByPOIForABA URL...");
		//Start - Call servlet, and read the string from the website or the servlet
		URL url = new URL(wbcClientUrlString);
		URLConnection urlCon = url.openConnection();
		urlCon.setUseCaches(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
		.getInputStream()));
		String inputLine;
		String outputURL = "";
		while ((inputLine = in.readLine()) != null) {
		System.out.println("inputLine - "+inputLine);
		outputURL = outputURL + inputLine;
		}
		System.out.println("outputURL - "+outputURL);
		System.out.println("Ends running WBS Client for for getAnatomicStructureByPOIForABA URL...");
		
		System.out.println("Starts redirecting on the webpage...");
		if ( outputURL.startsWith("http") ) {
		    response.sendRedirect(outputURL.toString().trim());
		} else {
			request.setAttribute("response", outputURL);
			String nextJSP = "/pages/incfGenericError.jsp";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
		}
		System.out.println("Ends redirectiing on the webpage...");
		//End
		
		} catch ( Exception e ) {
		
		e.printStackTrace();
		
		}
		
	}
	

	//WBC Client Service - http://incf-dev-mapserver.crbs.ucsd.edu:8080/incf-services/service/ABAServiceController?request=GetFineStructureNameByPOI&atlasSpaceName=ABA&x=263&y=159&z=227
	public void getFineStructureNameByPOIForABA( HttpServletRequest request, 
			   HttpServletResponse response, String spaceName, String functionName, String filter ) {

		//http://132.239.131.188:8080/incf-services/service/wbc?func=GetFineStructureNameByPOI&atlasSpaceName=ABA&&x=0&y=4&z=2
		//Parameters
		String hostName = incfConfig.getValue("ucsd.host.name");
		String portNumber = incfConfig.getValue("ucsd.port.number");
		String servicePath = incfConfig.getValue("ucsd.aba.service.path");

		System.out.println(" Parameters... " );
		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");
		String outputType = request.getParameter("output");
		
		try { 
		
		String wbcClientUrlString = "http://" + hostName + portNumber + servicePath + "request="+functionName+ "&srscode="+spaceName + "&x=" + coordinateX+"&y="+coordinateY+"&z="+coordinateZ+"&filter="+filter+"&output="+outputType; 
		
		System.out.println(" hostName - " + hostName );
		System.out.println(" Port Number - " + portNumber );
		System.out.println(" ABA Service Path - " + servicePath );
		System.out.println(" WBC Client URL - " + wbcClientUrlString );
		
		System.out.println("Starts running WBS Client for for getFineStructureByPOIForABA URL...");
		//Start - Call servlet, and read the string from the website or the servlet
		URL url = new URL(wbcClientUrlString);
		URLConnection urlCon = url.openConnection();
		urlCon.setUseCaches(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
		.getInputStream()));
		String inputLine;
		String outputURL = "";
		while ((inputLine = in.readLine()) != null) {
		System.out.println("inputLine - "+inputLine);
		outputURL = outputURL + inputLine;
		}
		System.out.println("outputURL - "+outputURL);
		System.out.println("Ends running WBS Client for for getFineStructureByPOIForABA URL...");
		
		System.out.println("Starts redirecting on the webpage...");
		if ( outputURL.startsWith("http") ) {
		    response.sendRedirect(outputURL.toString().trim());
		} else {
			request.setAttribute("response", outputURL);
			String nextJSP = "/pages/incfGenericError.jsp";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
		}
		System.out.println("Ends redirectiing on the webpage...");
		//End
		
		} catch ( Exception e ) {
		
		e.printStackTrace();
		
		}
		
	}

	
	//WBC Client Service - http://incf-dev-mapserver.crbs.ucsd.edu:8080/incf-services/service/ABAServiceController?request=GetAnatomicStructureNameByPOI&atlasSpaceName=ABA&x=263&y=159&z=227
	public void getAnatomicStructureNameByPOIForWHS( HttpServletRequest request, 
			   HttpServletResponse response, String spaceName, String functionName, String filter ) {

		//http://132.239.131.188:8080/incf-services/service/wbc?func=GetAnatomicStructureNameByPOI&atlasSpaceName=ABA&&x=0&y=4&z=2
		//Parameters
		String hostName = incfConfig.getValue("ucsd.host.name");
		String portNumber = incfConfig.getValue("ucsd.port.number");
		String servicePath = incfConfig.getValue("ucsd.whs.service.path");

		System.out.println(" Parameters... " );
		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");
		String outputType = request.getParameter("output");
		
		try { 
		
		String wbcClientUrlString = "http://" + hostName + portNumber + servicePath + "request="+functionName+ "&srscode="+spaceName + "&x=" + coordinateX+"&y="+coordinateY+"&z="+coordinateZ+"&filter="+filter+"&output="+outputType; 
		
		System.out.println(" hostName - " + hostName );
		System.out.println(" Port Number - " + portNumber );
		System.out.println(" ABA Service Path - " + servicePath );
		System.out.println(" WBC Client URL - " + wbcClientUrlString );
		
		System.out.println("Starts running WBS Client for for getAnatomicStructureByPOIForWHS URL...");
		//Start - Call servlet, and read the string from the website or the servlet
		URL url = new URL(wbcClientUrlString);
		URLConnection urlCon = url.openConnection();
		urlCon.setUseCaches(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
		.getInputStream()));
		String inputLine;
		String outputURL = "";
		while ((inputLine = in.readLine()) != null) {
		System.out.println("inputLine - "+inputLine);
		outputURL = outputURL + inputLine;
		}
		System.out.println("outputURL - "+outputURL);
		System.out.println("Ends running WBS Client for for getAnatomicStructureByPOIForWHS URL...");
		
		System.out.println("Starts redirecting on the webpage...");
		if ( outputURL.startsWith("http") ) {
		    response.sendRedirect(outputURL.toString().trim());
		} else {
			request.setAttribute("response", outputURL);
			String nextJSP = "/pages/incfGenericError.jsp";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
		}
		System.out.println("Ends redirectiing on the webpage...");
		//End
		
		} catch ( Exception e ) {
		
		e.printStackTrace();
		
		}
		
	}
	

	//WBC Client Service - http://incf-dev-mapserver.crbs.ucsd.edu:8080/incf-services/service/ABAServiceController?request=GetFineStructureNameByPOI&atlasSpaceName=ABA&x=263&y=159&z=227
	public void getFineStructureNameByPOIForWHS( HttpServletRequest request, 
			   HttpServletResponse response, String spaceName, String functionName, String filter ) {

		//http://132.239.131.188:8080/incf-services/service/wbc?func=GetFineStructureNameByPOI&atlasSpaceName=ABA&&x=0&y=4&z=2
		//Parameters
		String hostName = incfConfig.getValue("ucsd.host.name");
		String portNumber = incfConfig.getValue("ucsd.port.number");
		String servicePath = incfConfig.getValue("ucsd.aba.service.path");

		System.out.println(" Parameters... " );
		String coordinateX = request.getParameter("x");
		String coordinateY = request.getParameter("y");
		String coordinateZ = request.getParameter("z");
		String outputType = request.getParameter("output");

		try { 

		String wbcClientUrlString = "http://" + hostName + portNumber + servicePath + "request="+functionName+ "&srscode="+spaceName + "&x=" + coordinateX+"&y="+coordinateY+"&z="+coordinateZ+"&filter="+filter+"&output="+outputType; 

		System.out.println(" hostName - " + hostName );
		System.out.println(" Port Number - " + portNumber );
		System.out.println(" ABA Service Path - " + servicePath );
		System.out.println(" WBC Client URL - " + wbcClientUrlString );

		System.out.println("Starts running WBC Client for getFineStructureByPOIForWHS URL...");
		//Start - Call servlet, and read the string from the website or the servlet
		URL url = new URL(wbcClientUrlString);
		URLConnection urlCon = url.openConnection();
		urlCon.setUseCaches(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
		.getInputStream()));
		String inputLine;
		String outputURL = "";
		while ((inputLine = in.readLine()) != null) {
		System.out.println("inputLine - "+inputLine);
		outputURL = outputURL + inputLine;
		}
		System.out.println("outputURL - "+outputURL);
		System.out.println("Ends running WBS Client for for getFineStructureByPOIForWHS URL...");

		System.out.println("Starts redirecting on the webpage...");
		if ( outputURL.startsWith("http") ) {
		    response.sendRedirect(outputURL.toString().trim());
		} else {
			request.setAttribute("response", outputURL);
			String nextJSP = "/pages/incfGenericError.jsp";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
		}
		System.out.println("Ends redirectiing on the webpage...");
		//End

		} catch ( Exception e ) {

		e.printStackTrace();

		}

	}


	public void getCapabilities() {
	}

	public void getStructure() {
	}

	public void getGenes() {
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
