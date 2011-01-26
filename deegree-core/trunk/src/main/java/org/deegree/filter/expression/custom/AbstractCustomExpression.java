//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/filter/expression/custom/AbstractCustomExpression.java $
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
package org.deegree.filter.expression.custom;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.deegree.filter.Expression;

/**
 * Base class for implementing {@link CustomExpressionProvider}s.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 26904 $, $Date: 2010-09-21 07:49:07 -0700 (Tue, 21 Sep 2010) $
 */
public abstract class AbstractCustomExpression implements CustomExpressionProvider {

    @Override
    public Type getType() {
        return Type.CUSTOM;
    }

    @Override
    public CustomExpressionProvider parse100( XMLStreamReader xmlStream )
                            throws XMLStreamException {
        return parse( xmlStream );
    }

    @Override
    public CustomExpressionProvider parse110( XMLStreamReader xmlStream )
                            throws XMLStreamException {
        return parse( xmlStream );
    }

    @Override
    public CustomExpressionProvider parse200( XMLStreamReader xmlStream )
                            throws XMLStreamException {
        return parse( xmlStream );
    }

    /**
     * @param xmlStream
     * @return
     * @throws XMLStreamException
     */
    public abstract CustomExpressionProvider parse( XMLStreamReader xmlStream )
                            throws XMLStreamException;

    @Override
    public Expression[] getParams() {
        return new Expression[0];
    }

    @Override
    public String toString( String indent ) {
        return indent + "CustomExpression (" + getElementName() + ")";
    }
}