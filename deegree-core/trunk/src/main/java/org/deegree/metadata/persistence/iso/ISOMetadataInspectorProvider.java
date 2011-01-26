//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/metadata/persistence/iso/ISOMetadataInspectorProvider.java $
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

import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.deegree.metadata.persistence.MetadataInspectorProvider;
import org.deegree.metadata.persistence.MetadataStoreException;
import org.deegree.metadata.persistence.MetadataInspectorManager.InspectorKey;
import org.deegree.metadata.persistence.iso.parsing.inspectation.RecordInspector;
import org.deegree.metadata.persistence.iso19115.jaxb.ISOMetadataStoreConfig;
import org.deegree.metadata.persistence.iso19115.jaxb.ISOMetadataStoreConfig.Inspectors;
import org.slf4j.Logger;

/**
 * TODO add class documentation here
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: sthomas $
 * 
 * @version $Revision: 28229 $, $Date: 2010-11-15 08:03:29 -0800 (Mon, 15 Nov 2010) $
 */
public class ISOMetadataInspectorProvider implements MetadataInspectorProvider {

    private static Logger LOG = getLogger( ISOMetadataInspectorProvider.class );

    private RecordInspector inspector;

    @Override
    public RecordInspector getInspector( URL configURL )
                            throws MetadataStoreException {
        ISOMetadataStoreConfig config = getConfig( configURL );
        Inspectors inspectors = config.getInspectors();
        if ( inspectors != null ) {
            // for ( JAXBElement<? extends AbstractInspector> jaxbElem : inspectors.getAbstractInspector() ) {
            // AbstractInspector d = jaxbElem.getValue();
            // // if ( d instanceof IdentifierInspector ) {
            // // return inspector = FileIdentifierInspector.newInstance( (IdentifierInspector) d );
            // // } else if ( d instanceof InspireInspector ) {
            // // return inspector = InspireComplianceInspector.newInstance( (InspireInspector) d );
            // // } else if ( d instanceof CoupledResourceInspector ) {
            // // return inspector = CoupledDataInspector.newInstance( (CoupledResourceInspector) d );
            // // }
            //
            // }
        }

        return null;
    }

    private ISOMetadataStoreConfig getConfig( URL configURL )
                            throws MetadataStoreException {

        ISOMetadataStoreConfig config = null;
        if ( configURL == null ) {
            LOG.warn( "No metadata store configuration found!" );
        } else {
            try {
                JAXBContext jc = JAXBContext.newInstance( "org.deegree.metadata.persistence.iso19115.jaxb" );
                Unmarshaller u = jc.createUnmarshaller();
                config = (ISOMetadataStoreConfig) u.unmarshal( configURL );
            } catch ( JAXBException e ) {
                String msg = "Error in metadata store configuration file '" + configURL + "': " + e.getMessage();
                LOG.error( msg );
                throw new MetadataStoreException( msg, e );
            }
        }
        return config;

    }

    @Override
    public InspectorKey getInspectorKey() {
        int l = "Inspector".length();
        // String prefix = inspector.getName().name().substring( 0, inspector.getName().name().length() - l );
        // if ( prefix.equals( InspectorKey.CoupledResourceInspector.name() ) ) {
        // return InspectorKey.CoupledResourceInspector;
        // } else if ( prefix.equals( InspectorKey.IdentifierInspector.name() ) ) {
        // return InspectorKey.IdentifierInspector;
        // } else if ( prefix.equals( InspectorKey.InspireInspector.name() ) ) {
        // return InspectorKey.InspireInspector;
        // } else {
        // return null;
        // }
        return null;

    }

}
