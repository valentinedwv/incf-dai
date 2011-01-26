//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/feature/persistence/simplesql/SimpleSQLFeatureStoreProvider.java $
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
package org.deegree.feature.persistence.simplesql;

import static org.deegree.commons.utils.CollectionUtils.map;

import java.net.URL;
import java.util.LinkedList;

import javax.xml.bind.JAXBException;

import org.deegree.commons.utils.Pair;
import org.deegree.commons.utils.CollectionUtils.Mapper;
import org.deegree.commons.xml.jaxb.JAXBUtils;
import org.deegree.feature.persistence.FeatureStore;
import org.deegree.feature.persistence.FeatureStoreException;
import org.deegree.feature.persistence.FeatureStoreProvider;
import org.deegree.feature.persistence.simplesql.jaxb.SimpleSQLFeatureStoreConfig;
import org.deegree.feature.persistence.simplesql.jaxb.SimpleSQLFeatureStoreConfig.LODStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link FeatureStoreProvider} for the {@link SimpleSQLFeatureStore}.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 27972 $, $Date: 2010-11-10 03:15:34 -0800 (Wed, 10 Nov 2010) $
 */
public class SimpleSQLFeatureStoreProvider implements FeatureStoreProvider {

    private static final Logger LOG = LoggerFactory.getLogger( SimpleSQLFeatureStoreProvider.class );

    private static final String CONFIG_NS = "http://www.deegree.org/datasource/feature/simplesql";

    private static final String CONFIG_JAXB_PACKAGE = "org.deegree.feature.persistence.simplesql.jaxb";

    private static final String CONFIG_SCHEMA = "/META-INF/schemas/datasource/feature/simplesql/3.0.0/simplesql.xsd";

    private static final String CONFIG_TEMPLATE = "/META-INF/schemas/datasource/feature/simplesql/3.0.0/example.xml";

    private static Mapper<Pair<Integer, String>, LODStatement> lodMapper = new Mapper<Pair<Integer, String>, LODStatement>() {
        public Pair<Integer, String> apply( LODStatement u ) {
            return new Pair<Integer, String>( u.getAboveScale(), u.getValue() );
        }
    };

    @Override
    public String getConfigNamespace() {
        return CONFIG_NS;
    }

    @Override
    public URL getConfigSchema() {
        return SimpleSQLFeatureStoreProvider.class.getResource( CONFIG_SCHEMA );
    }

    @Override
    public URL getConfigTemplate() {
        return SimpleSQLFeatureStoreProvider.class.getResource( CONFIG_TEMPLATE );
    }

    @Override
    public FeatureStore getFeatureStore( URL configURL )
                            throws FeatureStoreException {

        SimpleSQLFeatureStore fs = null;
        try {
            SimpleSQLFeatureStoreConfig config = (SimpleSQLFeatureStoreConfig) JAXBUtils.unmarshall(
                                                                                                     CONFIG_JAXB_PACKAGE,
                                                                                                     CONFIG_SCHEMA,
                                                                                                     configURL );
            String connId = config.getConnectionPoolId();
            String srs = config.getStorageCRS();
            String stmt = config.getSQLStatement();
            String name = config.getFeatureTypeName();
            String ns = config.getFeatureTypeNamespace();
            String prefix = config.getFeatureTypePrefix();
            String bbox = config.getBBoxStatement();
            LinkedList<Pair<Integer, String>> lods = map( config.getLODStatement(), lodMapper );

            fs = new SimpleSQLFeatureStore( connId, srs, stmt, name, ns, prefix, bbox, lods );
        } catch ( JAXBException e ) {
            String msg = "Error in feature store configuration file '" + configURL + "': " + e.getMessage();
            LOG.error( msg );
            throw new FeatureStoreException( msg, e );
        }
        return fs;
    }
}