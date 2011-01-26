//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/rendering/r2d/styling/components/PerpendicularOffsetType.java $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

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
package org.deegree.rendering.r2d.styling.components;

import static org.deegree.rendering.r2d.styling.components.PerpendicularOffsetType.Substraction.None;
import static org.deegree.rendering.r2d.styling.components.PerpendicularOffsetType.Type.Standard;

import org.deegree.rendering.r2d.styling.Copyable;

/**
 * <code>PerpendicularOffsetType</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 21443 $, $Date: 2009-12-15 00:25:22 -0800 (Tue, 15 Dec 2009) $
 */
public class PerpendicularOffsetType implements Copyable<PerpendicularOffsetType> {

    /** Default is Standard. */
    public Type type = Standard;

    /** Default is None. */
    public Substraction substraction = None;

    /**
     * <code>Type</code>
     * 
     * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
     * @author last edited by: $Author: aschmitz $
     * 
     * @version $Revision: 21443 $, $Date: 2009-12-15 00:25:22 -0800 (Tue, 15 Dec 2009) $
     */
    public enum Type {
        /***/
        Standard, /***/
        Round, /***/
        Edged
    }

    /**
     * <code>Substraction</code>
     * 
     * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
     * @author last edited by: $Author: aschmitz $
     * 
     * @version $Revision: 21443 $, $Date: 2009-12-15 00:25:22 -0800 (Tue, 15 Dec 2009) $
     */
    public enum Substraction {
        /***/
        None, /***/
        NegativeOffset
    }

    public PerpendicularOffsetType copy() {
        PerpendicularOffsetType copy = new PerpendicularOffsetType();
        copy.type = type;
        copy.substraction = substraction;
        return copy;
    }

}
