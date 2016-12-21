package com.shangpin.ephub.product.business.ui.task.spuimport.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.response.HubResponse;

@Service
public class FTPClientUtil {

	private static FTPClient ftp;

	/**
	 * 
	 * @param path
	 *            上传到ftp服务器哪个路径下
	 * @param addr
	 *            地址
	 * @param port
	 *            端口号
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception
	 */
	private boolean connect(String host, int port, String username, String password) throws Exception {
		boolean result = false;
		ftp = new FTPClient();
		int reply;
		ftp.connect(host, port);
//		ftp.login(username, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			return result;
		}
		result = true;
		return result;
	}

	/**
	 * 
	 * @param file
	 *            上传的文件或文件夹
	 * @throws Exception
	 */
	public static HubResponse uploadFile(byte[] data, String path, String fileName) throws Exception {
		InputStream sbs = new ByteArrayInputStream(data);
//		InputStream sbs = new FileInputStream(new File("C://new.txt"));
//		HubResponse flag = checkFileTemplet(sbs);
//		if(flag.getCode().equals("0")){
		ftp.changeWorkingDirectory(path);
		System.out.println(ftp.getStatus());
		ftp.storeFile(fileName, sbs);
		ftp.quit();
//		}
		// BufferedOutputStream bos = null;
		// FileOutputStream fos = null;
			// File file = new File(fileName);
			// fos = new FileOutputStream(file);
			// bos = new BufferedOutputStream(fos);
			// bos.write(data);
			// bos.flush();
			// bos.close();
			// FileInputStream input = new FileInputStream(file);
			// ftp.storeFile(file.getName(), input);
			// input.close();
		
		return HubResponse.successResp(null);
	}
	private static HubResponse checkFileTemplet(InputStream sbs) throws Exception{
		
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(sbs);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		XSSFRow  xssfRow = xssfSheet.getRow(0);
		if (xssfRow == null) {
			return HubResponse.errorResp("文件内容为空");
		}
		
		Map<Integer,String> map = new HashMap<Integer,String>();
		map.put(0,"品类名称");
		map.put(1,"品类编号*");
		map.put(2,"品牌编号*");
		map.put(3,"品牌名称");
		map.put(4,"货号*");
		map.put(5,"适应性别*");
		map.put(6,"颜色*");
		map.put(7,"原尺码类型");
		map.put(8,"原尺码值");
		map.put(9,"材质*");
		map.put(10,"产地*");
		map.put(11,"市场价");
		map.put(12,"市场价币种");
		
		boolean flag = true;
		for(int i=0;i<map.size();i++){
			if (xssfRow.getCell(i) != null) {
				String fieldName = xssfRow.getCell(i).toString();
				if(!map.get(i).equals(fieldName)){
					flag = false;
				}
			}
		}
		if(flag){
			return HubResponse.successResp(null);
		}else{
			return HubResponse.errorResp("导入文件格式与模板不一致，请下载标准模板");			
		}
	}
	public static InputStream downFile(String remotePath) throws Exception{
		
    	InputStream in = null;
    	in = new FileInputStream(new File("C:\\ftp\\e.txt"));
		if(null != ftp){
	        if(StringUtils.isNotBlank(remotePath)){
	        	in = ftp.retrieveFileStream(remotePath);
	        	ftp.quit();
	        }
		}
		return in;
	}
	public static void main(String[] args) throws Exception {
		 FTPClientUtil t = new FTPClientUtil();
		 t.connect("192.168.3.189", 21, "moon", "123456");
		 t.downFile("/ftpDown/aa.txt");
	}
}