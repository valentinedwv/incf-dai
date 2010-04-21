/**
 * <p>Title: Image Assembly VO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: BIRN-CC, UCSD</p>
 * @author Asif Memon
 * @version 1.0
 */

package edu.ucsd.crbs.incf.components.services.aba;


/**
 * @author Asif Memon
 * 
 */
public class ABAServiceVO {

	/**
	 * The helper class to store the image assembly related data
	 */

	public ABAServiceVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String category;
	private String imageServiceWSURL;
	private String transformationOne;
	private String transformationTwo;
	private String transformationThree;
	
	    
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

	public String getTransformationOne() {
		return transformationOne;
	}

	public void setTransformationOne(String transformationOne) {
		this.transformationOne = transformationOne;
	}

	public String getTransformationTwo() {
		return transformationTwo;
	}

	public void setTransformationTwo(String transformationTwo) {
		this.transformationTwo = transformationTwo;
	}

	public String getTransformationThree() {
		return transformationThree;
	}

	public void setTransformationThree(String transformationThree) {
		this.transformationThree = transformationThree;
	}

	
}
