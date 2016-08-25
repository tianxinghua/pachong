package com.shangpin.iog.optical.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="Workbook",namespace="urn:schemas-microsoft-com:office")
//@XmlAccessorType(XmlAccessType.FIELD)
public class Workbook {
//	@XmlElement(name="Worksheet")
	private Worksheet worksheet;
	@XmlElement(name="OfficeDocumentSettings")
	private OfficeDocumentSettings officeDocumentSettings;
}
