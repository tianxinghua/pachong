environments{
	development{
        db {
            driverClassName = 'com.mysql.jdbc.Driver'
            url ='jdbc:mysql://192.168.9.135:3306/iog-test?useUnicode=true&amp;characterEncoding=utf-8'
            username = 'writer'
            password = 'wt@sp520'


            Ice.Default.Locator='SPIceGrid/Locator:default -h 192.168.20.204 -p 12000:default -h 192.168.20.205 -p 12000'

            MongoDB.hostname='192.168.20.82'
            MongoDB.port='27017'

            SOP.HOST='http://192.168.20.83:9090/'

            zookeeper.address='192.168.20.40:2181,192.168.20.41:2181,192.168.20.42:2181'

//            wmsUrl='http://wmsinventory.liantiao.com'
            wmsUrl='qa.wmsinventory.shangpin.com'
			
			
			
			redis.url='192.168.20.241'
            redis.port='6379'
            redis.password=''
        }
    }



    prodown{
        db {
            driverClassName = 'com.mysql.jdbc.Driver'
            url ='jdbc:mysql://iogdb.shangpin.com:3306/iog?useUnicode=true&amp;characterEncoding=utf-8'
            username = 'reader'
            password = 'rd@sp520'

            Ice.Default.Locator='SPIceGrid/Locator:default -h 172.20.10.242 -p 12000:default -h 172.20.10.246 -p 12000'

            MongoDB.hostname='iogmongodb.shangpin.com'
            MongoDB.port='27017'
            SOP.HOST='http://open.shangpin.com:8080'
            zookeeper.address='172.20.10.51:2181,172.20.10.52:2181,172.20.10.54:2181,172.20.10.55:2181,172.20.10.56:2181'

            wmsUrl='http://spwmsinventory.spidc1.com'
            
            
            
            redis.url='172.20.10.127'
            redis.port='6379'
            redis.password=''
        }
    }

	production{

        db {
            driverClassName = 'com.mysql.jdbc.Driver'
            url ='jdbc:mysql://iogdb.shangpin.com:3306/iog?useUnicode=true&amp;characterEncoding=utf-8&allowMultiQueries=true'
            username = 'writer'
            password = 'wt@sp520'

            Ice.Default.Locator='SPIceGrid/Locator:default -h 172.20.11.141 -p 12000:default -h 172.20.11.140 -p 12000'

            MongoDB.hostname='iogmongodb.shangpin.com'
            MongoDB.port='27017'
            SOP.HOST='http://open.shangpin.com:8080'
            zookeeper.address='172.20.10.51:2181,172.20.10.52:2181,172.20.10.54:2181,172.20.10.55:2181,172.20.10.56:2181'

            wmsUrl='http://spwmsinventory.spidc1.com'
          
            
            redis.url='redis.sp.com'
            redis.port='6379'
            redis.password=''

            rabbitmq.host=''
        }
    }


    hk{

        db {
            driverClassName = 'com.mysql.jdbc.Driver'
            url ='jdbc:mysql://49.213.13.167:3306/iog?useUnicode=true&amp;characterEncoding=utf-8'
            username = 'shangpin'
            password = 'shangpin@123'



            Ice.Default.Locator='SPIceGrid/Locator:default -h 172.20.10.242 -p 12000:default -h 172.20.10.246 -p 12000'

            MongoDB.hostname='49.213.13.167'      
            MongoDB.port='27017'
            SOP.HOST='http://open.shangpin.com:8080'

            zookeeper.address='192.168.20.40:2181,192.168.20.41:2181,192.168.20.42:2181'

            wmsUrl='http://spwmsinventory.spidc1.com'
			
			
			
            redis.url='10.11.20.140'
            redis.port='6379'
            redis.password=''
        }
    }

}
