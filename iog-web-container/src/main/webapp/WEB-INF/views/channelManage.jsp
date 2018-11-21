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
	<title>渠道信息维护</title>
	<link rel="stylesheet" type="text/css" href="<%=bathPath%>/js/jquery-easyui-1.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=bathPath%>/js/jquery-easyui-1.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=bathPath%>/js/jquery-easyui-1.4/demo.css">
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.min.js"></script>
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script>
    function searchFunction(){
        $.ajax({
            type : "POST",
            dataType : "json",
            async: false,
            url : "queryByChannel",
            data : {
                "channel":$("#channelName").val()
            },
            success : function(data) {
                $("#dg").datagrid('loadData',pageData(data,0));
                $("#dg").datagrid("loaded"); //移除屏蔽
            },
            error : function(err) {
                $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
                $("#dg").datagrid("loaded"); //移除屏蔽
            }
        });
    }
   /* function searchSupplierIdFunction(){
        $.ajax({
            type : "POST",
            dataType : "json",
            async: false,
            url : "querySupplierId",
            data : {
            },
            success : function(data) {
                $("#dg").datagrid('loadData',data);//这里的pageData是我自己创建的一个对象，用来封装获取的总条数，和数据，data.rows是我在控制器里面添加的一个map集合的键的名称
                $("#dg").datagrid("loaded"); //移除屏蔽
            },
            error : function(err) {
                $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
                $("#dg").datagrid("loaded"); //移除屏蔽
            }
        });
    }*/
    //点击保存按钮
    function saveFunction(){
        var checkedItems = $('#dg').datagrid('getChecked');
        if(checkedItems == null || checkedItems.length<1){
            $.messager.alert('操作提示', '请至少勾选一条数据!', 'error');
            return;
        }
        var list = [];
        for(i = 0;i < checkedItems.length;i++){
            checkedItems[i].channel = $("#channelName").val();
            list.push(checkedItems[i]);
        }
        $.ajax({
            type : "POST",
            dataType : "text",
            async: false,
            url : "saveChannel",
            data : {
                'list' : JSON.stringify(list),
                "channel":$("#channelName").val()
            },
            success : function(data) {
                if("success"== data){
                    $.messager.alert('操作成功', '操作成功!', 'success');
                }else{
                    $.messager.alert('操作失败', '请重新操作!', 'error');
                }
                window.location.href="gotoChannelManage";
            },
            error : function(err) {
                $.messager.alert('操作失败', '请重新操作!', 'error');
                window.location.href="gotoChannelManage";
            }
        });
	}

$(function(){
	 
	$('#dg').datagrid({    
	    fitColumns:true,
		fit:true,
	    singleSelect : false, /*是否选中一行*/
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
            {field:'checkid',checkbox:true},
	        {field:'supplierId',title:'供应商Id',align:'center',width:'300',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}
	        },    
	        {field:'supplierNo',title:'尚品值编码',align:'center',width:'300',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}
			}
	    ]] ,
        onLoadSuccess:function(row){//当表格成功加载时执行
            var rowData = row.rows;
            for(i = 0;i < rowData.length;i++){
                if(rowData[i].flag=="1"){
                    $("#dg").datagrid("selectRow", i);
                }
            }
        }
	});
});

function pageData(list,total){
    var obj=new Object(); 
    obj.total=total; 
    obj.rows=list; 
    return obj; 
}

</script>
</head>
<body class="easyui-layout" >

    <div data-options="region:'center'">
		<div id="test">
			<form id="ff"  method="post">
				<table>
					<tr>
						<td>渠道名称:</td>
						<td><input name="channel" id="channelName" type="text"></input></td>

						<td></td>
						<td><input type="button" value="查询" onclick="searchFunction()"></input></td>
						<%--<td><input type="button" value="查询全部供应商编号" onclick="searchSupplierIdFunction()"></input></td>--%>
						<td><input type="button" value="保存" onclick="saveFunction()"></input></td>
						<%--<td><input type="submit" value="查询" ></input></td>--%>
					</tr>
				</table>
			</form>
		</div>

		<table id="dg"></table>
    </div>
</body>

</html>