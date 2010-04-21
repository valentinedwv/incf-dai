package edu.ucsd.crbs.incf.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.ows.ServiceException;

import edu.ucsd.crbs.incf.common.CommonServiceVO;
import edu.ucsd.crbs.incf.common.INCFConfigurator;
import edu.ucsd.crbs.incf.components.transformation.abar2abav.ABATransform;
import edu.ucsd.crbs.incf.components.transformation.paxinos2whs.Paxinos2WHS;
import edu.ucsd.crbs.incf.components.transformation.paxinos2whs.WHS2Paxinos;
import edu.ucsd.crbs.incf.components.transformation.whsm2whsv.WHSm2WHSv;

public class INCFUtil {
	
	INCFConfigurator incfConfig = INCFConfigurator.INSTANCE;

	/**
	 * @param args
	 */
	public static void main(String[] args) { 
		// TODO Auto-generated method stub
		INCFUtil test = new INCFUtil();
		String result = test.directSpaceTransformation("abavoxel", "whs", "255", "511", "255");
		System.out.println("Results - " + result);
		test.getTabDelimNumbers(result);
		//test.getTabDelimNumbers("2 5 78 45 57 78");
		//test.getNearest200Micron("3699");
	}


	public String[] getTabDelimNumbers(String tabLimNumbers ) {

		StringTokenizer tokens = new StringTokenizer(tabLimNumbers, " ");
		int tokensSize = tokens.countTokens();

		String[] coordinateString = new String[tokensSize]; 
		String[] transformedCoordinates = new String[3]; //Returned coordinates are 3
		System.out.println( " tokens - " +tokensSize);

		int i = 0;
		while ( tokens.hasMoreTokens() ) {
			coordinateString[i] = tokens.nextToken(); 
			i++;
		}

			transformedCoordinates[0] = coordinateString[3];
			System.out.println( " transformedCoordinates x - " + transformedCoordinates[0] );
			if (tokensSize >= 5 ) {
			transformedCoordinates[1] = coordinateString[4];
			System.out.println( " transformedCoordinates y - " + transformedCoordinates[1] );
			}
			if (tokensSize >= 6 ) {
			transformedCoordinates[2] = coordinateString[5];
			System.out.println( " transformedCoordinates z - " + transformedCoordinates[2] );
			}

		return transformedCoordinates;
		
	}


	public String getNearest200Micron(String coordinateValue) {

		String firstHalf = coordinateValue.substring( 0, coordinateValue.length()-3 );
		String secondHalf = coordinateValue.substring( coordinateValue.length()-3, coordinateValue.length() );

		StringBuffer finalValue = new StringBuffer();

		System.out.println("Mod value is - " + Integer.parseInt(coordinateValue) % 200);

		int modValue1 = Integer.parseInt(secondHalf) % 200;
		int modValue2 = Integer.parseInt(firstHalf.concat(secondHalf)) - modValue1;

		if ( modValue1 >= 0 && modValue1 <= 100 ) {
			System.out.println("True" + Integer.parseInt(coordinateValue) % 200);
			modValue1 = 000;
		} else if ( modValue1 >= 101 && modValue1 <= 200 ) {
			System.out.println("True" + Integer.parseInt(coordinateValue) % 200);
			modValue1 = 200;
		}
		System.out.println( "Final Modvalue1 - " + modValue1 );
		System.out.println( "Final Modvalue2 - " + modValue2 );
		System.out.println( "Final value - " + finalValue.append(modValue2+modValue1));

		return finalValue.toString();
		
	}


	public String transformPOIABA2Agea( String fromSpace, String toSpace, String originalCoordinateX, 
			String originalCoordinateY, String originalCoordinateZ ) {

	String transformedCoordinateString = "";

	try {
	
		String transformationHostName = incfConfig.getValue("incf.transformationservice.host.name");
		String transformationPortNumber = incfConfig.getValue("incf.transformationservice.port.number");
		String transformationServicePath = incfConfig.getValue("incf.transformationservice.atlas.path");

		//Start - Create and run URL, and read the string from the webpage
		String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "atlas="+fromSpace+"&direction=forward&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
		System.out.println("Transformation matrix url is - " + transforMatrixURL); 
		System.out.println("X in transformation matrix method is - " + originalCoordinateX);
		URL url = new URL(transforMatrixURL);
		URLConnection urlCon = url.openConnection();
		urlCon.setUseCaches(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
				.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println("inputLine - "+inputLine);
			transformedCoordinateString = transformedCoordinateString + inputLine;
		}
		System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return transformedCoordinateString;
	
	}

	
	//These are for the transformations done by Steve Lamont 
	public String spaceTransformation( CommonServiceVO vo ) {

		System.out.println("Start - spaceTransformation Method...");
		
		String xmlResponseString = "";

		try { 

			System.out.println("Start - transformation matrix process...");

			//2) Get the transformed coordinates from Steve's program
			INCFUtil util = new INCFUtil();

			//ABAVOXEL to AGEA
			if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {
				
				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				xmlResponseString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

			//via whs
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				//First convert from paxinos to whs 
				xmlResponseString = util.indirectSpaceTransformation( vo );

			//via whs
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 
				xmlResponseString = util.indirectSpaceTransformation( vo );

			//via  WHS, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				//First convert from paxinos to whs 
				xmlResponseString = util.indirectSpaceTransformation( vo );

			//via  WHS, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 
				xmlResponseString = util.indirectSpaceTransformation( vo );

			//via  WHS, and then AGEA, then abavoxel
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				xmlResponseString = util.indirectSpaceTransformation( vo );

			//via  WHS, and then AGEA, then abavoxel
		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				xmlResponseString = util.indirectSpaceTransformation( vo );

			//via AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				//First convert from paxinos to whs 
				xmlResponseString = util.indirectSpaceTransformation( vo );

			//via AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				//First convert from paxinos to whs 
				xmlResponseString = util.indirectSpaceTransformation( vo );

            //via ABAVOXEL
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				//First convert from paxinos to whs 
				xmlResponseString = util.indirectSpaceTransformation( vo );

			//via ABAVOXEL
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				//First convert from paxinos to whs 
				xmlResponseString = util.indirectSpaceTransformation( vo );

	        //via ABAVOXEL, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				//First convert from paxinos to whs 
				xmlResponseString = util.indirectSpaceTransformation( vo );

			//via ABAVOXEL, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				//First convert from paxinos to whs 
				xmlResponseString = util.indirectSpaceTransformation( vo );
				
			} else {

				xmlResponseString = "No such transformation is available at this point";
				
			}

			//End

			System.out.println( "XML Response String - " + xmlResponseString ); 
			System.out.println("Ends running transformation  matrix...");

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		//4) Return response back to the cllient in a text/xml format
		return xmlResponseString;

	}


	public String directSpaceTransformation( String fromSpace, String toSpace, String originalCoordinateX, 
			String originalCoordinateY, String originalCoordinateZ ) {

	String transformedCoordinateString = "";

	System.out.println("DIRECT SPACE TRANSFORMATION...");

	try {

		//By Steve Lamont
		if (fromSpace.trim().equalsIgnoreCase("abavoxel") && toSpace.trim().equalsIgnoreCase("agea")) {

			System.out.println("Inside ABAVOX 2 AGEA...");
			
			String transformationHostName = incfConfig.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = incfConfig.getValue("incf.transformationservice.port.number");
			String transformationServicePath = incfConfig.getValue("incf.transformationservice.atlas.path");
	
			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "atlas="+fromSpace.toLowerCase()+"&direction=forward&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL); 
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}

			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

		}

		//By Steve Lamont
		else if (fromSpace.trim().equalsIgnoreCase("agea") && toSpace.trim().equalsIgnoreCase("abavoxel")) {

			System.out.println("Inside AGEA 2 ABAVOX...");

			String transformationHostName = incfConfig.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = incfConfig.getValue("incf.transformationservice.port.number");
			String transformationServicePath = incfConfig.getValue("incf.transformationservice.atlas.path");

			//Coordinates needs to be divided by 25 to come out of AGEA coordinates
			originalCoordinateX = String.valueOf((Math.round( Double.parseDouble(originalCoordinateX)/25)));
			originalCoordinateY = String.valueOf((Math.round( Double.parseDouble(originalCoordinateY)/25)));
			originalCoordinateZ = String.valueOf((Math.round( Double.parseDouble(originalCoordinateZ)/25)));

			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "direction=inverse&atlas="+toSpace.toLowerCase()+"&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL); 
			System.out.println("X in transformation matrix method is - " + originalCoordinateX);
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);
		}

		//By Steve Lamont
		else if (fromSpace.trim().equalsIgnoreCase("whs") && toSpace.trim().equalsIgnoreCase("agea")) {

			System.out.println("Inside WHS 2 AGEA...");

			String transformationHostName = incfConfig.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = incfConfig.getValue("incf.transformationservice.port.number");
			String transformationServicePath = incfConfig.getValue("incf.transformationservice.atlas.path");

			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "atlas="+fromSpace.toLowerCase()+"&direction=forward&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL); 
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

		}

		//By Steve Lamont
		else if (fromSpace.trim().equalsIgnoreCase("agea") && toSpace.trim().equalsIgnoreCase("whs")) {

			System.out.println("Inside AGEA 2 WHS...");

			String transformationHostName = incfConfig.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = incfConfig.getValue("incf.transformationservice.port.number");
			String transformationServicePath = incfConfig.getValue("incf.transformationservice.atlas.path");

			//Coordinates needs to be divided by 25 to come out of AGEA coordinates
			originalCoordinateX = String.valueOf((Math.round( Double.parseDouble(originalCoordinateX)/25)));
			originalCoordinateY = String.valueOf((Math.round( Double.parseDouble(originalCoordinateY)/25)));
			originalCoordinateZ = String.valueOf((Math.round( Double.parseDouble(originalCoordinateZ)/25)));

			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "direction=inverse&atlas="+toSpace.toLowerCase()+"&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL); 
			System.out.println("X in transformation matrix method is - " + originalCoordinateX);
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);
		}

		//By Steve Lamont
		else if (fromSpace.trim().equalsIgnoreCase("abareference") && toSpace.trim().equalsIgnoreCase("abavoxel")) {

			System.out.println("Inside ABAREF 2 ABAVOX...");

			int[] abar2abav = ABATransform.convertReferenceToVoxel(Double.parseDouble(originalCoordinateX), 
					Double.parseDouble(originalCoordinateY), Double.parseDouble(originalCoordinateZ));

			transformedCoordinateString = originalCoordinateX + " " + originalCoordinateY + " "+ originalCoordinateZ + " " + abar2abav[0] + " " + abar2abav[1]  + " " + abar2abav[2];

			System.out.println("ABAR to ABAV - TransformedCoordinateString - "+transformedCoordinateString);

		}

		//By Steve Lamont
		else if ( fromSpace.trim().equalsIgnoreCase("abavoxel") && toSpace.trim().equalsIgnoreCase("abareference") ) { 

			System.out.println("Inside ABAVOX 2 ABAREF...");

			double[] abav2abar = ABATransform.convertVoxelToReference(Integer.parseInt(originalCoordinateX), 
					Integer.parseInt(originalCoordinateY), Integer.parseInt(originalCoordinateZ)); 

			transformedCoordinateString = originalCoordinateX + " " + originalCoordinateY + " "+ originalCoordinateZ + " " + abav2abar[0] + " " + abav2abar[1]  + " " + abav2abar[2];

			System.out.println("ABAV to ABAR - TransformedCoordinateString - "+transformedCoordinateString);

		} else if ( fromSpace.trim().equalsIgnoreCase("paxinos") && toSpace.trim().equalsIgnoreCase("whs") ) { 

			System.out.println("Inside PAXINOS 2 WHS...");

			Paxinos2WHS paxinos2whs = new Paxinos2WHS();
			//client.getTransformation( 0.00, 4.29, -1.94 );
			transformedCoordinateString = paxinos2whs.getTransformation( Double.parseDouble(originalCoordinateX),  
			Double.parseDouble(originalCoordinateY), Double.parseDouble(originalCoordinateZ) );

			System.out.println("Paxinos to WHS - TransformedCoordinateString - "+transformedCoordinateString);

		} else if ( fromSpace.trim().equalsIgnoreCase("whs") && toSpace.trim().equalsIgnoreCase("paxinos") ) { 

			System.out.println("Inside WHS 2 PAXINOS...");

			WHS2Paxinos whs2paxinos = new WHS2Paxinos();
			transformedCoordinateString = whs2paxinos.getTransformation( Long.parseLong(originalCoordinateX), 
					Long.parseLong(originalCoordinateY), Long.parseLong(originalCoordinateZ) );

			System.out.println("WHS to PAXINOS - TransformedCoordinateString - "+transformedCoordinateString);

	} else {
		transformedCoordinateString = "No such transformation is available at this point. Sorry for the inconvinience";
		return transformedCoordinateString;
	} 

	System.out.println("Ends running transformation  matrix...");

	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return transformedCoordinateString;

	}

	
	//These are for the transformations done by Steve Lamont 
	public String indirectSpaceTransformation( CommonServiceVO vo ) {

		System.out.println("Start - INDIRECT SPACE TRANSFORMATION METHOD...");

		//1) Define and Get parameters from URL
		//Define Properties
		System.out.println(" Parameters... " );

		String hostName = incfConfig.getValue("ucsd.host.name");
		String servicePath = incfConfig.getValue("ucsd.ucsd.service.path");
		String portNumber = incfConfig.getValue("ucsd.port.number");
		String transformationMatrixURLPrefix = hostName + portNumber + servicePath;
		
		System.out.println(" X... " + vo.getOriginalCoordinateX() );
		System.out.println(" Y... " + vo.getOriginalCoordinateY() );
		System.out.println(" Z... " + vo.getOriginalCoordinateZ() );

		StringBuffer responseString = new StringBuffer();

		StringBuffer transformedCoordinates = new StringBuffer();
		String transformedCoordinateString = "";
		String xmlResponseString = "";

		String rawTransformationStringOne = "";
		String rawTransformationStringTwo = "";
		String rawTransformationStringThree = "";
		String rawTransformationStringFour = "";
		StringBuffer transformationOne = new StringBuffer();
		StringBuffer transformationTwo = new StringBuffer();
		StringBuffer transformationThree = new StringBuffer();
		StringBuffer transformationFour = new StringBuffer();
		String[] arrayOfTransformedCoordinatesOne = new String[3];
		String[] arrayOfTransformedCoordinatesTwo = new String[3];
		String[] arrayOfTransformedCoordinatesThree = new String[3];
		String[] arrayOfTransformedCoordinatesFour = new String[3];
		
		try { 

			System.out.println("Start - transformation matrix process...");

			INCFUtil util = new INCFUtil();

			//via whs
			if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("PAXINOS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeOne("WHS");
				vo.setToSRSCodeTwo("AGEA");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];

				System.out.println("PAXINOS to AGEA - TransformedCoordinateString - "+transformedCoordinateString);
				
			//via whs
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("AGEA");
				vo.setToSRSCodeOne("WHS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeTwo("PAXINOS");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];

				System.out.println("AGEA to PAXINOS - TransformedCoordinateString - "+transformedCoordinateString);

			//via  WHS, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("PAXINOS");
				vo.setToSRSCodeOne("WHS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("ABAVOXEL");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - agea to abavoxel
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesThree[0] + " " + arrayOfTransformedCoordinatesThree[1]  + " " + arrayOfTransformedCoordinatesThree[2];

				System.out.println("PAXINOS to ABAVOXEL - TransformedCoordinateString - "+transformedCoordinateString);

			//via  WHS, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAVOXEL");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("WHS");
				vo.setFromSRSCodeThree("WHS");
				vo.setToSRSCodeThree("PAXINOS");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - paxinos to whs
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");
				
				//Third convert - agea to abavoxel
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesThree[0] + " " + arrayOfTransformedCoordinatesThree[1]  + " " + arrayOfTransformedCoordinatesThree[2];
				System.out.println("ABAVOXEL TO PAXINOS - TransformedCoordinateString - "+transformedCoordinateString);

				//via  WHS, and then AGEA, then abavoxel
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 

				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("WHS");
				vo.setFromSRSCodeFour("WHS");
				vo.setToSRSCodeFour("PAXINOS");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - agea to abavoxel
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Fourth convert - abavoxel to abareference
				rawTransformationStringFour = util.directSpaceTransformation(vo.getFromSRSCodeFour(), vo.getToSRSCodeFour(), arrayOfTransformedCoordinatesThree[0], arrayOfTransformedCoordinatesThree[1], arrayOfTransformedCoordinatesThree[2] );

				arrayOfTransformedCoordinatesFour = util.getTabDelimNumbers(rawTransformationStringFour);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesFour[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationFour.append(vo.getFromSRSCodeFour()).append(" ")
								 .append(vo.getToSRSCodeFour()).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesFour[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesFour[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesFour[2]).append(" ");
				vo.setTransformationFour(transformationFour.toString());
				System.out.println("TransformationFour - " + vo.getTransformationFour());

				//Setting the transformation URL
				vo.setTransformationFourURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x="+arrayOfTransformedCoordinatesThree[0]+"&amp;y="+arrayOfTransformedCoordinatesThree[1]+"&amp;z="+arrayOfTransformedCoordinatesThree[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesFour[0] + " " + arrayOfTransformedCoordinatesFour[1]  + " " + arrayOfTransformedCoordinatesFour[2];
				System.out.println("ABAREFERENCE TO PAXINOS - TransformedCoordinateString - "+transformedCoordinateString);

				//via  WHS, and then AGEA, then abavoxel
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

					//First convert from paxinos to whs 
					vo.setFromSRSCodeOne("PAXINOS");
					vo.setToSRSCodeOne("WHS");
					vo.setFromSRSCodeTwo("WHS");
					vo.setToSRSCodeTwo("AGEA");
					vo.setFromSRSCodeThree("AGEA");
					vo.setToSRSCodeThree("ABAVOXEL");
					vo.setFromSRSCodeFour("ABAVOXEL");
					vo.setToSRSCodeFour("ABAREFERENCE");

					//First convert - paxinos to whs
					rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

					arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
						transformedCoordinateString = "Out of Range";
						return transformedCoordinateString;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
									 .append(vo.getToSRSCodeOne()).append(" ")
									 .append(vo.getOriginalCoordinateX()).append(" ")
									 .append(vo.getOriginalCoordinateY()).append(" ")
									 .append(vo.getOriginalCoordinateZ()).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
					vo.setTransformationOne(transformationOne.toString());
					System.out.println("TransformationOne - " + vo.getTransformationOne());

					//Setting the transformation URL
					vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

					//Second convert - whs to agea
					rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

					arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
						transformedCoordinateString = "Out of Range";
						return transformedCoordinateString;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
									 .append(vo.getToSRSCodeTwo()).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
					vo.setTransformationTwo(transformationTwo.toString());
					System.out.println("TransformationTwo - " + vo.getTransformationTwo());

					//Setting the transformation URL
					vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
					
					//Third convert - agea to abavoxel
					rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

					arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
						transformedCoordinateString = "Out of Range";
						return transformedCoordinateString;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
									 .append(vo.getToSRSCodeThree()).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
					vo.setTransformationThree(transformationThree.toString());
					System.out.println("TransformationThree - " + vo.getTransformationThree());

					//Setting the transformation URL
					vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

					//Fourth convert - abavoxel to abareference
					rawTransformationStringFour = util.directSpaceTransformation(vo.getFromSRSCodeFour(), vo.getToSRSCodeFour(), arrayOfTransformedCoordinatesThree[0], arrayOfTransformedCoordinatesThree[1], arrayOfTransformedCoordinatesThree[2] );

					arrayOfTransformedCoordinatesFour = util.getTabDelimNumbers(rawTransformationStringFour);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesFour[0].trim().equalsIgnoreCase("out") ) { 
						transformedCoordinateString = "Out of Range";
						return transformedCoordinateString;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationFour.append(vo.getFromSRSCodeFour()).append(" ")
									 .append(vo.getToSRSCodeFour()).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[2]).append(" ")
									 .append(arrayOfTransformedCoordinatesFour[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesFour[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesFour[2]).append(" ");
					vo.setTransformationFour(transformationFour.toString());
					System.out.println("TransformationFour - " + vo.getTransformationFour());

					//Setting the transformation URL
					vo.setTransformationFourURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x="+arrayOfTransformedCoordinatesThree[0]+"&amp;y="+arrayOfTransformedCoordinatesThree[1]+"&amp;z="+arrayOfTransformedCoordinatesThree[2]+"&amp;output=html");

					//Get and Set xml response string for transformation info
					vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

					//Return A transformation string
					transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesFour[0] + " " + arrayOfTransformedCoordinatesFour[1]  + " " + arrayOfTransformedCoordinatesFour[2];
					System.out.println("PAXINOS TO ABAREFERENCE - TransformedCoordinateString - "+transformedCoordinateString);

			//via AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAVOXEL");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("WHS");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];
				System.out.println("ABAVOXEL TO WHS - TransformedCoordinateString - "+transformedCoordinateString);

			//via AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("WHS");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("ABAVOXEL");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];
				System.out.println("WHS TO ABAVOXEL - TransformedCoordinateString - "+transformedCoordinateString);

            //via ABAVOXEL
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];
				System.out.println("ABAREFERENCE TO AGEA - TransformedCoordinateString - "+transformedCoordinateString);

			//via ABAVOXEL
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("AGEA");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("ABAREFERENCE");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesTwo[0] + " " + arrayOfTransformedCoordinatesTwo[1]  + " " + arrayOfTransformedCoordinatesTwo[2];
				System.out.println("AGEA TO ABAREFERENCE - TransformedCoordinateString - "+transformedCoordinateString);

	        //via ABAVOXEL, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("WHS");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - agea to abavoxel
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesThree[0] + " " + arrayOfTransformedCoordinatesThree[1]  + " " + arrayOfTransformedCoordinatesThree[2];
				System.out.println("ABAREFERENCE TO WHS - TransformedCoordinateString - "+transformedCoordinateString);

			//via ABAVOXEL, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("WHS");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("ABAVOXEL");
				vo.setFromSRSCodeThree("ABAVOXEL");
				vo.setToSRSCodeThree("ABAREFERENCE");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - agea to abavoxel
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					transformedCoordinateString = "Out of Range";
					return transformedCoordinateString;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));
				
				//Return A transformation string
				transformedCoordinateString = vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " "+ vo.getOriginalCoordinateZ() + " " + arrayOfTransformedCoordinatesThree[0] + " " + arrayOfTransformedCoordinatesThree[1]  + " " + arrayOfTransformedCoordinatesThree[2];
				System.out.println("WHS TO ABAREFERENCE - TransformedCoordinateString - "+transformedCoordinateString);

			} else {
				transformedCoordinateString = "No such transformation is available at this point. Sorry for the inconvinience";
				return transformedCoordinateString;
			} 

			System.out.println( "Transformed Coordinate String - " + transformedCoordinateString ); 
			System.out.println( "Ends running transformation  matrix..." );

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		//4) Return response back to the cllient in a text/xml format
		return transformedCoordinateString;

	}

	
	public CommonServiceVO spaceTransformationInfoXML( CommonServiceVO vo ) {

		System.out.println("Start - spaceTransformationView Method...");
		
		String xmlResponseString = "";

		try { 

			System.out.println("Start - transformation matrix process...");

			//2) Get the transformed coordinates from Steve's program
			INCFUtil util = new INCFUtil();

			//ABAVOXEL to AGEA
			if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));
		
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {
				
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via whs
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("PAXINOS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeOne("WHS");
				vo.setToSRSCodeTwo("AGEA");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via whs
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("AGEA");
				vo.setToSRSCodeOne("WHS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeTwo("PAXINOS");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via  WHS, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("PAXINOS");
				vo.setToSRSCodeOne("WHS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("ABAVOXEL");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via  WHS, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAVOXEL");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("WHS");
				vo.setFromSRSCodeThree("WHS");
				vo.setToSRSCodeThree("PAXINOS");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via  WHS, and then AGEA, then abavoxel
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("WHS");
				vo.setFromSRSCodeFour("WHS");
				vo.setToSRSCodeFour("PAXINOS");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via  WHS, and then AGEA, then abavoxel
		    } else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				vo.setFromSRSCodeOne("PAXINOS");
				vo.setToSRSCodeOne("WHS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("ABAVOXEL");
				vo.setFromSRSCodeFour("ABAVOXEL");
				vo.setToSRSCodeFour("ABAREFERENCE");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAVOXEL");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("WHS");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("WHS");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("ABAVOXEL");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

            //via ABAVOXEL
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via ABAVOXEL
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("AGEA");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("ABAREFERENCE");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

	        //via ABAVOXEL, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("WHS");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via ABAVOXEL, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("WHS");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("ABAVOXEL");
				vo.setFromSRSCodeThree("ABAVOXEL");
				vo.setToSRSCodeThree("ABAREFERENCE");

				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));
				
			}
			//End

			System.out.println( "XML Response String - " + xmlResponseString ); 
			System.out.println("Ends running transformation  matrix...");

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		//4) Return response back to the cllient in a text/xml format
		return vo;

	}


	//These are for the transformations done by Steve Lamont 
	public CommonServiceVO spaceTransformationView( CommonServiceVO vo ) {

		System.out.println("Start - spaceTransformationView Method...");

		//1) Define and Get parameters from URL
		//Define Properties
		System.out.println(" Parameters... " );

		String hostName = incfConfig.getValue("ucsd.host.name");
		String servicePath = incfConfig.getValue("ucsd.ucsd.service.path");
		String portNumber = incfConfig.getValue("ucsd.port.number");
		String transformationMatrixURLPrefix = hostName + portNumber + servicePath;
		
		System.out.println(" X... " + vo.getOriginalCoordinateX() );
		System.out.println(" Y... " + vo.getOriginalCoordinateY() );
		System.out.println(" Z... " + vo.getOriginalCoordinateZ() );

		StringBuffer responseString = new StringBuffer();

		StringBuffer transformedCoordinates = new StringBuffer();
		String xmlResponseString = "";

		String rawTransformationStringOne = "";
		String rawTransformationStringTwo = "";
		String rawTransformationStringThree = "";
		String rawTransformationStringFour = "";
		StringBuffer transformationOne = new StringBuffer();
		StringBuffer transformationTwo = new StringBuffer();
		StringBuffer transformationThree = new StringBuffer();
		StringBuffer transformationFour = new StringBuffer();
		String[] arrayOfTransformedCoordinatesOne = new String[3];
		String[] arrayOfTransformedCoordinatesTwo = new String[3];
		String[] arrayOfTransformedCoordinatesThree = new String[3];
		String[] arrayOfTransformedCoordinatesFour = new String[3];
		
		try { 

/*			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
*/
			System.out.println("Start - transformation matrix process...");

			//2) Get the transformed coordinates from Steve's program
			//http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/atlas_lookup.cgi?atlas=aba&direction=forward&x=263&y=159&z=227

			INCFUtil util = new INCFUtil();

			//ABAVOXEL to AGEA
			if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
/*					request.setAttribute("response", responseString);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
*/					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+ "&amp;output=html");

				//Get and Set xml response string for transformation info with execution
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));
		
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {
				
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
/*					request.setAttribute("response", responseString);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
*/					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+ "&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
/*					request.setAttribute("response", responseString);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
*/					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+ "&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
/*					request.setAttribute("response", responseString);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
*/					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+ "&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
/*					request.setAttribute("response", responseString);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
*/					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+ "&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
/*					request.setAttribute("response", responseString);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
*/					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+ "&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
/*					request.setAttribute("response", responseString);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
*/					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+ "&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
/*					request.setAttribute("response", responseString);
					String nextJSP = "/pages/incfGenericError.jsp";
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
*/					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+ "&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via whs
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("PAXINOS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeOne("WHS");
				vo.setToSRSCodeTwo("AGEA");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via whs
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("AGEA");
				vo.setToSRSCodeOne("WHS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeTwo("PAXINOS");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via  WHS, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("PAXINOS");
				vo.setToSRSCodeOne("WHS");
				vo.setFromSRSCodeTwo("WHS");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("ABAVOXEL");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - agea to abavoxel
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via  WHS, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAVOXEL");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("WHS");
				vo.setFromSRSCodeThree("WHS");
				vo.setToSRSCodeThree("PAXINOS");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - paxinos to whs
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");
				
				//Third convert - agea to abavoxel
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));
				
				//via  WHS, and then AGEA, then abavoxel
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("paxinos") ) {

				//First convert from paxinos to whs 

				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("WHS");
				vo.setFromSRSCodeFour("WHS");
				vo.setToSRSCodeFour("PAXINOS");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - agea to abavoxel
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Fourth convert - abavoxel to abareference
				rawTransformationStringFour = util.directSpaceTransformation(vo.getFromSRSCodeFour(), vo.getToSRSCodeFour(), arrayOfTransformedCoordinatesThree[0], arrayOfTransformedCoordinatesThree[1], arrayOfTransformedCoordinatesThree[2] );

				arrayOfTransformedCoordinatesFour = util.getTabDelimNumbers(rawTransformationStringFour);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesFour[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationFour.append(vo.getFromSRSCodeFour()).append(" ")
								 .append(vo.getToSRSCodeFour()).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesFour[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesFour[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesFour[2]).append(" ");
				vo.setTransformationFour(transformationFour.toString());
				System.out.println("TransformationFour - " + vo.getTransformationFour());

				//Setting the transformation URL
				vo.setTransformationFourURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x="+arrayOfTransformedCoordinatesThree[0]+"&amp;y="+arrayOfTransformedCoordinatesThree[1]+"&amp;z="+arrayOfTransformedCoordinatesThree[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

				//via  WHS, and then AGEA, then abavoxel
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

					//First convert from paxinos to whs 
					vo.setFromSRSCodeOne("PAXINOS");
					vo.setToSRSCodeOne("WHS");
					vo.setFromSRSCodeTwo("WHS");
					vo.setToSRSCodeTwo("AGEA");
					vo.setFromSRSCodeThree("AGEA");
					vo.setToSRSCodeThree("ABAVOXEL");
					vo.setFromSRSCodeFour("ABAVOXEL");
					vo.setToSRSCodeFour("ABAREFERENCE");

					//First convert - paxinos to whs
					rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

					arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
						vo.setErrorMessage("Out of Range");
						return vo;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
									 .append(vo.getToSRSCodeOne()).append(" ")
									 .append(vo.getOriginalCoordinateX()).append(" ")
									 .append(vo.getOriginalCoordinateY()).append(" ")
									 .append(vo.getOriginalCoordinateZ()).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
					vo.setTransformationOne(transformationOne.toString());
					System.out.println("TransformationOne - " + vo.getTransformationOne());

					//Setting the transformation URL
					vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

					//Second convert - whs to agea
					rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

					arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
						vo.setErrorMessage("Out of Range");
						return vo;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
									 .append(vo.getToSRSCodeTwo()).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
					vo.setTransformationTwo(transformationTwo.toString());
					System.out.println("TransformationTwo - " + vo.getTransformationTwo());

					//Setting the transformation URL
					vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
					
					//Third convert - agea to abavoxel
					rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

					arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
						vo.setErrorMessage("Out of Range");
						return vo;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
									 .append(vo.getToSRSCodeThree()).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
					vo.setTransformationThree(transformationThree.toString());
					System.out.println("TransformationThree - " + vo.getTransformationThree());

					//Setting the transformation URL
					vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

					//Fourth convert - abavoxel to abareference
					rawTransformationStringFour = util.directSpaceTransformation(vo.getFromSRSCodeFour(), vo.getToSRSCodeFour(), arrayOfTransformedCoordinatesThree[0], arrayOfTransformedCoordinatesThree[1], arrayOfTransformedCoordinatesThree[2] );

					arrayOfTransformedCoordinatesFour = util.getTabDelimNumbers(rawTransformationStringFour);

					//Exception Handling
					if ( arrayOfTransformedCoordinatesFour[0].trim().equalsIgnoreCase("out") ) { 
						vo.setErrorMessage("Out of Range");
						return vo;
					}

					// replace this with fromspace tospace x y z tx ty tz
					transformationFour.append(vo.getFromSRSCodeFour()).append(" ")
									 .append(vo.getToSRSCodeFour()).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesThree[2]).append(" ")
									 .append(arrayOfTransformedCoordinatesFour[0]).append(" ")
									 .append(arrayOfTransformedCoordinatesFour[1]).append(" ")
									 .append(arrayOfTransformedCoordinatesFour[2]).append(" ");
					vo.setTransformationFour(transformationFour.toString());
					System.out.println("TransformationFour - " + vo.getTransformationFour());

					//Setting the transformation URL
					vo.setTransformationFourURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x="+arrayOfTransformedCoordinatesThree[0]+"&amp;y="+arrayOfTransformedCoordinatesThree[1]+"&amp;z="+arrayOfTransformedCoordinatesThree[2]+"&amp;output=html");

					//Get and Set xml response string for transformation info
					vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAVOXEL");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("WHS");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("abavoxel") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("WHS");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("ABAVOXEL");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

            //via ABAVOXEL
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("agea") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via ABAVOXEL
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("AGEA");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("ABAREFERENCE");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

	        //via ABAVOXEL, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") && vo.getToSRSCodeOne().equalsIgnoreCase("whs") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("ABAREFERENCE");
				vo.setToSRSCodeOne("ABAVOXEL");
				vo.setFromSRSCodeTwo("ABAVOXEL");
				vo.setToSRSCodeTwo("AGEA");
				vo.setFromSRSCodeThree("AGEA");
				vo.setToSRSCodeThree("WHS");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - agea to abavoxel
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));

			//via ABAVOXEL, and then AGEA
			} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") && vo.getToSRSCodeOne().equalsIgnoreCase("abareference") ) {

				//First convert from paxinos to whs 
				vo.setFromSRSCodeOne("WHS");
				vo.setToSRSCodeOne("AGEA");
				vo.setFromSRSCodeTwo("AGEA");
				vo.setToSRSCodeTwo("ABAVOXEL");
				vo.setFromSRSCodeThree("ABAVOXEL");
				vo.setToSRSCodeThree("ABAREFERENCE");

				//First convert - paxinos to whs
				rawTransformationStringOne = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ() );

				arrayOfTransformedCoordinatesOne = util.getTabDelimNumbers(rawTransformationStringOne);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesOne[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationOne.append(vo.getFromSRSCodeOne()).append(" ")
								 .append(vo.getToSRSCodeOne()).append(" ")
								 .append(vo.getOriginalCoordinateX()).append(" ")
								 .append(vo.getOriginalCoordinateY()).append(" ")
								 .append(vo.getOriginalCoordinateZ()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ");
				vo.setTransformationOne(transformationOne.toString());
				System.out.println("TransformationOne - " + vo.getTransformationOne());

				//Setting the transformation URL
				vo.setTransformationOneURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x="+vo.getOriginalCoordinateX()+"&amp;y="+vo.getOriginalCoordinateY()+"&amp;z="+vo.getOriginalCoordinateZ()+"&amp;output=html");

				//Second convert - whs to agea
				rawTransformationStringTwo = util.directSpaceTransformation(vo.getFromSRSCodeTwo(), vo.getToSRSCodeTwo(), arrayOfTransformedCoordinatesOne[0], arrayOfTransformedCoordinatesOne[1], arrayOfTransformedCoordinatesOne[2] );

				arrayOfTransformedCoordinatesTwo = util.getTabDelimNumbers(rawTransformationStringTwo);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesTwo[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationTwo.append(vo.getFromSRSCodeTwo()).append(" ")
								 .append(vo.getToSRSCodeTwo()).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesOne[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ");
				vo.setTransformationTwo(transformationTwo.toString());
				System.out.println("TransformationTwo - " + vo.getTransformationTwo());

				//Setting the transformation URL
				vo.setTransformationTwoURL("http://"+transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x="+arrayOfTransformedCoordinatesOne[0]+"&amp;y="+arrayOfTransformedCoordinatesOne[1]+"&amp;z="+arrayOfTransformedCoordinatesOne[2]+"&amp;output=html");
				
				//Third convert - agea to abavoxel
				rawTransformationStringThree = util.directSpaceTransformation(vo.getFromSRSCodeThree(), vo.getToSRSCodeThree(), arrayOfTransformedCoordinatesTwo[0], arrayOfTransformedCoordinatesTwo[1], arrayOfTransformedCoordinatesTwo[2] );

				arrayOfTransformedCoordinatesThree = util.getTabDelimNumbers(rawTransformationStringThree);

				//Exception Handling
				if ( arrayOfTransformedCoordinatesThree[0].trim().equalsIgnoreCase("out") ) { 
					vo.setErrorMessage("Out of Range");
					return vo;
				}

				// replace this with fromspace tospace x y z tx ty tz
				transformationThree.append(vo.getFromSRSCodeThree()).append(" ")
								 .append(vo.getToSRSCodeThree()).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesTwo[2]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[0]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[1]).append(" ")
								 .append(arrayOfTransformedCoordinatesThree[2]).append(" ");
				vo.setTransformationThree(transformationThree.toString());
				System.out.println("TransformationThree - " + vo.getTransformationThree());

				//Setting the transformation URL
				vo.setTransformationThreeURL("http://" + transformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x="+arrayOfTransformedCoordinatesTwo[0]+"&amp;y="+arrayOfTransformedCoordinatesTwo[1]+"&amp;z="+arrayOfTransformedCoordinatesTwo[2]+"&amp;output=html");

				//Get and Set xml response string for transformation info
				vo.setXmlStringForTransformationInfo(util.getSpaceTransformationInfoXMLResponseString( vo ));
				
			} else {
				vo.setErrorMessage("No such transformation is available at this point.");
				return vo;
			} 

			//String transformationXMLResponseString = spaceTransformationXMLResponse(request, response);
			//System.out.println(" XML Response String - " + transformationXMLResponseString);
	
			//Start - Write the xml response back to the user
/*			String originalCoordinatesString = request.getParameter("x")+", "+request.getParameter("y")+", "+request.getParameter("z");
			String transformedCoordinatesString = arrayOfTransformedCoordinatesOne[0]+", "+arrayOfTransformedCoordinatesOne[1]+", "+arrayOfTransformedCoordinatesOne[2];
			xmlResponseString = util.getSpaceTransformationXMLResponse( vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), originalCoordinatesString, transformedCoordinatesString );
			System.out.println(" XML Response String - " + xmlResponseString);
			vo.setTransformationXMLResponseString(xmlResponseString);
*/			//End

			System.out.println( "XML Response String - " + xmlResponseString ); 
			System.out.println("Ends running transformation  matrix...");

		} catch ( Exception e ) {

			e.printStackTrace();

		} finally {

		}

		System.out.println("End - spaceTransformationForm Method...");

		//4) Return response back to the cllient in a text/xml format
		return vo;

	}

	
	//http://incf-dev-mapserver.crbs.ucsd.edu/cgi-bin/structure_lookup.cgi?atlas=aba&x=264&y=160&z=228
	public String getStructureNameLookup( String atlasSpaceName, String originalCoordinateX, 
			String originalCoordinateY, String originalCoordinateZ ) {

	String transformedCoordinateString = "";

	try {
	
		if ( atlasSpaceName.trim().equalsIgnoreCase("abavoxel") ) {

			String transformationHostName = incfConfig.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = incfConfig.getValue("incf.transformationservice.port.number");
			String transformationServicePath = incfConfig.getValue("incf.transformationservice.abavoxelstructure.path");
	
			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "atlas="+atlasSpaceName+"&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL);
			System.out.println("X in transformation matrix method is - " + originalCoordinateX);
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

		} else if ( atlasSpaceName.trim().equalsIgnoreCase("whs") ) { 

			String transformationHostName = incfConfig.getValue("incf.transformationservice.host.name");
			String transformationPortNumber = incfConfig.getValue("incf.transformationservice.port.number");
			String transformationServicePath = incfConfig.getValue("incf.transformationservice.whsstructure.path");

			//Start - Create and run URL, and read the string from the webpage
			String transforMatrixURL = "http://" + transformationHostName + transformationPortNumber + transformationServicePath + "&x=" + originalCoordinateX + "&y=" + originalCoordinateY + "&z=" + originalCoordinateZ;
			System.out.println("Transformation matrix url is - " + transforMatrixURL);
			System.out.println("X in transformation matrix method is - " + originalCoordinateX);
			URL url = new URL(transforMatrixURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				transformedCoordinateString = transformedCoordinateString + inputLine;
			}
			System.out.println("TransformedCoordinateString - "+transformedCoordinateString);

		}
		
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return transformedCoordinateString;

	}


	public String[] getWHSVoxelCoordinates( String coordinateX, String coordinateY, 
				                            String coordinateZ ) {

	String[] transformedCoordinates = new String[3];

	WHSm2WHSv whsm2whsv = new WHSm2WHSv();

	try {

		//Call Alexander's transformation method
		String whsm2whsvString = whsm2whsv.getTransformation(Long.parseLong(coordinateX), 
				Long.parseLong(coordinateY), Long.parseLong(coordinateZ));

		System.out.println("whsm2whsvString - " + whsm2whsvString);

		if ( whsm2whsvString == null || whsm2whsvString.equals("")) { 
			transformedCoordinates[0] = "out";
		} else if ( !whsm2whsvString.equals("") || whsm2whsvString != null ) { 

			whsm2whsvString = whsm2whsv.getTransformation(Long.parseLong(coordinateX), 
					Long.parseLong(coordinateY), Long.parseLong(coordinateZ) );

			transformedCoordinates = get3SpaceDelimNumbers(whsm2whsvString);

		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return transformedCoordinates;

	}


	public String[] get3SpaceDelimNumbers(String spaceDelimString ) {

		StringTokenizer tokens = new StringTokenizer(spaceDelimString, " ");
		int tokensSize = tokens.countTokens();

		String[] transformedCoordinates = new String[tokensSize];

		try{ 

			String[] coordinateString = new String[tokensSize]; 
			System.out.println( " tokens - " +tokensSize);

			int i = 0;
			while ( tokens.hasMoreTokens() ) {
				coordinateString[i] = tokens.nextToken();
				i++;
			}

			transformedCoordinates[0] = coordinateString[0];
			System.out.println( " transformedCoordinates x - " + transformedCoordinates[0] );
			transformedCoordinates[1] = coordinateString[1];
			System.out.println( " transformedCoordinates y - " + transformedCoordinates[1] );
			transformedCoordinates[2] = coordinateString[2];
			System.out.println( " transformedCoordinates z - " + transformedCoordinates[2] );

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return transformedCoordinates;

	}

	public String getSpaceTransformationXMLResponse( 
		          String srcSRSCode, String destSRSCode, 
		          String srcCoordinateString, String destCoordinateString ) { 

		StringBuffer sb = new StringBuffer();

		try { 

			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
			  .append("<coordinateTransformationInfoResponse xmlns:gml=\"http://www.opengis.net/gml\"> ")

			  .append("<queryinfo>")
			  .append("<criteria>")
			  .append("<input name=\"POI\">")
			  .append("<gml:Point srsName=\"INCF:").append(srcSRSCode).append("\">")
			  .append("<gml:pos>").append(srcCoordinateString).append("</gml:pos>")
			  .append("</gml:Point>")
			  .append("</input>")
			  .append("</criteria>")
			  .append("</queryinfo>")
			  .append("<POI>")
			  .append("<gml:Point srsName=\"INCF:").append(destSRSCode).append("\">")
			  .append("<gml:pos>").append(destCoordinateString).append("</gml:pos>")
			  .append("</gml:Point>")
		      .append("</POI>")

		      .append("</coordinateTransformationInfoResponse>");

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return sb.toString();

	}


	public String errorSpaceTransformationXMLResponse( 
	          String srcSRSCode, String destSRSCode, 
	          String srcCoordinateString, String errorMessage ) { 

	StringBuffer sb = new StringBuffer();

	try { 

		System.out.println("Inside an error xml method for transformation");
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		  .append("<coordinateTransformationInfoResponse xmlns:gml=\"http://www.opengis.net/gml\"> ")

		  .append("<queryinfo>")
		  .append("<criteria>")
		  .append("<input name=\"POI\">")
		  .append("<gml:Point srsName=\"INCF:").append(srcSRSCode).append("\">")
		  .append("<gml:pos>").append(srcCoordinateString).append("</gml:pos>")
		  .append("</gml:Point>")
		  .append("</input>")
		  .append("</criteria>")
		  .append("</queryinfo>")
		  .append("<POI>")
		  .append("<gml:Point srsName=\"INCF:").append(destSRSCode).append("\">")
		  .append("<gml:error>").append(errorMessage).append("</gml:error>")
		  .append("</gml:Point>")
	      .append("</POI>")
	      .append("</coordinateTransformationInfoResponse>");

	} catch ( Exception e ) {
		e.printStackTrace();
	}

	return sb.toString();

	}


	public String get2DImageFromUCSDXMLResponse( ArrayList list, String srsCode, 
			String coordinateX, String coordinateY, String coordinateZ, String filterValue ) { 

		StringBuffer sb = new StringBuffer();
		String coordinateString = coordinateX+","+coordinateY+","+coordinateZ;
		String imagePlaneEquation = "";
		String imagePlanePlacement = "";

		try { 

			Iterator iterator = list.iterator();
			CommonServiceVO vo;

			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
			  .append("<coordinateTransformationInfoResponse xmlns:gml=\"http://www.opengis.net/gml\"> ");

			sb.append("<queryinfo>");
			sb.append("<method name=\"Get2DImageAtPOI\"></method>");
	        sb.append("<criteria>");
	        sb.append("<input name=\"POI\">");
	        sb.append("<gml:Point srsName=\"INCF:"+srsCode+"\">");//SRSCode
	        sb.append("<gml:pos>"+coordinateString+"</gml:pos>");//POI
	        sb.append("</gml:Point>");
	        sb.append("</input>");
	        //sb.append("<input name=widthAndHeight>N,M</input>");//Width and Height
	        sb.append("<input name=\"Filter\">maptype:"+filterValue+"</input>");//MapType
	        sb.append("</criteria>");
	        sb.append("</queryinfo>");

	        sb.append("<image2DCollection>");

			while ( iterator.hasNext() ) { 

				vo = (CommonServiceVO) iterator.next();
				imagePlaneEquation = vo.getMinX()+","+vo.getMaxX()+","+vo.getMinY()+","+vo.getMaxY();
				imagePlanePlacement = vo.getTfw1()+","+vo.getTfw2()+","+vo.getTfw3()+","+vo.getTfw4()+","+vo.getTfw5()+","+vo.getTfw6();
		        
		        sb.append("<image2D>");
		        sb.append("<imageSource name=\""+vo.getImageBaseName()+"\" srsName=\""+srsCode+"\" type=\"WMS-png\" relevance=\"+-3\">");//ImageBaseName and srsName
		        sb.append("</imageSource>");
		        sb.append("<imagePosition>"); // Width and Height
		        sb.append("<imagePlaneEquation srsName=\""+srsCode+"\">"+imagePlaneEquation+"</imagePlaneEquation>");//SRSName and the Bounding Box
		        sb.append("<imagePlanePlacement srsName=\""+srsCode+"\">"+imagePlanePlacement+"</imagePlanePlacement>"); //TFW 6 values
		        sb.append("<corner position=\"topleft\">");
		        sb.append("<gml:Point srsName=\"INCF:"+srsCode+"\">");//SRSCode
		        sb.append("<gml:pos>"+vo.getTopLeft()+"</gml:pos>");//POI
		        sb.append("</gml:Point>");
		        sb.append("</corner>");
		        sb.append("<corner position=\"bottomleft\">");
		        sb.append("<gml:Point srsName=\"INCF:"+srsCode+"\">");//SRSCode
		        sb.append("<gml:pos>"+vo.getBottomLeft()+"</gml:pos>");//POI
		        sb.append("</gml:Point>");
		        sb.append("</corner>");
		        sb.append("<corner position=\"topright\">");
		        sb.append("<gml:Point srsName=\"INCF:"+srsCode+"\">");//SRSCode
		        sb.append("<gml:pos>"+vo.getTopRight()+"</gml:pos>");//POI
		        sb.append("</gml:Point>");
		        sb.append("</corner>");
		        sb.append("<corner position=\"bottomright\">");
		        sb.append("<gml:Point srsName=\"INCF:"+srsCode+"\">");//SRSCode
		        sb.append("<gml:pos>"+vo.getBottomRight()+"</gml:pos>");//POI
		        sb.append("</gml:Point>");
		        sb.append("</corner>");
		        sb.append("</imagePosition>"); 
		        sb.append("</image2D>");
	
			}

			sb.append("</image2DCollection>");
			sb.append("</coordinateTransformationInfoResponse>");

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return sb.toString();

	}


	public String error2DImageFromUCSDXMLResponse( String message, String srsCode, 
			String coordinateX, String coordinateY, String coordinateZ, String filterValue ) { 

		StringBuffer sb = new StringBuffer();
		String coordinateString = coordinateX+","+coordinateY+","+coordinateZ;
		String imagePlaneEquation = "";
		String imagePlanePlacement = "";

		try { 

			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
			  .append("<coordinateTransformationInfoResponse xmlns:gml=\"http://www.opengis.net/gml\"> ");

			sb.append("<queryinfo>");
			sb.append("<method name=\"Get2DImageAtPOI\"></method>");
	        sb.append("<criteria>");
	        sb.append("<input name=\"POI\">");
	        sb.append("<gml:Point srsName=\"INCF:"+srsCode+"\">");//SRSCode
	        sb.append("<gml:pos>"+coordinateString+"</gml:pos>");//POI
	        sb.append("</gml:Point>");
	        sb.append("</input>");
	        //sb.append("<input name=widthAndHeight>N,M</input>");//Width and Height
	        sb.append("<input name=\"Filter\">maptype:"+filterValue+"</input>");//MapType
	        sb.append("</criteria>");
	        sb.append("</queryinfo>");

	        sb.append("<error>");
	        sb.append(message);
			sb.append("</error>");

			sb.append("</coordinateTransformationInfoResponse>");

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return sb.toString();

	}

	  /**
	  *
	  * @param host String Host name of the computer running ArcIMS
	  * @param imageService String Name of Image Service from which to get image
	  *
	  * @return SpatialAtlasClientDataModel   dataModel
	  */
	 public CommonServiceVO getSimpleImageEnvelope( String host, String serviceName, String imageServerName ){
		
		System.out.println( "Begin - getSimpleImageEnvelope");

		String returnString = new String("");
		CommonServiceVO dataModel = new CommonServiceVO();
		System.out.println( "imageServerName - " + imageServerName );
		
	    try {

	    if ( imageServerName.trim().equalsIgnoreCase("MAPSERVER")) {
		
	   	 	System.out.println( "Inside MAPSERVER" );

	   	    try {

	   	    	//FIXME - Needs to come from the config file.
	   	    	String port = ":9090";
	   	    	String webDir = "crbsatlas/mapfiles";
	   	    	
	   	    	//String imageURLString = "http://" + host + ":" + port + "/cgi-bin/mapserv.exe?map="+webDir+"\\" + serviceName + ".map";
	   	    	String imageURLString = "http://" + host + port + "/cgi-bin/mapserv.exe?map="+webDir+"/" + serviceName + ".map";

	   	    	System.out.println("WMS URL String - " + imageURLString );

				URL url = new URL( imageURLString );
				
				WebMapServer wms = new WebMapServer(url);
				
				WMSCapabilities capabilities = wms.getCapabilities(); 
				Layer layer = capabilities.getLayer();
				dataModel.setMinX(String.valueOf( layer.getLatLonBoundingBox().getMinX() ) );
				dataModel.setMaxX(String.valueOf( layer.getLatLonBoundingBox().getMaxX() ) );
				dataModel.setMinY(String.valueOf( layer.getLatLonBoundingBox().getMinY() ) );
				dataModel.setMaxY(String.valueOf( layer.getLatLonBoundingBox().getMaxY() ) );
				
				dataModel.setBottomLeft(dataModel.getMinX() + ", " + dataModel.getMinY());
				dataModel.setTopRight(dataModel.getMaxX() + ", " + dataModel.getMaxY());
				dataModel.setBottomRight(dataModel.getMaxX() + ", " + dataModel.getMinY());
				dataModel.setTopLeft(dataModel.getMinX() + ", " + dataModel.getMaxY());
				
			    System.out.println("minX is - " + dataModel.getMinX());
			    System.out.println("maxX is - " + dataModel.getMaxX());
			    System.out.println("minY is - " + dataModel.getMinY());
			    System.out.println("maxY is - " + dataModel.getMaxY());

				} catch (ServiceException e) { 

					e.printStackTrace(); 

				} catch (Exception e) { 

					e.printStackTrace();

				}

		}

	    } catch(Exception e){
	     e.printStackTrace();
	   }

	   System.out.println( "End - getSimpleImageEnvelope");

	   return dataModel;

	 }

	public String getRoundCoordinateValue( String originalCoordinate ) {

		System.out.println( "Before - getRoundCoordinateValue"+ originalCoordinate);
		String roundedCoordinate = String.valueOf((Math.round( Double.parseDouble(originalCoordinate))));
		System.out.println( "After - getRoundCoordinateValue" + roundedCoordinate);

		return roundedCoordinate;

	}

	
	public String getSpaceTransformationInfoXMLResponseString( CommonServiceVO vo ) { 

		StringBuffer sb = new StringBuffer();

		try { 
			  
	 		  sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
			    .append("<coordinateTransformationInfoResponse xmlns:gml=\"http://www.opengis.net/gml\">\n ")

			    .append("<queryinfo>\n")
				//<queryURL name= GetTransformationInfo>URL</queryURL>
				.append("<criteria>\n");

				sb.append("<input name=\"fromSRSCode\">").append(vo.getFromSRSCode()).append("</input>\n");//fromSpaceName
				sb.append("<input name=\"toSRSCode\">").append(vo.getToSRSCode()).append("</input>\n");//vo.getToSRSCodeOne()

				sb.append("</criteria>\n")
				.append("</queryinfo>\n")
				.append("<coordinateTransformationInfo>\n");

	 		  	//Exception handling somewhere here before going to the first transformation

	 		    String orderNumber = "";
	 		    String code = "";
	 		    String implementingHub1 = "";
	 		    String implementingHub2 = "";
	 		    String implementingHub3 = "";
	 		    String implementingHub4 = "";
	 		    String transformationURL1 = "";
	 		    String transformationURL2 = "";
	 		    String transformationURL3 = "";
	 		    String transformationURL4 = "";
	 		    	
	 			String ucsdHostName = incfConfig.getValue("ucsd.host.name");
	 			String ucsdServicePath = incfConfig.getValue("ucsd.ucsd.service.path");
	 			String ucsdPortNumber = incfConfig.getValue("ucsd.port.number");
	 			String ucsdTransformationMatrixURLPrefix = ucsdHostName + ucsdPortNumber + ucsdServicePath;

	 			String abaHostName = incfConfig.getValue("ucsd.host.name");
	 			String abaServicePath = incfConfig.getValue("ucsd.aba.service.path");
	 			String abaPortNumber = incfConfig.getValue("ucsd.port.number");
	 			String abaTransformationMatrixURLPrefix = abaHostName + abaPortNumber + abaServicePath;


	 		    //order Number, fromSRSCode2toSRSCode, condition about UCSD or ABA, fromSRSCode, toSRSCode, transformationURL
	 		    if ( vo.getFromSRSCodeOne() != null ) {
		 		    if ( vo.getFromSRSCodeOne().equalsIgnoreCase("paxinos") ) {
		 		  		implementingHub1 = "UCSD";
		 		  		transformationURL1 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub1).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeOne()).append("\" toSRSCode=\"").append(vo.getToSRSCodeOne()).append("\">").append(transformationURL1).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("whs") ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub1).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeOne()).append("\" toSRSCode=\"").append(vo.getToSRSCodeOne()).append("\">").append(transformationURL1).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("agea") ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub1).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeOne()).append("\" toSRSCode=\"").append(vo.getToSRSCodeOne()).append("\">").append(transformationURL1).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abareference") ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub1).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeOne()).append("\" toSRSCode=\"").append(vo.getToSRSCodeOne()).append("\">").append(transformationURL1).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeOne().equalsIgnoreCase("abavoxel") ) {
		 		  		implementingHub1 = "ABA";
		 		  		transformationURL1 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeOne() + "&amp;toSRSCode=" + vo.getToSRSCodeOne() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeOne() + "2" + vo.getToSRSCodeOne(); 
		 		    	orderNumber = "1";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub1).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeOne()).append("\" toSRSCode=\"").append(vo.getToSRSCodeOne()).append("\">").append(transformationURL1).append("</coordinateTransformation>\n");
		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeTwo() != null ) {
		 		    if ( vo.getFromSRSCodeTwo().equalsIgnoreCase("paxinos") ) {
		 		  		implementingHub2 = "UCSD";
		 		  		transformationURL2 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(transformationURL2).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase("whs") ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(transformationURL2).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase("agea") ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(transformationURL2).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase("abareference") ) {
		 		  		implementingHub2 = "ABA";
		 		  		transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(transformationURL2).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeTwo().equalsIgnoreCase("abavoxel") ) {
		 		  		transformationURL2 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeTwo() + "&amp;toSRSCode=" + vo.getToSRSCodeTwo() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
		 		    	orderNumber = "2";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(transformationURL2).append("</coordinateTransformation>\n");
		 		  		implementingHub2 = "ABA";
		 		  	}
	 		    }

	 		    if ( vo.getFromSRSCodeThree() != null ) {
		 		    if ( vo.getFromSRSCodeThree().equalsIgnoreCase("paxinos") ) {
		 		  		implementingHub3 = "UCSD";
		 		  		transformationURL3 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(transformationURL3).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("whs") ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(transformationURL3).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("agea") ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(transformationURL3).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("abareference") ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(transformationURL3).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("abavoxel") ) {
		 		  		implementingHub3 = "ABA";
		 		  		transformationURL3 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeThree() + "&amp;toSRSCode=" + vo.getToSRSCodeThree() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
		 		    	orderNumber = "3";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(transformationURL3).append("</coordinateTransformation>\n");
		 		  	}
	 		    }
	 		    
	 		    if ( vo.getFromSRSCodeFour() != null ) {
		 		    if ( vo.getFromSRSCodeFour().equalsIgnoreCase("paxinos") ) {
		 		  		implementingHub4 = "UCSD";
		 		  		transformationURL4 = "http://" + ucsdTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub4).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeFour()).append("\" toSRSCode=\"").append(vo.getToSRSCodeFour()).append("\">").append(transformationURL4).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("whs") ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub4).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeFour()).append("\" toSRSCode=\"").append(vo.getToSRSCodeFour()).append("\">").append(transformationURL4).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("agea") ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub4).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeFour()).append("\" toSRSCode=\"").append(vo.getToSRSCodeFour()).append("\">").append(transformationURL4).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("abareference") ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub4).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeFour()).append("\" toSRSCode=\"").append(vo.getToSRSCodeFour()).append("\">").append(transformationURL4).append("</coordinateTransformation>\n");
		 		  	} else if ( vo.getFromSRSCodeThree().equalsIgnoreCase("abavoxel") ) {
		 		  		implementingHub4 = "ABA";
		 		  		transformationURL4 = "http://" + abaTransformationMatrixURLPrefix + "request=SpaceTransformation&amp;fromSRSCode=" + vo.getFromSRSCodeFour() + "&amp;toSRSCode=" + vo.getToSRSCodeFour() + "&amp;x=&amp;y=&amp;z=&amp;output=xml";
		 		  		code = vo.getFromSRSCodeFour() + "2" + vo.getToSRSCodeFour(); 
		 		    	orderNumber = "4";
		 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub4).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeFour()).append("\" toSRSCode=\"").append(vo.getToSRSCodeFour()).append("\">").append(transformationURL4).append("</coordinateTransformation>\n");
		 		  	}
	 		    }
	 		    
/*	 		    if ( vo.getTransformationOne() != null ) {
				} 
	 		    
	 		    if ( vo.getTransformationTwo() != null ) { 
	 		    	String transformationTwo = vo.getTransformationTwo();
	 		    	StringTokenizer tokensTwo = new StringTokenizer(transformationTwo, " ");
	 		    	String[] transformationTwoArray = new String[tokensTwo.countTokens()];
	 		    	int i = 0;
	 		    	while ( tokensTwo.hasMoreTokens() ) {
	 		    		transformationTwoArray[i] = tokensTwo.nextToken();
	 		    	}
	 		    	code = vo.getFromSRSCodeTwo() + "2" + vo.getToSRSCodeTwo(); 
					orderNumber = "2";
	 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub2).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeTwo()).append("\" toSRSCode=\"").append(vo.getToSRSCodeTwo()).append("\">").append(vo.getTransformationTwoURL()).append("</coordinateTransformation>\n");
				} 
	 		    
	 		    if ( vo.getTransformationThree() != null ) { 
	 		    	String transformationThree = vo.getTransformationThree();
	 		    	StringTokenizer tokensThree = new StringTokenizer(transformationThree, " ");
	 		    	String[] transformationThreeArray = new String[tokensThree.countTokens()];
	 		    	int i = 0;
	 		    	while ( tokensThree.hasMoreTokens() ) {
	 		    		transformationThreeArray[i] = tokensThree.nextToken();
	 		    	}
	 		    	code = vo.getFromSRSCodeThree() + "2" + vo.getToSRSCodeThree(); 
					orderNumber = "3";
	 		    	sb.append("<coordinateTransformation order=\"").append(orderNumber).append("\" code=\"").append(code).append("\" implementingHub=\"").append(implementingHub3).append("\" fromSRSCode=\"").append(vo.getFromSRSCodeThree()).append("\" toSRSCode=\"").append(vo.getToSRSCodeThree()).append("\">").append(vo.getTransformationThreeURL()).append("</coordinateTransformation>\n");
				}
*/
	 		    sb.append("</coordinateTransformationInfo>\n");
				sb.append("</coordinateTransformationInfoResponse>\n");

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return sb.toString();

		}


	public String errorSpaceTransformationInfoXMLResponse( CommonServiceVO vo ) { 

		StringBuffer sb = new StringBuffer();

		try { 
			  
	 		  sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
			    .append("<coordinateTransformationInfoResponse xmlns:gml=\"http://www.opengis.net/gml\">\n ")

			    .append("<queryinfo>\n")
				//<queryURL name= GetTransformationInfo>URL</queryURL>
				.append("<criteria>\n");

				sb.append("<input name=\"fromSRSCode\">").append(vo.getFromSRSCode()).append("</input>\n");//fromSpaceName
				sb.append("<input name=\"toSRSCode\">").append(vo.getToSRSCode()).append("</input>\n");//vo.getToSRSCodeOne()

				sb.append("</criteria>\n")
				.append("</queryinfo>\n")
				.append("<errormessage>\n")
				.append(vo.getErrorMessage());
	 		    sb.append("</errormessage>\n");
				sb.append("</coordinateTransformationInfoResponse>\n");

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return sb.toString();

		}

	
}
