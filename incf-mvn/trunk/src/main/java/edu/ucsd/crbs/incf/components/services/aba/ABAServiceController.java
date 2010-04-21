/**
 * <p>Title: SmartAtlas Data Access Servlet</p>
 * <p>Description: SmartAtlas Data Access Servlet</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: INCF-CC, UCSD</p>
 * @author Haiyun He
 * @version 1.0
 */
package edu.ucsd.crbs.incf.components.services.aba;

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
import edu.ucsd.crbs.incf.components.services.ucsd.UCSDServiceVO;
import edu.ucsd.crbs.incf.util.INCFUtil;

/**
 * AtlasTableInfoObject
 * 
 * The main servlet class to handle atlas related database access function (the
 * main purpose is to hide the direct connection to database)
 */
public class ABAServiceController extends HttpServlet {

	private INCFConfigurator configurator = INCFConfigurator.INSTANCE;

	INCFConfigurator incfConfig = INCFConfigurator.INSTANCE;

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
		System.out.println("************* " + servletName
				+ " : Initializing... *************");

		try {

			INCFLogger.logDebug(ABAServiceController.class,
					"Welcome to the ABA Servlet");

			System.out.println("Welcome to the ABA Servlet");

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
		String responseString = "";

		PrintWriter out = response.getWriter();

		try {

			// Get the function name
			String functionName = request.getParameter("request");
			System.out.println(" Request is - " + functionName);

			String outputType = request.getParameter("output");
			System.out.println(" Output Type is - " + outputType);

			if (functionName.equalsIgnoreCase("get2DImageAtPOI")) {

				System.out.println(" Inside get2DImageAtPOI at server side");
				get2DImageAtPOI(request, response);

			} else if (functionName.equalsIgnoreCase("GetCorrelationMap")) {

				System.out.println(" Inside OpenCoorelationMap at server side");
				responseString = getCoorelationMap(request, response);

			} else if (functionName.equalsIgnoreCase("GetGenesByPOI")) {

				System.out.println(" Inside Get Genes By POI at server side");
				responseString = getGenesByPOI(request, response);

			} else if (functionName.equalsIgnoreCase("GetStructureNameByPOI")) {
				String filter = request.getParameter("filter");
				String filterValue = filter.toLowerCase().replaceAll(
						"structureset:", "").trim();
				System.out.println("Structure Set - " + filterValue);
				if (filterValue.equalsIgnoreCase("Fine")) {
					System.out
							.println(" Inside Get Fine StructureName By POI at server side");
					responseString = getFineStructureNameByPOI(request,
							response);
				} else if (filterValue.equalsIgnoreCase("Anatomic")) {
					System.out
							.println(" Inside Get Anatomic StructureName By POI at server side");
					responseString = getAnatomicStructureNameByPOI(request,
							response);
				}
			} else if (functionName.equalsIgnoreCase("GetGenesByStructureName")) {

				String filter = request.getParameter("filter");
				String filterValue = filter.toLowerCase().replaceAll(
						"structureset:", "").trim();
				System.out.println("Structure Set - " + filterValue);
				if (filterValue.equalsIgnoreCase("Fine")) {
					System.out
							.println(" Inside Get Fine StructureName By POI at server side");
					responseString = getGenesByFineStructureName(request,
							response);
				} else if (filterValue.equalsIgnoreCase("Anatomic")) {
					System.out
							.println(" Inside Get Anatomic StructureName By POI at server side");
					responseString = getGenesByAnatomicStructureName(request,
							response);
				}

			} else if (functionName.equalsIgnoreCase("spaceTransformation")
					&& outputType.equalsIgnoreCase("html")
					|| functionName.equalsIgnoreCase("spaceTransformation")
					&& outputType.equalsIgnoreCase("txt")) {

				System.out
						.println(" Inside Space Transformation at server side");
				responseString = spaceTransformation(request, response);

			} else if (functionName.equalsIgnoreCase("spaceTransformation")
					&& outputType.equalsIgnoreCase("xml")) {

				responseString = spaceTransformationXMLResponse(request,
						response);

			} else if (functionName.equalsIgnoreCase("GetCapabilities")) {

				System.out
						.println(" Inside GetCapabilities WPS at server side");
				getCapabilitiesWPS(response);

			} else if (functionName.equalsIgnoreCase("DescribeProcess")) {

				System.out
						.println(" Inside Describe Process WPS at server side");
				describeProcessWPS(response);

			}

			if (outputType == null || outputType.trim().equalsIgnoreCase("")) {
				System.out.println("OutputType is NULL");
				out
						.println("Output Type is missing. Please provide output type.");
			} else if (outputType.trim().equalsIgnoreCase("txt")
					|| outputType.trim().equalsIgnoreCase("xml")) {
				System.out.println("OutputType is TEXT");
				out.println(responseString);
			} /*
				 * else if ( outputType.trim().equalsIgnoreCase("xml") ) {
				 * System.out.println("OutputType is XML"); out.println("XML
				 * Output is not supported at this time"); }
				 */else if (outputType.trim().equalsIgnoreCase("html")) {
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
			// End

		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		} finally {
		}

	}

	public static void main(String args[]) {

		String[] abbrevs = { "M1, M2, M3" };
		String[] rels = { "SELF, SIBLING, PARENT, CHILD" };
		String category = "MOUSE";
		ABAServiceController helper = new ABAServiceController();
		ABAServiceController controller = new ABAServiceController();

		try {

			// controller.getCapabilities();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Rest Service -
	// http://132.239.131.188:8080/incf-services/service/ABAServiceController?request=get2DImageAtPOI&srscode=ABA&x=263&y=159&z=227&width=217&height=152&filter=maptype:coronal&output=html
	public void get2DImageAtPOI(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - Ge2DImageAtPOI Method...");

		// Set the content type
		response.setContentType("image/jpeg");

		StringBuffer responseString = new StringBuffer();

		// http://mouse.brain-map.org/agea/all_coronal/slice_correlation_image?plane=coronal&index=7525&blend=0&width=217&height=152&loc=7525,4075,6300&lowerRange=0.5&upperRange=1
		// 1) Define and Get parameters from URL
		System.out.println(" Parameters... ");
		String height = request.getParameter("height");
		String width = request.getParameter("width");

		String.valueOf((Math.round(Double.parseDouble("235667.564"))));

		CommonServiceVO vo = new CommonServiceVO();

		String coordinateX = incfUtil.getRoundCoordinateValue(request
				.getParameter("x"));
		String coordinateY = incfUtil.getRoundCoordinateValue(request
				.getParameter("y"));
		String coordinateZ = incfUtil.getRoundCoordinateValue(request
				.getParameter("z"));

		// Start - Changes recommended by Ilya
		String filter = request.getParameter("filter");
		String mapType = filter.replaceAll("maptype:", "");
		String fromSpaceName = request.getParameter("SRSCode");
		String toSpaceName = "AGEA";

		if (filter == null || filter.trim().equals("")) {
			responseString
					.append("Filter parameter is missing. Please provide the filter parameter");
			// return responseString.toString();
		}
		if (!mapType.trim().equals("coronal")) {
			responseString.append("Incorrect Filter");
			// return responseString.toString();
		}

		// Start - Exception Handling
		if (fromSpaceName == null || fromSpaceName.trim().equals("")) {
			responseString
					.append("SRS Code is missing. Please provide the SRS Code");
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
		if (height == null || height.trim().equals("")) {
			responseString
					.append("Height definition is missing. Please provide the height");
			// return responseString.toString();
		}
		if (width == null || width.trim().equals("")) {
			responseString
					.append("Width definition is missing. Please provide the width");
			// return responseString.toString();
		}
		// End - Exception Handling

		// Define config Properties
		String hostName = incfConfig.getValue("incf.aba.host.name");
		String portNumber = incfConfig.getValue("incf.aba.port.number");
		String abaServicePath = incfConfig.getValue("incf.aba.service.path");

		/*
		 * if ( fromSpaceName.equals("whs") ) {
		 * 
		 * //This step is required to convert the whs mesh coordinate that wbc
		 * client generates into //whs voxel coordinates
		 * System.out.println("Inside WHS original coordinates transformation");
		 * INCFUtil util1 = new INCFUtil(); String [] whsVoxelCoordinates =
		 * util1.getWHSVoxelCoordinates(coordinateX, coordinateY, coordinateZ);
		 * coordinateX = whsVoxelCoordinates[0]; coordinateY =
		 * whsVoxelCoordinates[1]; coordinateZ = whsVoxelCoordinates[2];
		 *  }
		 */
		try {

			System.out.println("Starts Transformation  matrix process...");

			// 2) Get the transformed coordinates from Steve's program
			// http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/atlas_lookup.cgi?atlas=aba&direction=forward&x=263&y=159&z=227
			String transFormedCoodinateX = "";
			String transFormedCoodinateY = "";
			String transFormedCoodinateZ = "";

			INCFUtil util = new INCFUtil();

			vo.setFromSRSCodeOne(fromSpaceName);
			vo.setToSRSCodeOne(toSpaceName);
			vo.setOriginalCoordinateX(coordinateX);
			vo.setOriginalCoordinateY(coordinateY);
			vo.setOriginalCoordinateZ(coordinateZ);
			String transformedCoordinatesString = util.spaceTransformation(vo);

			String[] transformedCoordinates = util
					.getTabDelimNumbers(transformedCoordinatesString);

			transFormedCoodinateX = transformedCoordinates[0];
			transFormedCoodinateY = transformedCoordinates[1];
			transFormedCoodinateZ = transformedCoordinates[2];
			// End

			// Exception Handling
			if (transFormedCoodinateX.trim().equalsIgnoreCase("out")) {
				responseString.append("Out of Range");
				// return responseString.toString();
			}

			System.out.println("Ends running transformation  matrix...");

			// 3) Construct URL to get 2D Image from ABA space
			System.out.println("Starts running ABA getImage URL...");
			String index = transFormedCoodinateX;

			String imageURLPrefix = "http://" + hostName + portNumber
					+ abaServicePath + "all_" + mapType
					+ "/slice_correlation_image?" + "plane=" + mapType + "&";

			responseString.append(imageURLPrefix).append(
					"index=" + index + "&blend=0&width=" + width + "&height="
							+ height).append(
					"&loc=" + transFormedCoodinateX + ","
							+ transFormedCoodinateY + ","
							+ transFormedCoodinateZ).append(
					"&lowerRange=0.5&upperRange=1");

			System.out.println(" Complete URL for ABA is - "
					+ responseString.toString());

			response.sendRedirect(responseString.toString().trim());

			System.out.println("Ends running ABA getImage URL...");

		} catch (Exception e) {

			e.printStackTrace();
			responseString
					.append("Please contact the administrator to resolve this issue");

		} finally {

		}

		System.out.println("End - Ge2DImageAtPOI Method...");

		// 4) Return response back to the cllient in a text/xml format
		// return responseString.toString();

	}

	// Rest Service -
	// http://132.239.131.188:8080/incf-services/service/ABAServiceController?request=getCorrelationMap&srscode=ABA&x=263&y=159&z=227&filter=maptype:coronal&output=html
	public String getCoorelationMap(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - OpenCoorelationMap Method...");

		// Set the content type
		response.setContentType("text/html");

		StringBuffer responseString = new StringBuffer();

		// http://mouse.brain-map.org/agea/all_coronal?correlation&seedPoint=8375,4150,3800
		// //1) Define and Get parameters from URL
		// Define config Properties
		String hostName = incfConfig.getValue("incf.aba.host.name");
		String portNumber = incfConfig.getValue("incf.aba.port.number");
		String abaServicePath = incfConfig.getValue("incf.aba.service.path");

		System.out.println(" Parameters... ");
		String coordinateX = incfUtil.getRoundCoordinateValue(request
				.getParameter("x"));
		String coordinateY = incfUtil.getRoundCoordinateValue(request
				.getParameter("y"));
		String coordinateZ = incfUtil.getRoundCoordinateValue(request
				.getParameter("z"));

		// Start - Changes recommended by Ilya
		String filter = request.getParameter("filter");
		String mapType = filter.replaceAll("maptype:", "");
		String fromSpaceName = request.getParameter("SRSCode");
		String toSpaceName = "AGEA";

		if (filter == null || filter.trim().equals("")) {
			responseString
					.append("Filter parameter is missing. Please provide the filter parameter");
			return responseString.toString();
		}
		if (!mapType.trim().equals("coronal")) {
			responseString.append("Incorrect Filter");
			return responseString.toString();
		}
		// End - Changes recommended by Ilya

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

		if (mapType == null || mapType.trim().equals("")) {
			responseString
					.append("Map Type is missing. Please provide the Map Type");
			return responseString.toString();
		}

		/*
		 * if ( fromSpaceName.equals("whs") ) {
		 * 
		 * System.out.println("Inside WHS original coordinates transformation");
		 * INCFUtil util1 = new INCFUtil(); String [] whsVoxelCoordinates =
		 * util1.getWHSVoxelCoordinates(coordinateX, coordinateY, coordinateZ);
		 * coordinateX = whsVoxelCoordinates[0]; coordinateY =
		 * whsVoxelCoordinates[1]; coordinateZ = whsVoxelCoordinates[2];
		 *  }
		 */
		try {

			System.out.println("Start - transformation matrix process...");

			// 2) Get the transformed coordinates from Steve's program
			// http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/atlas_lookup.cgi?atlas=aba&direction=forward&x=263&y=159&z=227
			String transFormedCoodinateX = "";
			String transFormedCoodinateY = "";
			String transFormedCoodinateZ = "";

			INCFUtil util = new INCFUtil();

			CommonServiceVO vo = new CommonServiceVO();
			vo.setFromSRSCode(fromSpaceName);
			vo.setToSRSCode(toSpaceName);
			vo.setFromSRSCodeOne(fromSpaceName);
			vo.setToSRSCodeOne(toSpaceName);
			vo.setOriginalCoordinateX(coordinateX);
			vo.setOriginalCoordinateY(coordinateY);
			vo.setOriginalCoordinateZ(coordinateZ);

			String transformedCoordinatesString = util
					.spaceTransformation(vo);

			String[] transformedCoordinates = util
					.getTabDelimNumbers(transformedCoordinatesString);

			transFormedCoodinateX = transformedCoordinates[0];
			transFormedCoodinateY = transformedCoordinates[1];
			transFormedCoodinateZ = transformedCoordinates[2];
			// End

			// Exception Handling
			if (transFormedCoodinateX.trim().equalsIgnoreCase("out")) {
				responseString.append("Out of Range");
				return responseString.toString();
			}

			System.out.println("Ends running transformation  matrix...");

			// 3) Construct URL to get 2D Coorelation Map
			System.out
					.println("Start - Running ABA get coorelation Map URL...");

			String imageURLPrefix = "http://" + hostName + portNumber
					+ abaServicePath + "all_" + mapType + "?correlation&";

			responseString.append(imageURLPrefix).append(
					"seedPoint=" + transFormedCoodinateX + ","
							+ transFormedCoodinateY + ","
							+ transFormedCoodinateZ);

			System.out.println(" Complete URL for ABA is - "
					+ responseString.toString());

			System.out.println("Ends running ABA getImage URL...");

		} catch (Exception e) {

			e.printStackTrace();
			responseString
					.append("Please contact the administrator to resolve this issue");

		} finally {

		}

		System.out.println("End - Open CoorelationMap Method...");

		// 4) Return response back to the cllient in a text/xml format
		return responseString.toString();

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
		String hostName = incfConfig.getValue("incf.aba.host.name");
		String portNumber = incfConfig.getValue("incf.aba.port.number");
		String abaServicePath = incfConfig.getValue("incf.aba.service.path");

		System.out.println(" Parameters... ");
		String coordinateX = incfUtil.getRoundCoordinateValue(request
				.getParameter("x"));
		String coordinateY = incfUtil.getRoundCoordinateValue(request
				.getParameter("y"));
		String coordinateZ = incfUtil.getRoundCoordinateValue(request
				.getParameter("z"));

		StringBuffer responseString = new StringBuffer();

		// Start - Changes recommended by Ilya
		String fromSpaceName = request.getParameter("SRSCode");
		String toSpaceName = "AGEA";
		// End - Changes recommended by Ilya

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

		/*
		 * if ( fromSpaceName.equals("whs") ) {
		 * 
		 * System.out.println("Inside WHS original coordinates transformation");
		 * INCFUtil util1 = new INCFUtil(); String [] whsVoxelCoordinates =
		 * util1.getWHSVoxelCoordinates(coordinateX, coordinateY, coordinateZ);
		 * coordinateX = whsVoxelCoordinates[0]; coordinateY =
		 * whsVoxelCoordinates[1]; coordinateZ = whsVoxelCoordinates[2];
		 *  }
		 */
		try {

			System.out.println("Start - transformation matrix process...");

			// 2) Get the transformed coordinates from Steve's program
			// http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/atlas_lookup.cgi?atlas=aba&direction=forward&x=263&y=159&z=227
			String transFormedCoodinateX = "";
			String transFormedCoodinateY = "";
			String transFormedCoodinateZ = "";

			INCFUtil util = new INCFUtil();

			CommonServiceVO vo = new CommonServiceVO();
			vo.setFromSRSCode(fromSpaceName);
			vo.setToSRSCode(toSpaceName);
			vo.setFromSRSCodeOne(fromSpaceName);
			vo.setToSRSCodeOne(toSpaceName);
			vo.setOriginalCoordinateX(coordinateX);
			vo.setOriginalCoordinateY(coordinateY);
			vo.setOriginalCoordinateZ(coordinateZ);

			String transformedCoordinatesString = util
					.spaceTransformation(vo);

			String[] transformedCoordinates = util
					.getTabDelimNumbers(transformedCoordinatesString);

			// Exception Handling
			if (transformedCoordinates[0].trim().equalsIgnoreCase("out")) {
				responseString.append("Out of Range");
				return responseString.toString();
			}

			transFormedCoodinateX = util
					.getNearest200Micron(transformedCoordinates[0]);
			transFormedCoodinateY = util
					.getNearest200Micron(transformedCoordinates[1]);
			transFormedCoodinateZ = util
					.getNearest200Micron(transformedCoordinates[2]);
			// End

			System.out.println("Ends running transformation matrix...");

			// 3) Construct URL to get Genes By POI
			System.out.println("Start - Running ABA get GenesByPOI URL...");
			String imageURLPrefix = "http://" + hostName + portNumber
					+ abaServicePath + "GeneFinder.html?seedPoint=";

			responseString.append(imageURLPrefix).append(
					transFormedCoodinateX + "," + transFormedCoodinateY + ","
							+ transFormedCoodinateZ);

			System.out.println(" Complete URL for ABA is - "
					+ responseString.toString());

			System.out.println("Ends running ABA getGenesByPOI URL...");

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

	// http://132.239.131.188:8080/incf-services/service/wbc?request=GetFineStructureNameByPOI&atlasSpaceName=ABA&x=263&y=159&z=227
	public String getFineStructureNameByPOI(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - getFineStructureNameByPOI Method...");

		// Set the content type
		response.setContentType("text/html");

		// http://mouse.brain-map.org/agea/all_coronal/slice_correlation_image?plane=coronal&index=7525&blend=0&width=217&height=152&loc=7525,4075,6300&lowerRange=0.5&upperRange=1
		// 1) Define and Get parameters from URL
		System.out.println(" Parameters... ");
		String fromSpaceName = request.getParameter("SRSCode");
		String coordinateX = incfUtil.getRoundCoordinateValue(request
				.getParameter("x"));
		String coordinateY = incfUtil.getRoundCoordinateValue(request
				.getParameter("y"));
		String coordinateZ = incfUtil.getRoundCoordinateValue(request
				.getParameter("z"));
		String vocabulary = request.getParameter("vocabulary");

		// Define config Properties
		String hostName = incfConfig.getValue("incf.aba.host.name");
		String portNumber = incfConfig.getValue("incf.aba.port.number");
		String abaServicePath = incfConfig.getValue("incf.aba.service.path");

		String fineStructureName = "";
		String anatomicStructureName = "";
		String outOfRange = "";
		String responseString = "";

		// Start - Exception Handling
		if (fromSpaceName == null || fromSpaceName.trim().equals("")) {
			responseString = "SRS Code is missing. Please provide the srs code";
			return responseString;
		}

		if (vocabulary == null || vocabulary.trim().equals("")) {
			responseString = "Vocabulary is missing. Please provide vocabulary";
			return responseString;
		}

		if (coordinateX == null || coordinateX.trim().equals("")) {
			responseString = "Coordinate X is missing. Please provide Coordinate X";
			return responseString;
		}

		if (coordinateY == null || coordinateY.trim().equals("")) {
			responseString = "Coordinate Y is missing. Please provide Coordinate Y";
			return responseString;
		}

		if (coordinateZ == null || coordinateZ.trim().equals("")) {
			responseString = "Coordinate Z is missing. Please provide Coordinate Z";
			return responseString;
		}
		// End - Exception Handling

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

			// Exception Handling
			if (transformedCoordinates[0].trim().equalsIgnoreCase("out")) {
				responseString = "Out of Range";
				return responseString.toString();
			}

			coordinateX = transformedCoordinates[0];
			coordinateY = transformedCoordinates[1];
			coordinateZ = transformedCoordinates[2];

		}

		try {

			System.out.println("Starts Transformation matrix process...");

			// 2) Get the transformed coordinates from Steve's program
			// http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/structure_lookup.cgi?atlas=aba&x=264&y=160&z=228
			INCFUtil util = new INCFUtil();

			// Cannot say fromSpaceName as the structure look up is supported
			// only for abavoxel
			String structureNamesString = util.getStructureNameLookup(
					"abavoxel", coordinateX, coordinateY, coordinateZ);

			String[] structureNames = util
					.getTabDelimNumbers(structureNamesString);

			fineStructureName = structureNames[1];
			anatomicStructureName = structureNames[0];
			outOfRange = structureNames[2];

			// Start - Changes
			if (outOfRange != null && outOfRange.trim().equalsIgnoreCase("out")) {
				responseString = "Out of Range";
			} else if (fineStructureName.trim().equals("-")) {
				responseString = "No data found";
			} else if (!fineStructureName.trim().equals("")
					|| fineStructureName != null) {
				responseString = "Fine Structure Name: "
						.concat(fineStructureName);
			}
			// End - Changes

			System.out.println("Response String - " + responseString);

			// End
			System.out.println("Ends running transformation  matrix...");

		} catch (Exception e) {

			e.printStackTrace();
			responseString = "Please contact the administrator to resolve this issue.";

		} finally {

		}

		System.out.println("End - getFineStructureNameByPOI Method...");

		// 4) Return response back to the cllient in a text/xml format
		return responseString;

	}

	// http://132.239.131.188:8080/incf-services/service/wbc?request=GetFineStructureNameByPOI&atlasSpaceName=ABA&x=263&y=159&z=227
	public String getAnatomicStructureNameByPOI(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - getAnatomicStructureNameByPOI Method...");

		// Set the content type
		response.setContentType("text/html");

		// http://mouse.brain-map.org/agea/all_coronal/slice_correlation_image?plane=coronal&index=7525&blend=0&width=217&height=152&loc=7525,4075,6300&lowerRange=0.5&upperRange=1
		// 1) Define and Get parameters from URL
		System.out.println(" Parameters... ");
		String fromSpaceName = request.getParameter("SRSCode");
		String coordinateX = incfUtil.getRoundCoordinateValue(request
				.getParameter("x"));
		String coordinateY = incfUtil.getRoundCoordinateValue(request
				.getParameter("y"));
		String coordinateZ = incfUtil.getRoundCoordinateValue(request
				.getParameter("z"));
		String vocabulary = request.getParameter("vocabulary");

		// Define config Properties
		String hostName = incfConfig.getValue("incf.aba.host.name");
		String portNumber = incfConfig.getValue("incf.aba.port.number");
		String abaServicePath = incfConfig.getValue("incf.aba.service.path");

		String fineStructureName = "";
		String anatomicStructureName = "";

		StringBuffer responseString = new StringBuffer();

		// Start - Exception Handling
		if (fromSpaceName == null || fromSpaceName.trim().equals("")) {
			responseString
					.append("SRS Code is missing. Please provide the srs code");
			return responseString.toString();
		}

		if (vocabulary == null || vocabulary.trim().equals("")) {
			responseString
					.append("Vocabulary is missing. Please provide vocabulary");
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

			// Exception Handling
			if (transformedCoordinates[0].trim().equalsIgnoreCase("out")) {
				responseString.append("Out of Range");
				return responseString.toString();
			}

			coordinateX = transformedCoordinates[0];
			coordinateY = transformedCoordinates[1];
			coordinateZ = transformedCoordinates[2];

		}

		try {

			System.out.println("Starts Transformation matrix process...");

			// 2) Get the transformed coordinates from Steve's program
			// http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/structure_lookup.cgi?atlas=aba&x=264&y=160&z=228
			INCFUtil util = new INCFUtil();

			// Cannot say fromSpaceName as the structure look up is supported
			// only for abavoxel
			String structureNamesString = util.getStructureNameLookup(
					"abavoxel", coordinateX, coordinateY, coordinateZ);

			String[] structureNames = util
					.getTabDelimNumbers(structureNamesString);

			fineStructureName = structureNames[1];
			anatomicStructureName = structureNames[0];
			String outOfRange = structureNames[2];

			// Start - Changes
			if (outOfRange != null && outOfRange.trim().equalsIgnoreCase("out")) {
				responseString.append("Out of Range");
			} else if (anatomicStructureName.trim().equals("-")) {
				responseString.append("No data found");
			} else if (!anatomicStructureName.trim().equals("")
					|| anatomicStructureName != null) {
				responseString.append("Anatomic Structure Name: "
						.concat(anatomicStructureName));
			}
			// End - Changes
			System.out.println("Anatomic Structure - "
					+ responseString.toString());

			// End
			System.out.println("Ends running transformation matrix...");

		} catch (Exception e) {

			e.printStackTrace();
			responseString
					.append("Please contact the administrator to resolve this issue");

		} finally {

		}

		System.out.println("End - getAnatomicStructureNameByPOI Method...");

		// 4) Return response back to the cllient in a text/xml format
		return responseString.toString();

	}

	public String getGenesByFineStructureName(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - getGenesByFineStructureName Method...");

		// Set the content type
		response.setContentType("text/html");

		// http://132.239.131.188:8080/incf-services/service/ABAServiceController?request=GetStructureName&vocabulary=ABA&filter=structureset:Fine&term=DG&output=html
		// 1) Define and Get parameters from URL
		System.out.println(" Parameters... ");
		String vocabulary = request.getParameter("vocabulary");
		String term = request.getParameter("term");

		String responseString = "";

		// Start - Exception Handling
		if (vocabulary == null || vocabulary.trim().equals("")) {
			responseString = "Vocabulary is missing. Please provide vocabulary";
			return responseString;
		}

		if (term == null || term.trim().equals("")) {
			responseString = "Term is missing. Please provide the term";
			return responseString;
		}
		// End - Exception Handling

		try {

			// 2) Construct URL to get Genes By POI
			// http://mouse.brain-map.org/FineStructure/ARH/1.html
			System.out.println("Start - Running ABA get GenesByPOI URL...");
			String imageURLPrefix = "http://mouse.brain-map.org/FineStructure/"
					+ term + "/1.html";

			responseString = imageURLPrefix;

			System.out.println(" Complete URL for ABA is - "
					+ responseString.toString());

		} catch (Exception e) {

			e.printStackTrace();
			responseString = "Please contact the administrator to resolve this issue.";

		} finally {

		}

		System.out.println("End - getGenesByFineStructureName Method...");

		// 4) Return response back to the cllient in a text/xml format
		return responseString;

	}

	public String getGenesByAnatomicStructureName(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - getGenesByAnatomicStructureName Method...");

		// Set the content type
		response.setContentType("text/html");

		// http://132.239.131.188:8080/incf-services/service/ABAServiceController?request=GetStructureName&vocabulary=ABA&filter=structureset:Fine&term=DG&output=html
		// 1) Define and Get parameters from URL
		System.out.println(" Parameters... ");
		String vocabulary = request.getParameter("vocabulary");
		String term = request.getParameter("term");

		String responseString = "";

		// Start - Exception Handling
		if (vocabulary == null || vocabulary.trim().equals("")) {
			responseString = "Vocabulary is missing. Please provide vocabulary";
			return responseString;
		}

		if (term == null || term.trim().equals("")) {
			responseString = "Term is missing. Please provide the term";
			return responseString;
		}
		// End - Exception Handling

		try {

			// 2) Construct URL to get Genes By POI
			// http://mouse.brain-map.org/GeneExpression/Cerebellum/1.html?ispopup=1
			System.out.println("Start - Running ABA get GenesByPOI URL...");
			String imageURLPrefix = "http://mouse.brain-map.org/GeneExpression/"
					+ term + "/1.html?ispopup=1";

			responseString = imageURLPrefix;

			System.out.println(" Complete URL for ABA is - "
					+ responseString.toString());

		} catch (Exception e) {

			e.printStackTrace();
			responseString = "Please contact the administrator to resolve this issue.";

		} finally {

		}

		System.out.println("End - getGenesByAnatomicStructureName Method...");

		// 4) Return response back to the cllient in a text/xml format
		return responseString;

	}

	// These are for the transformations done by Steve Lamont
	// http://132.239.131.188:8080/incf-services/service/ABAServiceController?request=SpaceTransformation&srcsrscode=ABA&destsrscode&x=263&y=159&z=227
	public String spaceTransformation(HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("Start - ABA spaceTransformation Method...");

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

		if (!fromSpaceName.equalsIgnoreCase("abareference")) {
			coordinateX = incfUtil.getRoundCoordinateValue(request
					.getParameter("x"));
			coordinateY = incfUtil.getRoundCoordinateValue(request
					.getParameter("y"));
			coordinateZ = incfUtil.getRoundCoordinateValue(request
					.getParameter("z"));
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

			/*
			 * response.setContentType("text/xml"); PrintWriter out =
			 * response.getWriter();
			 */
			System.out.println("Start - transformation matrix process...");

			// 2) Get the transformed coordinates from Steve's program
			// http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/atlas_lookup.cgi?atlas=aba&direction=forward&x=263&y=159&z=227
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
				// responseString.append("Out of Range");
				message = "OUT OF BOUND";
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
			out.println("<ows:Title>ABA Services</ows:Title>\n");
			out
					.println("<ows:Abstract>ABA Services are created to access various aba atlas space features that are offered by UCSD to its clients.</ows:Abstract>\n");
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
			out.println("<ows:Identifier>GetCorrelationMap</ows:Identifier>\n");
			out.println("<ows:Title>Get Correlation Map</ows:Title> \n");
			out
					.println("<ows:Description>This method will return the URL and load the correlation map interface in the browser</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out
					.println("<ows:Identifier>GetStructureNameByPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get structure name by POI</ows:Title> \n");
			out
					.println("<ows:Description>Returns Fine/Anatomic structures segmented for the point of interest on map</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out.println("<ows:Identifier>Get2DImageAtPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get 2D Image at POI</ows:Title> \n");
			out
					.println("<ows:Description>Returns 2 Dimensional image based on point of interest on the map</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out.println("<ows:Identifier>GetGenesByPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get genes by POI</ows:Title> \n");
			out
					.println("<ows:Description>Returns genes segmented for the given point of interest on the map</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out
					.println("<ows:Identifier>GetGenesByStructureName</ows:Identifier>\n");
			out
					.println("<ows:Title>Get genes by Structure Name</ows:Title> \n");
			out
					.println("<ows:Description>Returns genes segmented for structure name</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out
					.println("<ows:Identifier>SpaceTransformation</ows:Identifier>\n");
			out
					.println("<ows:Title>Atlas Space Transformation from ABA to AGEA, and AGEA to ABA</ows:Title> \n");
			out
					.println("<ows:Description>Finds, transforms and executes the best transformation available from ABA to AGEA, and AGEA to ABA</ows:Description>\n");
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
			out
					.println("<ProcessDescription wps:processVersion=\"1.0.0\" statusSupported=\"true\">\n");
			out.println("<ows:Identifier>GetCorrelationMap</ows:Identifier>\n");
			out.println("<ows:Title>Get Correlation Map</ows:Title>\n");
			out
					.println("<ows:Description>This method will return the URL and load the correlation map interface in the browser</ows:Description>\n");
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
			out.println("<ows:Title>Atlas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as ABA</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("</DataInputs>\n");
			out.println("<ProcessOutputs>\n");
			out.println("<Output>\n");
			out.println("<ows:Identifier>CorrelationMap</ows:Identifier>\n");
			out.println("<ows:Title>Correlation Map</ows:Title>\n");
			out
					.println("<ows:Abstract>Correlation map browser interface</ows:Abstract>\n");
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

			out.println("<ProcessDescription>\n");
			out
					.println("<ows:Identifier>GetStructureNameByPOI</ows:Identifier>\n");
			out
					.println("<ows:Title>Get fine/anatomic structure name by POI</ows:Title>\n");
			out
					.println("<ows:Description>Returns fine/anatomic structures segmented for the point of interest</ows:Description>\n");
			out.println("<DataInputs>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>MapPoint</ows:Identifier>\n");
			out.println("<ows:Title>Map point</ows:Title>\n");
			out
					.println("<ows:Abstract>Point of interest on the map</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:double\"/>\n");
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
			out.println("<Input>\n");
			out.println("<ows:Identifier>Filter</ows:Identifier>\n");
			out.println("<ows:Title>Strutureset</ows:Title>\n");
			out
					.println("<ows:Abstract>Structureset:fine/anatomic</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>SRSCODE</ows:Identifier>\n");
			out.println("<ows:Title>Atlas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as ABA</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("</DataInputs>\n");
			out.println("<ProcessOutputs>\n");
			out.println("<Output>\n");
			out.println("<ows:Identifier>StructureName</ows:Identifier>\n");
			out
					.println("<ows:Title>Fine/Anatomic Structure Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Fine/Anatomic Structures segmented for the given point of interest</ows:Abstract>\n");
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
					.println("<ows:Abstract>Name of an atlas space such as ABA</ows:Abstract>\n");
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
			out.println("<ows:Identifier>GetGenesByPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get genes by POI</ows:Title>\n");
			out
					.println("<ows:Description>Returns genes segmented for the given point of interest on the map</ows:Description>\n");
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
			out.println("<ows:Title>Atlas Space Name</ows:Title>\n");
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

			out.println("<ProcessDescription>\n");
			out
					.println("<ows:Identifier>GetGenesByStructureName</ows:Identifier>\n");
			out.println("<ows:Title>Get genes by Structure Name</ows:Title>\n");
			out
					.println("<ows:Description>Returns genes segmented for structure name</ows:Description>\n");
			out.println("<DataInputs>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>StructureName</ows:Identifier>\n");
			out.println("<ows:Title>Structure Name</ows:Title>\n");
			out.println("<ows:Abstract>Name of the structure</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:double\"/>\n");
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
			out.println("<Input>\n");
			out.println("<ows:Identifier>Filter</ows:Identifier>\n");
			out.println("<ows:Title>Strutureset</ows:Title>\n");
			out
					.println("<ows:Abstract>Structureset:fine/anatomic</ows:Abstract>\n");
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
					.println("<ows:Abstract>Name of an atlas space such as ABA, or AGEA</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>ToSRSCODE</ows:Identifier>\n");
			out
					.println("<ows:Title>Destination Atlas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as AGEA, or ABA</ows:Abstract>\n");
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

	public void getCapabilitiesWPSWithAllProcesses(HttpServletResponse response) {

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
			out
					.println("<ows:Title>ABA Service/UCSD Service/WHS Service</ows:Title>\n");
			out
					.println("<ows:Abstract>ABA Service/UCSD Service/WHS Service are developed to access various services that are offered by UCSD to its clients.</ows:Abstract>\n");
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
			out
					.println("<ows:Identifier>LoadCorrelationMap</ows:Identifier>\n");
			out.println("<ows:Title>Load Correlation Map</ows:Title> \n");
			out
					.println("<ows:Description>This method will return the URL and load the correlation map interface in the browser</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out
					.println("<ows:Identifier>GetStructureNameByPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get structure name by POI</ows:Title> \n");
			out
					.println("<ows:Description>Returns structures segmented for the point of interest on map</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out.println("<ows:Identifier>Get2DImageAtPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get 2D Image at POI</ows:Title> \n");
			out
					.println("<ows:Description>Returns 2 Dimensional image based on point of interest on the map</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out.println("<ows:Identifier>GetGenesByPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get genes by POI</ows:Title> \n");
			out
					.println("<ows:Description>Returns genes segmented for the given point of interest on the map</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out
					.println("<ows:Identifier>GetReferenceSpaceInfo</ows:Identifier>\n");
			out
					.println("<ows:Title>Get Reference Space Information</ows:Title>\n");
			out
					.println("<ows:Description>Get the required reference space information</ows:Description>\n");
			out.println("</wps:Process>\n");

			out.println("<wps:Process>\n");
			out
					.println("<ows:Identifier>AtlasSpaceTransformation</ows:Identifier>\n");
			out.println("<ows:Title>Atlas Space Transformation</ows:Title> \n");
			out
					.println("<ows:Description>Finds, transforms and executes the best transformation available in the space based on the requirement</ows:Description>\n");
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

	private void describeProcessWPSWithAllProcesses(HttpServletResponse response) {

		StringBuffer sb = new StringBuffer();

		try {

			sb = new StringBuffer();

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();

			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			out
					.println("<wps:ProcessDescriptions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps\" xmlns:wps=\"http://www.opengis.net/wps\" xmlns:ows=\"http://www.opengis.net/ows\" version=\"1.0.0\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 ../wpsGetCapabilities_request.xsd\">\n");
			out
					.println("<ProcessDescription wps:processVersion=\"1.0.0\" statusSupported=\"true\">\n");
			out
					.println("<ows:Identifier>LoadCorrelationMap</ows:Identifier>\n");
			out.println("<ows:Title>Load Correlation Map</ows:Title>\n");
			out
					.println("<ows:Description>This method will return the URL and load the correlation map interface in the browser</ows:Description>\n");
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
			out.println("<ows:Title>Atlas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as ABARef, or ABAVox, or WHS, or UCSD</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("</DataInputs>\n");
			out.println("<ProcessOutputs>\n");
			out.println("<Output>\n");
			out.println("<ows:Identifier>CorrelationMap</ows:Identifier>\n");
			out.println("<ows:Title>Correlation Map</ows:Title>\n");
			out
					.println("<ows:Abstract>Correlation map browser interface</ows:Abstract>\n");
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
			out.println("<ProcessDescription>\n");
			out
					.println("<ows:Identifier>GetStructureNameByPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get structure name by POI</ows:Title>\n");
			out
					.println("<ows:Description>Returns structures segmented for the point of interest</ows:Description>\n");
			out.println("<DataInputs>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>MapPoint</ows:Identifier>\n");
			out.println("<ows:Title>Map point</ows:Title>\n");
			out
					.println("<ows:Abstract>Point of interest on the map</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:double\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>SRSCODE</ows:Identifier>\n");
			out.println("<ows:Title>Atlas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as ABARef, or ABAVox, or WHS, or UCSD</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("</DataInputs>\n");
			out.println("<ProcessOutputs>\n");
			out.println("<Output>\n");
			out.println("<ows:Identifier>StructureName</ows:Identifier>\n");
			out.println("<ows:Title>Structure Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Structures segmented for the given point of interest</ows:Abstract>\n");
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
					.println("<ows:Abstract>Name of an atlas space such as ABARef, or ABAVox, or WHS, or UCSD</ows:Abstract>\n");
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
			out.println("<ows:Identifier>GetGenesByPOI</ows:Identifier>\n");
			out.println("<ows:Title>Get genes by POI</ows:Title>\n");
			out
					.println("<ows:Description>Returns genes segmented for the given point of interest on the map</ows:Description>\n");
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
			out.println("<ows:Title>Atlas Space Name</ows:Title>\n");
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

			out.println("<ProcessDescription>\n");
			out
					.println("<ows:Identifier>GetReferenceSpaceInfo</ows:Identifier>\n");
			out
					.println("<ows:Title>Get Reference Space Information</ows:Title>\n");
			out
					.println("<ows:Description>Get the required reference space information</ows:Description>\n");
			out.println("<DataInputs>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>SRSCODE</ows:Identifier>\n");
			out.println("<ows:Title>Atlas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as ABA</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("</DataInputs>\n");
			out.println("<ProcessOutputs>\n");
			out.println("<Output>\n");
			out.println("<ows:Identifier>SpaceMetadata</ows:Identifier>\n");
			out.println("<ows:Title>Space Metadata</ows:Title>\n");
			out
					.println("<ows:Abstract>Returns all available transformations associated with the given atlas space name</ows:Abstract>\n");
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
					.println("<ows:Abstract>Name of an atlas space such as ABARef, or ABAVox, or WHS, or UCSD</ows:Abstract>\n");
			out.println("<LiteralData>\n");
			out.println("<ows:DataType ows:reference=\"xs:string\"/>\n");
			out.println("</LiteralData>\n");
			out.println("</Input>\n");
			out.println("<Input>\n");
			out.println("<ows:Identifier>ToSRSCODE</ows:Identifier>\n");
			out
					.println("<ows:Title>Destination Atlas Space Name</ows:Title>\n");
			out
					.println("<ows:Abstract>Name of an atlas space such as ABARef, or ABAVox, or WHS, or UCSD</ows:Abstract>\n");
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
