package edu.ucsd.crbs.incf.components.transformation.whsm2whsv;

import edu.ucsd.crbs.incf.util.INCFUtil;


public class WHSm2WHSv {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		INCFUtil util = new INCFUtil();
		util.getWHSVoxelCoordinates("100", "-197", "-141");

		//672 263 173
/*		WHSm2WHSv client = new WHSm2WHSv();
		client.getTransformation( 100, -197, -141 );
*/
	}


	public String getTransformation (long x, long y, long z) {

		//#(x,y,z) are the mesh coordinates
		String w = "";
		String responseString = "";

		//FIXME - exception handling code - range of numbers 
		try {
			
			long x1 = Math.round(-1.27*x-0.07*y+785);
			long y1 = Math.round(-0.05*x-1.25*y+22);
			long z1 = Math.round(-1.12*z-0.08*(x-292));

			//w = "X - " + String.valueOf(x1) + ", Y - " + String.valueOf(y1) + ", Z - " + String.valueOf(z1); 
			responseString = String.valueOf(x1) + " " + String.valueOf(y1) + " " + String.valueOf(z1);
			System.out.println( "Transformed coordinates are - " + responseString );
			
		} catch (Exception e) {

			e.printStackTrace();

		}

		System.out.println("Transformed - " + w);
		return responseString;

	}


}
