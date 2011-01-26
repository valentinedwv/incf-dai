//$HeadURL$
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
package org.deegree.gml.feature;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.deegree.gml.GMLVersion.GML_31;
import static org.deegree.gml.GMLVersion.GML_32;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.Assert;

import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.deegree.CoreTstProperties;
import org.deegree.commons.tom.genericxml.GenericXMLElementContent;
import org.deegree.commons.tom.primitive.PrimitiveValue;
import org.deegree.commons.xml.XMLParsingException;
import org.deegree.commons.xml.stax.XMLStreamReaderWrapper;
import org.deegree.cs.exceptions.TransformationException;
import org.deegree.cs.exceptions.UnknownCRSException;
import org.deegree.feature.Feature;
import org.deegree.feature.FeatureCollection;
import org.deegree.feature.StreamFeatureCollection;
import org.deegree.feature.property.Property;
import org.deegree.feature.types.ApplicationSchema;
import org.deegree.gml.GMLInputFactory;
import org.deegree.gml.GMLStreamReader;
import org.deegree.gml.GMLVersion;
import org.deegree.gml.ReferenceResolvingException;
import org.deegree.gml.feature.schema.ApplicationSchemaXSDDecoder;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * Tests that check the correct reading of {@link Feature} / {@link FeatureCollection} objects from GML instance
 * documents.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class GMLFeatureReaderTest {

    private static final Logger LOG = getLogger( GMLFeatureReaderTest.class );

    private static final String BASE_DIR = "testdata/features/";

    @Test
    public void testParsingPhilosopherFeatureCollection()
                            throws XMLStreamException, FactoryConfigurationError, IOException, XMLParsingException,
                            UnknownCRSException, ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "Philosopher_FeatureCollection.xml" );
        XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader( docURL.toString(),
                                                                                         docURL.openStream() );
        xmlReader.next();
        GMLFeatureReader decoder = new GMLFeatureReader( GMLVersion.GML_31, null, null, 2 );
        XMLStreamReaderWrapper wrapper = new XMLStreamReaderWrapper( xmlReader, docURL.toString() );
        FeatureCollection fc = (FeatureCollection) decoder.parseFeature( wrapper, null );
        decoder.getDocumentIdContext().resolveLocalRefs();
        Assert.assertEquals( 7, fc.size() );
    }

    @Test
    public void testParsingPhilosopherFeatureCollectionStream()
                            throws XMLStreamException, FactoryConfigurationError, IOException, XMLParsingException,
                            UnknownCRSException, ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "Philosopher_FeatureCollection.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        StreamFeatureCollection fc = gmlReader.readStreamFeatureCollection();

        int i = 0;
        while ( fc.read() != null ) {
            i++;
        }
        Assert.assertEquals( 7, i );
    }

    @Test(expected = XMLParsingException.class)
    public void testParsingPhilosopherFeatureCollectionNoSchema()
                            throws XMLStreamException, FactoryConfigurationError, IOException,
                            ReferenceResolvingException, XMLParsingException, UnknownCRSException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "Philosopher_FeatureCollection_no_schema.xml" );
        XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader( docURL.toString(),
                                                                                         docURL.openStream() );
        xmlReader.next();
        GMLFeatureReader gmlAdapter = new GMLFeatureReader( GMLVersion.GML_31, null, null, 2 );
        XMLStreamReaderWrapper wrapper = new XMLStreamReaderWrapper( xmlReader, docURL.toString() );
        FeatureCollection fc = (FeatureCollection) gmlAdapter.parseFeature( wrapper, null );
        gmlAdapter.getDocumentIdContext().resolveLocalRefs();
        Assert.assertEquals( 7, fc.size() );
    }

    @Test
    public void testParsingCiteSF0()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "dataset-sf0.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();
        Assert.assertEquals( fc.getGMLProperties().getDescription().getString(),
                             "Test data for assessing compliance with the GMLSF profile at level SF-0." );
        Assert.assertEquals( fc.getGMLProperties().getNames().length, 1 );
        Assert.assertEquals( fc.getGMLProperties().getNames()[0].getCode(), "CITE/WFS-1.1" );
        Assert.assertEquals( fc.getGMLProperties().getNames()[0].getCodeSpace(), null );
        Assert.assertEquals( 16, fc.size() );
    }

    @Test
    public void testParsingCiteSF0Stream()
                            throws XMLStreamException, FactoryConfigurationError, IOException, XMLParsingException,
                            UnknownCRSException, ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "dataset-sf0.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        StreamFeatureCollection fc = gmlReader.readStreamFeatureCollection();
        int i = 0;
        while ( fc.read() != null ) {
            i++;
        }
        Assert.assertEquals( 16, i );
    }

    /**
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     * @throws IOException
     * @throws XMLParsingException
     * @throws UnknownCRSException
     * @throws TransformationException
     */
    @Test
    public void testParsingCiteSF1()
                            throws XMLStreamException, FactoryConfigurationError, IOException, XMLParsingException,
                            UnknownCRSException, TransformationException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "dataset-sf1.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        Assert.assertEquals( 3, fc.size() );
    }

    @Test
    public void testParsingCiteSF1Stream()
                            throws XMLStreamException, FactoryConfigurationError, IOException, XMLParsingException,
                            UnknownCRSException, ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "dataset-sf1.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        StreamFeatureCollection fc = gmlReader.readStreamFeatureCollection();
        int i = 0;
        while ( fc.read() != null ) {
            i++;
        }
        Assert.assertEquals( 3, i );
    }

    @Test
    public void testParsingCiteSF2()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "dataset-sf2.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();
        Assert.assertEquals( 29, fc.size() );
    }

    @Test
    public void testParsingCiteSF2Stream()
                            throws XMLStreamException, FactoryConfigurationError, IOException, XMLParsingException,
                            UnknownCRSException, ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "dataset-sf2.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        StreamFeatureCollection fc = gmlReader.readStreamFeatureCollection();
        int i = 0;
        while ( fc.read() != null ) {
            i++;
        }
        Assert.assertEquals( 29, i );
    }

    @Test
    public void testParsingCite100()
                            throws FactoryConfigurationError, Exception {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "dataset.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_2, docURL );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();
        Assert.assertEquals( 106, fc.size() );
    }

    @Test
    public void testParsingCite100Stream()
                            throws FactoryConfigurationError, Exception {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "dataset.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_2, docURL );
        StreamFeatureCollection fc = gmlReader.readStreamFeatureCollection();
        int i = 0;
        while ( fc.read() != null ) {
            i++;
        }
        Assert.assertEquals( 106, i );
    }

    // @Test
    public void testParsingCityGML()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        String schemaURL = "http://schemas.opengis.net/citygml/profiles/base/1.0/CityGML.xsd";
        ApplicationSchemaXSDDecoder adapter = new ApplicationSchemaXSDDecoder( GMLVersion.GML_31, null, schemaURL );
        ApplicationSchema schema = adapter.extractFeatureTypeSchema();

        URL docURL = new URL( "file:/home/schneider/Desktop/waldbruecke_v1.0.0.gml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        gmlReader.setApplicationSchema( schema );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();

        // work with the fc
        for ( Feature feature : fc ) {
            LOG.debug( "member fid: " + feature.getId() );
        }
        LOG.debug( "member features: " + fc.size() );
    }

    // @Test
    public void testParsingXPlan20()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, ReferenceResolvingException {

        // BP2070
        URL docURL = new URL(
                              "file:/home/schneider/workspace/lkee_xplanung2/resources/testdata/XPlanGML_2_0/BP2070.gml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();

        // BP2135
        docURL = new URL( "file:/home/schneider/workspace/lkee_xplanung2/resources/testdata/XPlanGML_2_0/BP2135.gml" );
        gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();

        // PlanA
        docURL = new URL( "file:/home/schneider/workspace/lkee_xplanung2/resources/testdata/XPlanGML_2_0/FPlan_2.0.gml" );
        gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();

        // LA22
        docURL = new URL( "file:/home/schneider/workspace/lkee_xplanung2/resources/testdata/XPlanGML_2_0/LA 22.gml" );
        gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();

        // LA67
        docURL = new URL( "file:/home/schneider/workspace/lkee_xplanung2/resources/testdata/XPlanGML_2_0/LA67_2_0.gml" );
        gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();
    }

    @Test
    public void testParsingCustomProps()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "CustomProperties.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_31, docURL );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();
        Feature feature = fc.iterator().next();

        Property custom1Prop = feature.getProperty( new QName( "http://www.deegree.org/app", "custom1" ) );
        assertTrue( custom1Prop.getValue() instanceof GenericXMLElementContent );
        GenericXMLElementContent custom1PropValue = (GenericXMLElementContent) custom1Prop.getValue();
        assertNotNull( custom1PropValue.getXSType() );
        assertTrue( custom1PropValue.getXSType() instanceof XSComplexTypeDefinition );
        Assert.assertEquals( 2, custom1PropValue.getAttributes().size() );
        PrimitiveValue mimeTypeAttr = custom1PropValue.getAttributes().get( new QName( "mimeType" ) );
        Assert.assertEquals( "img/gif", mimeTypeAttr.getAsText() );
        Assert.assertEquals( "string", mimeTypeAttr.getXSType().getName() );
        PrimitiveValue lengthAttr = custom1PropValue.getAttributes().get( new QName( "length" ) );
        Assert.assertEquals( "5657", lengthAttr.getAsText() );
        Assert.assertEquals( "positiveInteger", lengthAttr.getXSType().getName() );
        // assertNull (custom1PropValue.getChildren());

        // System.out.println( "type: " + custom1Prop.getType() );
        // System.out.println( "value: " + custom1Prop.getValue() );
        // Property custom2Prop = feature.getProperty( new QName( "http://www.deegree.org/app", "custom2" ) );
        // System.out.println( "type: " + custom2Prop.getType() );
        // System.out.println( "value: " + custom2Prop.getValue() );
        // Property custom3Prop = feature.getProperty( new QName( "http://www.deegree.org/app", "custom3" ) );
        // System.out.println( "type: " + custom3Prop.getType() );
        // System.out.println( "value: " + custom3Prop.getValue() );
    }

    @Test
    public void testINSPIREAddresses1()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        String schemaURL = CoreTstProperties.getProperty( "schema_inspire_addresses" );
        if ( schemaURL == null ) {
            return;
        }
        ApplicationSchemaXSDDecoder adapter = new ApplicationSchemaXSDDecoder( GML_32, null, schemaURL );
        ApplicationSchema schema = adapter.extractFeatureTypeSchema();

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "inspire_addresses1.gml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GML_32, docURL );
        gmlReader.setApplicationSchema( schema );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();

        Assert.assertEquals( 4, fc.size() );
    }

    @Test
    public void testINSPIREAddresses1Stream()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        String schemaURL = CoreTstProperties.getProperty( "schema_inspire_addresses" );
        if ( schemaURL == null ) {
            return;
        }
        ApplicationSchemaXSDDecoder adapter = new ApplicationSchemaXSDDecoder( GML_32, null, schemaURL );
        ApplicationSchema schema = adapter.extractFeatureTypeSchema();

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "inspire_addresses1.gml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GML_32, docURL );
        gmlReader.setApplicationSchema( schema );
        StreamFeatureCollection fc = gmlReader.readStreamFeatureCollection();
        int i = 0;
        while ( fc.read() != null ) {
            i++;
        }
        Assert.assertEquals( 4, i );
    }

    @Test
    public void testINSPIREAddresses2()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        String schemaURL = CoreTstProperties.getProperty( "schema_inspire_addresses" );
        if ( schemaURL == null ) {
            return;
        }
        ApplicationSchemaXSDDecoder adapter = new ApplicationSchemaXSDDecoder( GML_32, null, schemaURL );
        ApplicationSchema schema = adapter.extractFeatureTypeSchema();

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "inspire_addresses2.gml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GML_32, docURL );
        gmlReader.setApplicationSchema( schema );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();

        Assert.assertEquals( 4, fc.size() );
    }

    @Test
    public void testINSPIREAddresses2Stream()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        String schemaURL = CoreTstProperties.getProperty( "schema_inspire_addresses" );
        if ( schemaURL == null ) {
            return;
        }
        ApplicationSchemaXSDDecoder adapter = new ApplicationSchemaXSDDecoder( GML_32, null, schemaURL );
        ApplicationSchema schema = adapter.extractFeatureTypeSchema();

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "inspire_addresses2.gml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GML_32, docURL );
        gmlReader.setApplicationSchema( schema );
        StreamFeatureCollection fc = gmlReader.readStreamFeatureCollection();
        int i = 0;
        while ( fc.read() != null ) {
            i++;
        }
        Assert.assertEquals( 4, i );
    }

    @Test
    public void testGeoServerWFS100FC()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "GeoServer_FC_WFS100.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_2, docURL );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();
        Assert.assertEquals( 4, fc.size() );
    }

    @Test
    public void testGeoServerWFS100FCStream()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "GeoServer_FC_WFS100.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GMLVersion.GML_2, docURL );
        StreamFeatureCollection fc = gmlReader.readStreamFeatureCollection();
        int i = 0;
        while ( fc.read() != null ) {
            i++;
        }
        Assert.assertEquals( 4, i );
    }

    @Test
    public void testGeoServerWFS110FC()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "GeoServer_FC_WFS110.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GML_31, docURL );
        FeatureCollection fc = (FeatureCollection) gmlReader.readFeature();
        gmlReader.getIdContext().resolveLocalRefs();
        Assert.assertEquals( 4, fc.size() );
    }

    @Test
    public void testGeoServerWFS110FCStream()
                            throws XMLStreamException, FactoryConfigurationError, IOException, ClassCastException,
                            ClassNotFoundException, InstantiationException, IllegalAccessException,
                            XMLParsingException, UnknownCRSException, JAXBException, TransformationException,
                            ReferenceResolvingException {

        URL docURL = GMLFeatureReaderTest.class.getResource( BASE_DIR + "GeoServer_FC_WFS110.xml" );
        GMLStreamReader gmlReader = GMLInputFactory.createGMLStreamReader( GML_31, docURL );
        StreamFeatureCollection fc = gmlReader.readStreamFeatureCollection();
        int i = 0;
        while ( fc.read() != null ) {
            i++;
        }
        Assert.assertEquals( 4, i );
    }
}