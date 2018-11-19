package com.shangpin.iog.spider;


import com.google.common.base.Joiner;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class OtherStoriesProcessor {

    String[] ua = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
            "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"};

    Random random = new Random();

    public List<GoodsEntity> updateInventorySpider(String url) throws IOException {
        System.setProperty("http.proxySet", "true");
        System.getProperties().put("http.proxyHost", "s5.proxy.mayidaili.com");
        System.getProperties().put("http.proxyPort", "8123");

        List<GoodsEntity> goodsList = new ArrayList<>();

        Document doc = Jsoup.connect(url)
                .timeout(10000)
                .userAgent(ua[random.nextInt(15)])
                .header("Proxy-Authorization",getAuthHeader())
                .get();
        String SPU = StringUtils.substringBetween(doc.html(),"'articleCode':'","'");
        String[] sizeNoHtmls= StringUtils.substringsBetween(doc.html(),"'variants' : [","],");
        String sizeNoHtml = "";
        for (int i = 0; i < sizeNoHtmls.length; i++) {
            if (sizeNoHtmls[i].contains(SPU)) {
                sizeNoHtml = sizeNoHtmls[i];
                break;
            }
        }
        //抽取size
        String[] sizeNames = StringUtils.substringsBetween(sizeNoHtml,"'sizeName' : '","'");

        String size = StringUtils.join(sizeNames, ",");

        //抽取库存
        String[] sizeNos = StringUtils.substringsBetween(sizeNoHtml,"'variantCode' : '","'");
        String sizeAPI = "https://www.stories.com/en_gbp/getAvailability?variants=" + StringUtils.join(sizeNos,",");
        String inventoryXml = Jsoup.connect(sizeAPI)
                .timeout(10000)
                .header("Cookie","TS01cea975=01c9cdaf9d33c71607f75d11e1d777a2e21eb04bd34066f05513174517bcbec24236a2971fd39ee4043947cec47e8831dd45881e41cae984194e8966062da9e3f6a4691d45; _ga=GA1.2.1702421776.1536634629; newsletter-signup=viewed; _gid=GA1.2.121820658.1536737591; praCookie=183d60fb-de99-4ade-b82b-ea6c8851c446||en_eur; newsletter-timpestamp=1536819819642; cookie-notification=viewed; HMCORP_locale=en_GB; HMCORP_currency=GBP; TS01f36c36=01dd555d354f1fc54917bbd25189d1eff69fa5bca7154904a33719634e1aad598270ffaea2cac44cde154f7e262843ce5dce48cee87fe53c49d9159c0c95000f6e019ecf102d69159d52f8ac281042dab289d39c2b; dtSa=-; stc112242=env:1537002556%7C20181016090916%7C20180915094002%7C2%7C1022955:20190915091002|uid:1536634628769.338274775.27745485.112242.1591002869:20190915091002|srchist:1022955%3A1537002556%3A20181016090916:20190915091002|tsa:1537002556137.815833660.3790951.5774336428476696.4:20180915094002; utag_main=v_id:0165c68fa874000cdb35915a367803078004f0700093c$_sn:14$_ss:0$_st:1537004405932$ses_id:1537002559415%3Bexp-session$_pn:2%3Bexp-session; dtPC=-; dtLatC=1; dtCookie=B4176A719376CDF73BE5A8CE83102797|QXBwZWFzZXIrUHJvZHVjdGlvbitEZXNrdG9wK1dlYnwx; JSESSIONID=526961F563DE1EF930848803F04EB931.zHYBECMAPPWW02; userCookie=\"{\\\"cartCount\\\":0}\"; TS0151804b=01c9cdaf9ddbff11f75aa249cc33a1c76bdbca6859b20ae27e54b70626ef7b9ff71439a9dabdab642b04ac893b69055b2437c8817ea89f9929f404d4e6a131881e1ed500b806de9d01e607fa7a40abd2471e719ba2856c7dd7f5b9ee9275eb23de3a9d71ee")
                .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .userAgent(ua[random.nextInt(15)])
                .header("Proxy-Authorization",getAuthHeader())
                .get().html();
        List<String> statuss = new ArrayList<>();

        for (int i = 0; i < sizeNos.length; i++) {
            if (inventoryXml.contains(sizeNos[i])) {
                statuss.add("5");
            } else {
                statuss.add("0");
            }
        }

        String qty = StringUtils.join(statuss,",");

        String price = doc.select("label[class=a-label js-a-label price-value]").text().replaceAll("£", "").replaceAll(" ","");


        String[] sizes = size.split(",");
        String[] qtys = qty.split(",");
        for (int i = 0; i < sizes.length; i++) {
            GoodsEntity goodsEntity = new GoodsEntity();
            goodsEntity.setSize(sizes[i]);
            goodsEntity.setQty(qtys[i]);
            goodsEntity.setForeignPrice(price);
            goodsList.add(goodsEntity);
        }
        return goodsList;
    }

    public static String getAuthHeader() {
        String appkey = "4170176";
        String secret = "50d9c2e3aea391cdf2cb2d00b1c306a7";

        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("app_key", appkey);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        paramMap.put("timestamp", format.format(new Date()));

        String[] keyArray = (String[]) paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(secret);
        for (String key : keyArray) {
            stringBuilder.append(key).append((String) paramMap.get(key));
        }

        stringBuilder.append(secret);
        String codes = stringBuilder.toString();

        String sign = DigestUtils.md5Hex(codes).toUpperCase();

        paramMap.put("sign", sign);

        String authHeader = "MYH-AUTH-MD5 "
                + Joiner.on('&').withKeyValueSeparator("=").join(paramMap);

        return authHeader;
    }
}