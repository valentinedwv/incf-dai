//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/filter/function/geometry/IsSurface.java $
package org.deegree.filter.function.geometry;

import java.util.List;

import org.deegree.commons.tom.TypedObjectNode;
import org.deegree.commons.tom.primitive.PrimitiveValue;
import org.deegree.feature.property.Property;
import org.deegree.filter.Expression;
import org.deegree.filter.FilterEvaluationException;
import org.deegree.filter.XPathEvaluator;
import org.deegree.filter.expression.Function;
import org.deegree.filter.function.FunctionProvider;
import org.deegree.geometry.Geometry;
import org.deegree.geometry.multi.MultiPolygon;
import org.deegree.geometry.multi.MultiSurface;
import org.deegree.geometry.primitive.Surface;

/**
 * Returns no value in case the argument expression evaluates to no value, or multiple values, or the value can not be
 * interpreted as a geometry.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 27253 $, $Date: 2010-10-18 02:49:13 -0700 (Mon, 18 Oct 2010) $
 */
public class IsSurface implements FunctionProvider {

    private static final String NAME = "IsSurface";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getArgCount() {
        return 1;
    }

    @Override
    public Function create( List<Expression> params ) {
        if ( params.size() != 1 ) {
            throw new IllegalArgumentException( NAME + " requires exactly one parameter." );
        }
        return new Function( NAME, params ) {
            @Override
            public <T> TypedObjectNode[] evaluate( T obj, XPathEvaluator<T> xpathEvaluator )
                                    throws FilterEvaluationException {
                Object[] vals = getParams()[0].evaluate( obj, xpathEvaluator );

                if ( vals.length != 1 || !( vals[0] instanceof Geometry ) && !( vals[0] instanceof Property )
                     && !( ( (Property) vals[0] ).getValue() instanceof Geometry ) ) {
                    return new TypedObjectNode[0];
                }
                Geometry geom = vals[0] instanceof Geometry ? (Geometry) vals[0]
                                                           : (Geometry) ( (Property) vals[0] ).getValue();

                // TODO is handling of multi geometries like this ok?
                boolean isSurface = geom instanceof Surface || geom instanceof MultiPolygon
                                    || geom instanceof MultiSurface;
                return new TypedObjectNode[] { new PrimitiveValue( Boolean.toString( isSurface ) ) };
            }
        };
    }
}