//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/filter/sql/expression/SQLColumn.java $
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
package org.deegree.filter.sql.expression;

import java.util.Collections;
import java.util.List;

import org.deegree.cs.CRS;

/**
 * {@link SQLExpression} that represents a table column.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 27754 $, $Date: 2010-11-03 10:40:25 -0700 (Wed, 03 Nov 2010) $
 */
public class SQLColumn implements SQLExpression {

    private int sqlType;

    private boolean isSpatial;

    private String column;

    private String table;

    private String srid;

    private CRS crs;

    public SQLColumn( String column ) {
        this.column = column;
        isSpatial = true;
    }

    public SQLColumn( String column, int sqlType ) {
        this.column = column;
        this.sqlType = sqlType;
    }

    public SQLColumn( String table, String column, boolean spatial, int sqlType, CRS crs, String srid ) {
        this.table = table;
        this.column = column;
        this.sqlType = sqlType;
        this.isSpatial = spatial;
        this.crs = crs;
        this.srid = srid;
    }

    @Override
    public CRS getCRS() {
        return crs;
    }

    @Override
    public String getSRID() {
        return srid;
    }

    @Override
    public int getSQLType() {
        return sqlType;
    }

    @Override
    public boolean isSpatial() {
        return isSpatial;
    }

    @Override
    public String toString() {
        return table == null ? column : ( table + "." + column );
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SQLLiteral> getLiterals() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public StringBuilder getSQL() {
        StringBuilder sb = new StringBuilder();
        if ( table != null ) {
            sb.append( table ).append( "." );
        }
        sb.append( column );
        return sb;
    }
}
