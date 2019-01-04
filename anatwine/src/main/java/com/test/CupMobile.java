package com.test;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class CupMobile {
 @XmlAttribute
 private String application="UPNoCard";
 @XmlAttribute
 private String version="1.01";
 private Transaction transaction;
public String getApplication() {
	return application;
}
public void setApplication(String application) {
	this.application = application;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public Transaction getTransaction() {
	return transaction;
}
public void setTransaction(Transaction transaction) {
	this.transaction = transaction;
}
}
