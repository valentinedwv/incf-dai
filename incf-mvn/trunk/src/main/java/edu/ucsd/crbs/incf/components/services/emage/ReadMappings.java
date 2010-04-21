/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucsd.crbs.incf.components.services.emage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * Reads a list of mappings from a text file and stores them in memory.
 * The mapping file should be of the format 'aba term* emap term'.
 * There must be a standard end of line charater as the file is parsed using a
 * BufferedReader.
 *
 * @author kcm1@hw.ac.uk
 * @see java.io.BufferedReader#readLine() 
 */
public class ReadMappings {

    /**
     * Reads data from an external file and parses it, then stores it into a Hashtable
     * that is returned to the client code.
     *
     *
     * @return A Hashtable where the aba terms are the keys and the corresponding
     * emap terms are the values.
     * @throws Exception All exceptions generated will be thrown.
     */
    protected synchronized Hashtable getMappingsFromFile() throws Exception {

    	
		InputStream path = null;

		path = getClass().getResourceAsStream("../../../config/mappings.txt");

		System.out.println("Path is - " + path);

		BufferedReader reader = new BufferedReader(new InputStreamReader(path));
/*		StringBuilder sb = new StringBuilder();
		String line1 = null;
		while ((line1 = reader.readLine()) != null) {
			sb.append(line1 + "\n");
		}
*/		//System.out.println("String is - " + sb.toString());
		
/*    	File f = new File("src/edu/ucsd/crbs/incf/components/services/emage/mappings.txt");
        System.out.println("Mapping - " + f.getAbsolutePath());

*/        /*if(sb.toString().trim().equals("") || sb.toString() == null ) {
            System.err.println("bisel.ReadMappings cannot find mapping file");
            System.exit(1);
        }*/

        Hashtable mappings = new Hashtable(17, new Float(1.25).floatValue());


        //BufferedReader br = new BufferedReader(new FileReader(f));
        String line = reader.readLine();
        while(line != null) {            
            StringTokenizer st = new StringTokenizer(line, "*");
            String aba = st.nextToken();
            String emap = st.nextToken();
            mappings.put(aba, emap);
            line = reader.readLine();
        }

        reader.close();
        return mappings;
    }


    /**
     * Simply used for testing purposes.
     * @param args None
     */
    public static void main(String[] args) {
        ReadMappings rs = new ReadMappings();
        try {
            Hashtable result = rs.getMappingsFromFile();
            Enumeration en = result.elements();
            while(en.hasMoreElements()) {
                System.out.println(en.nextElement());
            }

            System.out.println("Pons:"+result.get("Pons"));
            System.out.println("Hypothalamus:"+result.get("Hypothalamus"));
            System.out.println("Medulla:"+result.get("Medulla"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
