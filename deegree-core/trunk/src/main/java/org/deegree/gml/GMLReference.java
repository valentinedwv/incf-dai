//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/gml/GMLReference.java $
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

package org.deegree.gml;

import org.deegree.gml.props.GMLStdProps;

/**
 * Represents a reference to a {@link GMLObject}, corresponds to a GML property with an <code>xlink:href</code>
 * attribute.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 27043 $, $Date: 2010-09-28 00:21:51 -0700 (Tue, 28 Sep 2010) $
 * 
 * @param <T>
 *            type of the referenced object
 */
public class GMLReference<T extends GMLObject> implements GMLObject {

    private final GMLReferenceResolver resolver;

    private final String uri;

    private final String baseURL;

    private T object;

    /**
     * Creates a new {@link GMLReference} instance.
     * 
     * @param resolver
     *            used for resolving the reference, must not be <code>null</code>
     * @param uri
     *            the object's uri, must not be <code>null</code>
     * @param baseURL
     *            base URL for resolving the uri, may be <code>null</code> (no resolving of relative URLs)
     */
    public GMLReference( GMLReferenceResolver resolver, String uri, String baseURL ) {
        this.resolver = resolver;
        this.uri = uri;
        // if ( isLocal() ) {
        // id = uri.substring( 1 );
        // }
        this.baseURL = baseURL;
    }

    /**
     * Returns the URI of the object.
     * 
     * @return the URI of the object, never <code>null</code>
     */
    public String getURI() {
        return uri;
    }

    /**
     * Returns whether the reference has been resolved.
     * 
     * @return true, if the reference has been resolved, false otherwise
     */
    public boolean isResolved() {
        return object != null;
    }

    // TODO can we get rid of this method?
    public boolean isLocal() {
        return uri.startsWith( "#" );
    }

    /**
     * Sets the referenced object.
     * 
     * @param object
     *            the referenced object, may be <code>null</code>
     */
    public void resolve( T object ) {
        this.object = object;
    }

    /**
     * Returns the referenced {@link GMLObject} instance (may trigger resolving and fetching it).
     * 
     * @return the referenced {@link GMLObject} instance
     * @throws ReferenceResolvingException
     *             if the reference cannot be resolved
     */
    @SuppressWarnings("unchecked")
    public T getReferencedObject()
                            throws ReferenceResolvingException {
        if ( object == null ) {
            object = (T) resolver.getObject( uri, baseURL );
            if ( object == null ) {
                String msg = "Unable to resolve reference to '" + uri + "'.";
                throw new ReferenceResolvingException( msg );
            }
        }
        return object;
    }

    @Override
    public String getId() {
        if ( object != null ) {
            return object.getId();
        }
        if ( isLocal() ) {
            return uri.substring( 1 );
        }
        return getReferencedObject().getId();
    }

    @Override
    public GMLStdProps getGMLProperties() {
        return getReferencedObject().getGMLProperties();
    }
}