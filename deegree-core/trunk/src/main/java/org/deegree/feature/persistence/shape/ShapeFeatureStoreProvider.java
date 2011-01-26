//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/feature/persistence/shape/ShapeFeatureStoreProvider.java $
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
package org.deegree.feature.persistence.shape;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBException;

import org.deegree.commons.xml.XMLAdapter;
import org.deegree.commons.xml.jaxb.JAXBUtils;
import org.deegree.cs.CRS;
import org.deegree.feature.i18n.Messages;
import org.deegree.feature.persistence.FeatureStore;
import org.deegree.feature.persistence.FeatureStoreException;
import org.deegree.feature.persistence.FeatureStoreProvider;
import org.deegree.feature.persistence.shape.jaxb.ShapeFeatureStoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link FeatureStoreProvider} for the {@link ShapeFeatureStore}.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 27972 $, $Date: 2010-11-10 03:15:34 -0800 (Wed, 10 Nov 2010) $
 */
public class ShapeFeatureStoreProvider implements FeatureStoreProvider {

    private static final Logger LOG = LoggerFactory.getLogger( ShapeFeatureStoreProvider.class );

    private static final String CONFIG_NS = "http://www.deegree.org/datasource/feature/shape";

    private static final String CONFIG_JAXB_PACKAGE = "org.deegree.feature.persistence.shape.jaxb";

    private static final String CONFIG_SCHEMA = "/META-INF/schemas/datasource/feature/shape/3.0.0/shape.xsd";

    private static final String CONFIG_TEMPLATE = "/META-INF/schemas/datasource/feature/shape/3.0.0/example.xml";

    @Override
    public String getConfigNamespace() {
        return CONFIG_NS;
    }

    @Override
    public URL getConfigSchema() {
        return ShapeFeatureStoreProvider.class.getResource( CONFIG_SCHEMA );
    }

    @Override
    public URL getConfigTemplate() {
        return ShapeFeatureStoreProvider.class.getResource( CONFIG_TEMPLATE );
    }

    @Override
    public FeatureStore getFeatureStore( URL configURL )
                            throws FeatureStoreException {

        ShapeFeatureStore fs = null;
        try {
            ShapeFeatureStoreConfig config = (ShapeFeatureStoreConfig) JAXBUtils.unmarshall( CONFIG_JAXB_PACKAGE,
                                                                                             CONFIG_SCHEMA, configURL );

            XMLAdapter resolver = new XMLAdapter();
            resolver.setSystemId( configURL.toString() );

            String srs = config.getStorageCRS();
            CRS crs = null;
            if ( srs != null ) {
                // rb: if it is null, the shape feature store will try to read the prj files.
                // srs = "EPSG:4326";
                // } else {
                srs = srs.trim();
                crs = new CRS( srs );
            }

            String shapeFileName = null;
            try {
                shapeFileName = new File( resolver.resolve( config.getFile().trim() ).toURI() ).toString();
            } catch ( MalformedURLException e ) {
                String msg = Messages.getMessage( "STORE_MANAGER_STORE_SETUP_ERROR", e.getMessage() );
                LOG.error( msg, e );
                throw new FeatureStoreException( msg, e );
            } catch ( URISyntaxException e ) {
                String msg = Messages.getMessage( "STORE_MANAGER_STORE_SETUP_ERROR", e.getMessage() );
                LOG.error( msg );
                LOG.trace( "Stack trace:", e );
                throw new FeatureStoreException( msg, e );
            }

            Charset cs = null;
            String encoding = config.getEncoding();
            if ( encoding != null ) {
                try {
                    cs = Charset.forName( encoding );
                } catch ( Exception e ) {
                    String msg = "Unsupported encoding '" + encoding + "'. Continuing with encoding guessing mode.";
                    LOG.error( msg );
                }
            }

            // TODO make cache configurable
            Boolean genIdx = config.isGenerateAlphanumericIndexes();
            fs = new ShapeFeatureStore( shapeFileName, crs, cs, config.getFeatureTypeNamespace(),
                                        config.getFeatureTypeName(), config.getFeatureTypePrefix(), genIdx == null
                                                                                                    || genIdx, null );

        } catch ( JAXBException e ) {
            String msg = "Error in feature store configuration file '" + configURL + "': " + e.getMessage();
            LOG.error( msg );
            throw new FeatureStoreException( msg, e );
        }
        return fs;
    }
}