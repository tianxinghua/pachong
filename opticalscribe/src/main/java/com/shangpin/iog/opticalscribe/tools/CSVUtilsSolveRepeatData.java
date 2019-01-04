package com.shangpin.iog.opticalscribe.tools;

import com.shangpin.iog.opticalscribe.dto.TemplateProductDTO;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 15:48 2018/7/5
 * @Description:
 */
public class CSVUtilsSolveRepeatData {

    private static Logger logger = Logger.getLogger("info");

    static String splitSign = ",";

    private static OutputStreamWriter out= null;


    /**
     * 文件存放路径
     * @param filePath
     */
    public static void solveFinalProductData(String filePath,String destFilePath) throws Exception {
        //读取商品数据信息
        List<TemplateProductDTO> spSkuNoDTOS = DownloadAndReadCSV.readLocalCSV(filePath, TemplateProductDTO.class,",","gbk");

        int orginalSize = spSkuNoDTOS.size();
        System.out.println("========起始数据 size:"+orginalSize);
        logger.info("========起始数据 size:"+orginalSize);

        //商品集合Map<String,CSVDTO>
        Map<String,TemplateProductDTO> scvDTOMap = new HashMap<>();
        int size = spSkuNoDTOS.size();
        for (int i = 0; i < size; i++) {
            TemplateProductDTO csvdto = spSkuNoDTOS.get(i);
            String mapKey = csvdto.getProductModelCode()+"-"+csvdto.getSize();
            if(scvDTOMap.containsKey(mapKey)){
                TemplateProductDTO mapCsvdto = scvDTOMap.get(mapKey);
                String mapSex = mapCsvdto.getSex();
                if("women".equals(mapSex)){
                    System.out.println("=============map 中已有该women productModel 跳过=========productModel:"+mapKey);
                    logger.info("=============map 中已有该women productModel 跳过=========");
                    continue;
                }else{
                    System.out.println("替换男士："+csvdto.toString());
                    logger.info("替换男士 women："+csvdto.toString());
                    TemplateProductDTO menCsvDTO = scvDTOMap.put(mapKey, csvdto);
                    System.out.println("男士 men："+menCsvDTO.toString());
                    logger.info("男士 men："+menCsvDTO.toString());
                }
            }else{
                scvDTOMap.put(mapKey,csvdto);
            }
        }

        //开始导出数据
        try {
            out = new OutputStreamWriter(new FileOutputStream(destFilePath, true),"gb2312");
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer(
                        "gender" + splitSign +
                        "brand" + splitSign +

                        "category" + splitSign +
                        "SPU" + splitSign +

                        "productModel" + splitSign +
                        "season" + splitSign +

                        "material"+ splitSign +
                        "color"+ splitSign +

                        "size" + splitSign +
                        "proName" + splitSign +

                        "国外市场价" + splitSign +
                        "国内市场价" + splitSign +

                        "qty" + splitSign +
                        "qtyDesc" + splitSign +

                        "made" + splitSign +

                        "desc" + splitSign +
                        "pics" + splitSign +

                        "detailLink" + splitSign +
                        "measurement" + splitSign+

                        "supplierId" + splitSign+
                        "supplierNo" + splitSign+
                        "supplierSkuNo" + splitSign
        ).append("\r\n");
        out.write(buffer.toString());
        //遍历 scvDTOMap 导出数据
        int finalSize = scvDTOMap.size();
        Set<Map.Entry<String, TemplateProductDTO>> entrySet = scvDTOMap.entrySet();
        for (Map.Entry<String, TemplateProductDTO> entry : entrySet) {
            //导出数据
            exportExcel(entry.getValue());
        }
        System.out.println("========处理数据结束 ============");
        logger.info("========处理数据结束 ============");
    }

    private static void exportExcel(TemplateProductDTO dto) {
        //继续追加
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append(dto.getSex()==null?"":dto.getSex()).append(splitSign);
            buffer.append(dto.getBrand()==null?"":dto.getBrand()).append(splitSign);

            buffer.append(dto.getCategory()==null?"":dto.getCategory()).append(splitSign);
            buffer.append(dto.getSpuNo()==null?"":dto.getSpuNo()).append(splitSign);

            buffer.append(dto.getProductModelCode()==null?"":dto.getProductModelCode()).append(splitSign);
            buffer.append(dto.getSeason()==null?"":dto.getSeason()).append(splitSign);

            buffer.append(dto.getMaterial()==null?"":dto.getMaterial()).append(splitSign);
            buffer.append(dto.getColorName()==null?"":dto.getColorName()).append(splitSign);

            buffer.append(dto.getSize()==null?"":dto.getSize()).append(splitSign);
            buffer.append(dto.getProductName()==null?"":dto.getProductName()).append(splitSign);

            buffer.append(dto.getItemprice()==null?"":dto.getItemprice()).append(splitSign);
            buffer.append(dto.getItemsaleprice()==null?"":dto.getItemsaleprice()).append(splitSign);

            buffer.append(dto.getQty()==null?"":dto.getQty()).append(splitSign);
            buffer.append(dto.getQtyDesc()==null?"":dto.getQtyDesc()).append(splitSign);


            buffer.append(dto.getMade()==null?"":dto.getMade()).append(splitSign);

            buffer.append(dto.getDescript()==null?"":dto.getDescript()).append(splitSign);
            buffer.append(dto.getPicUrls()==null?"":dto.getPicUrls()).append(splitSign);

            buffer.append(dto.getUrl()==null?"":dto.getUrl()).append(splitSign);
            buffer.append(dto.getSizeDesc()==null?"":dto.getSizeDesc()).append(splitSign);

            buffer.append(dto.getSupplierId()==null?"":dto.getSupplierId()).append(splitSign);
            buffer.append(dto.getSupplierNo()==null?"":dto.getSupplierNo()).append(splitSign);
            buffer.append(dto.getSupplierSkuNo()==null?"":dto.getSupplierSkuNo());

            buffer.append("\r\n");
            out.write(buffer.toString());
            System.out.println(buffer.toString());
            logger.info(buffer.toString());
            out.flush();
        } catch (Exception e) {
        }
    }

}
