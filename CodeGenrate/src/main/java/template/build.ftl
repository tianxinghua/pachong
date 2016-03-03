

dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.11'
    compile(
    <#if stock=='stock'>
		project(':iog-common-product'),
        project(':openapi-ice'),
        
       <#else>
       		"com.google.code.gson:gson:2.3.1",
            project(':iog-common-product'),
        
    </#if>
 
    )
}

jar {

    dependsOn copyToLib , copyResources ,dependsClassPath
    exclude ("**/*.properties")
   // def manifestCp = configurations.compile.files.collect  {
   //    File file = it
   //    <#noparse> "lib/${file.name}"</#noparse> 
   // }.join(' ')

    manifest {
     <#if "stock"==stock>
	
   		 attributes 'Main-Class': 'com.shangpin.iog.${supplierName}.stock.StockImp'
    
  		 <#else>
   		 attributes 'Main-Class': 'com.shangpin.iog.${supplierName}.StartUp'
   </#if>
   
   
   //     attributes 'Class-Path': '. ./conf/  ' +  manifestCp
    }
}