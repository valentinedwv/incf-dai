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
package org.deegree.filter.spatial;

import org.deegree.commons.tom.TypedObjectNode;
import org.deegree.filter.FilterEvaluationException;
import org.deegree.filter.XPathEvaluator;
import org.deegree.filter.expression.PropertyName;
import org.deegree.geometry.Geometry;

/**
 * TODO add documentation here
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class Disjoint extends SpatialOperator {

    private final PropertyName propName;

    private final Geometry geometry;

    public Disjoint( PropertyName propName, Geometry geometry ) {
        this.propName = propName;
        this.geometry = geometry;
    }

    @Override
    public <T> boolean evaluate( T obj, XPathEvaluator<T> xpathEvaluator )
                            throws FilterEvaluationException {
        for ( TypedObjectNode paramValue : propName.evaluate( obj, xpathEvaluator ) ) {
            Geometry geom = checkGeometryOrNull( paramValue );
            if ( geom != null ) {
                Geometry transformedLiteral = getCompatibleGeometry( geom, geometry );
                return geom.isDisjoint( transformedLiteral );
            }
        }
        return false;
    }

    /**
     * @return the propName
     */
    public PropertyName getPropName() {
        return propName;
    }

    /**
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public String toString( String indent ) {
        String s = indent + "-Disjoint\n";
        s += indent + propName + "\n";
        s += indent + geometry;
        return s;
    }

    @Override
    public Object[] getParams() {
        return new Object[] { propName, geometry };
    }
}
