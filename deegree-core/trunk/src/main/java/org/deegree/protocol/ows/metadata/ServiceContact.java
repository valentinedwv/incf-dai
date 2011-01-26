//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/protocol/ows/metadata/ServiceContact.java $
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

import org.deegree.commons.tom.ows.CodeType;

/**
 * The <code>ServiceContact</code> bean encapsulates the corresponding GetCapabilities response metadata element.
 * 
 * @author <a href="mailto:ionita@lat-lon.de">Andrei Ionita</a>
 * 
 * @author last edited by: $Author: aionita $
 * 
 * @version $Revision: 26254 $, $Date: 2010-08-30 07:36:33 -0700 (Mon, 30 Aug 2010) $
 * 
 */
public class ServiceContact {

    private String individualName;

    private String positionName;

    private ContactInfo contactInfo;

    private CodeType role;

    /**
     * @param individualName
     */
    public void setIndividualName( String individualName ) {
        this.individualName = individualName;
    }

    /**
     * @return individualName, may be <code>null</code>.
     */
    public String getIndividualName() {
        return individualName;
    }

    /**
     * @param positionName
     */
    public void setPositionName( String positionName ) {
        this.positionName = positionName;
    }

    /**
     * @return positionName, may be <code>null</code>.
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * @param contactInfo
     */
    public void setContactInfo( ContactInfo contactInfo ) {
        this.contactInfo = contactInfo;
    }

    /**
     * @return contactInfo, may be <code>null</code>.
     */
    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    /**
     * @param role
     */
    public void setRole( CodeType role ) {
        this.role = role;
    }

    /**
     * @return role, may be <code>null</code>.
     */
    public CodeType getRole() {
        return role;
    }

}
