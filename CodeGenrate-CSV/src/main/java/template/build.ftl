

dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.11'
    compile(
    "net.sourceforge.javacsv:javacsv:2.0",
    <#if stock=='stock'>
    	<#if isHK=='1'>
    	project(':project-common'),
        project(':iog-sop')
	    	<#else>
		    	project(':iog-common-product'),
		        project(':openapi-ice'),
    	</#if>
		
       <#else>       		
            project(':iog-common-product'),
        	project(':iog-product-framework')
    </#if>
 
    )
}

jar {

    dependsOn copyToLib , copyResources ,dependsClassPath
    exclude ("**/*.properties")

    manifest {
     <#if "stock"==stock>
	
   		 attributes 'Main-Class': 'com.shangpin.iog.${supplierName}.stock.StockImp'
    
  		 <#else>
   		 attributes 'Main-Class': 'com.shangpin.iog.${supplierName}.StartUp'
   </#if>
   
    }
}