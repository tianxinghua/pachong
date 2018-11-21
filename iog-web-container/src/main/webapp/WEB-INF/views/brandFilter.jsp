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
<title>查询管理列表</title>
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
        url : "queryBrandFilter",
        data : {
            'page' : 1,
            'rows' : 100
        },
        success : function(data) {
            $("#dg").datagrid('loadData',pageData(data.rows,data.total));//这里的pageData是我自己创建的一个对象，用来封装获取的总条数，和数据，data.rows是我在控制器里面添加的一个map集合的键的名称
            var total =data.total;
            $("#dg").datagrid('getPager').pagination({total : total});//重置
			$("#dg").datagrid("loaded"); //移除屏蔽
        },
        error : function(err) {
            $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
            $("#dg").datagrid("loaded"); //移除屏蔽
        }
    });
}
    function searchFunction(){
        $.ajax({
            type : "POST",
            dataType : "json",
            async: false,
            url : "queryBrandFilter",
            data : {
                'page' : 1,
                'rows' : 100,
                'hubBrandNo' : $('#hubBrandNo').val(),
                'hubCategoryNo' : $('#hubCategoryNo').val(),
                'filterType':$('#filterType').val()
            },
            success : function(data) {
                $("#dg").datagrid('loadData',pageData(data.rows,data.total));//这里的pageData是我自己创建的一个对象，用来封装获取的总条数，和数据，data.rows是我在控制器里面添加的一个map集合的键的名称
                var total =data.total;
                $("#dg").datagrid('getPager').pagination({total : total});//重置
				$("#dg").datagrid("loaded"); //移除屏蔽
            },
            error : function(err) {
                $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
                //$("#dg").datagrid("loaded"); //移除屏蔽
            }
        });
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
	        {field:'hubBrandNo',title:'品牌编号',align:'center',width:'300',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}
	        },    
	        {field:'hubCategoryNo',title:'分类编号',align:'center',width:'300',
	        	styler: function(index,row,index){
	        		return 'font-size:15px';
	        		}
			},
			{field:'filterType',title:'状态',align:'center',width:'300',
				styler: function(value,row,index){
					return 'font-size:15px';
					},
                formatter: function(value,row,index){
                    if (row.filterType ==1){
                        return '未过滤';
                    }else{
                        return '过滤';
					}
                }
			},
            {field:'operate',title:'操作',align:'center',width:$(this).width()*0.1,
                formatter:function(value, row, index) {
                var value1 = row.hubBrandNo;
                var value2 = row.hubCategoryNo;
                var value3 = row.filterType;
                    var str;
                    if (row.filterType ==1){
						str = '<a href="javascript:" name="opera"  onclick="updateType('+"'"+value1+"'"+','+"'"+value2+"'"+','+"'"+value3+"'"+');" >点击变为：过滤</a>';
						return str;
                    }else{
                        str = '<a href="javascript:" name="opera"  onclick="updateType('+"'"+value1+"'"+','+"'"+value2+"'"+','+"'"+value3+"'"+');">点击变为：不过滤</a>';
                        return str;
                    }
                }
            }
	    ]]
	/*onLoadSuccess:function(data) {
        $("a[name='opera']").linkbutton({text: '', plain: true, iconCls: 'icon-add'});
    }*/
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
        async: false,
        url : "queryBrandFilter",
        data : {
            'page' : pageNumber,
            'rows' : pageSize,
            'hubBrandNo' : $('#hubBrandNo').val(),
            'hubCategoryNo' : $('#hubCategoryNo').val(),
            'filterType':$('#filterType').val()
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
function updateType(value1,value2,value3){
    //将未处理的变成处理状态，将处理的变成不处理状态
    if(value3 == 1){
        value3 = 0;
	}else{
        value3 = 1;
	}
    $.ajax({
        type : "POST",
        dataType : "text",
        async: false,
        url : "updateType",
        data : {
            'hubBrandNo' : value1,
            'hubCategoryNo' : value2,
            'filterType':value3
        },
        success : function(data) {
            if("success"== data){
                $.messager.alert('操作成功', '操作成功!', 'success');
            }else{
                $.messager.alert('操作失败', '请重新操作!', 'error');
            }
            test();
           // $("#dg").datagrid("loaded"); //移除屏蔽
        },
        error : function(err) {
            $.messager.alert('操作失败', '请重新操作!', 'error');
            test();
            //$("#dg").datagrid("loaded"); //移除屏蔽
        }
    });
}
</script>
</head>
<body class="easyui-layout" onload="test();">
<%--<div id="test">--%>
	<div data-options="region:'north'">
	<form id="ff"  method="post">
		<table>
			<tr>
				<td>品牌编号:</td>
				<td><input name="hubBrandNo" id="hubBrandNo" type="text"></input></td>

				<td>分类编号:</td>
				<td><input name="hubCategoryNo" id="hubCategoryNo" type="text"></input></td>

				<td>状态:</td>
				<%--<td><input name="filterType" id="filterType" type="text"></input></td>--%>
				<td><select name="filterType" id="filterType" style="width: 180px;">
					<option value="">请选择</option>
					<option value="0">过滤</option>
					<option value="1">未过滤</option>
				</select>
				</td>

				<td></td>
				<td><input type="button" value="查询" onclick="searchFunction()"></input></td>
				<%--<td><input type="submit" value="查询" ></input></td>--%>
			</tr>
		</table>
	</form>
</div>
    <div data-options="region:'center'">
		<table id="dg"></table>
    </div>
</body>

</html>