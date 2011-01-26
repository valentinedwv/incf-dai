//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/metadata/persistence/iso/ISOMetadataStore.java $
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
package org.deegree.metadata.persistence.iso;

import static org.deegree.commons.utils.JDBCUtils.close;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;
import org.deegree.commons.jdbc.ConnectionManager;
import org.deegree.commons.utils.JDBCUtils;
import org.deegree.commons.utils.time.DateUtils;
import org.deegree.filter.FilterEvaluationException;
import org.deegree.filter.OperatorFilter;
import org.deegree.filter.sql.postgis.PostGISWhereBuilder;
import org.deegree.metadata.ISORecord;
import org.deegree.metadata.MetadataResultType;
import org.deegree.metadata.i18n.Messages;
import org.deegree.metadata.persistence.MetadataCollection;
import org.deegree.metadata.persistence.MetadataInspectorException;
import org.deegree.metadata.persistence.MetadataQuery;
import org.deegree.metadata.persistence.MetadataResultSet;
import org.deegree.metadata.persistence.MetadataStore;
import org.deegree.metadata.persistence.MetadataStoreException;
import org.deegree.metadata.persistence.MetadataStoreTransaction;
import org.deegree.metadata.persistence.iso.parsing.IdUtils;
import org.deegree.metadata.persistence.iso.parsing.inspectation.CoupledDataInspector;
import org.deegree.metadata.persistence.iso.parsing.inspectation.FIInspector;
import org.deegree.metadata.persistence.iso.parsing.inspectation.InspireComplianceInspector;
import org.deegree.metadata.persistence.iso.parsing.inspectation.MetadataSchemaValidationInspector;
import org.deegree.metadata.persistence.iso.parsing.inspectation.RecordInspector;
import org.deegree.metadata.persistence.iso.resulttypes.Hits;
import org.deegree.metadata.persistence.iso19115.jaxb.CoupledResourceInspector;
import org.deegree.metadata.persistence.iso19115.jaxb.FileIdentifierInspector;
import org.deegree.metadata.persistence.iso19115.jaxb.ISOMetadataStoreConfig;
import org.deegree.metadata.persistence.iso19115.jaxb.InspireInspector;
import org.deegree.metadata.persistence.iso19115.jaxb.SchemaValidator;
import org.deegree.metadata.persistence.iso19115.jaxb.ISOMetadataStoreConfig.Inspectors;
import org.deegree.metadata.publication.InsertTransaction;
import org.deegree.protocol.csw.CSWConstants.ResultType;
import org.slf4j.Logger;

/**
 * {@link MetadataStore} implementation of Dublin Core and ISO Profile.
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: sthomas $
 * 
 * @version $Revision: 28229 $, $Date: 2010-11-15 08:03:29 -0800 (Mon, 15 Nov 2010) $
 */
public class ISOMetadataStore implements MetadataStore {

    private static final Logger LOG = getLogger( ISOMetadataStore.class );

    private final String connectionId;

    // if true, use old-style for spatial predicates (intersects instead of ST_Intersecs)
    private boolean useLegacyPredicates;

    /**
     * shows the encoding of the database that is used
     */
    private String encoding;

    private ISOMetadataStoreConfig config;

    // TODO remove...just inside for getById
    private static final String datasets = PostGISMappingsISODC.DatabaseTables.datasets.name();

    private static final String qp_identifier = PostGISMappingsISODC.DatabaseTables.qp_identifier.name();

    private static final String id = PostGISMappingsISODC.CommonColumnNames.id.name();

    private static final String backendIdentifier = PostGISMappingsISODC.CommonColumnNames.identifier.name();

    private static final String data = PostGISMappingsISODC.CommonColumnNames.data.name();

    private static final String recordfull = PostGISMappingsISODC.CommonColumnNames.recordfull.name();

    // private final Map<String, String> varToValue;

    /**
     * Creates a new {@link ISOMetadataStore} instance from the given JAXB configuration object.
     * 
     * @param config
     */
    public ISOMetadataStore( ISOMetadataStoreConfig config ) {
        this.connectionId = config.getJDBCConnId();
        this.config = config;
        // this.varToValue = new HashMap<String, String>();
        // String systemStartDate = "2010-11-16";
        // varToValue.put( "${SYSTEM_START_DATE}", systemStartDate );

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.record.persistence.RecordStore#destroy()
     */
    @Override
    public void destroy() {
        LOG.debug( "destroy" );

    }

    @Override
    public String getConnId() {
        return connectionId;
    }

    @Override
    public void setupMetametadata()
                            throws MetadataStoreException {

        try {
            InputStream in = ISOMetadataStore.class.getResourceAsStream( "metametadata.xml" );
            XMLStreamReader inStream = XMLInputFactory.newInstance().createXMLStreamReader( in );
            List<OMElement> om = new ArrayList<OMElement>();
            ISORecord rec = new ISORecord( inStream, null );
            om.add( rec.getAsOMElement() );
            MetadataStoreTransaction ta = acquireTransaction();
            InsertTransaction insert = new InsertTransaction( om, rec.getAsOMElement().getQName(), "insertMetametadata" );
            ta.performInsert( insert );
            ta.commit();
        } catch ( XMLStreamException e ) {
            LOG.debug( e.getMessage(), e );
            throw new MetadataStoreException( e.getMessage(), e );
        } catch ( FactoryConfigurationError e ) {
            LOG.debug( e.getMessage(), e );
            throw new MetadataStoreException( e.getMessage(), e );
        } catch ( MetadataStoreException e ) {
            LOG.debug( e.getMessage(), e );
            throw new MetadataStoreException( e.getMessage(), e );
        } catch ( MetadataInspectorException e ) {
            LOG.debug( e.getMessage(), e );
            throw new MetadataStoreException( e.getMessage(), e );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.record.persistence.RecordStore#init()
     */
    @Override
    public void init()
                            throws MetadataStoreException {

        LOG.debug( "init" );
        // lockManager = new DefaultLockManager( this, "LOCK_DB" );

        Connection conn = null;
        try {
            Class.forName( "org.postgresql.Driver" );
            conn = ConnectionManager.getConnection( connectionId );

            encoding = determinePostGRESEncoding( conn );

            String version = determinePostGISVersion( conn );
            if ( version.startsWith( "0." ) || version.startsWith( "1.0" ) || version.startsWith( "1.1" )
                 || version.startsWith( "1.2" ) ) {
                LOG.debug( Messages.getMessage( "DET_POSTGIS_PREDICATES_LEGACY", version ) );
                useLegacyPredicates = true;
            } else {
                LOG.debug( Messages.getMessage( "DET_POSTGIS_PREDICATES_MODERN", version ) );
            }

        } catch ( SQLException e ) {
            LOG.debug( e.getMessage(), e );
            throw new MetadataStoreException( e.getMessage(), e );
        } catch ( ClassNotFoundException e ) {
            LOG.debug( e.getMessage(), e );
            throw new MetadataStoreException( e.getMessage(), e );
        } finally {
            close( conn );
        }

    }

    /**
     * @param conn
     * @return the encoding of the PostGRES database.
     */
    private String determinePostGRESEncoding( Connection conn ) {
        String encodingPostGRES = "UTF-8";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery( "SHOW server_encoding" );
            rs.next();
            encodingPostGRES = rs.getString( 1 );
            LOG.debug( Messages.getMessage( "DET_POSTGRES_ENCODING", encodingPostGRES ) );
            stmt.close();
            rs.close();
        } catch ( Exception e ) {
            String msg = Messages.getMessage( "WARN_DET_POSTGRES_ENCODING", e.getMessage() );
            LOG.warn( msg );
        } finally {
            close( rs );
            close( stmt );
        }

        return encodingPostGRES;
    }

    private String determinePostGISVersion( Connection conn ) {
        String version = "1.0";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery( "SELECT postgis_version()" );
            rs.next();
            String postGISVersion = rs.getString( 1 );
            version = postGISVersion.split( " " )[0];
            LOG.debug( "PostGIS version: {}", version );

        } catch ( Exception e ) {
            String msg = Messages.getMessage( "DET_POSTGIS_VERSION", e.getMessage() );
            LOG.warn( msg );
        } finally {
            close( rs );
            close( stmt );
        }
        return version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.record.persistence.RecordStore#getRecords(javax.xml.stream.XMLStreamWriter,
     * javax.xml.namespace.QName)
     */
    @Override
    public MetadataResultSet getRecords( MetadataQuery query )
                            throws MetadataStoreException {

        String operationName = "getRecords";
        LOG.info( Messages.getMessage( "INFO_EXEC", operationName ) );
        PostGISMappingsISODC mapping = new PostGISMappingsISODC();
        PostGISWhereBuilder builder = null;
        Connection conn = null;

        MetadataResultSet result = null;
        MetadataResultType resultType = null;
        MetadataCollection col = new ISOCollection();

        try {
            conn = ConnectionManager.getConnection( connectionId );
            builder = new PostGISWhereBuilder( mapping, (OperatorFilter) query.getFilter(), query.getSorting(),
                                               useLegacyPredicates );

            switch ( query.getResultType() ) {
            case results:
                result = doResultsOnGetRecord( query, builder, conn );
                break;
            case hits:
                resultType = doHitsOnGetRecord( query, ResultType.hits, builder, conn, new ExecuteStatements() );
                result = new ISOMetadataResultSet( col, resultType );
                break;
            case validate:
                // is handled by the protocol layer
            }
        } catch ( FilterEvaluationException e ) {
            String msg = Messages.getMessage( "ERROR_OPERATION", operationName, e.getLocalizedMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );
        } catch ( SQLException e ) {
            String msg = Messages.getMessage( "ERROR_OPERATION", operationName, e.getMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );
        } finally {
            close( conn );
        }

        return result;
    }

    /**
     * The mandatory "resultType" attribute in the GetRecords operation is set to "hits".
     * 
     * @param writer
     *            - the XMLStreamWriter
     * @param typeName
     *            - the requested typeName
     * @param profileFormatNumberOutputSchema
     *            - the format number of the outputSchema
     * @param propertyAttributes
     *            - the properties that are identified by the request
     * @param con
     *            - the JDBCConnection
     * @throws MetadataStoreException
     */
    private MetadataResultType doHitsOnGetRecord( MetadataQuery recOpt, ResultType resultType,
                                                  PostGISWhereBuilder builder, Connection conn, ExecuteStatements exe )
                            throws MetadataStoreException {
        String resultTypeName = "hits";
        LOG.info( Messages.getMessage( "INFO_EXEC", "do " + resultTypeName + " on getRecords" ) );
        ResultSet rs = null;
        PreparedStatement ps = null;
        MetadataResultType result = null;

        try {

            int countRows = 0;
            int nextRecord = 0;
            int returnedRecords = 0;

            ps = exe.executeGetRecords( recOpt, true, builder, conn );

            rs = ps.executeQuery();
            rs.next();
            countRows = rs.getInt( 1 );
            LOG.debug( "rs for rowCount: " + rs.getInt( 1 ) );

            if ( countRows > recOpt.getMaxRecords() ) {
                nextRecord = recOpt.getMaxRecords() + 1;
                returnedRecords = recOpt.getMaxRecords();
            } else {
                nextRecord = 0;
                returnedRecords = countRows - recOpt.getStartPosition() + 1;
            }

            if ( resultType.equals( ResultType.results ) ) {
                result = new Hits( countRows, returnedRecords, nextRecord, DateUtils.formatISO8601Date( new Date() ) );
            } else {
                result = new Hits( countRows, 0, 1, DateUtils.formatISO8601Date( new Date() ) );
            }

        } catch ( Exception e ) {
            String msg = Messages.getMessage( "ERROR_REQUEST_TYPE", resultTypeName, e.getMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );
        } finally {
            close( rs );
            close( ps );
        }

        return result;

    }

    /**
     * The mandatory "resultType" attribute in the GetRecords operation is set to "results".
     * 
     * @param writer
     *            - the XMLStreamWriter
     * @param typeName
     *            - the requested typeName
     * @param profileFormatNumberOutputSchema
     *            - the format number of the outputSchema
     * @param recordStoreOptions
     *            - the properties that are identified by the request
     * @param con
     *            - the JDBCConnection
     * @throws SQLException
     * @throws XMLStreamException
     * @throws IOException
     */
    private MetadataResultSet doResultsOnGetRecord( MetadataQuery recordStoreOptions, PostGISWhereBuilder builder,
                                                    Connection conn )
                            throws MetadataStoreException {
        LOG.info( Messages.getMessage( "INFO_EXEC", "do results on getRecords" ) );
        MetadataResultType type = null;
        MetadataCollection col = new ISOCollection();
        ResultSet rs = null;
        ResultSet rsOut = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement stmtOut = null;
        ExecuteStatements exe = new ExecuteStatements();
        try {
            preparedStatement = exe.executeGetRecords( recordStoreOptions, false, builder, conn );
            type = doHitsOnGetRecord( recordStoreOptions, ResultType.results, builder, conn, exe );
            rs = preparedStatement.executeQuery();

            if ( rs != null && recordStoreOptions.getMaxRecords() != 0 ) {
                // generate the output based on the outputSchema
                while ( rs.next() ) {
                    int returnedID = rs.getInt( 1 );

                    StringBuilder outS = new StringBuilder();
                    outS.append( "SELECT " ).append( datasets ).append( '.' ).append( recordfull );
                    outS.append( " FROM " ).append( datasets );
                    outS.append( " WHERE " ).append( id );
                    outS.append( " = " ).append( returnedID );
                    stmtOut = conn.prepareStatement( outS.toString() );
                    LOG.debug( "" + stmtOut );
                    rsOut = stmtOut.executeQuery();
                    col.add( new ISORecord( writeXMLStreamReader( rsOut, 1 ), config.getAnyText() ) );
                    stmtOut.close();
                    rsOut.close();
                }

                rs.close();
            }
        } catch ( SQLException e ) {
            String msg = Messages.getMessage( "ERROR_REQUEST_TYPE", ResultType.results.name(), e.getMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );
        } finally {
            close( rs );
            close( rsOut );
            close( preparedStatement );
            close( stmtOut );
        }

        return new ISOMetadataResultSet( col, type );

    }

    @Override
    public MetadataResultSet getRecordsById( List<String> idList )
                            throws MetadataStoreException {

        String operationName = "getRecordsById";

        LOG.info( Messages.getMessage( "INFO_EXEC", operationName ) );

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        MetadataCollection col = new ISOCollection();
        try {

            conn = ConnectionManager.getConnection( connectionId );

            for ( String identifier : idList ) {
                String provedID = IdUtils.newInstance( conn ).proveIdExistence( identifier );
                if ( provedID == null ) {
                    String msg = Messages.getMessage( "NO_IDENTIFIER_FOUND", identifier );
                    LOG.info( msg );
                    throw new MetadataStoreException( msg );
                }

                StringBuilder select = new StringBuilder();
                select.append( "SELECT " ).append( "d." ).append( recordfull );
                select.append( " FROM " ).append( datasets ).append( " AS d" ).append( ',' );
                select.append( qp_identifier );
                select.append( " AS i" );
                select.append( " WHERE d." ).append( id );
                select.append( " = " ).append( "i.fk_datasets" ).append( " AND i." );
                select.append( backendIdentifier ).append( " = ? " );

                stmt = conn.prepareStatement( select.toString() );
                LOG.debug( "select RecordById statement: " + stmt );

                if ( stmt != null ) {

                    stmt.setObject( 1, identifier );
                    LOG.debug( "identifier: " + identifier );
                    LOG.debug( "" + stmt );
                    rs = stmt.executeQuery();

                    col.add( new ISORecord( writeXMLStreamReader( rs, 1 ), config.getAnyText() ) );

                }
                stmt.close();
            }

        } catch ( SQLException e ) {
            String msg = Messages.getMessage( "ERROR_OPERATION", operationName, e.getMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );
        } finally {
            JDBCUtils.close( rs, stmt, conn, LOG );

        }
        return new ISOMetadataResultSet( col, null );
    }

    private XMLStreamReader writeXMLStreamReader( ResultSet rs, int col )
                            throws SQLException, MetadataStoreException {
        InputStreamReader isr = null;
        Charset charset = encoding == null ? Charset.defaultCharset() : Charset.forName( encoding );
        XMLStreamReader xmlReader = null;
        while ( rs.next() ) {
            BufferedInputStream bais = new BufferedInputStream( rs.getBinaryStream( col ) );

            try {
                isr = new InputStreamReader( bais, charset );
                xmlReader = XMLInputFactory.newInstance().createXMLStreamReader( isr );
            } catch ( Exception e ) {
                String msg = Messages.getMessage( "ERROR_WRITING_RESULT", e.getMessage() );
                LOG.debug( msg );
                throw new MetadataStoreException( msg );
            } catch ( FactoryConfigurationError e ) {
                throw new MetadataStoreException( e.getMessage() );
            }
        }
        return xmlReader;
    }

    @Override
    public MetadataStoreTransaction acquireTransaction()
                            throws MetadataStoreException {
        ISOMetadataStoreTransaction ta = null;
        Connection conn = null;
        try {
            conn = ConnectionManager.getConnection( connectionId );
            List<RecordInspector> ri = new ArrayList<RecordInspector>();
            Inspectors inspectors = config.getInspectors();

            if ( inspectors != null ) {
                FileIdentifierInspector fi = inspectors.getFileIdentifierInspector();
                InspireInspector ii = inspectors.getInspireInspector();
                CoupledResourceInspector cri = inspectors.getCoupledResourceInspector();
                SchemaValidator sv = inspectors.getSchemaValidator();
                if ( fi != null ) {
                    ri.add( new FIInspector( fi ) );
                }
                if ( ii != null ) {
                    ri.add( new InspireComplianceInspector( ii ) );
                }
                if ( cri != null ) {
                    ri.add( new CoupledDataInspector( cri ) );
                }
                if ( sv != null ) {
                    ri.add( new MetadataSchemaValidationInspector( sv ) );
                }

            }
            ta = new ISOMetadataStoreTransaction( conn, ri, config.getAnyText(), useLegacyPredicates );
        } catch ( SQLException e ) {
            throw new MetadataStoreException( e.getMessage() );
        }
        return ta;
    }

}