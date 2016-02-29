package com.shangpin.iog.biffi.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Photos {
	@XmlElement(name="photo")
	List<String> photo;
	
	
	
	
//	public List<String> getPhotos() {
//        List<String> pics = new ArrayList<>();
//        if (photo != null && photo.size() > 0) {
//            for (Object picObj : photo) {
//                LinkedTreeMap picMap = (LinkedTreeMap)picObj;
//                Object urlObj = picMap.get("$");
//                if (urlObj != null) {
//                    pics.add(urlObj.toString());
//                }
//            }
//        }
//        return pics;
//    }
}
