environments{
	development{
        db {
            driverClassName = 'com.mysql.jdbc.Driver'
            url ='jdbc:mysql://192.168.20.82:3306/iog-test?useUnicode=true&amp;characterEncoding=utf-8'
            username = 'root'
            password = '123456'

//            driverClassName = 'net.sourceforge.jtds.jdbc.Driver'
//            url ='jdbc:jtds:sqlserver://192.168.9.115:1433/spider'
//            username = 'writeuser'
//            password = 'write@520'
            Ice.Default.Locator='SPIceGrid/Locator:default -h 192.168.20.204 -p 12000:default -h 192.168.20.205 -p 12000'
            MongoDB.hostname='192.168.20.112'
            SOP.HOST='http://192.168.20.83:9090/'
            MongoDB.port='27017'
            zookeeper.address='192.168.20.40:2181,192.168.20.41:2181,192.168.20.42:2181'

        }
    }
	test{
        db {
            driverClassName = 'com.mysql.jdbc.Driver'
            url ='jdbc:mysql://192.168.20.82:3306/iog?useUnicode=true&amp;characterEncoding=utf-8'
            username = 'root'
            password = '123456'


            Ice.Default.Locator='SPIceGrid/Locator:default -h 192.168.20.204 -p 12000:default -h 192.168.20.205 -p 12000'
            MongoDB.hostname='192.168.20.112'
            SOP.HOST='http://192.168.20.83:9090/'
            MongoDB.port='27017'
            zookeeper.address='192.168.20.40:2181,192.168.20.41:2181,192.168.20.42:2181'

        }
    }
	production{

        db {
            driverClassName = 'com.mysql.jdbc.Driver'
            url ='jdbc:mysql://172.20.30.141:3306/iog?useUnicode=true&amp;characterEncoding=utf-8'
            username = 'reader'
            password = 'rd@sp520'



            Ice.Default.Locator='SPIceGrid/Locator:default -h 172.20.10.242 -p 12000:default -h 172.20.10.246 -p 12000'

            MongoDB.hostname='172.20.30.215'
            MongoDB.port='27017'
            SOP.HOST='http://open.shangpin.com:8080'

            zookeeper.address='192.168.20.40:2181,192.168.20.41:2181,192.168.20.42:2181'



        }
    }


    dubbo{

        db {
            driverClassName = 'com.mysql.jdbc.Driver'
            url ='jdbc:mysql://172.20.30.141:3306/iog?useUnicode=true&amp;characterEncoding=utf-8'
            username = 'writer'
            password = 'wt@sp520'

            Ice.Default.Locator='SPIceGrid/Locator:default -h 172.20.10.242 -p 12000:default -h 172.20.10.246 -p 12000'

            MongoDB.hostname='172.20.30.215'
            MongoDB.port='27017'

            SOP.HOST='http://open.shangpin.com:8080'

            zookeeper.address='172.20.10.51:2181,172.20.10.52:2181,172.20.10.54:2181,172.20.10.55:2181,172.20.10.56:2181'



        }
    }




}