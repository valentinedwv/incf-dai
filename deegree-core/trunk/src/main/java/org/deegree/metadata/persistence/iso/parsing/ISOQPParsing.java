//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/metadata/persistence/iso/parsing/ISOQPParsing.java $
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
package org.deegree.metadata.persistence.iso.parsing;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.deegree.commons.tom.datetime.Date;
import org.deegree.commons.xml.NamespaceContext;
import org.deegree.commons.xml.XMLAdapter;
import org.deegree.commons.xml.XPath;
import org.deegree.cs.CRSCodeType;
import org.deegree.metadata.i18n.Messages;
import org.deegree.metadata.persistence.MetadataStoreException;
import org.deegree.metadata.persistence.types.Format;
import org.deegree.protocol.csw.CSWConstants;
import org.slf4j.Logger;

/**
 * Parsing regarding to ISO and DC application profile. Here the input XML document is parsed into its parts. So this is
 * the entry point to generate a record that fits with the backend. The queryable and returnable properties are
 * disentangled. This is needed to put them into the queryable property tables in the backend and makes them queryable.
 * In this context they are feasible to build the Dublin Core record which has nearly planar elements with no nested
 * areas.
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: sthomas $
 * 
 * @version $Revision: 28101 $, $Date: 2010-11-12 06:47:43 -0800 (Fri, 12 Nov 2010) $
 */
public final class ISOQPParsing extends XMLAdapter {

    private static final Logger LOG = getLogger( ISOQPParsing.class );

    private static NamespaceContext nsContextISOParsing = new NamespaceContext( XMLAdapter.nsContext );

    private QueryableProperties qp;

    private ReturnableProperties rp;

    static {
        nsContextISOParsing.addNamespace( CSWConstants.SRV_PREFIX, CSWConstants.SRV_NS );
    }

    public ISOQPParsing() {

    }

    /**
     * Parses the recordelement that should be inserted into the backend. Every elementknot is put into an OMElement and
     * its atomic representation:
     * <p>
     * e.g. the "fileIdentifier" is put into an OMElement identifier and its identification-String is put into the
     * {@link QueryableProperties}.
     * 
     * @param element
     *            the XML element that has to be parsed to be able to generate needed database properties
     * @return {@link ParsedProfileElement}
     * @throws IOException
     */
    public ParsedProfileElement parseAPISO( OMElement element )
                            throws MetadataStoreException {

        OMFactory factory = OMAbstractFactory.getOMFactory();

        setRootElement( element );
        if ( element.getDefaultNamespace() != null ) {
            nsContextISOParsing.addNamespace( rootElement.getDefaultNamespace().getPrefix(),
                                              rootElement.getDefaultNamespace().getNamespaceURI() );
        }

        qp = new QueryableProperties();

        rp = new ReturnableProperties();

        // for ( String error : ca.getMv().validate( rootElement ) ) {
        // throw new MetadataStoreException( "VALIDATION-ERROR: " + error );
        // }

        /*---------------------------------------------------------------
         * 
         * 
         * (default) Language
         * 
         * 
         *---------------------------------------------------------------*/
        String language = getNodeAsString(
                                           rootElement,
                                           new XPath(
                                                      "./gmd:language/gco:CharacterString | ./gmd:language/gmd:LanguageCode/@codeListValue",
                                                      nsContextISOParsing ), null );

        // Locale locale = new Locale(
        // getNodeAsString(
        // rootElement,
        // new XPath(
        // "./gmd:language/gco:CharacterString | ./gmd:language/gmd:LanguageCode/@codeListValue",
        // nsContextISOParsing ), null ) );

        qp.setLanguage( language );
        // LOG.debug( getElement( rootElement, new XPath( "./gmd:language", nsContextISOParsing ) ).toString() );

        /*---------------------------------------------------------------
         * 
         * 
         * CharacterSet
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * ParentIdentifier
         * 
         * 
         *---------------------------------------------------------------*/
        String parentId = getNodeAsString( rootElement, new XPath( "./gmd:parentIdentifier/gco:CharacterString",
                                                                   nsContextISOParsing ), null );
        qp.setParentIdentifier( parentId );

        /*---------------------------------------------------------------
         * 
         * 
         * Type
         * HierarchieLevel
         * 
         *---------------------------------------------------------------*/
        /**
         * if provided data is a dataset: type = dataset (default)
         * <p>
         * if provided data is a datasetCollection: type = series
         * <p>
         * if provided data is an application: type = application
         * <p>
         * if provided data is a service: type = service
         */
        String type = getNodeAsString( rootElement, new XPath( "./gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue",
                                                               nsContextISOParsing ), "dataset" );
        qp.setType( type );

        /*---------------------------------------------------------------
         * 
         * 
         * HierarchieLevelName
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * Contact
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * DateStamp
         * Modified
         * 
         * 
         *---------------------------------------------------------------*/
        String[] dateString = getNodesAsStrings( rootElement,
                                                 new XPath( "./gmd:dateStamp/gco:Date | ./gmd:dateStamp/gco:DateTime",
                                                            nsContextISOParsing ) );
//        OMElement es = getElement( rootElement, new XPath( "./gmd:fileIdentifier", nsContextISOParsing ) );
//
//        LOG.debug( "elem: " + es );
        Date[] date = new Date[dateString.length];
        try {
            int counter = 0;
            if ( dateString != null ) {
                for ( String dates : dateString ) {

                    date[counter++] = new Date( dates );

                }
            }
        } catch ( ParseException e ) {
            String msg = Messages.getMessage( "ERROR_PARSING", dateString[0], e.getMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );
        }

        qp.setModified( date );

        /*---------------------------------------------------------------
         * 
         * 
         * MetadataStandardName
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * MetadataStandardVersion
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * DataSetURI
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * Locale (for multilinguarity)
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * SpatialRepresentationInfo
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * ReferenceSystemInfo
         * 
         * 
         *---------------------------------------------------------------*/
        List<OMElement> crsElements = getElements(
                                                   rootElement,
                                                   new XPath(
                                                              "./gmd:referenceSystemInfo/gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier",
                                                              nsContextISOParsing ) );

        List<CRSCodeType> crsList = new LinkedList<CRSCodeType>();
        for ( OMElement crsElement : crsElements ) {
            String nilReasonCRS = getNodeAsString( crsElement, new XPath( "./gmd:code/@gco:nilReason",
                                                                          nsContextISOParsing ), null );

            String nilReasonAuth = getNodeAsString( crsElement, new XPath( "./gmd:codeSpace/@gco:nilReason",
                                                                           nsContextISOParsing ), null );

            // OMElement e = getElement( rootElement, new XPath( "./gmd:fileIdentifier", nsContextISOParsing ) );
            //
            // LOG.debug( "elem: " + e );

            if ( nilReasonCRS == null && nilReasonAuth == null ) {
                String crs = getNodeAsString( crsElement, new XPath( "./gmd:code/gco:CharacterString",
                                                                     nsContextISOParsing ), null );

                String crsAuthority = getNodeAsString( crsElement, new XPath( "./gmd:codeSpace/gco:CharacterString",
                                                                              nsContextISOParsing ), null );

                String crsVersion = getNodeAsString( crsElement, new XPath( "./gmd:version/gco:CharacterString",
                                                                            nsContextISOParsing ), null );

                if ( crsAuthority != null ) {
                    crsList.add( new CRSCodeType( crs, crsAuthority ) );
                } else if ( crs != null ) {

                    crsList.add( new CRSCodeType( crs ) );
                }
            }
        }

        qp.setCrs( crsList );

        /*---------------------------------------------------------------
         * 
         * 
         * MetadataExtensionInfo
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * IdentificationInfo
         * 
         * 
         *---------------------------------------------------------------*/
        List<OMElement> identificationInfo = getElements( rootElement, new XPath( "./gmd:identificationInfo",
                                                                                  nsContextISOParsing ) );

        ParseIdentificationInfo pI = new ParseIdentificationInfo( factory, nsContextISOParsing );
        pI.parseIdentificationInfo( identificationInfo, qp, rp );
        /*---------------------------------------------------------------
         * 
         * 
         * FileIdentifier
         * 
         * 
         *---------------------------------------------------------------*/
        String[] fileIdentifierString = getNodesAsStrings( rootElement,
                                                           new XPath( "./gmd:fileIdentifier/gco:CharacterString",
                                                                      nsContextISOParsing ) );

        qp.setIdentifier( fileIdentifierString );

        // TODO

        /*---------------------------------------------------------------
         * 
         * 
         * ContentInfo
         * 
         * 
         *---------------------------------------------------------------*/

        parseDistributionInfo();

        parseDataQualityInfo();

        /*---------------------------------------------------------------
         * 
         * 
         * PortrayalCatalogueInfo
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * MetadataConstraints
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * ApplicationSchemaInfo
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * MetadataMaintenance
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * Series
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * Describes
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * PropertyType
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * FeatureType
         * 
         * 
         *---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         * 
         * 
         * FeatureAttribute
         * 
         * 
         *---------------------------------------------------------------*/

        /*
         * sets the properties that are needed for building DC records
         */

        return new ParsedProfileElement( qp, rp, element );

    }

    /**
     * DistributionInfo
     */
    private void parseDistributionInfo() {

        List<OMElement> formats = new ArrayList<OMElement>();

        formats.addAll( getElements(
                                     rootElement,
                                     new XPath(
                                                "./gmd:distributionInfo/gmd:MD_Distribution/gmd:distributor/gmd:MD_Distributor/gmd:distributorFormat/gmd:MD_Format",
                                                nsContextISOParsing ) ) );

        formats.addAll( getElements(
                                     rootElement,
                                     new XPath(
                                                "./gmd:distributionInfo/gmd:MD_Distribution/gmd:distributionFormat/gmd:MD_Format",
                                                nsContextISOParsing ) ) );

        // String onlineResource = getNodeAsString(
        // rootElement,
        // new XPath(
        // "./gmd:distributionInfo/gmd:MD_Distribution/gmd:transferOptions/gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:linkage/gmd:URL",
        // nsContextISOParsing ), null );

        List<Format> listOfFormats = new ArrayList<Format>();
        for ( OMElement md_format : formats ) {

            String formatName = getNodeAsString( md_format, new XPath( "./gmd:name/gco:CharacterString",
                                                                       nsContextISOParsing ), null );

            String formatVersion = getNodeAsString( md_format, new XPath( "./gmd:version/gco:CharacterString",
                                                                          nsContextISOParsing ), null );

            Format formatClass = new Format( formatName, formatVersion );
            listOfFormats.add( formatClass );

        }

        qp.setFormat( listOfFormats );

    }

    /**
     * DataQualityInfo
     * 
     * @throws MetadataStoreException
     */
    private void parseDataQualityInfo()
                            throws MetadataStoreException {
        String source = getNodeAsString(
                                         rootElement,
                                         new XPath(
                                                    "./gmd:dataQualityInfo/gmd:DQ_DataQuality/gmd:lineage/gmd:LI_Lineage/gmd:statement/gco:CharacterString",
                                                    nsContextISOParsing ), null );
        if ( source != null ) {
            qp.setLineage( source );
            rp.setSource( source );
        }
        qp.setDegree( getNodeAsBoolean(
                                        rootElement,
                                        new XPath(
                                                   "./gmd:dataQualityInfo/gmd:DQ_DataQuality/gmd:report/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean",
                                                   nsContextISOParsing ), false ) );

        List<OMElement> titleElems = getElements(
                                                  rootElement,
                                                  new XPath(
                                                             "./gmd:dataQualityInfo/gmd:DQ_DataQuality/gmd:report/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title",
                                                             nsContextISOParsing ) );

        List<String> titleStringList = new ArrayList<String>();
        for ( OMElement titleElem : titleElems ) {
            String title = getNodeAsString( titleElem, new XPath( "./gco:CharacterString", nsContextISOParsing ), null );
            String[] titleList = getNodesAsStrings(
                                                    titleElem,
                                                    new XPath(
                                                               "./gmd:PT_FreeText/gmd:textGroup/gmd:LocalisedCharacterString",
                                                               nsContextISOParsing ) );
            if ( title != null ) {
                if ( !title.equals( "" ) ) {
                    titleStringList.add( title );
                }
            }
            if ( titleList.length > 0 ) {
                titleStringList.addAll( Arrays.asList( titleList ) );
            }
        }
        if ( !titleStringList.isEmpty() ) {
            qp.setSpecificationTitle( titleStringList );
        }
        String specDateType = getNodeAsString(
                                               rootElement,
                                               new XPath(
                                                          "./gmd:dataQualityInfo/gmd:DQ_DataQuality/gmd:report/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode/@codeListValue",
                                                          nsContextISOParsing ), null );
        if ( specDateType != null ) {
            qp.setSpecificationDateType( specDateType );
        }
        String specificationDateString = getNodeAsString(
                                                          rootElement,
                                                          new XPath(
                                                                     "./gmd:dataQualityInfo/gmd:DQ_DataQuality/gmd:report/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date",
                                                                     nsContextISOParsing ), null );
        Date dateSpecificationDate = null;

        try {
            if ( specificationDateString != null ) {
                dateSpecificationDate = new Date( specificationDateString );
            }
        } catch ( ParseException e ) {

            String msg = Messages.getMessage( "ERROR_PARSING", specificationDateString, e.getMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );

        }
        if ( dateSpecificationDate != null ) {
            qp.setSpecificationDate( dateSpecificationDate );
        }
    }

}
