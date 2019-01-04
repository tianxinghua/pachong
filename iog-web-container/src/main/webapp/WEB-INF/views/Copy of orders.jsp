<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%
    String bathPath = request.getContextPath();
// String supplierId = request.getParameter("supplier");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询订单列表</title>
<link rel="stylesheet"
	href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/icon.css"
	type="text/css" media="screen" />
<link rel="stylesheet"
	href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/default/easyui.css"
	type="text/css" />
<!-- <link rel="stylesheet" -->
<%-- 	href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/main.css" --%>
<!-- 	type="text/css" /> -->
<script type="text/javascript"
	src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery-1.8.3.min.js"></script>
<script type="text/javascript"
	src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=bathPath%>/js/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<!-- <script type="text/javascript" -->
<%-- 	src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.json-2.4.js"></script> --%>

<script>
function test(){
	$.ajax({
        type : "POST",
        dataType : "json",
        async: false,
        url : "orderPage",
        data : {
            'page' : 1,
            'rows' : 100,
            'supplierId':'${supplierId}'
        },
        success : function(data) {
            $("#dg").datagrid('loadData',pageData(data.rows,data.total));//这里的pageData是我自己创建的一个对象，用来封装获取的总条数，和数据，data.rows是我在控制器里面添加的一个map集合的键的名称
            var total =data.total;
            console.info(total);
            $("#dg").datagrid('getPager').pagination({total : total});//重置
            $("#dg").datagrid("loaded"); //移除屏蔽
        },
        error : function(err) {
            $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
            $("#dg").datagrid("loaded"); //移除屏蔽
        }
    });
}

	function getDate(date){
// 		var date = ; //获取一个时间对象  注意：如果是uinx时间戳记得乘于1000。比如php函数time()获得的时间戳就要乘于1000
// 		console.info(date);
		var year = date.getFullYear();  // 获取完整的年份(4位,1970)
		var Month = date.getMonth()+1;  // 获取月份(0-11,0代表1月,用的时候记得加上1)
		if(Month<10){
			Month = "0"+Month;
		}
		var Date = date.getDate();  // 获取日(1-31)
		if(Date<10){
			Date = "0"+Date;
		}
		var Hours = date.getHours();  // 获取小时数(0-23)
		if(Hours<10){
			Hours = "0"+Hours;
		}
		var Minutes = date.getMinutes();  // 获取分钟数(0-59)
		if(Minutes<10){
			Minutes = "0" + Minutes;
		}
		var Seconds = date.getSeconds(); 
		if(Seconds<10){
			Seconds = "0" + Seconds;
		}
		var dateTime = year+"-"+Month+"-"+Date+" "+Hours+":"+Minutes+":"+Seconds;
// 		console.info(dateTime);
		return dateTime;
	}

$(function(){
	 
	$('#dg').datagrid({    
	    fitColumns:true,
		fit:true,
	    singleSelect : true, /*是否选中一行*/
//         width:'100%', 
//         height:'auto',
//         border:false,
        rownumbers:true,
		pagination:true,
		pageSize:100,  nowrap:false,
    	rowStyler: function(index,row,index){
    		return 'font-size:505px';
    		}	,
	    columns:[[    
	        {field:'supplierName',title:'供货商名称',width:'100',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}	
	        
	        },    
	        {field:'spOrderId',title:'尚品订单编号',width:'250',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}	},
	        {field:'spPurchaseNo',title:'采购单编号',width:'150',
	    	        	styler: function(index,row,index){
	    	        		return 'font-size:15px';
	    	        		}	}, 
	        {field:'status',title:'订单状态',width:'100',
	    	    	        	styler: function(index,row,index){
	    	    	        		return 'font-size:15px';
	    	    	        		}	,
	        	styler: function(value,row,index){
					if (row.status==="采购异常Suc"||row.status==="采购异常Err"){
						return 'color:red;';
					} 
				}
}, 
	        {field:'detail',title:'Detail',width:'250',
	styler: function(index,row,index){
		return 'font-size:15px';
		}	}, 
	        {field:'memo',title:'Memo',width:'150',
        	styler: function(index,row,index){
        		return 'font-size:15px';
        		}	}, 
	        {field:'createTime',title:'CreateTime',width:'150',
    	        	styler: function(index,row,index){
    	        		return 'font-size:15px';
    	        		}	
	        	,formatter: function (value, row, index) {
		        	var time = getDate(new Date(row.createTime.time));
	                 return time;                                 
	            }
			}, 
	        {field:'updateTime',title:'UpdateTime',width:'150',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}	
				,formatter: function (value, row, index) {
		        	var time = getDate(new Date(row.createTime.time));
	                 return time;                                 
	            }
			}, 
	        {field:'excDesc',title:'异常原因',width:'300',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}	}, 
	        {field:'uuId',title:'UuId',width:'250',
	    	        	styler: function(index,row,index){
	    	        		return 'font-size:15px';
	    	        		}	}
	    ]]    
	}); 
	
    var pager=$('#dg').datagrid('getPager');
//     find(1,10);
    pager.pagination({
        pageNumber : 1,
        pageSize:100,
        pageList : [ 10,20,30,50,100 ],// 可以设置每页记录条数的列表
        onSelectPage: function (pageNumber, pageSize) {//分页触发  
            find(pageNumber, pageSize);  
        }
    });
  //设置分页控件  
//     var p = $('#tt').datagrid('getPager');  
//     p.pagination({  
//         pageSize: 5,//每页显示的记录条数，默认为10  
//         pageList: [5, 10, 15],//可以设置每页记录条数的列表  
//         beforePageText: '第',//页数文本框前显示的汉字  
//         afterPageText: '页    共 {pages} 页',  
//         displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'  
//     });  

});
function find(pageNumber, pageSize)
{
        $("#dg").datagrid('getPager').pagination({pageSize : pageSize, pageNumber : pageNumber});//重置
        $("#dg").datagrid("loading"); //加屏蔽
        $.ajax({
            type : "POST",
            dataType : "json",
            url : "orderPage",
            data : {
                'page' : pageNumber,
                'rows' : pageSize,
                'supplierId':'${supplierId}'
            },
            success : function(data) {
                $("#dg").datagrid('loadData',pageData(data.rows,data.total));//这里的pageData是我自己创建的一个对象，用来封装获取的总条数，和数据，data.rows是我在控制器里面添加的一个map集合的键的名称
                var total =data.total;
                console.info(total);
                $("#dg").datagrid('getPager').pagination({total : total});//重置
                $("#dg").datagrid("loaded"); //移除屏蔽
            },
            error : function(err) {
                $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
                $("#dg").datagrid("loaded"); //移除屏蔽
            }
        });
}
function pageData(list,total){
    var obj=new Object(); 
    obj.total=total; 
    obj.rows=list; 
    return obj; 
}
// $('#pp').pagination({total:100});
// $('#pp').pagination({
//     pageSize:10,//每页显示的大小。
//     pageList: [10,20,50,100],//选择每页显示的记录数的下拉框的值。
//     onSelectPage: function(pageNumber, pageSize){//选择相应的页码时刷新显示内容列表。
//     	  $("#high_light1").html("");
//         var search = {
//   	          supplier:   $('#supplierId').val(),
//   	          startDate:    null,
//   	          endDate:      null,
//   	          pageIndex: pageNumber,
//   	          pageSize:pageSize,
//   	          supplierName:null,
//   	          flag:null
//   	      };
    	
//     	var html="";
//     	html += "<table width='100%' width='100%' class='table_border' border='0' id='high_light1' lang='tabRowData' cellpadding='0' cellspacing='0' bgcolor='#C7EDCC' >";
//     	html += "<tr><td width='50'>序号</td>";
//     	html += "<td width='50'>供货商名称</td>";
//     	html += "<td width='50'>尚品订单编号</td>";
//     	html += "<td width='50'>采购单编号</td>";
//     	html += "<td width='50'>订单状态</td>";
//     	html += "<td width='50'>Detail</td>";
//     	html += "<td width='50'>Memo</td>";
//     	html += "<td width='50'>CreateTime</td>";
//     	html += "<td width='50'>UpdateTime</td>";
//     	html += "<td width='50'>异常原因</td>";
//     	html += "<td width='50'>UuId</td></tr>";
//     	alert('pageNumber:'+pageNumber+',pageSize:'+pageSize);
//     	$.ajax({
//             type: "GET",
//             async: false,
//             url: 'orders?queryJson='+$.toJSON(search),
//             data: {username:$("#username").val(), content:$("#content").val()},
//             dataType: "json",
//             success: function(data){
//             	var array = eval(data).orderList;
//             	var htmlTemp ="";
//             	console.info(array);
//             }
//     	});
//     	html+="</table>";
//     	console.info(html);
//         $("#high_light1").html(html); 
// //         $('#pp').pagination({total:100});
//     	$(this).pagination('loading');  
//         $(this).pagination('loaded'); 
//  	}});
 
// 	function filter(){
	  
// 	      return search;
// 	}

</script>
</head>
<body class="easyui-layout" data-options="fit:true" onload="test();">  
    <div data-options="region:'center'"  style="height:100px" style="background:#eee;">
    <table id="dg"></table>  

<!-- <table width="100%" width="100%" class="table_border" border="0" id="high_light" lang="tabRowData"   -->
<!--          cellpadding="0" cellspacing="0" bgcolor="#C7EDCC" > -->
<!-- <tr> -->
<!-- 	<td width="50">序号</td> -->
<!-- 	<td width="50">供货商名称</td> -->
<!-- 	<td width="50">尚品订单编号</td> -->
<!-- 	<td width="50">采购单编号</td> -->
<!-- 	<td width="80">订单状态</td> -->
<!-- 	<td width="50">Detail</td> -->
<!-- 	<td width="50">Memo</td> -->
<!-- 	<td width="50">CreateTime</td> -->
<!-- 	<td width="50">UpdateTime</td> -->
<!-- 	<td width="100">异常原因</td> -->
<!-- 	<td width="50">UuId</td> -->
<!-- </tr> -->
<%-- <c:forEach items="${orderList }" var="order" varStatus="status"> --%>
<!-- <tr> -->
	
<%-- 	<td>${status.index + 1}</td> --%>
<%-- 	<td>${order.supplierName }</td> --%>
<%-- 	<td>${order.spOrderId }</td> --%>
<%-- 	<td>${order.spPurchaseNo }</td> --%>
<%-- 	<td>${order.status }</td> --%>
<%-- 	<td>${order.detail }</td> --%>
<%-- 	<td>${order.memo }</td> --%>
<%-- 	<td><fmt:formatDate value="${order.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>
<%-- 	<td><fmt:formatDate value="${order.updateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>
<%-- 	<td>${order.excDesc }</td> --%>
<%-- 	<td>${order.uuId }</td> --%>
<%-- 	<td><input type="hidden" id="${order.supplierId }"></td> --%>
<!-- </tr> -->
<%-- </c:forEach> --%>

<!-- </table> -->
    </div>   
</body>
</html>