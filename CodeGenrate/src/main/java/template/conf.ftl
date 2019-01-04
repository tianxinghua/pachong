supplierId=${supplierId}
<#if stock==''>
day=90
picpath=/mnt/nfs/${supplierName}
<#else>
jobsSchedule=0 45 0/1 * * ? 
time=3600000
<#if isHK=='1'>
#服务地址
HOST=http://open.shangpin.com:8080
#supplier key
APP_KEY=
#supplier secret
APP_SECRET =
</#if>
</#if>