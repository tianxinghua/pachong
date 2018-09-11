package com.shangpin.iog.styleside.service;

import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * 非常基本的示例代码，演示如何创建OAuth 1.0系统到系统
 * 请求LearningStudio API
 */
public class OAuth {

    //public static void main(final String[] args) throws Exception{
    public static String getData(String httpMethod,String URI,String oauth_token, String consumerKey ,String secret,String oauth_version,String signatureMethod) throws Exception {
        // 设置创建OAuth 1.0签名所需的变量并发出请求
        byte[] requestBody = null;
        HttpsURLConnection request = null;
        BufferedReader in = null;
        URL url = new URL(String.format(URI));
        //设置Nonce和Timestamp参数
        String nonce = getRandom();
        String timestamp = getTimestamp();
        // 创建OAuth参数名称/值对
        Map<String, String> oauthParams = new LinkedHashMap<String, String>();
        oauthParams.put("oauth_signature_method", signatureMethod);
        oauthParams.put("oauth_nonce", nonce);
        oauthParams.put("oauth_timestamp", timestamp);
        oauthParams.put("oauth_consumer_key", consumerKey);
        oauthParams.put("oauth_token", oauth_token);
        oauthParams.put("oauth_version", oauth_version);
        //获取OAuth 1.0签名
        String signature = generateSignature(httpMethod, url, oauthParams, requestBody, secret);
        System.out.println(String.format("OAuth 1.0 Signature = %s", signature));

        // 将oauth_signature参数添加到OAuth参数集
        oauthParams.put("oauth_signature", signature);

        // 生成逗号分隔的字符串：keyName =“URL-encoded（value）”对
        StringBuilder sb = new StringBuilder();
        for (String keyName : oauthParams.keySet()) {
            String value = oauthParams.get((String) keyName);
            sb.append(keyName).append("=\"").append(URLEncoder.encode(value, "UTF-8")).append("\",");
        }

        String urlString = url.toString();
        // 省略url中的queryString
        int startOfQueryString = urlString.indexOf('?');
        if (startOfQueryString != -1) {
            urlString = urlString.substring(0, startOfQueryString);
        }
        String xauth = sb.toString();
        xauth = "OAuth " + xauth.substring(0, xauth.length() - 1);
        System.out.println(String.format("OAuth %s", xauth));
        JSONObject jsonObject = null;
        StringBuilder json = new StringBuilder();
        URL getUrl = new URL(URI);
        // 返回URLConnection子类的对象
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Authorization", xauth);
        // 连接
        connection.connect();
        int code = connection.getResponseCode();
        String lines;
        if (code == 200) {
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            // 使用Reader读取输入流
            BufferedReader reader = new BufferedReader(inputStreamReader);
            Stream<String> lines1 = reader.lines();
            while (( lines= reader.readLine()) != null) {
                json.append(lines);

            }
            reader.close();
        } else {
            InputStream inputStream = connection.getErrorStream(); // 得到网络返回的输入流
        }
        // 断开连接
        connection.disconnect();
        //将字符串转为json格式
        System.out.println(json.toString());
        return json.toString();


    }


    /**
     * 生成随机的随机数
     *
     * @return  请求的唯一标识符
     */
    private static String getNonce()
    {
        return RandomStringUtils.randomAlphanumeric(32).substring(0,9);
    }

    /**
     * Generates an integer representing the number of seconds since the unix epoch using the
     * date/time the request is issued
     *  生成一个整数，表示自unix时期以来使用的秒数发出请求的日期/时间
     * @return  请求的时间戳
     */
    private static String getTimestamp()
    {
        return Long.toString((System.currentTimeMillis() / 1000));
    }

    /**
     * 生成OAuth 1.0签名
     *
     * @param   httpMethod  请求的HTTP方法

     * @param   oauthParams 可关联的oAuth参数的关联集
     * @param   requestBody 序列化的POST / PUT消息体
     * @param   secret    用于验证教育合作伙伴身份的字母数字字符串（私钥）
     *
     * @return  包含Base64编码的签名摘要的字符串
     *
     * @throws  UnsupportedEncodingException
     */
    public static String generateSignature(
            String  httpMethod,
            URL url,
            Map<String, String> oauthParams,
            byte[] requestBody,
            String secret
    ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 确保HTTP方法是大写的
        httpMethod = httpMethod.toUpperCase();

        // 构造签名基本字符串的URL编码的OAuth参数部分
        String encodedParams = normalizeParams(httpMethod, url, oauthParams, requestBody);

        // URL编码相对URL
        String encodedUri = URLEncoder.encode(url.getPath(), "UTF-8");

        // 构建要使用Consumer Secret签名的签名基本字符串
        String baseString = String.format("%s&%s&%s", httpMethod, encodedUri, encodedParams);
        return  genHMAC(baseString,secret);  //sha1

    }

    /**
     * 根据OAuth 1.0规范化所有OAuth可签名参数和url查询参数
     *
     * @param   httpMethod  上层HTTP方法

     * @param   oauthParams 可关联的oAuth参数的关联集

     *
     * @return  包含规范化和编码的oAuth参数的字符串
     *
     * @throws  UnsupportedEncodingException
     */
    private static String normalizeParams(
            String httpMethod,
            URL url,
            Map<String, String> oauthParams,
            byte[] requestBody
    ) throws UnsupportedEncodingException
    {

        // 按字典顺序排序参数，按键1，然后按值
        Map<String, String> kvpParams = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        kvpParams.putAll(oauthParams);

        // 使用等号（“=”）将任何查询字符串参数放入键值对中进行标记
        // 键/值关系并使用＆符号（“＆”）连接每个参数
        if (url.getQuery() != null)
        {
            for(String keyValue : url.getQuery().split("&"))
            {
                String[] p = keyValue.split("=");
                kvpParams.put(p[0],p[1]);
            }

        }

        // 用“=”分隔键和值
        // 将kvp与“＆”分开
        StringBuilder combinedParams = new StringBuilder();
        String delimiter="";
        for(String key : kvpParams.keySet()) {
            combinedParams.append(delimiter);
            combinedParams.append(key);
            combinedParams.append("=");
            combinedParams.append(kvpParams.get(key));
            delimiter="&";
        }

        // url在返回之前再次编码整个字符串
        return URLEncoder.encode(combinedParams.toString(), "UTF-8");

    }

    /**
     * 生成Base64编码的CMAC-AES摘要
     *
     * @param   key 用于签名数据的密钥
     * @param   msg 要签名的数据
     *
     * @return  A CMAC-AES哈希
     *
     * @throws  UnsupportedEncodingException
     */
    private static String generateCmac(String key, String msg)
            throws UnsupportedEncodingException
    {
        byte[] keyBytes = key.getBytes("UTF-8");
        byte[] data = msg.getBytes("UTF-8");

        CMac macProvider = new CMac(new AESFastEngine());
        macProvider.init(new KeyParameter(keyBytes));
        macProvider.reset();

        macProvider.update(data, 0, data.length);
        byte[] output = new byte[macProvider.getMacSize()];
        macProvider.doFinal(output, 0);

        // 将CMAC转换为Base64字符串并删除Base64库添加的新行
        String cmac = Base64.encodeBase64String(output).replaceAll("\r\n", "");
        System.out.print("1111111111"+cmac);
        return cmac;


    }
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";


    /**
     * 使用 HMAC-SHA1 签名方法对data进行签名
     *
     * @param data
     *            被签名的字符串
     * @param key
     *            密钥
     * @return
    加密后的字符串
     */
    public static String genHMAC(String data, String key) {
        byte[] result = null;
        try {
            byte[] bytekey = key.getBytes("UTF-8");

            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(bytekey, HMAC_SHA1_ALGORITHM);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
            result = Base64.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (null != result) {
            return new String(result);
        } else {
            return null;
        }
    }

    public 	 static String getRandom() {
        Random rand = new Random();
        char[] letters = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        };
        String str = "";
        int index;
        boolean[] flags = new boolean[letters.length];//默认为false
        for (int i = 0; i < 5; i++) {
            do {
                index = rand.nextInt(letters.length);
            } while (flags[index] == true);
            char c = letters[index];
            str += c;
            flags[index] = true;
        }
        return  str;

    }


}


















