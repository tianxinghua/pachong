package com.shangpin.iog.theclutcher.utils;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5
{
  public static String encrypt32(String data)
    throws NoSuchAlgorithmException
  {
    char[] md5String = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    byte[] btInput = data.getBytes();

    MessageDigest mdInst = MessageDigest.getInstance("MD5");

    mdInst.update(btInput);

    byte[] md = mdInst.digest();

    int j = md.length;
    char[] str = new char[j * 2];
    int k = 0;
    for (int i = 0; i < j; ++i) {
      byte byte0 = md[i];
      str[(k++)] = md5String[(byte0 >>> 4 & 0xF)];
      str[(k++)] = md5String[(byte0 & 0xF)];
    }

    return new String(str);
  }

  public static String encrypt16(String data)
    throws NoSuchAlgorithmException
  {
    return encrypt32(data).substring(8, 24);
  }

  public static void main(String[] args)
    throws NoSuchAlgorithmException
  {
    System.out.println(encrypt32("中华人名"));
  }
}