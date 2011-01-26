//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/cs/CRSRegistry.java $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 Department of Geography, University of Bonn
 and
 lat/lon GmbH

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.cs;

import static java.lang.System.currentTimeMillis;

import java.util.List;

import javax.vecmath.Point2d;

import org.deegree.commons.utils.log.LoggingNotes;
import org.deegree.cs.components.Axis;
import org.deegree.cs.components.Ellipsoid;
import org.deegree.cs.components.GeodeticDatum;
import org.deegree.cs.components.Unit;
import org.deegree.cs.configuration.CRSConfiguration;
import org.deegree.cs.configuration.CRSProvider;
import org.deegree.cs.coordinatesystems.CoordinateSystem;
import org.deegree.cs.coordinatesystems.GeographicCRS;
import org.deegree.cs.coordinatesystems.ProjectedCRS;
import org.deegree.cs.exceptions.CRSConfigurationException;
import org.deegree.cs.exceptions.TransformationException;
import org.deegree.cs.exceptions.UnknownCRSException;
import org.deegree.cs.projections.cylindric.TransverseMercator;
import org.deegree.cs.transformations.Transformation;
import org.deegree.cs.transformations.TransformationFactory;
import org.deegree.cs.transformations.helmert.Helmert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * The <code>CRSRegistry</code> class wraps the access to the CRSProvider in the org.deegree.cs package by supplying a
 * static <code>create</code> method, thus encapsulating the access to the CoordinateSystems.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: lbuesching $
 * 
 * @version $Revision: 25628 $, $Date: 2010-08-02 01:16:18 -0700 (Mon, 02 Aug 2010) $
 * 
 */
@LoggingNotes(debug = "Get information about the retrieval of a crs from the registry.")
public class CRSRegistry {

    private static Logger LOG = LoggerFactory.getLogger( CRSRegistry.class );

    private synchronized static CRSProvider getProvider( String providerName ) {
        CRSConfiguration crsConfig = CRSConfiguration.getInstance( providerName );
        return crsConfig.getProvider();
    }

    /**
     * Creates a CRS from the given name using the given provider, if no CRS was found an UnkownCRSException will be
     * thrown.
     * 
     * @param providerName
     *            to be used for the creation of the crs.
     * @param name
     *            of the crs, e.g. EPSG:31466
     * @return a CoordinateSystem corresponding to the given name
     * @throws UnknownCRSException
     *             if the crs-name is not known
     */
    public synchronized static CoordinateSystem lookup( String providerName, String name )
                            throws UnknownCRSException {
        return lookup( providerName, name, false );
    }

    /**
     * Creates a CRS from the given name using the given provider, if no CRS was found an UnkownCRSException will be
     * thrown.
     * 
     * @param providerName
     *            to be used for the creation of the crs.
     * @param name
     *            of the crs, e.g. EPSG:31466
     * @param forceXY
     *            true if the axis order of the coordinate system should be x/y (EAST/NORTH; WEST/SOUTH); false id the
     *            defined axis order should be used
     * @throws UnknownCRSException
     * @throws UnknownCRSException
     *             if the crs-name is not known
     */
    public static CoordinateSystem lookup( String providerName, String name, boolean forceXY )
                            throws UnknownCRSException {
        long sT = currentTimeMillis();
        CRSProvider crsProvider = getProvider( providerName );
        long eT = currentTimeMillis() - sT;
        LOG.debug( "Getting provider: " + crsProvider + " took: " + eT + " ms." );
        CoordinateSystem realCRS = null;
        try {
            sT = currentTimeMillis();
            realCRS = crsProvider.getCRSByCode( CRSCodeType.valueOf( name ), forceXY );
            eT = currentTimeMillis() - sT;
            LOG.debug( "Getting crs ( " + name + " )from provider: " + crsProvider + " took: " + eT + " ms." );
        } catch ( CRSConfigurationException e ) {
            // throw new RuntimeException( "The crs configuration is broken, no way to recover from this." );
            throw new RuntimeException(
                                        "The crs configuration is broken, the crs: "
                                                                + name
                                                                + " could not be loaded, and there is no way to recover from this, original message: "
                                                                + e.getMessage(), e );
        }
        if ( realCRS == null ) {
            throw new UnknownCRSException( name );
        }
        LOG.debug( "Successfully created the crs with id: " + name );
        return realCRS;
    }

    /**
     * Get a 'real' coordinatesystem from the backend.
     * 
     * @param providerName
     * @param name
     * @return a real coordinate system not just a wrapper.
     * @throws UnknownCRSException
     */
    public synchronized static CoordinateSystem lookup( String providerName, CRSCodeType name )
                            throws UnknownCRSException {
        CRSProvider crsProvider = getProvider( providerName );
        CoordinateSystem realCRS = null;
        try {
            realCRS = crsProvider.getCRSByCode( name );
        } catch ( CRSConfigurationException e ) {
            LOG.error( e.getMessage(), e );
        }
        if ( realCRS == null ) {
            throw new UnknownCRSException( name.getOriginal() );
        }
        LOG.debug( "Successfully created the crs with id: " + name );
        return realCRS;
    }

    /**
     * Get a {@link Transformation} with given id, or <code>null</code> if it does not exist.
     * 
     * @param providerName
     *            to use.
     * @param id
     *            of the Transformation.
     * @return the identified transformation or <code>null<code> if no such transformation is found.
     */
    public synchronized static Transformation getTransformation( String providerName, String id ) {
        CRSProvider crsProvider = getProvider( providerName );
        CRSIdentifiable t = null;
        try {
            t = crsProvider.getIdentifiable( CRSCodeType.valueOf( id ) );
        } catch ( Throwable e ) {
            LOG.debug( "Could not retrieve a transformation for id: " + id );

        }
        if ( t != null && t instanceof Transformation ) {
            return (Transformation) t;
        }
        LOG.debug( "The given id: " + id + " is not of type transformation return null." );
        return null;
    }

    /**
     * Retrieve a {@link Transformation} (chain) which transforms coordinates from the given source into the given
     * target crs. If no such {@link Transformation} could be found or the implementation does not support inverse
     * lookup of transformations <code>null<code> will be returned.
     * 
     * @param providerName
     *            to use.
     * @param sourceCRS
     *            start of the transformation (chain)
     * @param targetCRS
     *            end point of the transformation (chain).
     * @return the given {@link Transformation} or <code>null<code> if no such transformation was found.
     * @throws TransformationException
     * @throws IllegalArgumentException
     */
    public synchronized static Transformation getTransformation( String providerName, CoordinateSystem sourceCRS,
                                                                 CoordinateSystem targetCRS )
                            throws IllegalArgumentException, TransformationException {
        return getTransformation( providerName, sourceCRS, targetCRS, null );
    }

    /**
     * Retrieve a {@link Transformation} (chain) which transforms coordinates from the given source into the given
     * target crs. If no such {@link Transformation} could be found or the implementation does not support inverse
     * lookup of transformations <code>null<code> will be returned.
     * 
     * @param providerName
     *            to use.
     * @param sourceCRS
     *            start of the transformation (chain)
     * @param targetCRS
     *            end point of the transformation (chain).
     * @param transformationsToBeUsed
     *            a list of transformations which must be used on the resulting transformation chain.
     * @return the given {@link Transformation} or <code>null<code> if no such transformation was found.
     * @throws TransformationException
     * @throws IllegalArgumentException
     */
    public synchronized static Transformation getTransformation( String providerName, CoordinateSystem sourceCRS,
                                                                 CoordinateSystem targetCRS,
                                                                 List<Transformation> transformationsToBeUsed )
                            throws IllegalArgumentException, TransformationException {
        CRSConfiguration crsConfiguration = CRSConfiguration.getInstance( providerName );

        TransformationFactory fac = crsConfiguration.getTransformationFactory();
        return fac.createFromCoordinateSystems( sourceCRS, targetCRS, transformationsToBeUsed );
    }

    /**
     * Creates a CRS from the given name, if no CRS was found an UnkownCRSException will be thrown.
     * 
     * @param name
     *            of the crs, e.g. EPSG:4326
     * @return a CoordinateSystem corresponding to the given name, using the configured provider.
     * @throws UnknownCRSException
     *             if the crs-name is not known
     */
    public synchronized static CoordinateSystem lookup( String name )
                            throws UnknownCRSException {
        return lookup( null, name );
    }

    /**
     * Creates a CRS from the given name, if no CRS was found an UnkownCRSException will be thrown.
     * 
     * @param name
     *            of the crs, e.g. EPSG:4326
     * @param forceXY
     *            true if the axis order of the coordinate system should be x/y (EAST/NORTH; WEST/SOUTH); false id the
     *            defined axis order should be used
     * @return a CoordinateSystem corresponding to the given name, using the configured provider.
     * @throws UnknownCRSException
     *             if the crs-name is not known
     */
    public synchronized static CoordinateSystem lookup( String name, boolean forceXY )
                            throws UnknownCRSException {
        return lookup( null, name, forceXY );
    }

    /**
     * Get a real coordinate system from the default provider.
     * 
     * @param name
     * @return a real coordinate system looked up in the default provider.
     * @throws UnknownCRSException
     */
    public synchronized static CoordinateSystem lookup( CRSCodeType name )
                            throws UnknownCRSException {
        return lookup( null, name );
    }

    /**
     * Wrapper for the private constructor of the org.deegree.cs class.
     * 
     * @param realCRS
     *            to wrap
     * 
     * @return a CRSDeliverable corresponding to the given crs.
     */
    public static CoordinateSystem lookup( CoordinateSystem realCRS ) {
        return realCRS;
    }

    /**
     * Wrapper for the private constructor to create a dummy projected crs with no projection parameters set, the
     * standard wgs84 datum and the given optional name as the identifier. X-Y axis are in metres.
     * 
     * @param name
     *            optional identifier, if missing, the word 'dummy' will be used.
     * 
     * @return a dummy CoordinateSystem having filled out all the essential values.
     */
    public static CoordinateSystem lookupDummyCRS( String name ) {
        if ( name == null || "".equals( name.trim() ) ) {
            name = "dummy";
        }
        /**
         * Standard axis of a geographic crs
         */
        final Axis[] axis_degree = new Axis[] { new Axis( Unit.DEGREE, "lon", Axis.AO_EAST ),
                                               new Axis( Unit.DEGREE, "lat", Axis.AO_NORTH ) };
        final Axis[] axis_projection = new Axis[] { new Axis( "x", Axis.AO_EAST ), new Axis( "y", Axis.AO_NORTH ) };

        final Helmert wgs_info = new Helmert( GeographicCRS.WGS84, GeographicCRS.WGS84, CRSCodeType.valueOf( name
                                                                                                             + "_wgs" ) );
        final GeodeticDatum datum = new GeodeticDatum( Ellipsoid.WGS84, wgs_info,
                                                       new CRSCodeType[] { CRSCodeType.valueOf( name + "_datum" ) } );
        final GeographicCRS geographicCRS = new GeographicCRS(
                                                               datum,
                                                               axis_degree,
                                                               new CRSCodeType[] { CRSCodeType.valueOf( name
                                                                                                        + "geographic_crs" ) } );
        final TransverseMercator projection = new TransverseMercator( true, geographicCRS, 0, 0, new Point2d( 0, 0 ),
                                                                      Unit.METRE, 1 );

        return new ProjectedCRS( projection, axis_projection,
                                 new CRSCodeType[] { CRSCodeType.valueOf( name + "projected_crs" ) } );

    }

}
