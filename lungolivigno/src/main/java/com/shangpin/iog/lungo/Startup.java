package com.shangpin.iog.lungo;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.lungo.util.FetchProduct;

import java.io.PrintStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Startup
{
  private static ApplicationContext factory;

  private static void loadSpringContext()
  {
    factory = new AnnotationConfigApplicationContext(new Class[] { AppContext.class });
  }

  public static void main(String[] args)
  {
    loadSpringContext();

    FetchProduct fetchService = (FetchProduct)factory.getBean("lungolivigno");
    System.out.println("------start---------");
    try {
      String url = "http://www.lungolivignofashion.com/collection/";
      fetchService.fetchProduct(url);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}