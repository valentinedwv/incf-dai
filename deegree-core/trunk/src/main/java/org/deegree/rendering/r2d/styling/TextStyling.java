//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/rendering/r2d/styling/TextStyling.java $
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

package org.deegree.rendering.r2d.styling;

import static java.awt.Color.BLACK;
import static org.deegree.rendering.r2d.styling.components.UOM.Pixel;

import org.deegree.rendering.r2d.styling.components.Fill;
import org.deegree.rendering.r2d.styling.components.Font;
import org.deegree.rendering.r2d.styling.components.Halo;
import org.deegree.rendering.r2d.styling.components.LinePlacement;
import org.deegree.rendering.r2d.styling.components.UOM;

/**
 * <code>TextStyling</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 20813 $, $Date: 2009-11-16 05:56:30 -0800 (Mon, 16 Nov 2009) $
 */
public class TextStyling implements Copyable<TextStyling>, Styling {

    /**
     * The unit of measure for all values.
     */
    public UOM uom = Pixel;
    
    /**
     * Default is a font with default settings.
     */
    public Font font = new Font();

    /**
     * Default is a black fill.
     */
    public Fill fill;

    /**
     * Default is 0.
     */
    public double rotation;

    /**
     * Default is 0.
     */
    public double displacementX;

    /**
     * Default is 0.
     */
    public double displacementY;

    /**
     * Default is 0.5.
     */
    public double anchorPointX = .5;

    /**
     * Default is 0.5.
     */
    public double anchorPointY = .5;

    /**
     * Default is no line placement.
     */
    public LinePlacement linePlacement;

    /**
     * Default is no halo.
     */
    public Halo halo;

    /**
     *
     */
    public TextStyling() {
        fill = new Fill();
        fill.color = BLACK;
    }

    public TextStyling copy() {
        TextStyling copy = new TextStyling();
        copy.font = font.copy();
        copy.fill = fill == null ? null : fill.copy();
        copy.rotation = rotation;
        copy.displacementX = displacementX;
        copy.displacementY = displacementY;
        copy.anchorPointX = anchorPointX;
        copy.anchorPointY = anchorPointY;
        copy.linePlacement = linePlacement == null ? null : linePlacement.copy();
        copy.halo = halo == null ? null : halo.copy();
        copy.uom = uom;
        return copy;
    }

}
