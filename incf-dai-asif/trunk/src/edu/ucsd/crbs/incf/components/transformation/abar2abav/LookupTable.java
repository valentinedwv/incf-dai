package edu.ucsd.crbs.incf.components.transformation.abar2abav;

import java.util.Arrays;

/**
 * A lookup table for sagittal coordinates in the ABA Reference.
 * @author spl
 * @version 1.0
 */

public class LookupTable {

    private LookupTable() {} // Don't instantiate this!

    private static final int[] ID = {
	130858,
	130859,
	130860,
	130861,
	130862,
	130863,
	130864,
	130865,
	130866,
	130867,
	130868,
	130869,
	130870,
	130871,
	130872,
	130873,
	130874,
	130875,
	130876,
	130877,
	130878,
    };
    
    private static final double[] Z = {
	1,
	1.2,
	1.4,
	1.6,
	1.8,
	2,
	2.2,
	2.4,
	2.6,
	2.8,
	3,
	3.2,
	3.4,
	3.6,
	3.8,
	4,
	4.2,
	4.4,
	4.6,
	4.8,
	5,
    };

    public static void main ( String[] args ) {
    	
    	LookupTable test = new LookupTable();
        //ABAReference r = ABATransform.convertVoxelToReference( 263, 159, 227 );
        double[] r = ABATransform.convertVoxelToReference( 8520, 2849, 4635 );
    	//double[] r = ABATransform.convertVoxelToReference( 4632, 4905, 2895 );
        System.out.println( "X - " + r[0] );
        System.out.println( "Y - " + r[1] );
        System.out.println( "Z - " + r[2] );
        
        int[] ref =
            ABATransform.convertReferenceToVoxel( -2.9995260988300005, 2.0007026631500002, 3.600060299820999 );
        //ABATransform.convertReferenceToVoxel( 1, 4.3, 1.78 );
        System.out.println( "R[0] - " + ref[0] );
        System.out.println( "R[1] - " + ref[1] );
        System.out.println( "R[2] - " + ref[2] );

    	//test.imageIDToZ(130878);
    	
    }
    
    /**
     * Convert from a image ID to the Z offset.
     * @param id the image ID.
     * @return the corresponding Z level.
     * @throws ArrayIndexOutOfBoundsException
     */

    static public double imageIDToZ( int id )
	throws ArrayIndexOutOfBoundsException 

    {

	int index = Arrays.binarySearch( ID, id );

	if ( index < 0 )
	    throw new ArrayIndexOutOfBoundsException( id + " not found." );

	System.out.println("Z is - " + Z[index]);
	return Z[index];

    }

    private static final double EPSILON = 0.05;

    /**
     * Convert from Z offset to an image ID.
     * @param z the Z level.
     * @return the image ID
     * @throws ArrayIndexOutOfBoundsException
     */

    static public int zToImageID( double z )
	throws ArrayIndexOutOfBoundsException 

    {

	int offset = Arrays.binarySearch( Z, z );

	if ( offset < 0 ) {

	    int insertion = -( offset + 1 );
	    
	    if ( ( 0 < insertion ) && ( insertion < Z.length ) ) {

		if ( Math.abs( Z[insertion - 1] - z ) < EPSILON )
		    offset = insertion - 1;
		else if ( Math.abs( Z[insertion] - z ) < EPSILON )
		    offset = insertion;

	    } else if ( ( insertion == 0 ) && 
			( Math.abs( Z[0] - z ) < EPSILON ) )
		offset = 0;
	    else if ( ( insertion <= Z.length ) &&
		      ( Math.abs( Z[insertion - 1] - z ) < EPSILON ) )
		offset = insertion - 1;

	}
	 
	if ( offset < 0 )
	    throw new ArrayIndexOutOfBoundsException( z + " not found." );
	
	return ID[offset];

    }

    /**
     * Convert from an index to an image ID
     * @param index the index (1-based).
     * @return the image ID.
     * @throws ArrayIndexOutOfBoundsException 
     */

    static public int indexToImageID( int index )
	throws ArrayIndexOutOfBoundsException 

    {

	return ID[index - 1];

    }

    /**
     * Convert from an index to Z offset.
     * @param index the index (1-based).
     * @return the Z offset.
     * @throws ArrayIndexOutOfBoundsException 
     */

    static public double indexToZ( int index )
	throws ArrayIndexOutOfBoundsException 

    {

	return Z[index - 1];

    }

}