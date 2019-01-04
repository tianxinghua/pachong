package com.shangpin.iog.optical.dto;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="OfficeDocumentSettings",namespace="urn:schemas-microsoft-com:office:office")
public class OfficeDocumentSettings {
	
	private String AllowPNG;

}
