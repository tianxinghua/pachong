package com.shangpin.iog.webcontainer.front.util;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.shangpin.iog.webcontainer.front.dto.Img;
import com.shangpin.iog.webcontainer.front.util.queue.PicQueue;
public class NewSavePic {
	private PicQueue picQueue; 
	private ThreadPoolExecutor executor; 
	public NewSavePic(ThreadPoolExecutor executor) {
		super();
		this.executor = executor;
	}
	
	public NewSavePic(PicQueue picQueue, ThreadPoolExecutor executor) {
		super();
		this.picQueue = picQueue;
		this.executor = executor;
	}

	public String saveImg(String local_picturetem,File file){
		Short[] needColsNo = new Short[]{0,1,2,3,4,5,6,7,8,9};
		String dirPath = local_picturetem+File.separator+new Date().getTime();
		File f1 = new File(dirPath);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		int a = 0;
		List<Img> dtoList = new Excel2DTO().toDTO(file, 0, needColsNo, Img.class);
		for (Img steImg : dtoList) {
			System.out.println("++++"+a+"++++++");
			a++;
			String[] ingArr = steImg.getIngArr();
			int i = 0;
			for (String img : ingArr) {
				if (StringUtils.isNotBlank(img)) {
					try {
						i++;
						File f = new File(dirPath+"/"+steImg.getProductModel().replace("/", " ")+" "+steImg.getColor()+" ("+i+").jpg");
						if (f.exists()) {
							continue;
						}
						executor.execute(new DowmImage(img.trim(),steImg.getProductModel().replace("/", " ")+" "+steImg.getColor()+" ("+i+").jpg",dirPath));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return dirPath;
	}
	public String saveImg(String local_picturetem, File file,PicQueue picQueue){
		Short[] needColsNo = new Short[]{0,1,2,3,4,5,6,7,8,9};
		String dirPath = local_picturetem+File.separator+new Date().getTime();
		File f1 = new File(dirPath);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		int a = 0;
		List<Img> dtoList = new Excel2DTO().toDTO(file, 0, needColsNo, Img.class);
		for (Img steImg : dtoList) {
			System.out.println("++++"+a+"++++++");
			a++;
			String[] ingArr = steImg.getIngArr();
			int i = 0;
			for (String img : ingArr) {
				if (StringUtils.isNotBlank(img)) {
					try {
						i++;
						File f = new File(dirPath+"/"+steImg.getProductModel().replace("/", " ")+" "+steImg.getColor()+" ("+i+").jpg");
						if (f.exists()) {
							continue;
						}
						executor.execute(new DowmImage(img.trim(),steImg.getProductModel().replace("/", " ")+" "+steImg.getColor()+" ("+i+").jpg",dirPath,picQueue));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return dirPath;
	}
	public ThreadPoolExecutor getExecutor() {
		return executor;
	}
	
}
