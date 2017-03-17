package com.shangpin.iog.pozzilei.stock.dto;

import java.util.List;

public class SeasoncodeList {
	private Integer number_of_seasoncodes;
	private List<Seasoncode> SeasonCode;
	private String result;

	public void setNumber_of_seasoncodes(Integer number_of_seasoncodes) {
		this.number_of_seasoncodes = number_of_seasoncodes;
	}

	public void setSeasonCode(List<Seasoncode> SeasonCode) {
		this.SeasonCode = SeasonCode;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getNumber_of_seasoncodes() {
		return this.number_of_seasoncodes;
	}

	public List<Seasoncode> getSeasonCode() {
		return this.SeasonCode;
	}

	public String getResult() {
		return this.result;
	}
}