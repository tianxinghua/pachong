package com.shangpin.iog.tony.service;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;

import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SupplierStockDTO;
import com.shangpin.iog.service.SupplierStockService;
import com.shangpin.iog.tony.common.Constant;
import com.shangpin.iog.tony.common.MyJsonClient;

import com.shangpin.iog.tony.dto.EventDTO;
import com.shangpin.iog.tony.dto.ReturnDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
@Service("tonyfetch")
public class TonyStockImp {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");


    OutTimeConfig outTimeConfig =     new OutTimeConfig(1000 * 60 , 1000 * 60 , 1000 * 60);
    @Autowired
    SupplierStockService supplierStockService;
    public void  fetchStock() {

        Gson gson = new Gson();
        Map<String,Integer> stockMap = new HashMap<>();
        String json = Constant.EVENTS_INPUT;
        String rtnData ="";
        try {
            rtnData = HttpUtil45.operateData("post", "json", Constant.EVENTS_URL,outTimeConfig, null, json, "", "");
            logger.info("rtnData = " + rtnData);
            System.out.println("rtnData=="+rtnData);
        } catch (ServiceException e) {
            e.printStackTrace();
            loggerError.error(" reason : " + e.getMessage());
        }



        ReturnDTO returnDTO=gson.fromJson(rtnData, ReturnDTO.class );//new TypeToken<List<SaleDTO>>(){}.getType()

        if("ok".equals(returnDTO.getStatus())){
            List<EventDTO>  eventDTOs  = returnDTO.getData().getEvents();
            if(null!=eventDTOs){
                for(EventDTO eventDTO:eventDTOs){
                    if(0==eventDTO.getType()){//购买减少库存
                        //0: the quantity of the involved product is changed (you can find a sku attribute to identify the product and a qty attribute that contains the new quantity you have to display on your selling channel);
                        stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
                    }else if(1==eventDTO.getType()){
                        //1: the involved item was removed from the stock (you can find the sku attribute to identify the product and you have to put the quantity to 0)
                        stockMap.put(eventDTO.getAdditional_info().getSku(),0);
                    }else if(2==eventDTO.getType()){
                        //  2: the inventory was updated and you have to reload all the inventory using the proper web service (the getItemsList web service)
                    }else if(3==eventDTO.getType()){
                         //3: the item was upserted. It means that the involved item was added to the inventory or updated. You can find the item directly in the event and you have to refresh the info on your selling channel
                    }else if(4==eventDTO.getType()){
                        // 4 the item changed quantity or the stock price. You can find in the event the sku attribute of the involved product, the qty attribute (that you have to display on your selling channel), the qty_diff attribute (not important in this case) and the stock price
//                        if(0==eventDTO.getAdditional_info().getStock_price()){//停止销售
//                            stockMap.put(eventDTO.getAdditional_info().getSku(),0);
//                        }else{
                            stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
//                        }
                    }
                }
                for(Iterator<Map.Entry<String,Integer>> itor= stockMap.entrySet().iterator();itor.hasNext();){
                    Map.Entry<String,Integer> entry  = itor.next();
                    SupplierStockDTO supplierStockDTO = new SupplierStockDTO();
                    supplierStockDTO.setSupplierId(Constant.SUPPLIER_ID);
                    supplierStockDTO.setId(UUIDGenerator.getUUID());
                    supplierStockDTO.setSupplierSkuId(entry.getKey());
                    supplierStockDTO.setQuantity(entry.getValue());
                    supplierStockDTO.setOptTime(new Date());

                    try {
                        supplierStockService.saveStock(supplierStockDTO);

                    } catch (ServiceMessageException e) {
                        try {
                            if(e.getMessage().equals("数据插入失败键重复")){
                                //update
                                supplierStockService.updateStock(supplierStockDTO);
                                logger.info(supplierStockDTO.getSupplierSkuId() + " : 更新库存" + supplierStockDTO.getQuantity());
                                //

                            } else{
                                e.printStackTrace();
                            }

                        } catch (ServiceException e1) {
                            e1.printStackTrace();
                        }
                    }

                    //实时更新库存


                }

            }

        }
    }

    /**
     * 更新库存数量
     */
    private void updateSOPInventory(){

    }

    public static void main(String[] args) throws Exception {
/*        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("TESSABIT更新数据库开始");
        impl.updateProductStock("2015091501331", "2015-01-01 00:00", format.format(new Date()));
        logger.info("TESSABIT更新数据库结束");
        System.exit(0);*/

//        List<String> skuNo = new ArrayList<>();
//        skuNo.add("356530S1533_1500-40");
//        skuNo.add("356530S1533_1000-40");
//        Map returnMap = impl.grabStock(skuNo);
//        System.out.println("test return size is "+returnMap.keySet().size());
/*        for (Object key:returnMap.keySet()){
            System.out.print("key is " + key);
            System.out.println(",value is "+returnMap.get(key));
        }*/

         long start = 0;//计时开始时间
         long end = 0;//计时结束时间
        start = System.currentTimeMillis();
//        String json = new MyJsonClient().getEvents();
        //String json = "rtnData=={\"status\":\"ok\",\"data\":{\"events\":[{\"_id\":{\"$id\":\"561e6e2be4b0b71d53085f0f\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":1,\"date\":{\"sec\":1444834859,\"usec\":487000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"from\":\"shop\",\"sku\":\"356530S1533_1000-40\",\"order_id\":{\"$id\":\"561e6e21e4b0b71d53085f07\"}}},{\"_id\":{\"$id\":\"561e745fe4b0269c2a643f68\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444836447,\"usec\":622000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":2,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7455e4b0269c2a643f60\"}}},{\"_id\":{\"$id\":\"561e775ce4b0b5f049525a41\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444837212,\"usec\":802000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":1,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7752e4b0b5f049525a38\"}}},{\"_id\":{\"$id\":\"561e77a2e4b0b5f049525a4b\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444837282,\"usec\":801000},\"additional_info\":{\"qty_diff\":-3,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":4,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7798e4b0b5f049525a42\"}}},{\"_id\":{\"$id\":\"561e7d36e4b0c01df26d77e9\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444838710,\"usec\":853000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"qty\":4,\"from\":\"shop\",\"sku\":\"356530S1533_1000-40\",\"order_id\":{\"$id\":\"561e7d2ce4b0c01df26d77e0\"}}}]}}";
        end = System.currentTimeMillis();
        String json="{\"status\":\"ok\",\"data\":{\"events\":[{\"_id\":{\"$id\":\"56277a7ae4b06ca4edbc8526\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427834,\"usec\":422000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562760d32ec771f6c9f24263\"},\"sku\":\"CD0046AF967_8K873-38\",\"title\":\"D\\u00e9collet\\u00e9 Bellucci con stampa maiolica\",\"desc\":\"D\\u00e9collet\\u00e9 Bellucci con stampa maiolica<br>Modello a punta in tessuto broccato<br \\/>Dettagli con cristalli sul davanti<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in tessuto<br>Altezza tacco: 9 cm<br>Interno scarpe: in tessuto<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: blu, bianco<br>Product Model: Bellucci\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":645,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"White\",\"color\":\"bianco\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Bellucci pump with maiolica print\",\"desc_en\":\"Bellucci pump with maiolica print<br>Pointed toe model in brocade fabric<br \\/>Crystals detail on the toe<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Fabric<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: blue, White<br>Product Model: Bellucci\",\"barcode\":\"21500193506\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-4_1.jpg\"]}}},{\"_id\":{\"$id\":\"56277a84e4b06ca4edbc853c\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427844,\"usec\":426000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562760d92ec771f6c9f24264\"},\"sku\":\"CD0046AF967_8K873-38.5\",\"title\":\"D\\u00e9collet\\u00e9 Bellucci con stampa maiolica\",\"desc\":\"D\\u00e9collet\\u00e9 Bellucci con stampa maiolica<br>Modello a punta in tessuto broccato<br \\/>Dettagli con cristalli sul davanti<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in tessuto<br>Altezza tacco: 9 cm<br>Interno scarpe: in tessuto<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: blu, bianco<br>Product Model: Bellucci\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":645,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"White\",\"color\":\"bianco\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Bellucci pump with maiolica print\",\"desc_en\":\"Bellucci pump with maiolica print<br>Pointed toe model in brocade fabric<br \\/>Crystals detail on the toe<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Fabric<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: blue, White<br>Product Model: Bellucci\",\"barcode\":\"21500193507\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-4_1.jpg\"]}}},{\"_id\":{\"$id\":\"56277a8ee4b06ca4edbc8552\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427854,\"usec\":424000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562760e02ec771f6c9f24265\"},\"sku\":\"CD0046AF967_8K873-39\",\"title\":\"D\\u00e9collet\\u00e9 Bellucci con stampa maiolica\",\"desc\":\"D\\u00e9collet\\u00e9 Bellucci con stampa maiolica<br>Modello a punta in tessuto broccato<br \\/>Dettagli con cristalli sul davanti<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in tessuto<br>Altezza tacco: 9 cm<br>Interno scarpe: in tessuto<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: blu, bianco<br>Product Model: Bellucci\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":645,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"White\",\"color\":\"bianco\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Bellucci pump with maiolica print\",\"desc_en\":\"Bellucci pump with maiolica print<br>Pointed toe model in brocade fabric<br \\/>Crystals detail on the toe<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Fabric<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: blue, White<br>Product Model: Bellucci\",\"barcode\":\"21500193508\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0046af967_8k873-4_1.jpg\"]}}},{\"_id\":{\"$id\":\"56277a98e4b06ca4edbc8568\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427864,\"usec\":417000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562760e72ec771f6c9f24266\"},\"sku\":\"CD0228A1471_80317-36\",\"title\":\"D\\u00e9collet\\u00e9 in vernice Kate\",\"desc\":\"D\\u00e9collet\\u00e9 in vernice Kate<br>Modello a punta<br \\/>Dettagli applicati sul davanti<br \\/>Tacco ricoperto in vernice<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in pelle<br>Altezza tacco: 9 cm<br>Interno scarpe: in pelle<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: rosso<br>Product Model: Kate\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":695,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"red\",\"color\":\"rosso\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Kate patent leather pumps\",\"desc_en\":\"Kate patent leather pumps<br>Pointed toe<br \\/>Buttons and studs detail<br \\/>Covered heel<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Leather<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: red<br>Product Model: Kate\",\"barcode\":\"21500193901\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-4_1.jpg\"]}}},{\"_id\":{\"$id\":\"56277aa2e4b06ca4edbc857e\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427874,\"usec\":426000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562760ee2ec771f6c9f24267\"},\"sku\":\"CD0228A1471_80317-36.5\",\"title\":\"D\\u00e9collet\\u00e9 in vernice Kate\",\"desc\":\"D\\u00e9collet\\u00e9 in vernice Kate<br>Modello a punta<br \\/>Dettagli applicati sul davanti<br \\/>Tacco ricoperto in vernice<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in pelle<br>Altezza tacco: 9 cm<br>Interno scarpe: in pelle<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: rosso<br>Product Model: Kate\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":695,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"red\",\"color\":\"rosso\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Kate patent leather pumps\",\"desc_en\":\"Kate patent leather pumps<br>Pointed toe<br \\/>Buttons and studs detail<br \\/>Covered heel<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Leather<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: red<br>Product Model: Kate\",\"barcode\":\"21500193902\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-4_1.jpg\"]}}},{\"_id\":{\"$id\":\"56277aace4b06ca4edbc8594\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427884,\"usec\":421000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562760f52ec771f6c9f24268\"},\"sku\":\"CD0228A1471_80317-37\",\"title\":\"D\\u00e9collet\\u00e9 in vernice Kate\",\"desc\":\"D\\u00e9collet\\u00e9 in vernice Kate<br>Modello a punta<br \\/>Dettagli applicati sul davanti<br \\/>Tacco ricoperto in vernice<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in pelle<br>Altezza tacco: 9 cm<br>Interno scarpe: in pelle<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: rosso<br>Product Model: Kate\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":695,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"red\",\"color\":\"rosso\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Kate patent leather pumps\",\"desc_en\":\"Kate patent leather pumps<br>Pointed toe<br \\/>Buttons and studs detail<br \\/>Covered heel<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Leather<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: red<br>Product Model: Kate\",\"barcode\":\"21500193903\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-4_1.jpg\"]}}},{\"_id\":{\"$id\":\"56277ab6e4b06ca4edbc85aa\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427894,\"usec\":423000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562760fc2ec771f6c9f24269\"},\"sku\":\"CD0228A1471_80317-37.5\",\"title\":\"D\\u00e9collet\\u00e9 in vernice Kate\",\"desc\":\"D\\u00e9collet\\u00e9 in vernice Kate<br>Modello a punta<br \\/>Dettagli applicati sul davanti<br \\/>Tacco ricoperto in vernice<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in pelle<br>Altezza tacco: 9 cm<br>Interno scarpe: in pelle<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: rosso<br>Product Model: Kate\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":695,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"red\",\"color\":\"rosso\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Kate patent leather pumps\",\"desc_en\":\"Kate patent leather pumps<br>Pointed toe<br \\/>Buttons and studs detail<br \\/>Covered heel<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Leather<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: red<br>Product Model: Kate\",\"barcode\":\"21500193904\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-4_1.jpg\"]}}},{\"_id\":{\"$id\":\"56277ac0e4b06ca4edbc85c0\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427904,\"usec\":423000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562761032ec771f6c9f2426a\"},\"sku\":\"CD0228A1471_80317-38\",\"title\":\"D\\u00e9collet\\u00e9 in vernice Kate\",\"desc\":\"D\\u00e9collet\\u00e9 in vernice Kate<br>Modello a punta<br \\/>Dettagli applicati sul davanti<br \\/>Tacco ricoperto in vernice<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in pelle<br>Altezza tacco: 9 cm<br>Interno scarpe: in pelle<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: rosso<br>Product Model: Kate\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":695,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"red\",\"color\":\"rosso\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Kate patent leather pumps\",\"desc_en\":\"Kate patent leather pumps<br>Pointed toe<br \\/>Buttons and studs detail<br \\/>Covered heel<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Leather<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: red<br>Product Model: Kate\",\"barcode\":\"21500193905\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-4_1.jpg\"]}}},{\"_id\":{\"$id\":\"56277acae4b06ca4edbc85d6\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427914,\"usec\":419000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562761092ec771f6c9f2426b\"},\"sku\":\"CD0228A1471_80317-38.5\",\"title\":\"D\\u00e9collet\\u00e9 in vernice Kate\",\"desc\":\"D\\u00e9collet\\u00e9 in vernice Kate<br>Modello a punta<br \\/>Dettagli applicati sul davanti<br \\/>Tacco ricoperto in vernice<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in pelle<br>Altezza tacco: 9 cm<br>Interno scarpe: in pelle<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: rosso<br>Product Model: Kate\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":695,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"red\",\"color\":\"rosso\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Kate patent leather pumps\",\"desc_en\":\"Kate patent leather pumps<br>Pointed toe<br \\/>Buttons and studs detail<br \\/>Covered heel<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Leather<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: red<br>Product Model: Kate\",\"barcode\":\"21500193906\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-4_1.jpg\"]}}},{\"_id\":{\"$id\":\"56277ad4e4b06ca4edbc85ec\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427924,\"usec\":420000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562761102ec771f6c9f2426c\"},\"sku\":\"CD0228A1471_80317-39\",\"title\":\"D\\u00e9collet\\u00e9 in vernice Kate\",\"desc\":\"D\\u00e9collet\\u00e9 in vernice Kate<br>Modello a punta<br \\/>Dettagli applicati sul davanti<br \\/>Tacco ricoperto in vernice<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in pelle<br>Altezza tacco: 9 cm<br>Interno scarpe: in pelle<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: rosso<br>Product Model: Kate\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":695,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"red\",\"color\":\"rosso\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Kate patent leather pumps\",\"desc_en\":\"Kate patent leather pumps<br>Pointed toe<br \\/>Buttons and studs detail<br \\/>Covered heel<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Leather<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: red<br>Product Model: Kate\",\"barcode\":\"21500193907\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-4_1.jpg\"]}}},{\"_id\":{\"$id\":\"56277adee4b06ca4edbc8602\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":3,\"date\":{\"sec\":1445427934,\"usec\":427000},\"additional_info\":{\"item\":{\"_id\":{\"$id\":\"562761162ec771f6c9f2426d\"},\"sku\":\"CD0228A1471_80317-40\",\"title\":\"D\\u00e9collet\\u00e9 in vernice Kate\",\"desc\":\"D\\u00e9collet\\u00e9 in vernice Kate<br>Modello a punta<br \\/>Dettagli applicati sul davanti<br \\/>Tacco ricoperto in vernice<br>Posizione del logo: Logo impresso sulla suola<br>Composizione suola scarpe: in cuoio<br>Composizione tomaia scarpe: in pelle<br>Altezza tacco: 9 cm<br>Interno scarpe: in pelle<br>Confezione: Sacchetto anti-polvere incluso<br>Conversione taglie: EU<br>Made in: Made in Italy<br>Colore: rosso<br>Product Model: Kate\",\"cur\":1,\"brand\":\"Dolce & Gabbana\",\"qty\":1,\"price\":695,\"cat_ids\":[{\"$id\":\"561d7300b49dbb9c2c551c89\"},{\"$id\":\"561d7300b49dbb9c2c551c55\"}],\"color_en\":\"red\",\"color\":\"rosso\",\"sex\":\"Female\",\"season\":\"AW-1516\",\"title_en\":\"Kate patent leather pumps\",\"desc_en\":\"Kate patent leather pumps<br>Pointed toe<br \\/>Buttons and studs detail<br \\/>Covered heel<br>Posizione del logo: Logo embossed on the sole<br>Altezza tacco: 9 cm<br>Interno scarpe: Leather<br>Confezione: Dust bag included<br>Conversione taglie: EU<br>Colore: red<br>Product Model: Kate\",\"barcode\":\"21500193908\",\"age\":\"Adult\",\"images\":[\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-1_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-2_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-3_1.jpg\",\"http:\\/\\/www.tonyboutique.com\\/media\\/catalog\\/product\\/c\\/d\\/cd0228a1471_80317-4_1.jpg\"]}}}" +
                "]}}";

//        Items[] array = new Gson().fromJson(new StringUtil().getItemsArray(itemsJson),
//                new TypeToken<Items[]>() {}.getType());
        Gson gson = new Gson();
        ReturnDTO returnDTO=gson.fromJson(json, ReturnDTO.class );//new TypeToken<List<SaleDTO>>(){}.getType()
        Map<String,Integer> stockMap = new HashMap<>();
        if("ok".equals(returnDTO.getStatus())){
           List<EventDTO>  eventDTOs  = returnDTO.getData().getEvents();
            for(EventDTO eventDTO:eventDTOs){
                if(0==eventDTO.getType()){//购买减少库存
                   stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
                }else if(1==eventDTO.getType()){
                    stockMap.put(eventDTO.getAdditional_info().getSku(),0);
                }else if(2==eventDTO.getType()){
                    stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
                }else if(3==eventDTO.getType()){
                    stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
                }else if(4==eventDTO.getType()){
                    if(0==eventDTO.getAdditional_info().getStock_price()){//停止销售
                        stockMap.put(eventDTO.getAdditional_info().getSku(),0);
                    }else{
                        stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
                    }

                }

            }


        }

        //定义三方
//        Map returnMap = new HashMap();
//        String key = "";
//        Integer value = null;
//        String[] strArr = json.split("additional_info");
//        System.out.println(strArr.length);
//        logger.info("为TESSABIT供应商产品库存循环赋值");
//        start = System.currentTimeMillis();
//        for (String item:strArr){
//            System.out.println(item+"===========");
//            key = MyStringUtil.getSkuId(item);
//            value = MyStringUtil.getQty(item);
//            System.out.println("Sku ID is - :"+key+",stock is "+value);
//            logger.info("Sku ID is "+key+",stock is "+value);
//            returnMap.put(key,value);
//        }
    }
}
