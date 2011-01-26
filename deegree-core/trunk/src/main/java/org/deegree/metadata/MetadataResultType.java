//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/metadata/MetadataResultType.java $
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
package org.deegree.metadata;

/**
 * Responsible for the information holding in the header of any getRecords operation.
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: sthomas $
 * 
 * @version $Revision: 27201 $, $Date: 2010-10-07 05:44:46 -0700 (Thu, 07 Oct 2010) $
 */
public interface MetadataResultType {

    /**
     * How many records match the request. getNumberOfRecordsReturned instead is the number that can really be seen in
     * the response.
     * 
     * @return the number of records that match the request
     */
    int getNumberOfRecordsMatched();

    /**
     * How many records should be performed in the response, whereas getNumberOfRecordsMatched is the number that could
     * be requested.
     * 
     * @return the number of records that should be returned.
     */
    int getNumberOfRecordsReturned();

    /**
     * Indicates the next record that could be queried after the last performed record.
     * 
     * @return the next record if there are more, otherwise 0.
     */
    int getNextRecord();

    /**
     * TODO
     * 
     * @return
     */
    String getExpires();

}
