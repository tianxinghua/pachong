package com.shangpin.ep.order.util;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by jinweile on 2015/3/4.
 */
public class ToolsUtils {

    /**
     * InputStream转OutputStream <font color="red">，通过ByteArrayOutputStream实现</font>
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream InputStreamToOutputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos;
    }

    /**
     * InputStream转OutputStream <font color="red">，通过ByteArrayOutputStream实现</font>
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String InputStreamToString(InputStream is) throws IOException {
        return InputStreamToOutputStream(is).toString();
    }

    /**
     * InputStream转String <font color="red">，通过BufferedReader实现</font>
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String ConvertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        String result = sb.toString();
        return result.endsWith("\n") ? result.substring(0, result.length() - 2) : result;
    }

    /**
     * url转码
     *
     * @param source
     * @return
     * @throws Exception
     */
    public static String urlEncode(String source, String charset) throws Exception {
        return URLEncoder.encode(source, charset);
    }

    /**
     * url解码
     *
     * @param source
     * @return
     * @throws Exception
     */
    public static String urlDecode(String source, String charset) throws Exception {
        return URLDecoder.decode(source, charset);
    }

}
