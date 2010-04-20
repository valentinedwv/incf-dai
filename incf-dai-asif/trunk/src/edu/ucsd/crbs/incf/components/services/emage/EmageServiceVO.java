/**
 * <p>Title: Image Assembly VO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: BIRN-CC, UCSD</p>
 * @author Asif Memon
 * @version 1.0
 */

package edu.ucsd.crbs.incf.components.services.emage;

import java.util.ArrayList;

import edu.ucsd.crbs.incf.common.INCFLogger;

/**
 * @author Asif Memon
 * 
 */
public class EmageServiceVO {

	/**
	 * The helper class to store the image assembly related data
	 */

	public EmageServiceVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String category;
	private String imageServiceWSURL;
	private String structureName;
	private String structureAbbrev;


	//-------------------Public methods---------------------

	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getImageServiceWSURL() {
		return imageServiceWSURL;
	}
	
	public void setImageServiceWSURL(String imageServiceWSURL) {
		this.imageServiceWSURL = imageServiceWSURL;
	}

	public String getStructureAbbrev() {
		return structureAbbrev;
	}

	public void setStructureAbbrev(String structureAbbrev) {
		this.structureAbbrev = structureAbbrev;
	}

	public String getStructureName() {
		return structureName;
	}

	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}

	
}
