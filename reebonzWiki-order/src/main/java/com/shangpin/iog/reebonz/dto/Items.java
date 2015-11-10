package com.shangpin.iog.reebonz.dto;

import java.util.List;

/**
 * Created by 赵根春  on 2015/9/25.
 */
public class Items {

    private String numFound;
    private String start;
    private List<Item> docs;
    
	public List<Item> getDocs() {
		return docs;
	}
	public void setDocs(List<Item> docs) {
		this.docs = docs;
	}
	public String getNumFound() {
		return numFound;
	}
	public void setNumFound(String numFound) {
		this.numFound = numFound;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
 
  
}
