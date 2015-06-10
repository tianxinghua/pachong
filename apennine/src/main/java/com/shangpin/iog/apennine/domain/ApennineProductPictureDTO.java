package com.shangpin.iog.apennine.domain;

import java.io.Serializable;

public class ApennineProductPictureDTO implements Serializable{
	private static final long serialVersionUID = -2481116950396771014L;
	private String scodePicSrc;
	private String scodePicType;
	private String isQualified;
	private String Def1;
	private String Def2;
	private String Def3;
	public String getScodePicSrc() {
		return scodePicSrc;
	}
	public void setScodePicSrc(String scodePicSrc) {
		this.scodePicSrc = scodePicSrc;
	}
	public String getScodePicType() {
		return scodePicType;
	}
	public void setScodePicType(String scodePicType) {
		this.scodePicType = scodePicType;
	}
	public String getIsQualified() {
		return isQualified;
	}
	public void setIsQualified(String isQualified) {
		this.isQualified = isQualified;
	}
	public String getDef1() {
		return Def1;
	}
	public void setDef1(String def1) {
		Def1 = def1;
	}
	public String getDef2() {
		return Def2;
	}
	public void setDef2(String def2) {
		Def2 = def2;
	}
	public String getDef3() {
		return Def3;
	}
	public void setDef3(String def3) {
		Def3 = def3;
	}
}
