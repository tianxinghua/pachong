package com.shangpin.iog.revolve;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.revolve.stock.dto.ProductDTO;
import com.shangpin.iog.revolve.stock.schedule.AppContext;
import com.shangpin.iog.revolve.stock.util1.CVSUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component("revolveStock")
public class StockClientImp extends AbsUpdateProductStock
{
  private static Logger logger = Logger.getLogger("info");
  private static ApplicationContext factory;
  private static ResourceBundle bdl = null;
  private static String supplierId;
  private static String url;
  private static String filepath;

  private static void loadSpringContext()
  {
    factory = new AnnotationConfigApplicationContext(new Class[] { AppContext.class });
  }

  @SuppressWarnings("unchecked")
public Map<String, String> grabStock(Collection<String> skuNo)
    throws ServiceException, Exception
  {
    String skuId = "";
    Map skustock = new HashMap();
    Map skuData = new HashMap();

    logger.info("===============开始下载库存文件==============");
    OutTimeConfig outTimeConf = new OutTimeConfig(600000, 3600000, 3600000);
    String data = HttpUtil45.get(url, outTimeConf, null);
    int loop = 0;
    while ((HttpUtil45.errorResult.equals(data)) && (loop < 100)) {
      logger.info("===============" + loop + "==============");
      Thread.sleep(3000L);
      data = HttpUtil45.get(url, outTimeConf, null);
      ++loop;
    }

    String path = save("products.txt", data);

    List<ProductDTO> items = CVSUtil.readCSV(path, ProductDTO.class, '\t');
    logger.info("csv转换items.size==========" + items.size());
    for (ProductDTO item : items) {
      try {
    	  String reg = "^\\d+$";
    	  System.out.println(item.getId()+"==>"+item.getSellableqty());
    	  if(item.getSellableqty()!=null&&item.getSellableqty().matches(reg)){
    		  
    		  int stock = Integer.parseInt(item.getSellableqty());
    		  if(stock<3){
    			
    			  stock = 0;
    		  }
    		  skuData.put(item.getId(),stock+"");
    	  }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    Iterator it = skuNo.iterator();

    while (it.hasNext()) {
      skuId = (String)it.next();
      if (skuData.containsKey(skuId)) {
        skustock.put(skuId, skuData.get(skuId));
      }else{
    	  skustock.put(skuId, "0");
      }
    }

    return skustock;
  }

  public String save(String name, String data) {
    String path = filepath + File.separator + name;
    try {
      File file = new File(path);

      if (!(file.exists())) {
        try {
          file.getParentFile().mkdirs();
          file.createNewFile();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
      FileWriter fwriter = null;
      try {
        fwriter = new FileWriter(filepath + File.separator + name);
        fwriter.write(data);
      } catch (IOException ex) {
        ex.printStackTrace();
      } finally {
        try {
          fwriter.flush();
          fwriter.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return path;
  }

  static
  {
    if (null == bdl)
      bdl = ResourceBundle.getBundle("conf");
    supplierId = bdl.getString("supplierId");
    url = bdl.getString("url");
    filepath = bdl.getString("filepath");
  }
}