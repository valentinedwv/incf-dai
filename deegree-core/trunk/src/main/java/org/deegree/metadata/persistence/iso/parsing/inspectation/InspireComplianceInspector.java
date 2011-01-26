//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/metadata/persistence/iso/parsing/inspectation/InspireComplianceInspector.java $
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
package org.deegree.metadata.persistence.iso.parsing.inspectation;

import static org.slf4j.LoggerFactory.getLogger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.deegree.metadata.persistence.MetadataInspectorException;
import org.deegree.metadata.persistence.iso19115.jaxb.InspireInspector;
import org.slf4j.Logger;

/**
 * Inspects the INSPIRE compliance of the metadataset.
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: sthomas $
 * 
 * @version $Revision: 27795 $, $Date: 2010-11-04 08:34:56 -0700 (Thu, 04 Nov 2010) $
 */
public class InspireComplianceInspector implements RecordInspector {

    private static final Logger LOG = getLogger( InspireComplianceInspector.class );

    private final InspireInspector ric;

    private Connection conn;

    public InspireComplianceInspector( InspireInspector ric ) {
        this.ric = ric;
    }

    public InspireInspector getRic() {
        return ric;
    }

    @Override
    public OMElement inspect( OMElement record, Connection conn )
                            throws MetadataInspectorException {
        this.conn = conn;
        // TODO make it plugable.
        List<InspireCompliance> inspireList = new ArrayList<InspireCompliance>();
        inspireList.add( new ResourceIdentifier( ric ) );
        for ( InspireCompliance c : inspireList ) {
            record = c.inspect( record, conn );
        }

        return record;
    }
}
