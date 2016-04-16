<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <link rel="stylesheet" href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/icon.css" type="text/css" media="screen" />
    <link rel="stylesheet"  href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/default/easyui.css" type="text/css" />
    <link rel="stylesheet" href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/main.css" type="text/css" />
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery-1.10.1.min.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.json-2.4.js"></script>
<style type="text/css"> 
	#showExcel { 
				
				position:absolute;
				padding:0px 20px 20px 20px;
				top:280px;           
				left:350px;         
				width:600px; 
				height:200px;
				background:LightSkyBlue;}  
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
	        var search = {
	            supplier:   $('#supplier').val(),
	            startDate:    $('#startDate').val(),
	            endDate:      $('#endDate').val(),
	            pageIndex: $('#pageIndex').val(),
	            pageSize:$('#pageSize').val(),
	            supplierName:$ ('#supplier').find("option:selected").text(),
	            flag:str
	        };
	        return search;
	}
	function downloadpicture(){
		var search = filter("");
	    alert($.toJSON(search));
	    window.open('downLoadPicture?queryJson='+$.toJSON(search), '','');
	}
    function exportProduct(str) {

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

        /* if("-1"== $('#supplier').val()){
            alert("请选择供应商");

            return;
        } */

        var search = {
            supplier:   $('#supplier').val(),
            startDate:    $('#startDate').val(),
            endDate:      $('#endDate').val(),
            pageIndex: $('#pageIndex').val(),
            pageSize:$('#pageSize').val(),
            supplierName:$ ('#supplier').find("option:selected").text(),
            flag:str

        };
        window.open('csv?queryJson='+$.toJSON(search), '','');
    }

    function exportDiffProduct(str) {


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

        var search = {
            supplier:   $('#supplier').val(),
            startDate:    $('#startDate').val(),
            endDate:      $('#endDate').val(),
            pageIndex: $('#pageIndex').val(),
            pageSize:$('#pageSize').val(),
            supplierName:$ ('#supplier').find("option:selected").text(),
            flag:str

        };
        window.open('csv?queryJson='+$.toJSON(search), '','');
    }
	
//     function updatePrice(){
// 		var supplierId = $('#supplier').val();
// 		if("-1"== supplierId){
// 	         alert("请选择供应商");
// 	         return;
// 	     }
// 		var skuIds = prompt("请输入要更新的skuid用英文,隔开如:123,321,654");
// 		if(skuIds!=null){
// 			$.get("updatePrice", { "supplierId": supplierId, "skuIds": skuIds });
// 		}
// 	}
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
    /**
    *	导出订单
    *
    */
    function exportOrder(str){


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

        /* if("-1"== $('#supplier').val()){
            alert("请选择供应商");

            return;
        } */

        var search = {
            supplier:   $('#supplier').val(),
            startDate:    $('#startDate').val(),
            endDate:      $('#endDate').val(),
            pageIndex: $('#pageIndex').val(),
            pageSize:$('#pageSize').val(),
            supplierName:$ ('#supplier').find("option:selected").text(),
            flag:str

        };
        window.open('csv?queryJson='+$.toJSON(search), '','');
    }
    
    /**
    *	查看订单
    *
    */
    function queryOrder(){


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

        /* if("-1"== $('#supplier').val()){
            alert("请选择供应商");

            return;
        } */

        var search = {
            supplier:   $('#supplier').val(),
            startDate:    $('#startDate').val(),
            endDate:      $('#endDate').val(),
            pageIndex: $('#pageIndex').val(),
            pageSize:$('#pageSize').val(),
            supplierName:$ ('#supplier').find("option:selected").text(),            

        };
        window.open('orders?queryJson='+$.toJSON(search), '','');
    }
    
	/* function exportProductByCond(){
		var iWidth=600; //弹出窗口的宽度;
		var iHeight=400; //弹出窗口的高度;
		var iTop = (window.screen.height-30-iHeight)/2; //获得窗口的垂直位置;
		var iLeft = (window.screen.width-10-iWidth)/2; //获得窗口的水平位置;
		window.open("exportByConditions", "按条件导出", "height="+iHeight+", width="+iWidth+", toolbar =no, menubar=no,top="+iTop+",left="+iLeft+"");
	
	}   */ 
	function show(){
		$("#showExcel").removeAttr("hidden");
	}
	function unshow(){
		$("#showExcel").attr("hidden",'hidden');
	}
</script>
<script type="text/javascript"	src="<%=bathPath%>/js/DatePicker/config.js"></script>
<script type="text/javascript"	src="<%=bathPath%>/js/DatePicker/WdatePicker.js"></script>
</head>
<body>

<div style="padding: 20px 20px 20px 20px;">
    <form action="" id="brandForm">
        <table>
            <tr>
                <td >选择供应商</td>
                <td colspan="3">

                   <select id="supplier">
                       <option value="-1">请选择</option>
                       <c:forEach var="supplier" items="${supplierDTOList}">
                           <option value="${supplier.supplierId}">${supplier.supplierName}</option>
                       </c:forEach>

                   </select>
                </td>
            </tr>
            <tr>

                <td>导出时间:</td>
                <td colspan="3"><input id="startDate" name="startDate" style="width: 130px;" onFocus="var endDate=$dp.$('endDate');WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',minDate:'#F{$dp.$D(\'endDate\',{M:-1})}'})">

                <input id="endDate" name="endDate" style="width: 130px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}',maxDate:'#F{$dp.$D(\'startDate\',{M:+1})}'})"></td>

            </tr>
            <tr>
                <td>开始行数</td><td><input type="text" id="pageIndex" name="pageIndex"/> </td>
                <td>导出行数</td><td><input type="text" id="pageSize" name="pageSize"/> </td>
            </tr>
        </table>
    </form>
</div>
<div style="text-align: left; padding: 0px 20px 20px 20px;"><a
	href="javascript:void(0)" onclick="exportProduct('same')" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>导出</a> 
	<a
	href="javascript:void(0)" onclick="exportProduct('ep_regular')" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>按条件导出</a> 
	<a
	href="javascript:void(0)" onclick="exportDiffProduct('diff')" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>价格变化导出</a>
	<a href="javascript:void(0)"
	onclick="clearText()" id="btn-cancel" icon="icon-cancel" class='easyui-linkbutton'>清空</a>
	<a href="javascript:void(0)" onclick="updatePrice()" id="btn-edit" icon="icon-edit" class='easyui-linkbutton'>更新价格</a>
	<a href="javascript:void(0)" onclick="exportOrder('order')" id="btn-edit" icon="icon-search" class='easyui-linkbutton'>导出订单</a>
	<a href="javascript:void(0)" onclick="queryOrder()" id="btn-edit" icon="icon-search" class='easyui-linkbutton'>查看订单</a>
	<br><br><br>
	<a href="javascript:void(0)" onclick="downloadpicture()" id="dowm" icon="icon-search" class='easyui-linkbutton'>下载图片</a>
		<br><br><br>
	<form action="uploadFileAndDown" method="post" enctype="multipart/form-data">
		<input type="file" name="uploadFile">
		<input type="submit" value ="开始上传" onmouseenter="show()" onmouseleave="unshow()">
	</form>
	<br><br><br>
	<a href="stockUpdateException" onclick="stock()" id="btn-save" icon="icon-search" class='easyui-linkbutton'>库存更新异常查看</a> 
	<a href="orderUpdateException" onclick="order()" id="btn-save" icon="icon-search" class='easyui-linkbutton'>订单更新异常查看</a>
	
	
</div>

<div id="showExcel" hidden="hidden">
	<h5>传入excel表格格式</h5>
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
	<h5>下载结果为  code blue_1.jpg， code blue_2.jpg</h5>
</div>
<script>

</script>
<div id="stock" style="text-align: left; padding: 0px 20px 20px 20px;">
	<table border="1" >
  	<tr>
  		<td>supplierId</td>
  		<td>supplierName</td>
  		<td>updateTime</td>
  		<td>当前时间差</td>
  		<td>更新错误数</td>
  		<td>更新正确数</td>
  		<td>更新总数</td>
  	</tr>
	<c:forEach items="${redList}" var="reddata">
	  	<tr bgcolor="Salmon">
	  		<td>${reddata.supplierId}</td>
	  		<td>${reddata.supplierName}</td>
	  		<td><fmt:formatDate value="${reddata.updateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	  		<td>${reddata.dif }</td>
	  		<td>${reddata.errorNum}</td>
	  		<td>${reddata.rightNum}</td>
	  		<td>${reddata.totalNum}</td>
	  	</tr>
  	</c:forEach>
  	<c:forEach items="${greenList}" var="gredata">
	  	<tr >
	  		<td>${gredata.supplierId}</td>
	  		<td>${gredata.supplierName}</td>
	  		<td><fmt:formatDate value="${gredata.updateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	  		<td>${gredata.dif }</td>
	  		<td>${gredata.errorNum}</td>
	  		<td>${gredata.rightNum}</td>
	  		<td>${gredata.totalNum}</td>
	  	</tr>
  	</c:forEach>
  </table>
</div>
<div id="order" style="text-align: left; padding: 0px 20px 20px 20px;">
	<table border="1" >
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
	  		<td><fmt:formatDate value="${reddata.updateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	  		<td>${reddata.dif }</td>
	  	</tr>
  	</c:forEach>
  	<c:forEach items="${greenOrderList}" var="gredata">
	  	<tr >
	  		<td>${gredata.supplierId}</td>
	  		<td>${gredata.supplierName}</td>
	  		<td><fmt:formatDate value="${gredata.updateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	  		<td>${gredata.dif }</td>
	  	</tr>
  	</c:forEach>
  </table>
</div>
</body>
</html>