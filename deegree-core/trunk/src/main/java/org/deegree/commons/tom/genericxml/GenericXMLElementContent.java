//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/commons/tom/genericxml/GenericXMLElementContent.java $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
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
package org.deegree.commons.tom.genericxml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xerces.xs.XSTypeDefinition;
import org.deegree.commons.tom.TypedObjectNode;
import org.deegree.commons.tom.primitive.PrimitiveValue;

/**
 * {@link TypedObjectNode} that represents the content of a generic XML element with associated XML schema type
 * information.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 26897 $, $Date: 2010-09-21 06:54:03 -0700 (Tue, 21 Sep 2010) $
 */
public class GenericXMLElementContent implements TypedObjectNode {

    protected Map<QName, PrimitiveValue> attrs;

    protected List<TypedObjectNode> children;

    protected XSTypeDefinition type;

    public GenericXMLElementContent( XSTypeDefinition type, Map<QName, PrimitiveValue> attrs,
                                     List<TypedObjectNode> children ) {
        this.type = type;
        this.attrs = attrs;
        this.children = children;
    }

    public Map<QName, PrimitiveValue> getAttributes() {
        return attrs;
    }

    public List<TypedObjectNode> getChildren() {
        return children;
    }

    public XSTypeDefinition getXSType() {
        return type;
    }

    public void setAttribute( QName name, PrimitiveValue value ) {
        if ( attrs == null ) {
            attrs = new LinkedHashMap<QName, PrimitiveValue>();
        }
        attrs.put( name, value );
    }

    public void addChild( TypedObjectNode node ) {
        if ( children == null ) {
            children = new ArrayList<TypedObjectNode>();
        }
        children.add( node );
    }

    @Override
    public String toString() {
        String s = "";
        if ( children != null ) {
            for ( TypedObjectNode child : children ) {
                s += child.toString();
            }
        }
        return s;
    }

    public void setChildren( List<TypedObjectNode> newChildren ) {
        this.children = newChildren;
    }
}