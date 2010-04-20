package edu.ucsd.crbs.incf.components.services.aba;

import java.util.StringTokenizer;

import edu.ucsd.crbs.incf.common.INCFConfigurator;
import edu.ucsd.crbs.incf.components.services.emage.EmageServiceController;
import edu.ucsd.crbs.incf.components.services.emage.INCFClient;
import edu.ucsd.crbs.incf.util.INCFUtil;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{ 
		
/*		String a = "mapType:coronal";
		String b = a.replaceAll("mapType:", "");
		System.out.println("B = " + b);
		INCFConfigurator config = INCFConfigurator.INSTANCE;
		System.out.println( "Logger - " + config.getValue("atlas.linuxlogger.filepath") ); 

		String originalCoordinateX = String.valueOf((Math.round( Double.parseDouble("235667.564"))));
		System.out.println( "coordinate - " + originalCoordinateX ); 
*/
/*		EmageServiceController controller = new EmageServiceController();
		controller.convertStructureNameWSClient(controller.xmlQueryStringToGetEmageStructure("dg"));
*/
/*        INCFClient ic = new INCFClient();
        System.out.println("PONS:"+ic.getEmageStructureName("PONS"));
*/
		//System.out.println("medulla:"+ic.getEmageStructureName("medulla"));

		//System.out.println("Banana:"+ic.getEmageStructureName("Banana"));
		//From WHS to ABARef //655 136 308
			//WHS1: 136 655 308
			//WHS2: 147 649 293

		INCFUtil util = new INCFUtil();
		//Point 1
		//util.spaceTransformation("whs","abavoxel","136","655","308");
		//util.spaceTransformation("abavoxel","abareference","149","154","310");

		//Point 2
		//util.spaceTransformation("whs","abavoxel","147","649","293");
		//util.spaceTransformation("abavoxel","abareference","154","167","301");
		//util.spaceTransformation("paxinos","whs","1.0","4.3","1.78");

		//Start
		String transformationOne = "fromSpaceName toSpaceName x 2 z out of range";
		StringTokenizer tokens = new StringTokenizer(transformationOne, " ");
		String[] transformationOneData = new String[tokens.countTokens()];
		int i = 0;
		while( tokens.hasMoreTokens() ) {
			transformationOneData[i] = tokens.nextToken();
			i++;
		}

		System.out.println(" 1 - " + transformationOneData[0]);
		System.out.println(" 2 - " + transformationOneData[1]);
		System.out.println(" 3 - " + transformationOneData[2]);
		System.out.println(" 4 - " + transformationOneData[3]);
		System.out.println(" 5 - " + transformationOneData[4]);
		System.out.println(" 6 - " + transformationOneData[5]);
		System.out.println(" 7 - " + transformationOneData[6]);
		System.out.println(" 8 - " + transformationOneData[7]);
		//End
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
