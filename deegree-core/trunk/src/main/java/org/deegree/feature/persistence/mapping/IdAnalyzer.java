//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/feature/persistence/mapping/IdAnalyzer.java $
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
package org.deegree.feature.persistence.mapping;

import java.util.HashMap;
import java.util.Map;

import org.deegree.feature.persistence.mapping.id.FIDMapping;
import org.deegree.feature.types.FeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for analyzing if a given feature or geometry id can be attributed to a certain feature type.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 27878 $, $Date: 2010-11-09 05:20:43 -0800 (Tue, 09 Nov 2010) $
 */
class IdAnalyzer {

    private static Logger LOG = LoggerFactory.getLogger( IdAnalyzer.class );

    private final Map<String, FeatureType> prefixToFt = new HashMap<String, FeatureType>();

    /**
     * Creates a new {@link IdAnalyzer} instance for the given {@link MappedApplicationSchema}.
     * 
     * @param schema
     *            application schema with mapping information, must not be <code>null</code>
     */
    IdAnalyzer( MappedApplicationSchema schema ) {
        for ( FeatureType ft : schema.getFeatureTypes() ) {
            if ( !ft.isAbstract() ) {
                FeatureTypeMapping ftMapping = schema.getMapping( ft.getName() );
                if ( ftMapping != null ) {
                    FIDMapping fidMapping = ftMapping.getFidMapping();
                    if ( fidMapping != null ) {
                        LOG.debug( fidMapping.getPrefix() + " -> " + ft.getName() );
                        prefixToFt.put( fidMapping.getPrefix(), ft );
                    }
                }
            }
        }
    }

    public IdAnalysis analyze( String featureOrGeomId ) {

        int delimPos = featureOrGeomId.indexOf( '_' );
        if ( delimPos == -1 ) {
            throw new IllegalArgumentException( "Cannot determine feature type for id '" + featureOrGeomId
                                                + "': contains no underscore." );
        }
        delimPos = featureOrGeomId.indexOf( '_', delimPos + 1 );
        if ( delimPos == -1 ) {
            throw new IllegalArgumentException( "Cannot determine feature type for id '" + featureOrGeomId
                                                + "': must contain two underscores." );
        }        
        String prefix = featureOrGeomId.substring( 0, delimPos + 1 );
        FeatureType ft = prefixToFt.get( prefix );
        if ( ft == null ) {
            throw new IllegalArgumentException();
        }

        String idRemainder = featureOrGeomId.substring( delimPos + 1 );

        // TODO geometry ids (e.g. PLACE_GEOM_1)

        return new IdAnalysis( ft, idRemainder, true );
    }
}
