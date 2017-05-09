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
	<link rel="stylesheet" type="text/css" href="<%=bathPath%>/js/jquery-easyui-1.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=bathPath%>/js/jquery-easyui-1.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=bathPath%>/js/jquery-easyui-1.4/demo.css">
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.min.js"></script>
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script>
function test(){
	$.ajax({
        type : "POST",
        dataType : "json",
        async: false,
        url : "hubOrderPage",
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
    		return 'background-color:#C7EDCC;';
    		}	,
	    columns:[[    
	        {field:'supplierName',title:'供货商名称',
	        	styler: function(index,row,index){
	        		return 'font-size:45px';
	        		}	
	        
	        },    
	        {field:'spMasterOrderNo',title:'主订单号',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}	},
      		  {field:'orderNo',title:'子订单号',
  	        	styler: function(index,row,index){
  	        		return 'font-size:15px';
  	        		}	},
	        {field:'spPurchaseNo',title:'采购单编号',
	    	        	styler: function(index,row,index){
	    	        		return 'font-size:15px';
	    	        		}	}, 
	        {field:'orderStatus',title:'订单状态',
	    	    	        	styler: function(index,row,index){
	    	    	        		return 'font-size:15px';
	    	    	        		}	,
	        	styler: function(value,row,index){
					if (row.orderStatus==="采购异常"||row.orderStatus==="应该采购异常"||row.orderStatus==="采购异常失败"){
						return 'color:red;';
					} 
				}
			}, 
		    {field:'pushStatus',title:'推送状态',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}	,
			styler: function(value,row,index){
				if (row.pushStatus==="订单推送失败"||row.pushStatus==="锁库取消失败"||row.pushStatus==="锁库失败"||row.pushStatus==="退单失败"){
					return 'color:blue;';
				} 
			}
			}, 
	        {field:'spSku',title:'spSkuId',
				styler: function(index,row,index){
		return 'font-size:15px';
		}	}, 
	        {field:'supplierSku',title:'supplierSkuId',
        	styler: function(index,row,index){
        		return 'font-size:15px';
        		}	}, 
	        {field:'createTime',title:'CreateTime',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}	
        	,formatter: function (value, row, index) {
	        	var time = getDate(new Date(value.time));
                 return time;                                 
            }
		}, 
        {field:'lockTime',title:'LockTime',
        	styler: function(index,row,index){
        		return 'font-size:15px';
        		}	
			,formatter: function (value, row, index) {
				if(value==null){
					return;
				}
	        	var time = getDate(new Date(value.time));
                 return time;                                 
            }
		}, 
		{field:'payTime',title:'PayTime',
        	styler: function(index,row,index){
        		return 'font-size:15px';
        		}	
			,formatter: function (value, row, index) {
				if(value==null){
					return;
				}
	        	var time = getDate(new Date(value.time));
                 return time;                                 
            }
		},{field:'confirmTime',title:'ConfirmTime',
        	styler: function(index,row,index){
        		return 'font-size:15px';
        		}	
			,formatter: function (value, row, index) {
				if(value==null){
					return;
				}
	        	var time = getDate(new Date(value.time));
                 return time;                                 
            }
		},
	        {field:'excDesc',title:'异常原因',width:200,
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}	}
	    ]]    
	}); 
	
    var pager=$('#dg').datagrid('getPager');
    	pager.pagination({
        pageNumber : 1,
        pageSize:100,
        pageList : [50,100,300,500],// 可以设置每页记录条数的列表
        onSelectPage: function (pageNumber, pageSize) {//分页触发  
            find(pageNumber, pageSize);  
            //query(pageNumber, pageSize);
        }
    });		

});

	function find(pageNumber, pageSize)
	{
	        $("#dg").datagrid('getPager').pagination({pageSize : pageSize, pageNumber : pageNumber});//重置
	        $("#dg").datagrid("loading"); //加屏蔽
	        $.ajax({
	            type : "POST",
	            dataType : "json",
	            url : "hubOrderPage",
	            data : {
                	 'page' : pageNumber,
                     'rows' : pageSize,
                     'supplierId':'${supplierId}',
                     'CGD':	$("#CGD").val(),
                     'supplierSkuId':$("#supplierSkuId").val(),
                     'spSkuId':$("#spSkuId").val(),
                     'orderStatus':$("#orderStatus").val(),
                     'pushStatus':$("#pushStatus").val()
	                
	            },
	            success : function(data) {
	                $("#dg").datagrid('loadData',pageData(data.rows,data.total));//这里的pageData是我自己创建的一个对象，用来封装获取的总条数，和数据，data.rows是我在控制器里面添加的一个map集合的键的名称
	                var total =data.total;
	                console.info(data);
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
	function query(pageNumber, pageSize){
		$("#dg").datagrid('getPager').pagination({pageSize : pageSize, pageNumber : pageNumber});//重置
        $("#dg").datagrid("loading"); //加屏蔽

        $.ajax({
            type : "POST",
            dataType : "json",
            url : "hubOrderPage",
            data : {
                'page' : pageNumber,
                'rows' : pageSize,
                'supplierId':'${supplierId}',
                'CGD':	$("#CGD").val(),
                'supplierSkuId':$("#supplierSkuId").val(),
                'spSkuId':$("#spSkuId").val(),
                'orderStatus':$("#orderStatus").val(),
                'pushStatus':$("#pushStatus").val()
                
            },
            success : function(data) {
                $("#dg").datagrid('loadData',pageData(data.rows,data.total));//这里的pageData是我自己创建的一个对象，用来封装获取的总条数，和数据，data.rows是我在控制器里面添加的一个map集合的键的名称
                var total =data.total;
                console.info(data);
                $("#dg").datagrid('getPager').pagination({total : total});//重置
                $("#dg").datagrid("loaded"); //移除屏蔽
            }
        });
		
	}

</script>
</head>

	<body class="easyui-layout" onload="test();">  
	 <div data-options="region:'north'">
		 
		  	<strong>CGD：</strong>
			           <input class="easyui-validatebox" type="text" name="CGD" id="CGD"></input>
			   <strong>supplierSkuId：</strong>
			           <input class="easyui-validatebox" type="text" name="supplierSkuId" id="supplierSkuId"></input>
			            <strong>spSkuId：</strong>
			  
			            <input class="easyui-validatebox" type="text" name="spSkuId" id="spSkuId" ></input>
			        <strong>支付状态：</strong>
			        <select name="search_type" id="orderStatus" >
			            <option value="">--请选择--</option>
			            <option value="0" >未支付</option>
			            <option value="1" >未支付取消</option>
			            <option value="2" >支付</option>
			            <option value="3" >退款</option>
			            <option value="4" >已发货</option>
			            <option value="5" >采购异常</option>
			            <option value="6" >应该采购异常</option>
			            <option value="7" >采购异常失败</option>
			        </select>
			        <strong>推送状态：</strong>
			         <select name="search_type" id="pushStatus" >
			            <option value="">--请选择--</option>
			            <option value=0 >无锁库API</option>
			            <option value="1" >锁库</option>
			            <option value="2" >无库存</option>
			            <option value="3" >锁库失败</option>
			            <option value="4" >订单推送确认</option>
			            <option value="5" >订单推送失败</option>
			            <option value="7" >锁库取消失败</option>
			            <option value="8" >无锁库取消API</option>
			            <option value="9" >退单</option>
			            <option value="10" >退单失败</option>
			            <option value="11" >无退单API</option>
			        </select>
			        <button value="ssss" onclick="query(1,100)">Query</button>
		 </div>
	    <div data-options="region:'center'">
			<table id="dg"></table>
	    </div>  
	</body>
</html>