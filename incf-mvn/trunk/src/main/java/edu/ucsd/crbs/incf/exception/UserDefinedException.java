package edu.ucsd.crbs.incf.exception;

public class UserDefinedException extends Exception{

	private String message = ""; 
	
  public UserDefinedException() {
    super();
  }


  public UserDefinedException(String msg){
    super(msg);
    this.message = msg;
  }


public String getMessage() {
	return message;
}


public void setMessage(String message) {
	this.message = message;
}
  
  
}