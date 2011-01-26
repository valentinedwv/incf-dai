//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/commons/xml/NamespaceContext.java $
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
package org.deegree.commons.xml;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of Jaxen's (http://jaxen.codehaus.org) <code>NamespaceContext</code> interface.
 * <p>
 * NOTE: This should be used everywhere inside deegree, don't use <code>org.jaxen.SimpleNamespaceContext</code> -- this
 * prevents unnecessary binding to Jaxen.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 24581 $, $Date: 2010-05-26 08:05:14 -0700 (Wed, 26 May 2010) $
 */
public class NamespaceContext implements org.jaxen.NamespaceContext {

    // keys: prefices (String), values: namespaces (String)
    private Map<String, String> namespaceMap = new HashMap<String, String>();

    private javax.xml.namespace.NamespaceContext javaNsc;

    /**
     * Creates a new instance of <code>NamespaceContext</code> with only the prefix 'xmlns:' being bound.
     */
    public NamespaceContext() {
        namespaceMap.put( CommonNamespaces.XMLNS_PREFIX, CommonNamespaces.XMLNS );
    }

    /**
     * Creates a new instance of <code>NamespaceContext</code> that contains all the bindings from the argument
     * <code>NamespaceContext</code>.
     * 
     * @param nsContext
     *            bindings to copy
     */
    public NamespaceContext( NamespaceContext nsContext ) {
        namespaceMap = new HashMap<String, String>( nsContext.namespaceMap );
    }

    /**
     * Creates a new instance of <code>NamespaceContext</code> that contains all the bindings from the argument
     * <code>NamespaceContext</code>.
     * 
     * @param nsContext
     *            bindings to copy
     */
    public NamespaceContext( javax.xml.namespace.NamespaceContext nsContext ) {
        this.javaNsc = nsContext;
    }

    /**
     * registers a new prefix with an assigned namespace URI
     * 
     * @param prefix
     * @param namespace
     * @return this: new XPath(..., new NamespaceContext().addNamespace(...)
     */
    public NamespaceContext addNamespace( String prefix, String namespace ) {
        namespaceMap.put( prefix, namespace );
        return this;
    }

    public String translateNamespacePrefixToUri( String prefix ) {
        if ( javaNsc != null ) {
            return javaNsc.getNamespaceURI( prefix );
        }
        return namespaceMap.get( prefix );
    }

    /**
     * 
     * @param prefix
     * @return namespcae URI assigned to a prefix
     */
    public String getURI( String prefix ) {
        if ( prefix.equals( "" ) ) {
            return null;
        }
        return namespaceMap.get( prefix );
    }

    @Override
    public String toString() {
        return namespaceMap.toString();
    }
}
