package com.shangpin.spider.gather.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author njt
 * @date 创建时间：2017年12月8日 下午3:14:08
 * @version 1.0
 * @parameter
 */

public class KillChrome {
	private static Logger log = LoggerFactory.getLogger(KillChrome.class);
	
	public static void main(String[] args) {
		killChrome("chrome.exe");
	}
	
	public static void killChrome(String task) {
		String pid = null;
		// String linuxOrder = "ps -ef|grep phantomjs.exe";
		String cmd = "tasklist /FI \"IMAGENAME eq chrome.exe\"";
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.indexOf("chrome.exe") != -1) {
					line = line.replaceAll(" ", "");
					line = line.replace(task, "");
					pid = line.substring(0, line.indexOf("Console"));
				}
				log.info("chrome进程ID为:" + pid);
				Runtime.getRuntime().exec("taskkill /F /PID " + pid);
				log.info("杀死进程!");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			log.info("杀死chrome进程有误!" + e.getMessage());
		}
	}

}
