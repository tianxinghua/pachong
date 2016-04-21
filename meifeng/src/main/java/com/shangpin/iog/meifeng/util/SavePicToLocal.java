package com.shangpin.iog.meifeng.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.shangpin.iog.meifeng.dto.SteImg;

public class SavePicToLocal {
	
	public static void main(String[] args) {
		Short[] needColsNo = new Short[]{0,1,2,3,4,5,6,7,8,9};
		String filePath = "E://steImg.xlsx";
		int a= 0;
		List<SteImg> dtoList = new Excel2DTO().toDTO(filePath, 0, needColsNo, SteImg.class);
		for (SteImg steImg : dtoList) {
			System.out.println(a++);
			String[] ingArr = steImg.getIngArr();
			int i = 1;
			for (String img : ingArr) {
				if (StringUtils.isNotBlank(img)) {
					try {
						i++;
						File f = new File("E://steIMgs//"+steImg.getProductModel()+"-"+steImg.getColor()+"_"+i+".jpg");
						if (f.exists()) {
							continue;
						}
						SavePicToLocal.download(img, steImg.getProductModel()+"-"+steImg.getColor()+"_"+i+".jpg", "E://steIMgs");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
    public static void download(String urlString, String filename,String savePath) throws Exception {  
        // 构造URL  
        URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求超时为5s  
        con.setConnectTimeout(5*1000);  
        // 输入流  
        InputStream is = con.getInputStream();  
      
        // 1K的数据缓冲  
        byte[] bs = new byte[1024];  
        // 读取到的数据长度  
        int len;  
        // 输出的文件流  
       File sf=new File(savePath);  
       if(!sf.exists()){  
           sf.mkdirs();  
       }  
       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);  
        // 开始读取  
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链接  
        os.close();  
        is.close();  
    }
}
