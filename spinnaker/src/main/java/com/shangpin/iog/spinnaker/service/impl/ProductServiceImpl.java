package com.shangpin.iog.spinnaker.service.impl;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.InVoke;
import com.shangpin.iog.common.utils.excel.AccountsExcelTemplate;
import com.shangpin.iog.dto.SpinnakerProductDTO;
import com.shangpin.iog.service.ProductService;
import com.shangpin.iog.spinnaker.dao.ProductsMapper;
import com.shangpin.iog.spinnaker.domain.Product;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by loyalty on 15/5/20.
 */
@Service
public class ProductServiceImpl implements ProductService {
    protected  final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductsMapper productsDAO;


    private static Map<String,String>  seasonMap = new HashMap<String,String>(){
        {
            put("000", "2005秋冬");
            put("001", "2006春夏");
            put("002", "2006秋冬");
            put("003", "2007春夏");
            put("003.", "2007秋冬");
            put("004", "2008春夏");
            put("005", "2008秋冬");
            put("006", "2009春夏");
            put("007", "2009秋冬");
            put("008", "2010春夏");
            put("009", "2010秋冬");
            put("010", "2011春夏");


            put("067VELA", "2011秋冬");
            put("070", "2012春夏");
            put("071", "2012秋冬");
            put("072", "2013春夏");
            put("073", "2013秋冬");
            put("074", "2014春夏");
            put("075", "2014秋冬");
            put("076", "2015春夏");
            put("077", "2015秋冬");

        }
    };

    private static Map<String,String>  brandMap = new HashMap<String,String>(){
        {



            put("BERNIE MEV", "");
            put("EQUIPMENT", "");
            put("LEO", "");
            put("MEXICANA", "");
            put("MEZCALERO", "");
            put("P.A.R.O.S.H.", "");
            put("PHILIPPE MODEL", "");
            put("PIAZZA SEMPIONE", "");
            put("PINK MEMORIES", "");
            put("REED KRAKOFF", "");
            put("SEBAGO", "");
            put("SIMONETTA RAVIZZA", "");
            put("TOMS", "");
            put("UNFLEUR", "");
            put("V 73", "");
            put("7 FOR ALL MANKIND", "B0981");
            put("ADRIANO GOLDSCHMIED", "B1347");
            put("ALEXANDER MCQUEEN", "B0287");
            put("BALLY", "B0109");
            put("BOTTEGA VENETA", "B0010");
            put("BRUNELLO CUCINELLI", "B0754");
            put("BURBERRY", "B0005");
            put("CAR SHOE", "B0648");
            put("CASTANER", "B1113");
            put("CHRISTOPHER KANE", "");
            put("CITIZENS OF HUMANITY", "B1348");
            put("DAVID LERNER", "");
            put("DOLCE GABBANA", "B0004");
            put("DOLCE&GABBANA abbigliamento", "B0004");
            put("DONDUP", "B1349");
            put("DR MARTENS", "");
            put("EMILIO PUCCI", "B0381");
            put("VANESSA BRUNO", "B0317");
            put("VALENTINO GARAVANI", "B0144");
            put("VALENTINO ABBIGLIAMENTO", "B0144");
            put("UGG", "B0152");
            put("TOD`S", "B0316");
            put("STUART WEITZMAN", "B0514");
            put("SERGIO ROSSI", "B0202");
            put("SALVATORE FERRAGAMO", "B0084");
            put("SAINT LAURENT", "B0041");
            put("RL POLO JEANS", "B0269");
            put("RL BLACK LABEL", "B0269");
            put("RENE CAOVILLA", "B0543");
            put("RALPH LAUREN", "B0070");
            put("PRADA ABBIGLIAMENTO", "B0002");
            put("PINKO", "B0143");
            put("MONCLER GAMME ROUGE", "B0334");
            put("MONCLER", "B0334");
            put("MIU MIU ABBIGLIAMENTO", "B0007");
            put("MICHAEL KORS", "B0520");
            put("MELISSA", "B0789");
            put("LONGCHAMP", "B0318");
            put("LOEWE", "B0141");
            put("LANVIN", "B0059");
            put("ERMANNO SCERVINO", "B1002");
            put("FENDI", "B0009");
            put("FENDI ABBIGLIAMENTO", "B0009");
            put("GIUSEPPE ZANOTTI DESIGN", "B0544");
            put("GIVENCHY", "B0200");
            put("GUCCI", "B0003");
            put("J BRAND", "B0804");
            put("J BRAND-SIMONE ROCHA", "B0804");
            put("JACOB COHEN", "B0573");
            put("JAMIN PUECH", "B0621");
            put("JEFFREY CAMPBELL", "B1474");
            put("JIMMY CHOO", "B0061");
            put("GOLDEN GOOSE", "");
            put("HERNO", "");
            put("HUNTER", "");
            put("JAMES PERSE", "");



        }
    };




    @Override
    public void fetchProduct() throws ServiceException {




    }
    @Override
    public Page<SpinnakerProductDTO> findProduct(String category,Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {




        List<SpinnakerProductDTO> productList = null;
        Page<SpinnakerProductDTO> page  =  null;
        try {
            if(null!=pageIndex&&null!=pageSize){
                page = new Page<>(pageIndex,pageSize);
                productList = productsDAO.findListByCategoryAndLastDate(category,startDate,endDate, new RowBounds(pageIndex, pageSize));


            }else{
                productList = productsDAO.findListByCategoryAndLastDate(category,startDate,endDate);
                page = new Page<>(1,productList.size());
            }

            for(SpinnakerProductDTO dto :productList){




            }
            page.setItems(productList);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
            throw new ServiceMessageException("数据导出失败");
        }

        return page;
    }

    @Override
    public AccountsExcelTemplate exportProduct(String templatePath ,String category,Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {

        try {
            AccountsExcelTemplate template = AccountsExcelTemplate.newInstance(templatePath);

            Page<SpinnakerProductDTO> page = this.findProduct(category, startDate, endDate, pageIndex, pageSize);
            int count =0;
            for(SpinnakerProductDTO dto:page.getItems()){
//                template.setCellStyle("STYLE_1");
                template.createRow(count);
                template.createCell(dto.getCategory());//商品品类
                template.createCell("");//商品品类编号
                template.createCell("");// 品牌编号
                template.createCell(dto.getProducerId());// 品牌名称
                template.createCell("");//货号
                template.createCell(dto.getItemId());//供应商sku

                template.createCell(dto.getProductName());//商品名称
                template.createCell(dto.getBarcode());//条形码
                template.createCell(dto.getColor());//颜色
                template.createCell("");//尺码
                template.createCell("");//材质
                template.createCell("");//产地
                template.createCell(dto.getUrl());//图片
                dto.setItemPic();
                template.createCell(dto.getItemPictureUrl1());
                template.createCell(dto.getItemPictureUrl2());
                template.createCell(dto.getItemPictureUrl3());
                template.createCell(dto.getItemPictureUrl4());
                template.createCell(dto.getItemPictureUrl5());
                template.createCell(dto.getItemPictureUrl6());
                template.createCell(dto.getItemPictureUrl7());
                template.createCell(dto.getItemPictureUrl8());
                template.createCell("");//PC端描述
                template.createCell(dto.getProductDetail());//移动端描述
                template.createCell(dto.getStock());//库存
                template.createCell(dto.getSupplyPrice());//进货价
                template.createCell("");//币种




                count++;
            }
            return template;
        } catch (ServiceException e) {
            throw e;
        }

    }

    @Override
    public StringBuffer exportProduct(String category, Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {
        StringBuffer buffer = new StringBuffer("CategoryName品类名称," +
                "CategroyNo品类编号,BrandNo品牌编号,BrandName品牌,ProductModel货号,SupplierSkuNo供应商SkuNo," +
                " type ,"+
                "SopProductName商品名称,BarCode条形码,ProductColor颜色,ProductSize尺码,Materia材质,ProductOrigin产地,productUrl1," +
                "productUrl2,productUrl3,productUrl4,productUrl5,productUrl6,productUrl7,productUrl8,productUrl9," +
                "PcDesc PC端描述,MDesc 手机端描述,Stock 库存,Price 进货价,Currency 币种,上市季节").append("\r\n");
        Page<SpinnakerProductDTO> page = this.findProduct(category, startDate, endDate, pageIndex, pageSize);
        String productSize,season="", productDetail="",brandId="";
        for(SpinnakerProductDTO dto:page.getItems()){

            try {
                //处理图片
                dto.setItemPic();
                buffer.append(dto.getCategory()).append(",").append("品类编号").append(",");
                //品牌
                brandId=dto.getProducerId().trim();
                if(brandMap.containsKey(brandId)){
                    brandId=brandMap.get(brandId);
                }else{
                    brandId ="";
                }
                buffer.append(brandId).append(",");
                buffer.append(dto.getProducerId()).append(",");
                //货号
                buffer.append(dto.getProductName() + " " + dto.getColor()).append(",").append(dto.getItemId()).append(",");
                //欧洲习惯 第一个先看 男女
                buffer.append(dto.getType()).append(",");
                //产品名称
                buffer.append(dto.getDescription()).append(",");
                buffer.append("\"\t" + dto.getBarcode() + "\"").append(",").append(dto.getColor()).append(",");
                //获取尺码
                productSize=dto.getItemSize();
                if(StringUtils.isNotBlank(productSize)){
                    productSize=productSize.substring(0,dto.getItemSize().length()-4);
                    if(productSize.indexOf("+")>0){
                        productSize=productSize.replace("+",".5");
                    }

                }else{
                    productSize="";
                }

                buffer.append(productSize).append(",");


                //明细描述
                productDetail = dto.getProductDetail();
                if(StringUtils.isNotBlank(productDetail)&&productDetail.indexOf(",")>0){
                    productDetail = productDetail.replace(",",".");
                }


                buffer.append(productDetail).append(",")
                        .append(productDetail).append(",").append(dto.getUrl()).append(",");
                buffer.append(dto.getItemPictureUrl1()).append(",").append(dto.getItemPictureUrl2()).append(",").append(dto.getItemPictureUrl3()).append(",")
                        .append(dto.getItemPictureUrl4()).append(",").append(dto.getItemPictureUrl5()).append(",")
                        .append(dto.getItemPictureUrl6()).append(",").append(dto.getItemPictureUrl7()).append(",")
                        .append(dto.getItemPictureUrl8()).append(",").append(dto.getDescription()).append(",");

                buffer.append(productDetail).append(",");
                buffer.append(dto.getStock()).append(",")
                        .append(dto.getSupplyPrice()).append(",").append("").append(",");
                //季节

                if(StringUtils.isNotBlank(dto.getSeason())){
                    if(seasonMap.containsKey(dto.getSeason())){
                        season = seasonMap.get(dto.getSeason());
                    }else{
                        season = "";
                    }
                }else{
                    season = "";
                }
                buffer.append(season);

                buffer.append("\r\n");
            } catch (Exception e) {
                logger.debug(dto.getItemId()+"拉取失败"+  e.getMessage());
                continue;
            }

        }
        return buffer ;
    }

    private Map<String,String> getMateriaAndProductOrigin(){
        return null;
    }
}
