environments{
	development{
        db {
//            driverClassName = 'org.mysql.jdbc.Driver'
//            url ='jdbc:mysql://192.168.20.82:3306/iog?useUnicode=true&amp;characterEncoding=utf-8'
//            username = 'root'
//            password = '123456'
            driverClassName = 'net.sourceforge.jtds.jdbc.Driver'
            url ='jdbc:jtds:sqlserver://192.168.9.115:1433/spider'
            username = 'writeuser'
            password = 'write@520'
        }
    }
	test{}
	production{}
}