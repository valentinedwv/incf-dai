//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/metadata/persistence/iso/ExecuteStatements.java $
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

import static org.slf4j.LoggerFactory.getLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.deegree.commons.utils.JDBCUtils;
import org.deegree.commons.utils.time.DateUtils;
import org.deegree.feature.persistence.mapping.DBField;
import org.deegree.feature.persistence.mapping.Join;
import org.deegree.filter.sql.PropertyNameMapping;
import org.deegree.filter.sql.expression.SQLLiteral;
import org.deegree.filter.sql.postgis.PostGISWhereBuilder;
import org.deegree.metadata.i18n.Messages;
import org.deegree.metadata.persistence.GenericDatabaseExecution;
import org.deegree.metadata.persistence.MetadataQuery;
import org.deegree.metadata.persistence.MetadataStoreException;
import org.slf4j.Logger;

/**
 * Executes statements that does the interaction with the underlying database. This is a PostGRES implementation.
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: sthomas $
 * 
 * @version $Revision: 27881 $, $Date: 2010-11-09 06:41:11 -0800 (Tue, 09 Nov 2010) $
 */
public class ExecuteStatements implements GenericDatabaseExecution {

    private static final Logger LOG = getLogger( ExecuteStatements.class );

    private static final String databaseTable = PostGISMappingsISODC.DatabaseTables.datasets.name();

    private static final String id = PostGISMappingsISODC.CommonColumnNames.id.name();

    @Override
    public int executeDeleteStatement( Connection connection, PostGISWhereBuilder builder )
                            throws MetadataStoreException {

        LOG.info( Messages.getMessage( "INFO_EXEC", "delete-statement" ) );
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Integer> deletableDatasets;
        try {

            preparedStatement = getPreparedStatementDatasetIDs( null, false, connection, builder );

            int i = 1;
            if ( builder.getWhere() != null ) {
                for ( SQLLiteral o : builder.getWhere().getLiterals() ) {
                    preparedStatement.setObject( i++, o.getValue() );
                }
            }
            if ( builder.getOrderBy() != null ) {
                for ( SQLLiteral o : builder.getOrderBy().getLiterals() ) {
                    preparedStatement.setObject( i++, o.getValue() );
                }
            }

            LOG.debug( Messages.getMessage( "INFO_TA_DELETE_FIND", preparedStatement.toString() ) );

            rs = preparedStatement.executeQuery();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append( "DELETE FROM " );
            stringBuilder.append( PostGISMappingsISODC.DatabaseTables.datasets.name() );
            stringBuilder.append( " WHERE " ).append( PostGISMappingsISODC.CommonColumnNames.id.name() );
            stringBuilder.append( " = ?" );

            deletableDatasets = new ArrayList<Integer>();
            if ( rs != null ) {

                while ( rs.next() ) {
                    deletableDatasets.add( rs.getInt( 1 ) );

                }
                rs.close();
                for ( int d : deletableDatasets ) {

                    preparedStatement = connection.prepareStatement( stringBuilder.toString() );
                    preparedStatement.setInt( 1, d );

                    LOG.debug( Messages.getMessage( "INFO_TA_DELETE_DEL", preparedStatement.toString() ) );
                    preparedStatement.executeUpdate();

                }
            }

        } catch ( SQLException e ) {
            String msg = Messages.getMessage( "ERROR_SQL", preparedStatement.toString(), e.getMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );
        } finally {
            JDBCUtils.close( rs, preparedStatement, null, LOG );

        }

        return deletableDatasets.size();

    }

    private PreparedStatement getPreparedStatementDatasetIDs( MetadataQuery query, boolean setCount,
                                                              Connection connection, PostGISWhereBuilder builder )
                            throws MetadataStoreException {
        String orderByclause = null;
        StringBuilder getDatasetIDs = new StringBuilder( 300 );
        if ( builder.getOrderBy() != null ) {
            int length = builder.getOrderBy().getSQL().length();
            orderByclause = builder.getOrderBy().getSQL().toString().substring( 0, length - 4 );
        }

        String rootTableAlias = builder.getAliasManager().getRootTableAlias();

        getDatasetIDs.append( "SELECT " );
        if ( setCount ) {
            getDatasetIDs.append( "COUNT( DISTINCT " );
            getDatasetIDs.append( rootTableAlias );
            getDatasetIDs.append( '.' );
            getDatasetIDs.append( id );
            getDatasetIDs.append( ')' );
        } else {
            getDatasetIDs.append( " DISTINCT " );
            getDatasetIDs.append( rootTableAlias );
            getDatasetIDs.append( '.' );
            getDatasetIDs.append( id );
            if ( orderByclause != null ) {
                getDatasetIDs.append( ',' );
                getDatasetIDs.append( orderByclause );
            }
        }
        getDatasetIDs.append( " FROM " );
        getDatasetIDs.append( databaseTable );
        getDatasetIDs.append( " " );
        getDatasetIDs.append( rootTableAlias );

        for ( PropertyNameMapping mappedPropName : builder.getMappedPropertyNames() ) {
            String currentAlias = rootTableAlias;
            for ( Join join : mappedPropName.getJoins() ) {
                DBField from = join.getFrom();
                DBField to = join.getTo();
                getDatasetIDs.append( " LEFT OUTER JOIN " );
                getDatasetIDs.append( to.getTable() );
                getDatasetIDs.append( " AS " );
                getDatasetIDs.append( to.getAlias() );
                getDatasetIDs.append( " ON " );
                getDatasetIDs.append( currentAlias );
                getDatasetIDs.append( "." );
                getDatasetIDs.append( from.getColumn() );
                getDatasetIDs.append( "=" );
                currentAlias = to.getAlias();
                getDatasetIDs.append( currentAlias );
                getDatasetIDs.append( "." );
                getDatasetIDs.append( to.getColumn() );
            }
        }

        if ( builder.getWhere() != null ) {
            getDatasetIDs.append( " WHERE " );
            getDatasetIDs.append( builder.getWhere().getSQL() );
        }

        if ( builder.getOrderBy() != null && !setCount ) {
            getDatasetIDs.append( " ORDER BY " );
            getDatasetIDs.append( builder.getOrderBy().getSQL() );
        }

        if ( !setCount && query != null ) {
            getDatasetIDs.append( " OFFSET " ).append( Integer.toString( query.getStartPosition() - 1 ) );
            getDatasetIDs.append( " LIMIT " ).append( query.getMaxRecords() );
        }

        try {
            return connection.prepareStatement( getDatasetIDs.toString() );
        } catch ( SQLException e ) {
            String msg = Messages.getMessage( "ERROR_SQL", getDatasetIDs.toString(), e.getMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );
        }
    }

    private void updatePrecondition( PostGISMappingsISODC mapping, PostGISWhereBuilder builder ) {

        // for ( QName propName : mapping.getPropToTableAndCol().keySet() ) {
        // String nsURI = propName.getNamespaceURI();
        // String prefix = propName.getPrefix();
        // QName analysedQName = new QName( nsURI, "", prefix );
        // qNameSet.add( analysedQName );
        // }
        //
        // for ( QName qName : typeNames.keySet() ) {
        // if ( qName.equals( qNameSet.iterator().next() ) ) {
        // formatNumber = typeNames.get( qName );
        // }
        // }
        //
        // PreparedStatement str = getRequestedIDStatement( formatTypeInISORecordStore.get( ReturnableElement.full ),
        // gdds, formatNumber, builder );
        //
        // ResultSet rsUpdatableDatasets = str.executeQuery();
        // List<Integer> updatableDatasets = new ArrayList<Integer>();
        // while ( rsUpdatableDatasets.next() ) {
        // updatableDatasets.add( rsUpdatableDatasets.getInt( 1 ) );
        //
        // }
        // str.close();
        // rsUpdatableDatasets.close();
        //
        // if ( updatableDatasets.size() != 0 ) {
        // PreparedStatement stmt = null;
        // StringBuilder stringBuilder = new StringBuilder();
        // stringBuilder.append( "SELECT " ).append( formatTypeInISORecordStore.get( ReturnableElement.full ) );
        // stringBuilder.append( '.' ).append( data );
        // stringBuilder.append( " FROM " ).append( formatTypeInISORecordStore.get( ReturnableElement.full ) );
        // stringBuilder.append( " WHERE " ).append( formatTypeInISORecordStore.get( ReturnableElement.full ) );
        // stringBuilder.append( '.' ).append( format );
        // stringBuilder.append( " = 2 AND " ).append( formatTypeInISORecordStore.get( ReturnableElement.full ) );
        // stringBuilder.append( '.' ).append( fk_datasets ).append( " = ?;" );
        // for ( int i : updatableDatasets ) {
        //
        // stmt = conn.prepareStatement( stringBuilder.toString() );
        // stmt.setObject( 1, i );
        // ResultSet rsGetStoredFullRecordXML = stmt.executeQuery();
        //
        // while ( rsGetStoredFullRecordXML.next() ) {
        // for ( MetadataProperty recProp : upd.getRecordProperty() ) {
        //
        // PropertyNameMapping propMapping = mapping.getMapping( recProp.getPropertyName(), null );
        //
        // Object obje = mapping.getPostGISValue( (Literal<?>) recProp.getReplacementValue(),
        // recProp.getPropertyName() );
        //
        // // creating an OMElement read from backend byteData
        // InputStream in = rsGetStoredFullRecordXML.getBinaryStream( 1 );
        // XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader( in );
        //
        // OMElement elementBuiltFromDB = new StAXOMBuilder( reader ).getDocument().getOMDocumentElement();
        //
        // OMElement omElement = recursiveElementKnotUpdate( elementBuiltFromDB,
        // elementBuiltFromDB.getChildElements(),
        // propMapping.getTargetField().getColumn(),
        // obje.toString() );
        //
        // QName localName = omElement.getQName();
        //
        // ExecuteStatements executeStatements = new ExecuteStatements();
        //
        // if ( localName.equals( new QName( CSW_202_NS, "Record", CSW_PREFIX ) )
        // || localName.equals( new QName( CSW_202_NS, "Record", "" ) ) ) {
        //
        // executeStatements.executeUpdateStatement( conn, affectedIds,
        // new ISOQPParsing().parseAPDC( omElement ) );
        //
        // } else {
        //
        // // executeStatements.executeUpdateStatement(
        // // conn,
        // // affectedIds,
        // // new ISOQPParsing().parseAPISO(
        // // fi,
        // // ic,
        // // ci,
        // // omElement,
        // // true ) );
        //
        // }
        //
        // }
        // }
        // stmt.close();
        // rsGetStoredFullRecordXML.close();
        //
        // }
        // }

    }

    /**
     * This method replaces the text content of an elementknot.
     * <p>
     * TODO this is suitable for updates which affect an elementknot that has just one child. <br>
     * BUG - if there a more childs like in the "keyword"-elementknot.
     * 
     * @param element
     *            where to start in the OMTree
     * @param childElements
     *            as an Iterator above all the childElements of the element
     * @param searchForLocalName
     *            is the name that is searched for. This is the elementknot thats content should be updated.
     * @param newContent
     *            is the new content that should be updated
     * @return OMElement
     */
    private OMElement recursiveElementKnotUpdate( OMElement element, Iterator childElements, String searchForLocalName,
                                                  String newContent ) {

        Iterator it = element.getChildrenWithLocalName( searchForLocalName );

        if ( it.hasNext() ) {
            OMElement u = null;
            while ( it.hasNext() ) {
                u = (OMElement) it.next();
                LOG.debug( "rec: " + u.toString() );
                u.getFirstElement().setText( newContent );
                LOG.debug( "rec2: " + u.toString() );
            }
            return element;

        }
        while ( childElements.hasNext() ) {
            OMElement elem = (OMElement) childElements.next();

            recursiveElementKnotUpdate( elem, elem.getChildElements(), searchForLocalName, newContent );

        }

        return element;

    }

    @Override
    public PreparedStatement executeGetRecords( MetadataQuery query, boolean setCount, PostGISWhereBuilder builder,
                                                Connection conn )
                            throws MetadataStoreException {
        PreparedStatement preparedStatement = null;
        java.util.Date date = null;
        try {

            LOG.debug( Messages.getMessage( "INFO_EXEC", "getRecords-statement" ) );

            preparedStatement = getPreparedStatementDatasetIDs( query, setCount, conn, builder );

            int i = 1;
            if ( builder.getWhere() != null ) {
                for ( SQLLiteral o : builder.getWhere().getLiterals() ) {
                    if ( o.getSQLType() == Types.TIMESTAMP ) {
                        date = DateUtils.parseISO8601Date( o.getValue().toString() );
                        Timestamp d = new Timestamp( date.getTime() );
                        preparedStatement.setTimestamp( i++, d );
                    } else if ( o.getSQLType() == Types.BOOLEAN ) {
                        String bool = o.getValue().toString();
                        boolean b = false;
                        if ( bool.equals( "true" ) ) {
                            b = true;
                        }
                        preparedStatement.setBoolean( i++, b );
                    } else {
                        preparedStatement.setObject( i++, o.getValue() );
                    }
                }
            }
            if ( builder.getOrderBy() != null ) {
                for ( SQLLiteral o : builder.getOrderBy().getLiterals() ) {
                    preparedStatement.setObject( i++, o.getValue() );
                }
            }

            LOG.debug( preparedStatement.toString() );
        } catch ( SQLException e ) {
            String msg = Messages.getMessage( "ERROR_SQL", preparedStatement.toString(), e.getMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );
        } catch ( ParseException e ) {
            String msg = Messages.getMessage( "ERROR_PARSING", date, e.getMessage() );
            LOG.debug( msg );
            throw new MetadataStoreException( msg );
        }
        return preparedStatement;

    }

    @Override
    public String getEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

}
