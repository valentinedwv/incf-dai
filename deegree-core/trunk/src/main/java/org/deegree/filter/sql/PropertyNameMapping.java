//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/filter/sql/PropertyNameMapping.java $
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
package org.deegree.filter.sql;

import static org.deegree.commons.tom.primitive.PrimitiveType.STRING;

import java.util.Collections;
import java.util.List;

import org.deegree.commons.tom.primitive.PrimitiveType;
import org.deegree.cs.CRS;
import org.deegree.feature.persistence.mapping.DBField;
import org.deegree.feature.persistence.mapping.Join;
import org.deegree.filter.expression.PropertyName;

/**
 * Represents a {@link PropertyName} that's mapped to a relational model.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 27754 $, $Date: 2010-11-03 10:40:25 -0700 (Wed, 03 Nov 2010) $
 */
public class PropertyNameMapping {

    private final DBField valueField;

    private final List<Join> joins;

    private final PrimitiveType pt;

    private final CRS crs;

    private final String srid;

    public PropertyNameMapping( String table, String column, CRS crs, String srid ) {
        this.valueField = new DBField( table, column );
        this.joins = Collections.emptyList();
        this.pt = STRING;
        this.crs = crs;
        this.srid = srid;
    }

    /**
     * @param aliasManager
     * @param valueField
     * @param joins
     * @param crs
     * @param srid
     */
    public PropertyNameMapping( TableAliasManager aliasManager, DBField valueField, List<Join> joins, CRS crs,
                                String srid ) {
        this.valueField = valueField;
        this.joins = joins;
        this.pt = STRING;
        this.crs = crs;
        this.srid = srid;

        String currentAlias = aliasManager.getRootTableAlias();
        if ( joins != null ) {
            for ( Join join : joins ) {
                join.getFrom().setAlias( currentAlias );
                currentAlias = aliasManager.generateNew();
                join.getTo().setAlias( currentAlias );
            }
        }
        valueField.setAlias( currentAlias );
    }

    public CRS getCRS() {
        return crs;
    }

    public String getSRID() {
        return srid;
    }

    public DBField getTargetField() {
        return valueField;
    }

    public PrimitiveType getTargetFieldType() {
        return pt;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public int getSQLType() {
        return -1;
    }

    public boolean isSpatial() {
        return true;
    }

    @Override
    public String toString() {
        String s = "";
        for ( Join join : joins ) {
            s += join;
            s += ",";
        }
        s += valueField;
        return s;
    }
}