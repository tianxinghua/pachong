package com.shangpin.iog.coltorti.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Customer {
	private String name;
	private String surname;
	private String address;
	private String zip;
	private String city;
	private String state;
	private String country;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Customer(String name, String surname, String address, String zip,
			String city, String state, String country) {
		super();
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.zip = zip;
		this.city = city;
		this.state = state;
		this.country = country;
	}
	public Customer() {
		super();
	}
	
}
