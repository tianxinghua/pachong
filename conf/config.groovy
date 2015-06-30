environments{
	development{
        db {
            driverClassName = 'com.mysql.jdbc.Driver'
            url ='jdbc:mysql://192.168.20.82:3306/iog?useUnicode=true&amp;characterEncoding=utf-8'
            username = 'root'
            password = '123456'

//            driverClassName = 'net.sourceforge.jtds.jdbc.Driver'
//            url ='jdbc:jtds:sqlserver://192.168.9.115:1433/spider'
//            username = 'writeuser'
//            password = 'write@520'
            Ice.Default.Locator='SPIceGrid/Locator:default -h 192.168.20.204 -p 12000:default -h 192.168.20.205 -p 12000'
        }
    }
	test{}
	production{

        db {
            driverClassName = 'com.mysql.jdbc.Driver'
            url ='jdbc:mysql://192.168.20.82:3306/iog?useUnicode=true&amp;characterEncoding=utf-8'
            username = 'root'
            password = '123456'

            Ice.Default.Locator='SPIceGrid/Locator:default -h 172.20.10.242 -p 12000:default -h 172.20.10.246 -p 12000'


//            driverClassName = 'net.sourceforge.jtds.jdbc.Driver'
//            url ='jdbc:jtds:sqlserver://192.168.9.115:1433/spider'
//            username = 'writeuser'
//            password = 'write@520'
        }
    }
}