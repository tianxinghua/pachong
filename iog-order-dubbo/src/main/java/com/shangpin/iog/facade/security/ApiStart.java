package com.shangpin.iog.facade.security;



import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2015/3/4.
 */
public class ApiStart {

    /**
     * 入口函数
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //启动容器
        getInstance().start();
        //开始心跳
        HeatBeat heatbeat = new HeatBeat();
        heatbeat.start();
        // 按任意键退出
        System.in.read();
        getInstance().stop();
    }

    public static class HeatBeat extends Thread {

        public void run() {
            while (true) {
                try {

                    Thread.sleep(50000);
                } catch (Exception e) {
                }
            }
        }

    }

    /**
     * 容器实例
     */
    private static volatile ClassPathXmlApplicationContext instance = null;

    /**
     * 锁
     */
    private static volatile Lock lockhelper = new ReentrantLock();

    /**
     * 获取Spring容器实例
     *
     * @return
     */
    public static ClassPathXmlApplicationContext getInstance() {
        if (instance == null) {
            lockhelper.lock();
            if (instance == null) {
                try {
                    instance = new ClassPathXmlApplicationContext("applicationContext.xml");
                } finally {
                    lockhelper.unlock();
                }
            }
        }

        return instance;
    }

    /**
     * 释放资源
     */
    public static void Dispose() {
        if (instance != null) {
            lockhelper.lock();
            if (instance != null) {
                try {
                    instance.destroy();
                    instance = null;
                } finally {
                    lockhelper.unlock();
                }
            }
        }
    }

}
