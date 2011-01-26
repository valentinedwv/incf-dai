//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/metadata/persistence/iso/resulttypes/Results.java $
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
package org.deegree.metadata.persistence.iso.resulttypes;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.deegree.commons.xml.XMLAdapter;
import org.deegree.metadata.MetadataResultType;
import org.deegree.protocol.csw.CSWConstants.ReturnableElement;

/**
 * Impementation of the {@link MetadataResultType} which is a subclass of {@link Hits} and extends it by the records
 * that are matched from request.
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: sthomas $
 * 
 * @version $Revision: 27201 $, $Date: 2010-10-07 05:44:46 -0700 (Thu, 07 Oct 2010) $
 */
public class Results extends Hits {

    private final XMLStreamReader stream;

    /**
     * 
     * @param resultType
     * @param numberOfRecordsMatched
     * @param numberOfRecordsReturned
     * @param recordSchema
     * @param nextRecord
     * @param expires
     */
    public Results( int numberOfRecordsMatched, int numberOfRecordsReturned, int nextRecord, String expires,
                    XMLStreamReader stream ) {
        super( numberOfRecordsMatched, numberOfRecordsReturned, nextRecord, expires );
        this.stream = stream;
    }

    public void serialize( XMLStreamWriter writer, ReturnableElement returnType )
                            throws XMLStreamException {

        stream.nextTag();
        XMLAdapter.writeElement( writer, stream );

        stream.close();
    }

}
