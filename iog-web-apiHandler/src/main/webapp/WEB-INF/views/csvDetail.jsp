<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
 <%
    String bathPath = request.getContextPath();
 %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="keywords" content="jquery,ui,easy,easyui,web">
	<meta name="description" content="easyui help you build your web page easily!">
	<title>csvDetail</title>
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.net/Public/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.net/Public/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.net/Public/js/easyui/demo/demo.css">
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.min.js"></script>
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.json-2.4.js"></script>
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.edatagrid.js"></script>
	<script type="text/javascript">
		$(function(){
			$('#dg').edatagrid({
				url: 'getcsvDetail?supplierId=${supplierId}',
				saveUrl: 'saveCsvDetail?supplierId=${supplierId}',
				updateUrl: 'updateCsvDetail',
				destroyUrl: 'destroyCsvDetail'
			});
		});
		
	</script>
	<style type="text/css"> 
		.align-center{ 
			margin:0 auto; /* 居中 这个是必须的，，其它的属性非必须 */ 
			width:900px; /* 给个宽度 顶到浏览器的两边就看不出居中效果了 */
			height:650px;
		} 
	</style> 
</head>
<body>
	<h2>详情页csv各个属性</h2>
	<div class="demo-info" style="margin-bottom:10px">
		<div class="demo-tip icon-tip">&nbsp;</div>
		<div>Double click the row to begin editing.</div>
	</div>
	<div style="margin:20px 0;"></div>
	
	<div class="align-center"> 
		<table id="dg" title="" style="width:100%;height:100%;"
				toolbar="#toolbar" pagination="true" idField="id"
				rownumbers="true" fitColumns="true" singleSelect="true">
			<thead>
				<tr>
					<th field="id" hidden="true"></th>
					<th field="supplierId" hidden="true"></th> 
					<th field="supplierName" width="150" >供货商名称</th>
					<th field="attriName" width="150" editor="{type:'validatebox',options:{required:true}}">属性名称</th>
					<th field="attriValue" width="150" editor="{type:'validatebox'}">属性对应列名/号</th>
					<th field="attriRule" width="150" editor="{type:'validatebox'}">拆分规则</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="javascript:$('#dg').edatagrid('addRow')">New</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="javascript:$('#dg').edatagrid('destroyRow')">Destroy</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:$('#dg').edatagrid('saveRow')">Save</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:$('#dg').edatagrid('cancelRow')">Cancel</a>
		</div>
	</div>
	 
</body>
</html>