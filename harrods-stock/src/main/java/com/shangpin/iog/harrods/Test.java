package com.shangpin.iog.harrods;

/**
 * Created by wanner on 2018/6/27
 */
public class Test {

    public static void main(String[] args){

        /**
         * 程序配置：
         * 拉取的时间、用户名、用户密码、fiter、每页拉取数、语言为配置文件控制
         * 页码为程序中循环增加，
         * 程序的循环结束（while循环）标志：
         * 拉取的信息的 list size()为 0 的时候结束
         */
        long startMillis = System.currentTimeMillis();
        int page=0;
        boolean loop= true;
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " 天 " + hours + " 小时 " + minutes + " 分钟 "
                + seconds + " 秒 ";
    }

}
