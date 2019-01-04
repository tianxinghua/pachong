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
public class CSVUtilsMergerData {

    private static Logger logger = Logger.getLogger("info");

    static String splitSign = ",";

    private static OutputStreamWriter out= null;


    /**
     * 匹配规则 productMode_size  （货号_尺码） 相同的数据视为 同一数据 把中文的中相应信息合并到外文 记录中
     * @param cnFilePath 中文csv 存放路径
     * @param otherLanguageFilePath  其他语言 csv 文件存放路径
     */
    public static void mergeDataAndExport(String cnFilePath ,String otherLanguageFilePath,String destFilePath) throws Exception {

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

        //读取商品数据信息
        List<TemplateProductDTO> cnDTOS = DownloadAndReadCSV.readLocalCSV(cnFilePath, TemplateProductDTO.class,",","gbk");

        List<TemplateProductDTO> otherSkuNoDTOS = DownloadAndReadCSV.readLocalCSV(otherLanguageFilePath, TemplateProductDTO.class,",","gbk");

        Map<String,TemplateProductDTO> cnResultMap = getMapByTemplateProductDTOList(cnDTOS);

        int otherSize = otherSkuNoDTOS.size();
        System.out.println("========起始数据 otherSize:"+otherSize);
        logger.info("========起始数据 otherSize:"+otherSize);

        for (int i = 0; i < otherSize; i++) {
            TemplateProductDTO otherDto = otherSkuNoDTOS.get(i);
            String supplierSkuNo = otherDto.getSupplierSkuNo();
            //处理 map key生成
            if(supplierSkuNo==null||"".equals(supplierSkuNo)){
                supplierSkuNo = otherDto.getProductModelCode()+"_"+otherDto.getSize();
            }
            if(cnResultMap.containsKey(supplierSkuNo)){
                TemplateProductDTO cnDTO = cnResultMap.get(supplierSkuNo);
                TemplateProductDTO finalDto = mergeData(otherDto,cnDTO);
                out.write(finalDto.getCSVString());
            }else{
                out.write(otherDto.getCSVString());
            }
        }
        out.flush();
        out.close();
        out = null;
        System.out.println("========处理数据结束 ============");
        logger.info("========处理数据结束 ============");
    }

    /**
     * 合并中文 和 外文 数据
     * @param otherDto
     * @param cnDTO
     * @return
     */
    private static TemplateProductDTO mergeData(TemplateProductDTO otherDto, TemplateProductDTO cnDTO) {

        otherDto.setCategory(cnDTO.getCategory());
        otherDto.setSeason((cnDTO.getSeason()==null||"".equals(cnDTO.getSeason()))?otherDto.getSeason():cnDTO.getSeason());
        otherDto.setMaterial((cnDTO.getMaterial()==null||"".equals(cnDTO.getMaterial()))?otherDto.getMaterial():cnDTO.getMaterial());
        otherDto.setColorName((cnDTO.getColorName()==null||"".equals(cnDTO.getColorName()))?otherDto.getColorName():cnDTO.getColorName());

        otherDto.setProductName((cnDTO.getProductName()==null||"".equals(cnDTO.getProductName()))?otherDto.getProductName():cnDTO.getProductName());
        otherDto.setItemsaleprice((cnDTO.getItemsaleprice()==null||"".equals(cnDTO.getItemsaleprice()))?otherDto.getItemsaleprice():cnDTO.getItemsaleprice());
        otherDto.setDescript((cnDTO.getDescript()==null||"".equals(cnDTO.getDescript()))?otherDto.getDescript():cnDTO.getDescript());

        otherDto.setSizeDesc((cnDTO.getSizeDesc()==null||"".equals(cnDTO.getSizeDesc()))?otherDto.getSizeDesc():cnDTO.getSizeDesc());

        return otherDto;
    }

    /**
     * 将 模板 dto 转换为 key value map  key：supplierSKuNo(如果为空那么 就以 productModel_size 组合 )  value TemplateProductDTO
     * @param cnSkuNoDTOS
     * @return
     */
    private static Map<String, TemplateProductDTO> getMapByTemplateProductDTOList(List<TemplateProductDTO> cnSkuNoDTOS) {
        if(cnSkuNoDTOS==null||cnSkuNoDTOS.size()==0){
            return null;
        }
        Map<String, TemplateProductDTO> resultMap = new HashMap<>();
        int size = cnSkuNoDTOS.size();
        for (int i = 0; i <size ; i++) {
            TemplateProductDTO templateProductDTO = cnSkuNoDTOS.get(i);
            String supplierSkuNo = templateProductDTO.getSupplierSkuNo();
            if(supplierSkuNo==null||"".equals(supplierSkuNo)){
                supplierSkuNo = templateProductDTO.getProductModelCode()+"_"+templateProductDTO.getSize();
            }
            resultMap.put(supplierSkuNo,templateProductDTO);
        }
        return resultMap;
    }



}
