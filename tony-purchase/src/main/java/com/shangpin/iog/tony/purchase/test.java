package com.shangpin.iog.tony.purchase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/9/28.
 */
public class test {

    public static void main(String[] args) throws  Exception{
/*        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time=format.format(date);
        System.out.println(time);*/

        String str = "263815;FW15;Givenchy;BB05100014;001;Woman;Fall Winter;Bags;Shoulder Bags;Antigona Medium;Black;;;;Black matt leather Antigona bag:    detachable and adjustable leather shoulder strap double leather handles   front logo  zipper closure  inside pocket with zipper  cotton lining;Givenchy: Black Medium Antigona Handbag;1491;1491;;1491;;NO;1;Blacks";
        System.out.println(str.split(";").length);
        String s2 = "171003;FW15;Burberry;3201798;2310C;Woman;Fall Winter;Boots;Rain.;;Beige;100%pvc;;MADE IN ITALY;;Burberry: Beige Check Rain Boots;195;195;;195;;NO;1;Clears and neutrals;;0;Height;Heel;Platform;Calf;Insole;;;;;16/12/2014 00:00:00;;;0;;;;;;Completa;NC;Main;Shoes";
        System.out.println(s2.split(";").length);
        String s3 = "263422;FW15;Givenchy;BK06071337;960;Men;Fall Winter;Bags;Bags;;Black;;;;;Givenchy: Black Rottweiler Print Faux Leather Pouch;270;270;;270;;NO;1;Blacks and greys;;0;Height;Width;Depth;Shoulder Strap;Handles;;;;;17/03/2015 00:00:00;;;0;;;;Pvc;;Completa;NC;Main;Accessories";
        System.out.println(s3.split(";").length);
    }
}
