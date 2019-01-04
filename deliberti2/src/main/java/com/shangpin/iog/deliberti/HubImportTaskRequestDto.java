package com.shangpin.iog.deliberti;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubImportTaskRequestDto implements Serializable {
	private static final long serialVersionUID = -9080013205013160520L;
    /**
     * 原始文件名
     */
    private String fileName;
    private String createUser;
    private byte [] uploadfile;

}