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
public class SavePic {
//	ThreadPoolExecutor executor =new ThreadPoolExecutor(10, 100,60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
									2, 30, 300, TimeUnit.MILLISECONDS,
									new ArrayBlockingQueue<Runnable>(3),
									new ThreadPoolExecutor.CallerRunsPolicy());
	public String saveImg(File file){
		Short[] needColsNo = new Short[]{0,1,2,3,4,5,6,7,8,9};
		//TODO 暂时保存  /usr/local/picturetem
		String dirPath = "/usr/local/picturetem/"+new Date().getTime();
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
	public ThreadPoolExecutor getExecutor() {
		return executor;
	}
}
