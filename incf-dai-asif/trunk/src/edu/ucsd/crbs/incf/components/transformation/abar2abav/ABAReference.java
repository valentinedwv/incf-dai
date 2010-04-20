package edu.ucsd.crbs.incf.components.transformation.abar2abav;

/**
 * A container object for ABA Reference coordinates.
 * @author spl
 * @version 1.0
 */

public class ABAReference

{

    private final double _u;
    private final double _v;
    private final int _image_id;

    /**
     * Constructs a new ABAReference object representing a reference
     * grid location and image ID.
     * @param u Bregma value.
     * @param v Y value.
     * @param image_id image ID.
     */

    public ABAReference( double u, double v, int image_id )

    {

        this._u = u;
        this._v = v;
        this._image_id = image_id;

    }

    ABAReference( double u, double v, double w )

    {

        this._u = u;
        this._v = v;
        this._image_id = LookupTable.zToImageID( w );

    }

    double[] getUVWI()

    {

	return new double[] {
	    this._u, this._v, LookupTable.imageIDToZ( this._image_id ), 1,
	};

    }

    /**
     * Gets the ABA Reference Grid Bregma value.
     * @return the ABA Reference Grid Bregma value.
     */

    public double getU()

    {

        return this._u;

    }

    /**
     * Gets the ABA Reference Grid Y value.
     * @return the ABA Reference Grid Y value.
     */

    public double getV()

    {

        return this._v;

    }

    /**
     * Gets the ABA Reference image ID value.
     * @return the Image ID.
     */

    public int getImageID()

    {

        return this._image_id;

    }

}
