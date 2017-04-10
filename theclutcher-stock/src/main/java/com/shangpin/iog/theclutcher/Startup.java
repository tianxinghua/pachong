package com.shangpin.iog.theclutcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.theclutcher.schedule.AppContext;

public class Startup
{
  private static Logger logger = LoggerFactory.getLogger(Startup.class);
  private static ApplicationContext factory;

  private static void loadSpringContext()
  {
    factory = new AnnotationConfigApplicationContext(new Class[] { AppContext.class });
  }

  public static void main(String[] args)
  {
    System.out.println("start");
    loadSpringContext();
    logger.info(" schedule start  ");
  }
}