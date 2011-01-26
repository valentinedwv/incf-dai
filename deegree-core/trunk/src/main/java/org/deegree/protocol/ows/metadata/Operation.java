//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/protocol/ows/metadata/Operation.java $
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
package org.deegree.protocol.ows.metadata;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.deegree.commons.utils.Pair;

/**
 * The <code>Operation</code> bean encapsulates the corresponding GetCapabilities response metadata element.
 * 
 * @author <a href="mailto:ionita@lat-lon.de">Andrei Ionita</a>
 * 
 * @author last edited by: $Author: aionita $
 * 
 * @version $Revision: 26254 $, $Date: 2010-08-30 07:36:33 -0700 (Mon, 30 Aug 2010) $
 * 
 */
public class Operation {

    private List<DCP> dcp;

    private List<Domain> parameter;

    private List<Domain> constraint;

    private List<Pair<URL, URL>> metadata;

    private String name;

    /**
     * @return dcp, never <code>null</code>.
     */
    public List<DCP> getDCP() {
        if ( dcp == null ) {
            dcp = new ArrayList<DCP>();
        }
        return dcp;
    }

    /**
     * @return parameter, never <code>null</code>.
     */
    public List<Domain> getParameter() {
        if ( parameter == null ) {
            parameter = new ArrayList<Domain>();
        }
        return parameter;
    }

    /**
     * @return constraint, never <code>null</code>
     */
    public List<Domain> getConstraint() {
        if ( constraint == null ) {
            constraint = new ArrayList<Domain>();
        }
        return constraint;
    }

    /**
     * @return metadata, never <code>null</code>
     */
    public List<Pair<URL, URL>> getMetadata() {
        if ( metadata == null ) {
            metadata = new ArrayList<Pair<URL, URL>>();
        }
        return metadata;
    }

    /**
     * @param name
     */
    public void setName( String name ) {
        this.name = name;
    }

}
