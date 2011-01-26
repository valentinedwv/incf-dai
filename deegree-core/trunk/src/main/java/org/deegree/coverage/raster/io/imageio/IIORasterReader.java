//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/coverage/raster/io/imageio/IIORasterReader.java $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 Department of Geography, University of Bonn
 and
 lat/lon GmbH

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
package org.deegree.coverage.raster.io.imageio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.deegree.commons.utils.FileUtils;
import org.deegree.coverage.raster.AbstractRaster;
import org.deegree.coverage.raster.SimpleRaster;
import org.deegree.coverage.raster.cache.RasterCache;
import org.deegree.coverage.raster.data.container.BufferResult;
import org.deegree.coverage.raster.data.info.RasterDataInfo;
import org.deegree.coverage.raster.geom.RasterGeoReference;
import org.deegree.coverage.raster.geom.RasterRect;
import org.deegree.coverage.raster.geom.RasterGeoReference.OriginLocation;
import org.deegree.coverage.raster.io.RasterIOOptions;
import org.deegree.coverage.raster.io.RasterReader;
import org.deegree.coverage.raster.io.WorldFileAccess;
import org.deegree.coverage.raster.utils.RasterFactory;
import org.deegree.cs.CRS;
import org.deegree.cs.coordinatesystems.CoordinateSystem;
import org.deegree.geometry.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author <a href="mailto:tonnhofer@lat-lon.de">Oliver Tonnhofer</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 24731 $, $Date: 2010-06-07 11:37:18 -0700 (Mon, 07 Jun 2010) $
 * 
 */
public class IIORasterReader implements RasterReader {

    private static final Set<String> SUPPORTED_TYPES;

    private static Logger LOG = LoggerFactory.getLogger( IIORasterReader.class );

    static {
        SUPPORTED_TYPES = new HashSet<String>();

        ImageIO.scanForPlugins();
        String[] readerFormatNames = ImageIO.getReaderFormatNames();
        if ( readerFormatNames != null ) {
            for ( String format : readerFormatNames ) {
                if ( format != null && !"".equals( format.trim() ) && !format.contains( " " ) ) {
                    SUPPORTED_TYPES.add( format.toLowerCase() );
                }
            }
        }
        // SUPPORTED_TYPES.add( IIORasterReader.class.getCanonicalName() );
        // SUPPORTED_TYPES.add( "iio" );
        // SUPPORTED_TYPES.add( "imageio" );
        // SUPPORTED_TYPES.add( "iio-reader" );
        // String[] types = new String[] { "jpg", "jpeg", "png", "tif", "tiff", "jp2", "gif" };
        //
        // for ( String type : types ) {
        // Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix( type );
        // if ( iter != null && iter.hasNext() ) {
        // SUPPORTED_TYPES.add( type.toLowerCase() );
        // LOG.debug( "register ImageReader for " + type );
        // } else {
        // LOG.error( "no ImageReader for " + type + " found" );
        // }
        // }
    }

    private IIORasterDataReader reader;

    private int width;

    private int height;

    private RasterGeoReference rasterReference;

    private String dataLocationId;

    public boolean canLoad( File filename ) {
        return true;
    }

    public AbstractRaster load( File file, RasterIOOptions options )
                            throws IOException {
        LOG.debug( "reading " + file + " with ImageIO" );
        reader = new IIORasterDataReader( file, options );
        AbstractRaster r = loadFromReader( reader, options );
        return r;
    }

    @Override
    public AbstractRaster load( InputStream stream, RasterIOOptions options )
                            throws IOException {
        reader = new IIORasterDataReader( stream, options );
        return loadFromReader( reader, options );
    }

    private AbstractRaster loadFromReader( IIORasterDataReader reader, RasterIOOptions options ) {
        width = reader.getColumns();
        height = reader.getRows();
        RasterIOOptions opts = new RasterIOOptions();
        opts.copyOf( options );
        setID( opts );

        OriginLocation definedRasterOrigLoc = opts.getRasterOriginLocation();
        MetaDataReader metaDataReader = new MetaDataReader( reader.getMetaData(), definedRasterOrigLoc );
        CoordinateSystem crs = metaDataReader.getCRS();
        rasterReference = metaDataReader.getRasterReference();

        if ( rasterReference == null ) {
            if ( opts.hasRasterGeoReference() ) {
                rasterReference = opts.getRasterGeoReference();
            } else {
                // create a 1:1 mapping
                rasterReference = new RasterGeoReference( definedRasterOrigLoc, 1, -1, 0.5, height - 0.5 );
                if ( opts.readWorldFile() ) {
                    try {
                        if ( reader.file() != null ) {
                            rasterReference = WorldFileAccess.readWorldFile( reader.file(), opts );
                        }
                    } catch ( IOException e ) {
                        //
                    }
                }
            }
        }

        // reader.close();
        // read crs from options (if any)
        CRS readCRS = null;
        if ( crs == null ) {
            readCRS = opts.getCRS();
        } else {
            readCRS = new CRS( crs );
        }

        Envelope envelope = rasterReference.getEnvelope( width, height, readCRS );

        // RasterDataContainer source = RasterDataContainerFactory.withDefaultLoadingPolicy( reader );
        // RasterDataContainer source = RasterDataContainerFactory.withLoadingPolicy( reader, options.getLoadingPolicy()
        // );
        RasterDataInfo rdi = reader.getRasterDataInfo();

        RasterCache cache = RasterCache.getInstance( opts );
        SimpleRaster result = cache.createFromCache( this, this.dataLocationId );
        if ( result == null ) {
            result = RasterFactory.createEmptyRaster( rdi, envelope, rasterReference, this, true, opts );
        }
        this.reader.dispose();
        return result;
        //
        // return new SimpleRaster( data, envelope, rasterReference );
    }

    private void setID( RasterIOOptions options ) {
        this.dataLocationId = options != null ? options.get( RasterIOOptions.ORIGIN_OF_RASTER ) : null;
        if ( dataLocationId == null ) {
            if ( reader != null && reader.file() != null ) {
                this.dataLocationId = FileUtils.getFilename( reader.file() );
            }
        }
    }

    @Override
    public Set<String> getSupportedFormats() {
        return SUPPORTED_TYPES;
    }

    @Override
    public File file() {
        return reader == null ? null : reader.file();
    }

    @Override
    public boolean shouldCreateCacheFile() {
        return reader == null ? true : reader.shouldCreateCacheFile();
    }

    @Override
    public RasterGeoReference getGeoReference() {
        return rasterReference;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public BufferResult read( RasterRect rect, ByteBuffer buffer )
                            throws IOException {
        return reader == null ? null : reader.read( rect, buffer );
    }

    public RasterDataInfo getRasterDataInfo() {
        return reader == null ? null : reader.getRasterDataInfo();
    }

    @Override
    public boolean canReadTiles() {
        return reader == null ? false : reader.getReadTiles();
    }

    @Override
    public String getDataLocationId() {
        return dataLocationId;
    }

    @Override
    public void dispose() {
        if ( reader != null ) {
            reader.dispose();
        }
    }
}
