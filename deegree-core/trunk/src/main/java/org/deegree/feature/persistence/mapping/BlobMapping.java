//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/feature/persistence/mapping/BlobMapping.java $
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

import org.deegree.cs.CRS;
import org.deegree.feature.persistence.BlobCodec;

/**
 * Encapsulates the BLOB mapping parameters for a {@link MappedApplicationSchema}.
 * 
 * @see MappedApplicationSchema
 * @see FeatureTypeMapping
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 27750 $, $Date: 2010-11-03 08:49:44 -0700 (Wed, 03 Nov 2010) $
 */
public class BlobMapping {

    private final String table;

    private final CRS storageCRS;

    private final BlobCodec codec;

    /**
     * Creates a new {@link BlobMapping} instance.
     * 
     * @param table
     *            the name of the table that stores the BLOBs, must not be <code>null</code>
     * @param storageCRS
     *            crs used for storing geometries / envelopes, must not be <code>null</code>
     * @param codec
     *            the decoder / encoder used for the BLOBs, must not be <code>null</code>
     */
    public BlobMapping( String table, CRS storageCRS, BlobCodec codec ) {
        this.table = table;
        this.storageCRS = storageCRS;
        this.codec = codec;
    }

    /**
     * Returns the name of the table that stores the BLOBs.
     * 
     * @return the name of the table that stores the BLOBs, never <code>null</code>
     */
    public String getTable() {
        return table;
    }

    /**
     * Returns the {@link CRS} used for storing the geometries / envelopes.
     * 
     * @return the crs, never <code>null</code>
     */
    public CRS getCRS() {
        return storageCRS;
    }

    /**
     * Returns the {@link BlobCodec} for encoding and decoding features / geometries.
     * 
     * @return the codec, never <code>null</code>
     */
    public BlobCodec getCodec() {
        return codec;
    }

    /**
     * Returns the name of the column that stores the gml ids.
     * 
     * @return the name of the column, never <code>null</code>
     */
    public String getGMLIdColumn() {
        return "gml_id";
    }

    /**
     * 
     * @return
     */
    public String getDataColumn() {
        return "binary_object";
    }

    /**
     * 
     * @return
     */
    public String getBBoxColumn() {
        return "gml_bounded_by";
    }

    /**
     * 
     * @return
     */
    public String getTypeColumn() {
        return "ft_type";
    }

    public String getInternalIdColumn() {
        return "id";
    }
}