package com.shangpin.api.airshop.product.d;

@SuppressWarnings("serial")
public class ExceptionMessage extends Exception {
	private String errorMessge;
	  @Override
	    public String getMessage(){
	        return getErrorMessage();
	    }
	  
	  public ExceptionMessage(String errorMessge) {
	        this.errorMessge = errorMessge;
	    }
	  
	  private String getErrorMessage(){
		  
		  return errorMessge + "required data cannot be empty";
	  }
}
