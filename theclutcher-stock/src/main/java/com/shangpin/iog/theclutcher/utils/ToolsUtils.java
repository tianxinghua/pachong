package com.shangpin.iog.theclutcher.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class ToolsUtils
{
  public static ByteArrayOutputStream InputStreamToOutputStream(InputStream is)
    throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int i = -1;
    while ((i = is.read()) != -1) {
      baos.write(i);
    }
    return baos;
  }

  public static String InputStreamToString(InputStream is)
    throws IOException
  {
    return InputStreamToOutputStream(is).toString();
  }

  public static String ConvertStreamToString(InputStream is)
    throws IOException
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line = null;
    while ((line = reader.readLine()) != null) {
      sb.append(new StringBuilder().append(line).append("\n").toString());
    }
    String result = sb.toString();
    return ((result.endsWith("\n")) ? result.substring(0, result.length() - 2) : result);
  }

  public static String urlEncode(String source, String charset)
    throws Exception
  {
    if (source != null) {
      return URLEncoder.encode(source, charset);
    }
    return null;
  }

  public static String urlDecode(String source, String charset)
    throws Exception
  {
    return URLDecoder.decode(source, charset);
  }
}