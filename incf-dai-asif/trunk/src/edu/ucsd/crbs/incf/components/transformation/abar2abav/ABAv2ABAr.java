package edu.ucsd.crbs.incf.components.transformation.abar2abav;

import edu.ucsd.crbs.incf.components.services.aba.ABAServiceController;
import edu.ucsd.crbs.incf.util.INCFUtil;

public class ABAv2ABAr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ABAv2ABAr client = new ABAv2ABAr();
		String responseString = client.getTransformation( 214, 44, 226 );
		INCFUtil util = new INCFUtil();
		util.get3SpaceDelimNumbers(responseString);
		
	}


	public String getTransformation (long x, long y, long z) { 

		//#(x,y,z) are the voxel coordinates
		String responseString = "";

		try {

			double z1 = (213.8-x)/35.9;

			long slide_num = Math.round( 54.45 - z1*10 );

			if ( slide_num < 1 ) {
				//responseString = "Error -- voxel out of range.  New z coordinate is " + String.valueOf(z1) + " mm; too high.";
				responseString = "Out of Range";
			} else if ( slide_num > 132 ) {
				//responseString = "Error -- voxel out of range.  New z coordinate is " + String.valueOf(z1) + " mm; too low.";
				responseString = "Out of Range";
			} else {
				//responseString = "Bregma: slide number " + String.valueOf(slide_num) + " (" + String.valueOf(z1) + "mm), grid position (" + String.valueOf((226-z)/33.5) + "mm, " + String.valueOf((y-44)/35.5) + "mm)";
				responseString = String.valueOf(x) + " " + String.valueOf(y) + " "+ String.valueOf(z) + " " + String.valueOf((226-z)/33.5) + " " + String.valueOf((y-44)/35.5)  + " " + String.valueOf(z1);
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		System.out.println("Transformed - " + responseString);
		return responseString;

	}

}
