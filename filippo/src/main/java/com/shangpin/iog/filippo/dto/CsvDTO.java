package com.shangpin.iog.filippo.dto;

public class CsvDTO {
	private String ART_ID;
	private String VAR_ID;
	private String ART_FID;
	private String STG;
	private String BND_ID;
	private String BND_NAME;
	private String ART;
	private String ART_VAR;
	private String ART_FAB;
	private String ART_COL;
	private String SR_ID;
	private String SR_DES;
	private String GRP_ID;
	private String GRP_DES;
	private String SUB_GRP_ID;
	private String SUB_GRP_DES;
	private String ART_DES;
	private String TG_ID;
	private String COL_ID;
	private String COL_DES;
	private String REF;
//	private String VSO;
	private String EUR;
	private String TG;
	private String QTY;
	private String MADEIN;
	private String WV;
	private String COMP;
	private String IMGTYP;
	private String IMG;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((TG == null) ? 0 : TG.hashCode());
		result = prime * result + ((VAR_ID == null) ? 0 : VAR_ID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CsvDTO other = (CsvDTO) obj;
		if (TG == null) {
			if (other.TG != null)
				return false;
		} else if (!TG.equals(other.TG))
			return false;
		if (VAR_ID == null) {
			if (other.VAR_ID != null)
				return false;
		} else if (!VAR_ID.equals(other.VAR_ID))
			return false;
		return true;
	}
	public String getART_ID() {
		return ART_ID;
	}
	public void setART_ID(String aRT_ID) {
		ART_ID = aRT_ID;
	}
	public String getVAR_ID() {
		return VAR_ID;
	}
	public void setVAR_ID(String vAR_ID) {
		VAR_ID = vAR_ID;
	}
	public String getART_FID() {
		return ART_FID;
	}
	public void setART_FID(String aRT_FID) {
		ART_FID = aRT_FID;
	}
	public String getSTG() {
		return STG;
	}
	public void setSTG(String sTG) {
		STG = sTG;
	}
	public String getBND_ID() {
		return BND_ID;
	}
	public void setBND_ID(String bND_ID) {
		BND_ID = bND_ID;
	}
	public String getBND_NAME() {
		return BND_NAME;
	}
	public void setBND_NAME(String bND_NAME) {
		BND_NAME = bND_NAME;
	}
	public String getART() {
		return ART;
	}
	public void setART(String aRT) {
		ART = aRT;
	}
	public String getART_VAR() {
		return ART_VAR;
	}
	public void setART_VAR(String aRT_VAR) {
		ART_VAR = aRT_VAR;
	}
	public String getART_FAB() {
		return ART_FAB;
	}
	public void setART_FAB(String aRT_FAB) {
		ART_FAB = aRT_FAB;
	}
	public String getART_COL() {
		return ART_COL;
	}
	public void setART_COL(String aRT_COL) {
		ART_COL = aRT_COL;
	}
	public String getSR_ID() {
		return SR_ID;
	}
	public void setSR_ID(String sR_ID) {
		SR_ID = sR_ID;
	}
	public String getSR_DES() {
		return SR_DES;
	}
	public void setSR_DES(String sR_DES) {
		SR_DES = sR_DES;
	}
	public String getGRP_ID() {
		return GRP_ID;
	}
	public void setGRP_ID(String gRP_ID) {
		GRP_ID = gRP_ID;
	}
	public String getGRP_DES() {
		return GRP_DES;
	}
	public void setGRP_DES(String gRP_DES) {
		GRP_DES = gRP_DES;
	}
	public String getSUB_GRP_ID() {
		return SUB_GRP_ID;
	}
	public void setSUB_GRP_ID(String sUB_GRP_ID) {
		SUB_GRP_ID = sUB_GRP_ID;
	}
	public String getSUB_GRP_DES() {
		return SUB_GRP_DES;
	}
	public void setSUB_GRP_DES(String sUB_GRP_DES) {
		SUB_GRP_DES = sUB_GRP_DES;
	}
	public String getART_DES() {
		return ART_DES;
	}
	public void setART_DES(String aRT_DES) {
		ART_DES = aRT_DES;
	}
	public String getTG_ID() {
		return TG_ID;
	}
	public void setTG_ID(String tG_ID) {
		TG_ID = tG_ID;
	}
	public String getCOL_ID() {
		return COL_ID;
	}
	public void setCOL_ID(String cOL_ID) {
		COL_ID = cOL_ID;
	}
	public String getCOL_DES() {
		return COL_DES;
	}
	public void setCOL_DES(String cOL_DES) {
		COL_DES = cOL_DES;
	}
	public String getREF() {
		return REF;
	}
	public void setREF(String rEF) {
		REF = rEF;
	}
	public String getEUR() {
		return EUR;
	}
	public void setEUR(String eUR) {
		EUR = eUR;
	}
	public String getTG() {
		return TG;
	}
	public void setTG(String tG) {
		TG = tG;
	}
	public String getQTY() {
		return QTY;
	}
	public void setQTY(String qTY) {
		QTY = qTY;
	}
	public String getMADEIN() {
		return MADEIN;
	}
	public void setMADEIN(String mADEIN) {
		MADEIN = mADEIN;
	}
	public String getWV() {
		return WV;
	}
	public void setWV(String wV) {
		WV = wV;
	}
	public String getCOMP() {
		return COMP;
	}
	public void setCOMP(String cOMP) {
		COMP = cOMP;
	}
	public String getIMGTYP() {
		return IMGTYP;
	}
	public void setIMGTYP(String iMGTYP) {
		IMGTYP = iMGTYP;
	}
	public String getIMG() {
		return IMG;
	}
	public void setIMG(String iMG) {
		IMG = iMG;
	}
//	public String getVSO() {
//		return VSO;
//	}
//	public void setVSO(String vSO) {
//		VSO = vSO;
//	}
	
}
