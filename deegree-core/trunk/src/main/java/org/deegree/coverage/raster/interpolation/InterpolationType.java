//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/coverage/raster/interpolation/InterpolationType.java $
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
package org.deegree.coverage.raster.interpolation;

/**
 * Enum for all implemented interpolation types.
 * 
 * @author <a href="mailto:tonnhofer@lat-lon.de">Oliver Tonnhofer</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 19082 $, $Date: 2009-08-13 03:28:39 -0700 (Thu, 13 Aug 2009) $
 */
public enum InterpolationType {
    /** bilinear interpolation */
    BILINEAR,
    /** nearest neighbor interpolation */
    NEAREST_NEIGHBOR,
    /** No interpolation */
    NONE;

    /**
     * Get interpolation for the given string. This method is case insensitive, words can be separated with a
     * whitespace, minus or underscore.
     * 
     * @param interpolation
     * @return the interpolation
     */
    public static InterpolationType fromString( String interpolation ) {
        String key = interpolation.toUpperCase();
        key = key.replaceAll( "-", "_" );
        key = key.replaceAll( "\\s", "_" );
        return InterpolationType.valueOf( key );
    }
}
