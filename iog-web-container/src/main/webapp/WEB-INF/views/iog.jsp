<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String bathPath = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- cache -->
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">

<title>导出商品(最新价格)</title>
<link rel="stylesheet"
	href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/icon.css"
	type="text/css" media="screen" />
<link rel="stylesheet"
	href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/default/easyui.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/main.css"
	type="text/css" />
<script type="text/javascript"
	src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery-1.10.1.min.js"></script>
<script type="text/javascript"
	src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=bathPath%>/js/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.json-2.4.js"></script>
	

	
<style type="text/css">
#showExcel {
	position: absolute;
	padding: 0px 20px 20px 20px;
	top: 280px;
	left: 550px;
	width: 600px;
	height: 200px;
	background: LightSkyBlue;
}

#showSkuExcel {
	position: absolute;
	padding: 0px 20px 20px 20px;
	top: 200px;
	left: 380px;
	width: 300px;
	height: 200px;
	background: LightSkyBlue;
}
</style>

<script type="text/javascript">
	function filter(str){
		  if(typeof($('#pageIndex').val()) != "undefined"&& $.trim($('#pageIndex').val()).length !=0){
	            if(isNaN($('#pageIndex').val())){
	                alert("请输入数字");
	                $('#pageIndex').focus();
	                return ;
	            }
	            if(typeof($('#pageSize').val()) == "undefined"||$.trim($('#pageSize').val()).length ==0){
	                alert("页码有数值时，导出行数必须也要有数值");
	                return ;
	            }
	        }
	        if(typeof($('#pageSize').val()) != "undefined"&&$.trim($('#pageSize').val()).length !=0){
	            if(isNaN($('#pageSize').val())){
	                alert("请输入数字");
	                $('#pageSize').focus();
	                return ;
	            }
	            if(typeof($('#pageIndex').val()) == "undefined"||$.trim($('#pageIndex').val()).length ==0){
	                alert("导出行数有数值时，页码必须也要有数值");
	                return ;
	            }
	        }
	        if(str == 'ep_rule' || str == 'report'){
	        	if("-1"== $('#supplier').val()){
	        		alert("请选择KA或重点供应商");
	            	return;
	        	}
	        }
	        if(str == 'buExport'){
	        	if("-1"== $('#bus').val()){
	        		alert("请选择一个BU");
	            	return;
	        	}
	        }
	        if(str == 'everyday'){	 
	        console.log("date="+$('#startDate').val());      
	        	if('' == $('#startDate').val() && '' == $('#endDate').val()){
	        		alert("请选择日期");
	            	return;
	        	}
	        }
	        var search = {
	            supplier:   $('#supplier').val(),
	            startDate:    $('#startDate').val(),
	            endDate:      $('#endDate').val(),
	            pageIndex: $('#pageIndex').val(),
	            pageSize:$('#pageSize').val(),
	            supplierName:$ ('#supplier').find("option:selected").text(),
	            flag:str,
	            bu:    $('#bus').val()
	        };
	        return search;
	}
	//下载图片
	function downloadpicture(){
		var search = filter("");
		if(null != search.supplier){
			window.open('downLoadPicture?queryJson='+$.toJSON(search), '','');
		}else{
			alert("请选择一个供应商！");
		}
	    
	}
	//下载图片
	function OnlineDownLoad(){
		var search = filter("");
	    window.open('OnlineDownLoad?queryJson='+$.toJSON(search), '','');
	}
	//导出商品
    function exportProduct(str) {
    	var search = filter(str);
    	if(null != search){
    		window.open('csv?queryJson='+$.toJSON(search), '','');
    	}else{
    		return;
    	}
        
    }
	//导出价格不同商品
    function exportDiffProduct(str) {
    	var search = filter(str);
        window.open('csv?queryJson='+$.toJSON(search), '','');
    }
	//更新价格
    function updatePrice(){
		var supplierId = $('#supplier').val();
		if("-1"== supplierId){
	         alert("请选择供应商");
	         return;
	     }
		window.open('getSkuIds?supplierId='+supplierId, '','');
	}

    function clearText(){
        $('#supplier').val('-1');
        $('#pageIndex').val('');
        $('#pageSize').val('');
        $('#startDate').val('');
        $('#endDate').val('');


    }
//     导出订单
    function exportOrder(str){
    	var search = filter(str);
        window.open('csv?queryJson='+$.toJSON(search), '','');
    }
    
//    查看订单
    function queryOrder(){
    	var search = filter("");
        window.open('orders?supplierId='+$('#supplier').val(), '','');
    }
    function queryNewOrder(){
    	var search = filter("");
        window.open('hubOrders?supplierId='+$('#supplier').val(), '','');
    }
	function show(){
		$("#showExcel").removeAttr("hidden");
	}
	function unshow(){
		$("#showExcel").attr("hidden",'hidden');
	}
	function showSkuExcel(){
		$("#showSkuExcel").removeAttr("hidden");
	}
	function unshowSkuExcel(){
		$("#showSkuExcel").attr("hidden",'hidden');
	}
	function changeReason(id,reason,opeation){
		$.get("changeErrReason",{"supplierId":id,"reason":reason,"opeation":opeation})
	}
	
	/**
	*选择BU导出
	*/
	function buExport(str){
		var search = filter(str);
    	if(null != search){
    		window.open('buexport?queryJson='+$.toJSON(search), '','');
    	}else{
    		return;
    	}
	}
	
	function downloadpicBySupplier(){		
		if("-1"== $('#supplier').val()){
	         alert("请选择供应商");
	         return;
	    }
		var search = filter("");		
		window.open('downloadpicBySupplier?queryJson='+$.toJSON(search), '','');
		
	}
	
	function tempExport(str){
		var search = filter(str);
    	if(null != search){
    		window.open('export?queryJson='+$.toJSON(search), '','');
    	}else{
    		return;
    	}
    }
    function everydayExport(everyday){
    	var search = filter(everyday);
    	if(null != search){
    		window.open('exporteveryday?queryJson='+$.toJSON(search), '','');
    	}else{
    		return;
    	}
    }
	
</script>
<script type="text/javascript"
	src="<%=bathPath%>/js/DatePicker/config.js"></script>
<script type="text/javascript"
	src="<%=bathPath%>/js/DatePicker/WdatePicker.js"></script>
</head>
<body>

	<div style="padding: 20px 20px 20px 20px;">
		<form action="" id="brandForm">
			<table>
				<tr>
					<td>选择供应商</td>
					<td colspan="3"><select id="supplier" style="width: 130px;height: 21px">
							<option value="-1">请选择</option>
							<c:forEach var="supplier" items="${supplierDTOList}">
								<option value="${supplier.supplierId}">${supplier.supplierName}</option>
							</c:forEach>

					</select></td>
				</tr>
				<tr>

					<td>导出时间:</td>
					<td>
						<input id="startDate" name="startDate"
						style="width: 130px;"
						onFocus="var endDate=$dp.$('endDate');WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',minDate:'#F{$dp.$D(\'endDate\',{M:-1})}'})">
					</td>
					<td>至</td>
					<td>
						<input id="endDate" name="endDate" style="width: 130px;"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}',maxDate:'#F{$dp.$D(\'startDate\',{M:+1})}'})">
					</td>

				</tr>
				<tr>
					<td>开始行数</td>
					<td><input type="text" id="pageIndex" name="pageIndex" style="width: 130px;height: 21px"/></td>
					<td>导出行数</td>
					<td><input type="text" id="pageSize" name="pageSize" style="width: 130px;height: 21px"/></td>
				</tr>
				<tr>
					<td>选择BU</td>
					<td colspan="3" style="width: 130px;height: 21px"><select id="bus" style="width: 130px;height: 21px">
							<option value="-1">请选择</option>
							<c:forEach var="bu" items="${BUs}">
								<option value="${bu}">${bu}</option>
							</c:forEach>

					</select></td>
				</tr>
			</table>
		</form>
	</div>
	<div style="text-align: left; padding: 0px 20px 20px 20px;">
		<a
	href="javascript:void(0)" onclick="exportProduct('same')" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>导出</a> 
	<a
	href="javascript:void(0)" onclick="exportProduct('ep_rule')" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>按条件导出</a> 
	<a
	href="javascript:void(0)" onclick="exportProduct('report')" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>报表导出</a> 
	<a
	href="javascript:void(0)" onclick="buExport('buExport')" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>选择BU导出</a> 	
	<a
	href="javascript:void(0)" onclick="exportDiffProduct('diff')" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>价格变化导出</a>
	<a href="javascript:void(0)"
	onclick="clearText()" id="btn-cancel" icon="icon-cancel" class='easyui-linkbutton'>清空</a>
	<a href="javascript:void(0)" onclick="updatePrice()" id="btn-edit" icon="icon-edit" class='easyui-linkbutton'>更新价格</a>
	<a href="javascript:void(0)" onclick="exportOrder('order')" id="btn-edit" icon="icon-search" class='easyui-linkbutton'>导出订单</a>
	
	<a href="javascript:void(0)" onclick="queryNewOrder()" id="btn-edit" icon="icon-search" class='easyui-linkbutton'>新查看订单</a>
	
	<a href="javascript:void(0)" onclick="queryOrder()" id="btn-edit" icon="icon-search" class='easyui-linkbutton'>查看订单</a>
	<a href="javascript:void(0)" onclick="everydayExport('everyday')" id="btn-edit" icon="icon-search" class='easyui-linkbutton'>每日导出</a>
	<a
	href="javascript:void(0)" onclick="exportProduct('all')" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>全部导出</a> 
	<!-- <a
	href="javascript:void(0)" onclick="tempExport('temp')" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>临时导出</a>  -->
	<br><br><br>
	<!-- <a href="javascript:void(0)" onclick="downloadpicture()" icon="icon-search" class='easyui-linkbutton'>下载图片</a> -->
	<div>
		<div
			style="width: 30%; padding-left: 1; margin: 0; float: left; box-sizing: border-box;">
			<form id="tmp111" action="uploadAndDownPics"
				method="post" enctype="multipart/form-data">
				<input type="file" name="tmpfffff" id="tmpfffff">
				<input type="submit" value="上传" id="uploadAndDownPics">				
			</form>
		</div>	
		<div
			style="width:30%; padding-left: 1; margin: 0; float: left; box-sizing: border-box;">
			<a href="javascript:void(0)" onclick="downloadpicBySupplier()" icon="icon-search" class='easyui-linkbutton'>选择供应商下载图片</a>
		</div>		
			
	</div>
	<br><br><br>
	<a href="javascript:void(0)" onclick="OnlineDownLoad()" icon="icon-search" class='easyui-linkbutton' title="按条件导出产品的下载图片,命名为spskuid,失败重试10次">下载图片online</a>
		<br><br><br>
	<form action="uploadFileAndDown" method="post" enctype="multipart/form-data">
		<input type="file" name="uploadFile">
		下载数:<input type="number" name="threadnum">
		<input type="submit" value ="开始上传" onmouseenter="show()" onmouseleave="unshow()">
	</form>
	
			<br>


		<div>
		
			<div
				style="width: 30%; padding-left: 1; margin: 0; float: left; box-sizing: border-box;">
				<form id="deletePreSaleFileAction" action="deletePreSaleFile"
					method="post" enctype="multipart/form-data">
					<input type="file" name="deletePreSaleFile" id="deletePreSaleFile">

					<a href="#" class="easyui-linkbutton" id="submitdeleteSku"
						onmouseenter="showSkuExcel()" onmouseleave="unshowSkuExcel()"
						data-options="iconCls:'icon-remove'">预售商品删除</a>
				</form>
			</div>
			<div
				style="width: 70%; padding: 0; margin: 0; float: left; box-sizing: border-box;">
				<form id="uploadPreSaleFileAndDown"
					action="uploadPreSaleFileAndDown" method="post"
					enctype="multipart/form-data">
					<input type="file" name="uploadPreSaleFile" id="uploadPreSaleFile">

					<a href="#" class="easyui-linkbutton" id="submitSku"
						onmouseenter="showSkuExcel()" onmouseleave="unshowSkuExcel()"
						data-options="iconCls:'icon-save'">预售商品上传</a>
				</form>
			</div>			
		</div>
		<%-- <br>
		<div>		
			<div
				style="width: 30%; padding: 0; margin: 0; float: left; box-sizing: border-box;">
				<form id="uploadPreSaleFileAndDown"
					action="uploadPreSaleFileAndDown" method="post"
					enctype="multipart/form-data">
					<input type="file" name="uploadPreSaleFile" id="uploadPreSaleFile">

					<a href="#" class="easyui-linkbutton" id="submitSku"						
						data-options="iconCls:'icon-save'">BU规则导入</a>
				</form>
			</div>
			<div
				style="width: 70%; padding: 0; margin: 0; float: left; box-sizing: border-box;">
				<form id="uploadPreSaleFileAndDown"
					action="uploadPreSaleFileAndDown" method="post"
					enctype="multipart/form-data">
					<input type="file" name="uploadPreSaleFile" id="uploadPreSaleFile">

					<a href="#" class="easyui-linkbutton" id="submitSku"						
						data-options="iconCls:'icon-save'">BU需要特殊处理的品牌导入</a>
				</form>
			</div>		
		</div> --%>
	<br><br><br>
	<a href="stockUpdateException" onclick="stock()" id="btn-save" icon="icon-search" class='easyui-linkbutton'>库存更新异常查看</a> 
	<a href="orderUpdateException" id="btn-save" icon="icon-search" class='easyui-linkbutton'>订单更新异常查看</a>

	</div>
	<script>
$(function(){
	if("${resultMessage}"!=""){
		var result = "${resultMessage}";
		$.messager.show({
			title:'我的消息',
			msg:result,
			timeout:3000,
			showType:'slide'

		});
	}
	
	
    $('#submitSku').bind('click', function(){    
    	if($("#uploadPreSaleFile").val()==""||typeof($('#uploadPreSaleFile').val()) == "undefined"){
    		$.messager.alert('警告','请选择excel文件'); 
    	}else{
    		$("#uploadPreSaleFileAndDown").submit();
    	}  
    });
    $('#submitdeleteSku').bind('click', function(){    
    	if($("#deletePreSaleFile").val()==""||typeof($('#deletePreSaleFile').val()) == "undefined"){
    		$.messager.alert('警告','请选择excel文件'); 
    	}else{
    		$("#deletePreSaleFileAction").submit();
    	}  
    });
	
});

</script>
	<div id="showExcel" hidden="hidden">
		<h5>若下载受限请限制下载数,默认30</h5>
		<h5>传入excel(07/2010版)表格格式,表名不能为中文</h5>
		<h5>第二列必须有,但可以为空。第一行列名，从第二行开始下载</h5>
		<form action="">
			<table border="1">
				<tr>
					<td>名称1</td>
					<td>名称2</td>
					<td>url1</td>
					<td>url2</td>
					<td>......</td>
				</tr>
				<tr>
					<td>code</td>
					<td>blue</td>
					<td>http://1.1.1.1/adeg.jpg</td>
					<td>http://1.1.1.1/lkjh.jpg</td>
					<td>......jpg</td>
				</tr>
			</table>
		</form>
		<h5>下载结果为 code blue_1.jpg， code blue_2.jpg</h5>
	</div>

	<div id="showSkuExcel" hidden="hidden">
		<h5>excel格式</h5>
		<h5>传入excel(07/2010版)表格格式</h5>
		<form action="">
			<table border="1">
				<tr>
					<td>supplierId</td>
					<td>supplierSkuId</td>
				</tr>
				<tr>
					<td>2016******</td>
					<td>*****</td>
				</tr>
				<tr>
					<td>2016******</td>
					<td>*****</td>
				</tr>
				<tr>
					<td>.......</td>
					<td>.......</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="stock" style="text-align: left; padding: 0px 20px 20px 20px;">
		<table border="1">
			<tr>
				<td>supplierId</td>
				<td>supplierName</td>
				<td>updateTime</td>
				<td>当前时间差</td>
				<td>错误原因</td>
				<td>端口</td> 
				<td>其他</td> 
				<td>邮箱</td> 
				<td>启用</td> 
			</tr>
			<c:forEach items="${redList}" var="reddata">
				<tr bgcolor="Salmon">
					<td>${reddata.supplierId}</td>
					<td>${reddata.supplierName}</td>
					<td><fmt:formatDate value="${reddata.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${reddata.dif }</td>
					<form id="${gredata.supplierId}">
						<td><input type="text" name="errorNum" value="${reddata.errorNum}" onchange="changeReason('${reddata.supplierId}',this.value,'errorNum')"/></td>
						<td><input type="text" name="rightNum" value="${reddata.rightNum}" onchange="changeReason('${reddata.supplierId}',this.value,'rightNum')"/></td>
						<td><input type="text" name="totalNum" value="${reddata.totalNum}" onchange="changeReason('${reddata.supplierId}',this.value,'totalNum')"/></td>
					</form>
					<td ><input type="text" name="email" style="width:420px;" value="${reddata.email }"  onchange="changeReason('${reddata.supplierId}',this.value,'email')"/></td>
					<c:choose>
						<c:when test="${reddata.status == 1}">
						<td><input type=checkbox checked value="0"  onchange="changeReason('${reddata.supplierId}',this.value,'status')"></td>
						</c:when>
						<c:otherwise>
						<td><input type=checkbox  value="1" onchange="changeReason('${reddata.supplierId}',this.value,'status')"></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
			<c:forEach items="${greenList}" var="gredata">
				<tr>
					<td>${gredata.supplierId}</td>
					<td>${gredata.supplierName}</td>
					<td><fmt:formatDate value="${gredata.updateTime }"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${gredata.dif }</td>
					<form id="${gredata.supplierId}">
						<td><input type="text" name="errorNum" value="${gredata.errorNum}" onchange="changeReason('${gredata.supplierId}',this.value,'errorNum')"/></td>
						<td><input type="text" name="rightNum" value="${gredata.rightNum}" onchange="changeReason('${gredata.supplierId}',this.value,'rightNum')"/></td>
						<td><input type="text" name="totalNum" value="${gredata.totalNum}" onchange="changeReason('${gredata.supplierId}',this.value,'totalNum')"/></td>
					</form>
					
					<td ><input type="text" name="email" style="width:420px;" value="${gredata.email }"  onchange="changeReason('${gredata.supplierId}',this.value,'email')"/></td>
					<c:choose>
						<c:when test="${gredata.status == 1}">
						<td><input type=checkbox checked value="0"  onchange="changeReason('${gredata.supplierId}',this.value,'status')"></td>
						</c:when>
						<c:otherwise>
						<td><input type=checkbox  value="1" onchange="changeReason('${gredata.supplierId}',this.value,'status')"></td>
						</c:otherwise>
					</c:choose>
						
				</tr>
			</c:forEach>
		</table>
	</div>
	<div id="order" style="text-align: left; padding: 0px 20px 20px 20px;">
		<table border="1">
			<tr>
				<td>supplierId</td>
				<td>supplierName</td>
				<td>updateTime</td>
				<td>当前时间差</td>

			</tr>
			<c:forEach items="${redOrderList}" var="reddata">
				<tr bgcolor="Salmon">
					<td>${reddata.supplierId}</td>
					<td>${reddata.supplierName}</td>
					<td><fmt:formatDate value="${reddata.updateTime }"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${reddata.dif }</td>
				</tr>
			</c:forEach>
			<c:forEach items="${greenOrderList}" var="gredata">
				<tr>
					<td>${gredata.supplierId}</td>
					<td>${gredata.supplierName}</td>
					<td><fmt:formatDate value="${gredata.updateTime }"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${gredata.dif }</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>
