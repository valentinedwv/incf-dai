//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/gml/GMLStreamWriter.java $
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
package org.deegree.gml;

import static org.deegree.commons.xml.CommonNamespaces.GML3_2_NS;
import static org.deegree.commons.xml.CommonNamespaces.GMLNS;
import static org.deegree.commons.xml.CommonNamespaces.OGCNS;
import static org.deegree.commons.xml.CommonNamespaces.XLNNS;
import static org.deegree.gml.GMLVersion.GML_32;

import java.util.HashSet;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.deegree.cs.CRS;
import org.deegree.cs.exceptions.TransformationException;
import org.deegree.cs.exceptions.UnknownCRSException;
import org.deegree.feature.Feature;
import org.deegree.filter.expression.PropertyName;
import org.deegree.geometry.Geometry;
import org.deegree.geometry.io.CoordinateFormatter;
import org.deegree.gml.dictionary.Definition;
import org.deegree.gml.dictionary.GMLDictionaryWriter;
import org.deegree.gml.feature.GMLFeatureWriter;
import org.deegree.gml.geometry.GML2GeometryWriter;
import org.deegree.gml.geometry.GML3GeometryWriter;
import org.deegree.gml.geometry.GMLGeometryWriter;
import org.deegree.gml.utils.AdditionalObjectHandler;
import org.deegree.protocol.wfs.getfeature.XLinkPropertyName;

/**
 * Stream-based writer for GML instance documents or GML fragments.
 * <p>
 * Instances of this class are not thread-safe.
 * </p>
 * 
 * @see GMLObject
 * @see GMLOutputFactory
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 27148 $, $Date: 2010-10-03 09:22:46 -0700 (Sun, 03 Oct 2010) $
 */
public class GMLStreamWriter {

    private final GMLVersion version;

    private XMLStreamWriter xmlStream;

    private String indentString;

    private int inlineXLinklevels;

    private String localXLinkTemplate;

    private CRS crs;

    private CoordinateFormatter formatter;

    private String schemaLocation;

    private GMLGeometryWriter geometryWriter;

    private GMLFeatureWriter featureWriter;

    private GMLDictionaryWriter dictionaryWriter;

    private PropertyName[] featureProps;

    private XLinkPropertyName[] xlinkProps;

    private int traverseXLinkExpiry;

    private Map<String, String> prefixToNs;

    private AdditionalObjectHandler additionalObjectHandler;

    /**
     * Creates a new {@link GMLStreamWriter} instance.
     * 
     * @param version
     *            GML version of the output, must not be <code>null</code>
     * @param xmlStream
     *            XML stream used to write the output, must not be <code>null</code>
     * @throws XMLStreamException
     */
    GMLStreamWriter( GMLVersion version, XMLStreamWriter xmlStream ) throws XMLStreamException {
        this.version = version;
        this.xmlStream = xmlStream;
        xmlStream.setPrefix( "ogc", OGCNS );
        xmlStream.setPrefix( "gml", version != GML_32 ? GMLNS : GML3_2_NS );
        xmlStream.setPrefix( "xlink", XLNNS );
    }

    /**
     * Controls the value of the <code>xsi:schemaLocation</code> attribute in the root element.
     * 
     * @param schemaLocation
     *            value to be exported in the <code>xsi:schemaLocation</code> attribute in the root element, or
     *            <code>null</code> (no <code>xsi:schemaLocation</code> attribute will be exported)
     */
    public void setSchemaLocation( String schemaLocation ) {
        this.schemaLocation = schemaLocation;
    }

    /**
     * Controls the indentation of the generated XML.
     * 
     * @param indentString
     *            string to be used for one level of indentation (must be some combination of whitespaces), can be
     *            <code>null</code> (turns off indentation)
     */
    public void setIndentation( String indentString ) {
        this.indentString = indentString;
    }

    /**
     * Controls the output CRS for written geometries.
     * 
     * @param crs
     *            crs to be used for the geometries, can be <code>null</code> (keeps the original CRS)
     */
    public void setOutputCRS( CRS crs ) {
        this.crs = crs;
    }

    /**
     * Controls the format (e.g. number of decimal places) for written coordinates.
     * 
     * @param formatter
     *            formatter to use, may be <code>null</code> (don't do any formatting)
     */
    public void setCoordinateFormatter( CoordinateFormatter formatter ) {
        this.formatter = formatter;
    }

    /**
     * Controls the namespace prefixes that are used whenever a qualified element or attribute is written (and no
     * namespace prefix has been bound on the stream).
     * 
     * @param prefixToNs
     *            keys: prefix, value: namespace, may be <code>null</code>
     */
    public void setNamespaceBindings( Map<String, String> prefixToNs ) {
        this.prefixToNs = prefixToNs;
    }

    /**
     * Controls the number of xlink levels that will be expanded inside property elements.
     * 
     * @param inlineXLinklevels
     *            number of xlink levels to be expanded, -1 expands to any depth
     */
    public void setXLinkDepth( int inlineXLinklevels ) {
        this.inlineXLinklevels = inlineXLinklevels;
    }

    /**
     * Controls the number number of seconds to wait when remote xlinks are expanded inside property elements.
     * 
     * @param traverseXLinkExpiry
     *            number of seconds to wait for the resolving of remote xlinks, -1 sets no timeout
     */
    public void setXLinkExpiry( int traverseXLinkExpiry ) {
        this.traverseXLinkExpiry = traverseXLinkExpiry;
    }

    /**
     * Controls the representation of local xlinks.
     * 
     * @param localXLinkTemplate
     *            template used to create references to local objects, e.g.
     *            <code>http://localhost:8080/d3_wfs_lab/services?SERVICE=WFS&REQUEST=GetGmlObject&VERSION=1.1.0&TRAVERSEXLINKDEPTH=1&GMLOBJECTID={}</code>
     *            , the substring <code>{}</code> is replaced by the object id
     */
    public void setLocalXLinkTemplate( String localXLinkTemplate ) {
        this.localXLinkTemplate = localXLinkTemplate;
    }

    /**
     * Sets the feature properties to be included for exported {@link Feature} instances.
     * 
     * @param featureProps
     *            feature properties to be included, or <code>null</code> (include all feature props)
     */
    public void setFeatureProperties( PropertyName[] featureProps ) {
        this.featureProps = featureProps;
    }

    /**
     * Sets a specific XLink-expansion behaviour for object properties (e.g. {@link Feature} or {@link Geometry}
     * properties).
     * 
     * @param xlinkProps
     *            XLink-behaviour information, or <code>null</code> (no property-specific xlink behaviour)
     */
    public void setXLinkFeatureProperties( XLinkPropertyName[] xlinkProps ) {
        this.xlinkProps = xlinkProps;
    }

    public void setAdditionalObjectHandler( AdditionalObjectHandler handler ) {
        this.additionalObjectHandler = handler;
    }

    /**
     * Returns whether the specified gml object has already been exported.
     * 
     * @param gmlId
     *            id of the object, must not be <code>null</code>
     * @return true, if the object has been exported, false otherwise
     */
    public boolean isObjectExported( String gmlId ) {
        // TODO do this properly
        if ( featureWriter != null ) {
            return featureWriter.isExported( gmlId );
        }
        return false;
    }

    /**
     * Writes a GML representation of the given {@link GMLObject} to the stream.
     * 
     * @param object
     *            object to be written, must not be <code>null</code>
     * @throws XMLStreamException
     * @throws UnknownCRSException
     * @throws TransformationException
     */
    public void write( GMLObject object )
                            throws XMLStreamException, UnknownCRSException, TransformationException {
        if ( object instanceof Feature ) {
            write( (Feature) object );
        } else if ( object instanceof Geometry ) {
            write( (Geometry) object );
        } else if ( object instanceof Definition ) {
            write( (Definition) object );
        } else {
            throw new XMLStreamException( "Unhandled GMLObject: " + object );
        }
    }

    /**
     * Writes a GML representation of the given {@link Feature} to the stream.
     * 
     * @param feature
     *            object to be written, must not be <code>null</code>
     * @throws XMLStreamException
     * @throws UnknownCRSException
     * @throws TransformationException
     */
    public void write( Feature feature )
                            throws XMLStreamException, UnknownCRSException, TransformationException {
        getFeatureWriter().export( feature );
    }

    /**
     * Writes a GML representation of the given {@link Geometry} to the stream.
     * 
     * @param geometry
     *            object to be written, must not be <code>null</code>
     * @throws XMLStreamException
     * @throws UnknownCRSException
     * @throws TransformationException
     */
    public void write( Geometry geometry )
                            throws XMLStreamException, UnknownCRSException, TransformationException {
        getGeometryWriter().export( geometry );
    }

    /**
     * Writes a GML representation of the given {@link Definition} to the stream.
     * 
     * @param definition
     *            object to be written, must not be <code>null</code>
     * @throws XMLStreamException
     */
    public void write( Definition definition )
                            throws XMLStreamException {
        getDictionaryWriter().write( definition );
    }

    /**
     * Closes the underlying XML stream.
     * 
     * @throws XMLStreamException
     */
    public void close()
                            throws XMLStreamException {
        xmlStream.close();
    }

    /**
     * Returns the underlying XML stream.
     * 
     * @return the underlying XML stream, never <code>null</code>
     */
    public XMLStreamWriter getXMLStream() {
        return xmlStream;
    }

    private GMLFeatureWriter getFeatureWriter() {
        if ( featureWriter == null ) {
            featureWriter = new GMLFeatureWriter( version, xmlStream, crs, formatter, localXLinkTemplate, featureProps,
                                                  inlineXLinklevels, traverseXLinkExpiry, xlinkProps, false, true,
                                                  prefixToNs, additionalObjectHandler );
        }
        return featureWriter;
    }

    private GMLGeometryWriter getGeometryWriter() {
        if ( geometryWriter == null ) {
            switch ( version ) {
            case GML_2: {
                // TODO
                geometryWriter = new GML2GeometryWriter( xmlStream, crs, formatter, new HashSet<String>() );
                break;
            }
            case GML_30:
            case GML_31:
            case GML_32: {
                // TODO
                geometryWriter = new GML3GeometryWriter( version, xmlStream, crs, formatter, false,
                                                         new HashSet<String>() );
                break;
            }
            }
        }
        return geometryWriter;
    }

    private GMLDictionaryWriter getDictionaryWriter() {
        if ( dictionaryWriter == null ) {
            dictionaryWriter = new GMLDictionaryWriter( version, xmlStream );
        }
        return dictionaryWriter;
    }
}