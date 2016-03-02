package com.shangpin.iog.meifeng.util;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.shangpin.iog.meifeng.dto.SteImg;

public class SavePic {
	public static void main(String[] args) {
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
			    2, 10, 300, TimeUnit.MILLISECONDS,
			    new ArrayBlockingQueue<Runnable>(3),
			    new ThreadPoolExecutor.CallerRunsPolicy()
			  );
		Short[] needColsNo = new Short[]{0,1,2,3,4,5,6,7,8,9};
		String filePath = "E://steImg.xlsx";
		int a = 0;
		List<SteImg> dtoList = new Excel2DTO().toDTO(filePath, 0, needColsNo, SteImg.class);
		for (SteImg steImg : dtoList) {
			System.out.println("++++"+a+"++++++");
			a++;
			String[] ingArr = steImg.getIngArr();
			int i = 0;
			for (String img : ingArr) {
				if (StringUtils.isNotBlank(img)) {
					try {
						i++;
						File f = new File("E://steIMgs33333//"+steImg.getProductModel()+"-"+steImg.getColor()+"_"+i+".jpg");
						if (f.exists()) {
							continue;
						}
						executor.execute(new Test(img,steImg.getProductModel()+"-"+steImg.getColor()+"_"+i+".jpg","E://steIMgs33333"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
