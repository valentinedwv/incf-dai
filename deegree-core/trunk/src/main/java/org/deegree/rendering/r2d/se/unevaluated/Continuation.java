//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/rendering/r2d/se/unevaluated/Continuation.java $
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

package org.deegree.rendering.r2d.se.unevaluated;

import org.deegree.feature.Feature;
import org.deegree.filter.XPathEvaluator;

/**
 * <code>Continuation</code> is not a real continuation...
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 26928 $, $Date: 2010-09-22 08:26:55 -0700 (Wed, 22 Sep 2010) $
 * @param <T>
 */
public abstract class Continuation<T> {

    private Continuation<T> next;

    /**
     *
     */
    public Continuation() {
        // enable next to be null
    }

    /**
     * @param next
     */
    public Continuation( Continuation<T> next ) {
        this.next = next;
    }

    /**
     * @param base
     * @param f
     */
    public abstract void updateStep( T base, Feature obj, XPathEvaluator<Feature> evaluator );

    /**
     * @param base
     * @param f
     */
    public void evaluate( T base, Feature obj, XPathEvaluator<Feature> evaluator ) {
        updateStep( base, obj, evaluator );
        if ( next != null ) {
            next.evaluate( base, obj, evaluator );
        }
    }

    /**
     * <code>Updater</code>
     * 
     * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
     * @author last edited by: $Author: mschneider $
     * 
     * @version $Revision: 26928 $, $Date: 2010-09-22 08:26:55 -0700 (Wed, 22 Sep 2010) $
     * @param <T>
     */
    public static interface Updater<T> {
        /**
         * @param obj
         * @param val
         */
        void update( T obj, String val );
    }

    /**
     * Updater for a string buffer.
     */
    public static final Updater<StringBuffer> SBUPDATER = new Updater<StringBuffer>() {
        public void update( StringBuffer obj, String val ) {
            obj.append( val );
        }
    };
}