/**
 * <p>Title: SmartAtlas Data Access Servlet</p>
 * <p>Description: SmartAtlas Data Access Servlet</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: INCF-CC, UCSD</p>
 * @author Haiyun He
 * @version 1.0
 */
package edu.ucsd.crbs.incf.components.services.agea;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucsd.crbs.incf.common.INCFConfigurator;
import edu.ucsd.crbs.incf.common.INCFLogger;

/**
 * AtlasTableInfoObject
 * 
 * The main servlet class to handle atlas related database access function (the
 * main purpose is to hide the direct connection to database)
 */
public class AGEAServiceController extends HttpServlet {

	private INCFConfigurator configurator = INCFConfigurator.INSTANCE;

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

			System.out.println("************* " + servletName
					+ " : Some changeInitializing... *************");

			INCFLogger.logDebug(AGEAServiceController.class,
					"Welcome to the ABA Servlet");

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
			throws ServletException, IOException {

		java.util.Vector v = new java.util.Vector();

		try {

			response.setContentType("application/x-java-serialized-object");
			OutputStream os = response.getOutputStream();
			ObjectOutputStream output = new ObjectOutputStream(os);

				// Get the function name
				String functionName = request.getParameter("func");

				if (functionName.equalsIgnoreCase("getImageAtROI")) {
					getImageAtROI();
				} else if (functionName.equalsIgnoreCase("getCapabilities")) {
					getCapabilities();
				} else if (functionName.equalsIgnoreCase("getGenes")) {
					getGenes();
				} else if (functionName.equalsIgnoreCase("getStructure")) {
					getStructure();
				} else if (functionName.equalsIgnoreCase("spaceTransformation")) {
					spaceTransformation();
				} else if (functionName.equalsIgnoreCase("openMap")) {
					openMap();
				}

				output.writeObject(v);
				output.flush();
				output.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static void main(String args[]) {

		String[] abbrevs = { "M1, M2, M3" };
		String[] rels = { "SELF, SIBLING, PARENT, CHILD" };
		String category = "MOUSE";
		AGEAServiceController helper = new AGEAServiceController();

		try {
			// helper.getLabelSet( );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getImageAtROI() {

		try {

		} catch (Exception e) {

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
	 * Get the image information registered with a slice for the SDO
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param conn
	 *            Connection
	 * @return Vector
	 */
	public Vector getImageInfoForSDO(HttpServletRequest request, Connection conn) {

		Vector v = new Vector();
		try {
		} catch (Exception ex) {
		} finally {
			return v;
		}

	}

	/**
	 * Implementation method to get the image information registered with a
	 * slice
	 * 
	 * @param nSliceID
	 *            String
	 * @param category
	 *            String
	 * @param conn
	 *            Connection
	 * @throws Exception
	 * @return Vector
	 */
	public Vector getImageInfo(String nSliceID, String category, Connection conn)
			throws Exception {

		Vector retV = new Vector();
		return retV;

	}

	/**
	 * Implementation method to get the image information registered with a
	 * slice
	 * 
	 * @param nSliceID
	 *            String
	 * @param category
	 *            String
	 * @param conn
	 *            Connection
	 * @throws Exception
	 * @return Vector
	 */
	public Vector getImageInfoForSDO(String finalCompleteList, String category,
			Connection conn) throws Exception {

		Vector retV = new Vector();

		return retV;

	}

	/**
	 * Clean up the resources
	 * 
	 * @throws ServletException
	 */
	public void destory() throws ServletException {
		try {
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
