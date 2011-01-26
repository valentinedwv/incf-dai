//$HeadURL$
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

package org.deegree.geometry.standard.primitive;

import java.util.List;

import org.deegree.commons.uom.Measure;
import org.deegree.commons.uom.Unit;
import org.deegree.cs.CRS;
import org.deegree.geometry.Envelope;
import org.deegree.geometry.Geometry;
import org.deegree.geometry.points.Points;
import org.deegree.geometry.precision.PrecisionModel;
import org.deegree.geometry.primitive.OrientableSurface;
import org.deegree.geometry.primitive.Point;
import org.deegree.geometry.primitive.Surface;
import org.deegree.geometry.primitive.patches.SurfacePatch;
import org.deegree.geometry.standard.AbstractDefaultGeometry;
import org.deegree.gml.props.GMLStdProps;

/**
 * Default implementation of {@link OrientableSurface}.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class DefaultOrientableSurface extends AbstractDefaultGeometry implements OrientableSurface {

    private String id;

    private CRS crs;

    private Surface baseSurface;

    private boolean isReversed;

    private GMLStdProps standardProps;

    /**
     * Creates a new <code>DefaultOrientableSurface</code> instance from the given parameters.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param baseSurface
     *            base surface
     * @param isReversed
     *            set to true, if the orientation of the base Surface shall be reversed
     */
    public DefaultOrientableSurface( String id, CRS crs, Surface baseSurface, boolean isReversed ) {
        super( id, crs, null );
        this.baseSurface = baseSurface;
        this.isReversed = isReversed;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public CRS getCoordinateSystem() {
        return crs;
    }

    @Override
    public SurfaceType getSurfaceType() {
        return SurfaceType.OrientableSurface;
    }

    @Override
    public Surface getBaseSurface() {
        return baseSurface;
    }

    @Override
    public boolean isReversed() {
        return isReversed;
    }

    // -----------------------------------------------------------------------
    // Surface methods that are just delegated to the wrapped base surface
    // -----------------------------------------------------------------------

    public boolean contains( Geometry geometry ) {
        return baseSurface.contains( geometry );
    }

    @Override
    public boolean crosses( Geometry geometry ) {
        return baseSurface.crosses( geometry );
    }

    public Geometry getDifference( Geometry geometry ) {
        return baseSurface.getDifference( geometry );
    }

    @Override
    public Measure getDistance( Geometry geometry, Unit requestedUnit ) {
        return baseSurface.getDistance( geometry, requestedUnit );
    }

    public boolean equals( Geometry geometry ) {
        return baseSurface.equals( geometry );
    }

    public Measure getArea( Unit requestedBaseUnit ) {
        return baseSurface.getArea( requestedBaseUnit );
    }

    public Geometry getBuffer( Measure distance ) {
        return baseSurface.getBuffer( distance );
    }

    public Point getCentroid() {
        return baseSurface.getCentroid();
    }

    public Geometry getConvexHull() {
        return baseSurface.getConvexHull();
    }

    @Override
    public int getCoordinateDimension() {
        return baseSurface.getCoordinateDimension();
    }

    public Envelope getEnvelope() {
        return baseSurface.getEnvelope();
    }

    public GeometryType getGeometryType() {
        return baseSurface.getGeometryType();
    }

    public List<? extends SurfacePatch> getPatches() {
        return baseSurface.getPatches();
    }

    public Measure getPerimeter( Unit requestedUnit ) {
        return baseSurface.getPerimeter( requestedUnit );
    }

    public PrecisionModel getPrecision() {
        return baseSurface.getPrecision();
    }

    public PrimitiveType getPrimitiveType() {
        return baseSurface.getPrimitiveType();
    }

    public Geometry getIntersection( Geometry geometry ) {
        return baseSurface.getIntersection( geometry );
    }

    public boolean intersects( Geometry geometry ) {
        return baseSurface.intersects( geometry );
    }

    @Override
    public boolean isDisjoint( Geometry geometry ) {
        return baseSurface.isDisjoint( geometry );
    }

    @Override
    public boolean overlaps( Geometry geometry ) {
        return baseSurface.overlaps( geometry );
    }

    @Override
    public boolean touches( Geometry geometry ) {
        return baseSurface.touches( geometry );
    }

    public boolean isBeyond( Geometry geometry, Measure distance ) {
        return baseSurface.isBeyond( geometry, distance );
    }

    public boolean isWithin( Geometry geometry ) {
        return baseSurface.isWithin( geometry );
    }

    public boolean isWithinDistance( Geometry geometry, Measure distance ) {
        return baseSurface.isWithinDistance( geometry, distance );
    }

    public Geometry getUnion( Geometry geometry ) {
        return baseSurface.getUnion( geometry );
    }

    @Override
    public Points getExteriorRingCoordinates() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Points> getInteriorRingsCoordinates() {
        throw new UnsupportedOperationException();
    }

    @Override
    public com.vividsolutions.jts.geom.Geometry getJTSGeometry() {
        // TODO Auto-generated method stub
        return null;
    }
}
