//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/commons/utils/io/Zip.java $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
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
package org.deegree.commons.utils.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 27459 $, $Date: 2010-10-20 09:16:32 -0700 (Wed, 20 Oct 2010) $
 */
public class Zip {

    /**
     * @param in
     * @param dir
     * @throws IOException
     */
    public static void unzip( final InputStream in, final File dir )
                            throws IOException {
        ZipInputStream zin = new ZipInputStream( in );
        ZipEntry entry;

        if ( !dir.exists() ) {
            dir.mkdir();
        }

        while ( ( entry = zin.getNextEntry() ) != null ) {
            if ( entry.isDirectory() ) {
                File f = new File( dir, entry.getName() );
                f.mkdir();
                continue;
            }

            byte[] bs = new byte[16384];
            File f = new File( dir, entry.getName() );
            File parent = f.getAbsoluteFile().getParentFile();
            parent.mkdirs();
            FileOutputStream out = new FileOutputStream( f );
            int read;
            while ( ( read = zin.read( bs ) ) != -1 ) {
                out.write( bs, 0, read );
            }
            out.close();
        }

        in.close();
    }

}
