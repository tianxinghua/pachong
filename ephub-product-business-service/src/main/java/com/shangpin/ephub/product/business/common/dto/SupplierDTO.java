package com.shangpin.ephub.product.business.common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * <p>Title:SupplierDTO </p>
 * <p>Description: 通过请求sop api获取supplierid等信息返回的实体对象</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月22日 下午2:22:50
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierDTO {

	@JsonIgnore
	private String supplierId;
	@JsonIgnore
	private String supplierNo;
	@JsonIgnore
	private String supplierName;
	@JsonIgnore
	private String supplierAlias;
	@JsonIgnore
	private String areaNo;
	@JsonIgnore
	private String companyAddress;
	@JsonIgnore
	private String telCountryCode;
	@JsonIgnore
	private String telAreaCode;
	@JsonIgnore
	private String companyTel;
	@JsonIgnore
	private String faxCountryCode;
	@JsonIgnore
	private String faxAreaCode;
	@JsonIgnore
	private String companyFax;
	@JsonIgnore
	private String companyCode;
	@JsonIgnore
	private String companyUrl;
	@JsonIgnore
	private String connectionUser;
	@JsonIgnore
	private String connectionPhone;
	@JsonIgnore
	private String connectionEmail;
	@JsonIgnore
	private String contactName;
	@JsonIgnore
	private String licenseNo;
	@JsonIgnore
	private String accountLicence;
	@JsonIgnore
	private String taxNo;
	@JsonIgnore
	private String businessCode;
	@JsonIgnore
	private String accountBank;
	@JsonIgnore
	private String account;
	@JsonIgnore
	private String legalRep;
	@JsonIgnore
	private String legalRepCardNo;
	@JsonIgnore
	private String certificateOfIncorporation;
	@JsonIgnore
	private String businessRegistration;
	@JsonIgnore
	private List<?> spSupplierButtJointMapping;
	@JsonIgnore
	private String supplierQualification;
	@JsonIgnore
	private String supplierContract;
	@JsonIgnore
	private String sopUserNo;
	
	@JsonProperty("SupplierId")
	public String getSupplierId() {
		return supplierId;
	}
	@JsonProperty("SupplierId")
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	@JsonProperty("SupplierNo")
	public String getSupplierNo() {
		return supplierNo;
	}
	@JsonProperty("SupplierNo")
	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}
	@JsonProperty("SupplierName")
	public String getSupplierName() {
		return supplierName;
	}
	@JsonProperty("SupplierName")
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	@JsonProperty("SupplierAlias")
	public String getSupplierAlias() {
		return supplierAlias;
	}
	@JsonProperty("SupplierAlias")
	public void setSupplierAlias(String supplierAlias) {
		this.supplierAlias = supplierAlias;
	}
	@JsonProperty("AreaNo")
	public String getAreaNo() {
		return areaNo;
	}
	@JsonProperty("AreaNo")
	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}
	@JsonProperty("CompanyAddress")
	public String getCompanyAddress() {
		return companyAddress;
	}
	@JsonProperty("CompanyAddress")
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	@JsonProperty("TelCountryCode")
	public String getTelCountryCode() {
		return telCountryCode;
	}
	@JsonProperty("TelCountryCode")
	public void setTelCountryCode(String telCountryCode) {
		this.telCountryCode = telCountryCode;
	}
	@JsonProperty("TelAreaCode")
	public String getTelAreaCode() {
		return telAreaCode;
	}
	@JsonProperty("TelAreaCode")
	public void setTelAreaCode(String telAreaCode) {
		this.telAreaCode = telAreaCode;
	}
	@JsonProperty("CompanyTel")
	public String getCompanyTel() {
		return companyTel;
	}
	@JsonProperty("CompanyTel")
	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}
	@JsonProperty("FaxCountryCode")
	public String getFaxCountryCode() {
		return faxCountryCode;
	}
	@JsonProperty("FaxCountryCode")
	public void setFaxCountryCode(String faxCountryCode) {
		this.faxCountryCode = faxCountryCode;
	}
	@JsonProperty("FaxAreaCode")
	public String getFaxAreaCode() {
		return faxAreaCode;
	}
	@JsonProperty("FaxAreaCode")
	public void setFaxAreaCode(String faxAreaCode) {
		this.faxAreaCode = faxAreaCode;
	}
	@JsonProperty("CompanyFax")
	public String getCompanyFax() {
		return companyFax;
	}
	@JsonProperty("CompanyFax")
	public void setCompanyFax(String companyFax) {
		this.companyFax = companyFax;
	}
	@JsonProperty("CompanyCode")
	public String getCompanyCode() {
		return companyCode;
	}
	@JsonProperty("CompanyCode")
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	@JsonProperty("CompanyUrl")
	public String getCompanyUrl() {
		return companyUrl;
	}
	@JsonProperty("CompanyUrl")
	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
	}
	@JsonProperty("ConnectionUser")
	public String getConnectionUser() {
		return connectionUser;
	}
	@JsonProperty("ConnectionUser")
	public void setConnectionUser(String connectionUser) {
		this.connectionUser = connectionUser;
	}
	@JsonProperty("ConnectionPhone")
	public String getConnectionPhone() {
		return connectionPhone;
	}
	@JsonProperty("ConnectionPhone")
	public void setConnectionPhone(String connectionPhone) {
		this.connectionPhone = connectionPhone;
	}
	@JsonProperty("ConnectionEmail")
	public String getConnectionEmail() {
		return connectionEmail;
	}
	@JsonProperty("ConnectionEmail")
	public void setConnectionEmail(String connectionEmail) {
		this.connectionEmail = connectionEmail;
	}
	@JsonProperty("ContactName")
	public String getContactName() {
		return contactName;
	}
	@JsonProperty("ContactName")
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	@JsonProperty("LicenseNo")
	public String getLicenseNo() {
		return licenseNo;
	}
	@JsonProperty("LicenseNo")
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	@JsonProperty("AccountLicence")
	public String getAccountLicence() {
		return accountLicence;
	}
	@JsonProperty("AccountLicence")
	public void setAccountLicence(String accountLicence) {
		this.accountLicence = accountLicence;
	}
	@JsonProperty("TaxNo")
	public String getTaxNo() {
		return taxNo;
	}
	@JsonProperty("TaxNo")
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	@JsonProperty("BusinessCode")
	public String getBusinessCode() {
		return businessCode;
	}
	@JsonProperty("BusinessCode")
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	@JsonProperty("AccountBank")
	public String getAccountBank() {
		return accountBank;
	}
	@JsonProperty("AccountBank")
	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
	@JsonProperty("Account")
	public String getAccount() {
		return account;
	}
	@JsonProperty("Account")
	public void setAccount(String account) {
		this.account = account;
	}
	@JsonProperty("LegalRep")
	public String getLegalRep() {
		return legalRep;
	}
	@JsonProperty("LegalRep")
	public void setLegalRep(String legalRep) {
		this.legalRep = legalRep;
	}
	@JsonProperty("LegalRepCardNo")
	public String getLegalRepCardNo() {
		return legalRepCardNo;
	}
	@JsonProperty("LegalRepCardNo")
	public void setLegalRepCardNo(String legalRepCardNo) {
		this.legalRepCardNo = legalRepCardNo;
	}
	@JsonProperty("CertificateOfIncorporation")
	public String getCertificateOfIncorporation() {
		return certificateOfIncorporation;
	}
	@JsonProperty("CertificateOfIncorporation")
	public void setCertificateOfIncorporation(String certificateOfIncorporation) {
		this.certificateOfIncorporation = certificateOfIncorporation;
	}
	@JsonProperty("BusinessRegistration")
	public String getBusinessRegistration() {
		return businessRegistration;
	}
	@JsonProperty("BusinessRegistration")
	public void setBusinessRegistration(String businessRegistration) {
		this.businessRegistration = businessRegistration;
	}
	@JsonProperty("SpSupplierButtJointMapping")
	public List<?> getSpSupplierButtJointMapping() {
		return spSupplierButtJointMapping;
	}
	@JsonProperty("SpSupplierButtJointMapping")
	public void setSpSupplierButtJointMapping(List<?> spSupplierButtJointMapping) {
		this.spSupplierButtJointMapping = spSupplierButtJointMapping;
	}
	@JsonProperty("SupplierQualification")
	public String getSupplierQualification() {
		return supplierQualification;
	}
	@JsonProperty("SupplierQualification")
	public void setSupplierQualification(String supplierQualification) {
		this.supplierQualification = supplierQualification;
	}
	@JsonProperty("Contract")
	public String getSupplierContract() {
		return supplierContract;
	}
	@JsonProperty("Contract")
	public void setSupplierContract(String supplierContract) {
		this.supplierContract = supplierContract;
	}
	@JsonProperty("SopUserNo")
	public String getSopUserNo() {
		return sopUserNo;
	}
	@JsonProperty("SopUserNo")
	public void setSopUserNo(String sopUserNo) {
		this.sopUserNo = sopUserNo;
	}
	
	
    
}
