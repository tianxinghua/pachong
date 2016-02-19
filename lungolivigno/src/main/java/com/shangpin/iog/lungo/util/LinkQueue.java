package com.shangpin.iog.lungo.util;

import java.util.HashSet;
import java.util.Set;

public class LinkQueue
{
  private static Set<String> visitedUrl = new HashSet();

  private static Queue unVisitedUrl = new Queue();

  private static Queue allSkuUrl = new Queue();

  public static Queue getUnVisitedUrl() {
    return unVisitedUrl; }

  public static Queue getAllSkuUrl() {
    return allSkuUrl;
  }

  public static void addVisitedUrl(String url) {
    visitedUrl.add(url);
  }

  public static void removeVisitUrl(String url) {
    visitedUrl.remove(url);
  }

  public static String unVisitEdUrlDeQueue() {
    return unVisitedUrl.deQueue(); }

  public static String allSkuUrlDeQueue() {
    return allSkuUrl.deQueue();
  }

  public static void addUnvisitedUrl(String url) {
    unVisitedUrl.enQueue(url); }

  public static void addAllSkuUrl(String url) {
    allSkuUrl.enQueue(url);
  }

  public static int getVisitedUrlNum() {
    return visitedUrl.size();
  }

  public static boolean unVisitedUrlsEmpty() {
    return unVisitedUrl.isQueueEmpty(); }

  public static boolean allSkuUrllsEmpty() {
    return allSkuUrl.isQueueEmpty();
  }
}