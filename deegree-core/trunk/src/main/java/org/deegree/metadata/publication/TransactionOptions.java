//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/metadata/publication/TransactionOptions.java $
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
package org.deegree.metadata.publication;

/**
 * This class holds all the necessary attributes needed for the transaction operation.
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: sthomas $
 * 
 * @version $Revision: 26745 $, $Date: 2010-09-14 07:56:02 -0700 (Tue, 14 Sep 2010) $
 */
public class TransactionOptions {

    private boolean inspire;

    private boolean fileIdentifierAvailable;

    /**
     * Creates a new {@link TransactionOptions} instance.
     * 
     * @param inspire
     * @param fileIdentifierAvailable
     */
    public TransactionOptions( boolean inspire, boolean fileIdentifierAvailable ) {
        this.inspire = inspire;
        this.fileIdentifierAvailable = fileIdentifierAvailable;
    }

    /**
     * @return the inspire
     */
    public boolean isInspire() {
        return inspire;
    }

    /**
     * @return the fileIdentifierAvailable
     */
    public boolean isFileIdentifierAvailable() {
        return fileIdentifierAvailable;
    }

}
