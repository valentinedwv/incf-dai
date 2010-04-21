/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucsd.crbs.incf.components.services.emage;

import java.util.Hashtable;

/**
 * Provides single point of contact for all code produced by BISEL for the INCF 
 * demo.  
 * 
 * Currently this includes:
 * <ol>
 * <li> Mappings between ABA and EMAP anatomies
 * <li> A method to map between an ABA term and an EMAP term
 * </ol>
 *
 * 
 * @author kcm
 */
public class INCFClient {

    Hashtable mappings = null;

    /**
     * Uses ReadMappings to get the list of mappings used by getEMAPStructureName.
     * @see bisel.ReadMappings
     */
    public INCFClient() {
        ReadMappings rm = new ReadMappings();
        try {
            mappings = rm.getMappingsFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Takes a list of ABA to EMAP mappings and simply returns an appropriate
     * EMAP term based on the input (ABA term).
     *
     *
     * @param abaStructureName An ABA term.
     * @return The corresponding EMAP term.
     */
    public String getEmageStructureName(String abaStructureName) {

        abaStructureName = abaStructureName.toLowerCase();

        if (mappings.containsKey(abaStructureName)) {
            return (String) mappings.get(abaStructureName);
        } else {
            return "";
        }
    }

    /**
     * Used for testing purposes.
     * @param args Not needed.
     */
    public static void main(String[] args) {
        INCFClient ic = new INCFClient();
        System.out.println("pons:"+ic.getEmageStructureName("pons"));
        System.out.println("Pons:"+ic.getEmageStructureName("Pons"));
        System.out.println("PONS:"+ic.getEmageStructureName("PONS"));
        System.out.println("medulla:"+ic.getEmageStructureName("medulla"));
        System.out.println("Banana:"+ic.getEmageStructureName("Banana"));
    }
}
