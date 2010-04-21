package edu.ucsd.crbs.incf.components.transformation.abar2abav;

import edu.ucsd.crbs.incf.util.INCFUtil;

public class ABAr2ABAv {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ABAr2ABAv client = new ABAr2ABAv();
		String responseString = client.getTransformation( 0, 0, 0 );
		INCFUtil util = new INCFUtil();
		util.get3SpaceDelimNumbers(responseString);
		
	}

	
	
	public String getTransformation (double x, double y, double slide) {

		//#slide the bregma offset of the slide, (x,y) are the coordinates of the point on the slide's grid 

		String w = "";
		String responseString = "";

		try {

			w = "Voxel coordinates: (" + String.valueOf(Math.round(213.8-35.9*slide)) + "," + String.valueOf(Math.round(44+35.5*y)) + "," + String.valueOf(Math.round(226-33.5*y)) + ")";
			responseString = String.valueOf(x) + " " + String.valueOf(y) + " "+ String.valueOf(slide) + " " + String.valueOf(Math.round(213.8-35.9*slide)) + " " + String.valueOf(Math.round(44+35.5*y)) + " " + String.valueOf(Math.round(226-33.5*y));
			System.out.println("ResponseString - " + responseString );

		} catch (Exception e) {

			e.printStackTrace();

		}

		System.out.println("Transformed - " + w);

		return responseString;

	}
	
}
