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
	<meta charset="UTF-8">
	<title>csv</title>
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.net/Public/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.net/Public/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.net/Public/js/easyui/demo/demo.css">
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.min.js"></script>
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.4/jquery.json-2.4.js"></script>
	
	<style type="text/css">
		#fm{
			margin:0;
			padding:10px 30px;
		}
		.ftitle{
			font-size:14px;
			font-weight:bold;
			color:#666;
			padding:5px 0;
			margin-bottom:10px;
			border-bottom:1px solid #ccc;
		}
		.fitem{
			margin-bottom:5px;
		}
		.fitem label{
			display:inline-block;
			width:80px;
		}
	</style>
	
	<script type="text/javascript">
		var url;
		function newUser(){
			$('#dlg').dialog('open').dialog('setTitle','Add');
			$('#fm').form('clear');									
			url = 'save_user';
		}
		function editUser(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$('#dlg').dialog('open').dialog('setTitle','Edit');
				$('#fm').form('load',row);				
				url = 'updateCsv';
			}else{
				alert("请选中一行");
			}
		}
		function saveUser(){
			var usr = {};
			usr.supplierId=$('#supplierId').val();
			usr.supplierNo=$('#supplierNo').val();
			usr.crontime=$('#crontime').val();
			usr.fetchUrl=$('#fetchUrl').val();
			usr.status=$('#status').val();
			var jsonDate = $.toJSON(usr);		
			$('#fm').form('submit',{
				url: url,
				onSubmit: function(){
					return $(this).form('validate');
				},
				data:jsonDate,
				dataType:'json',
				contentType:'application/json;charset=utf-8',
				success: function(result){
					var result = eval('('+result+')');
					if (result.success){
						$('#dlg').dialog('close');		// close the dialog
						$('#dg').datagrid('reload');	// reload the user data
					} else {
						$.messager.show({
							title: 'Error',
							msg: result.msg
						});
					}
				}
			});
		}
		function removeUser(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.confirm('Confirm','确实要删除该供货商以及全部属性？',function(r){
					if (r){
						$.post('removeCsv',{supplierId:row.supplierId},function(result){
							if (result.success){
								$('#dg').datagrid('reload');	// reload the user data
							} else {
								$.messager.show({	// show error message
									title: 'Error',
									msg: result.msg
								});
							}
						},'json');
					}
				});
			}else{
				alert("请先选中一行"); 
			}
		}
		
		function formatOper(val,row,index){ 
    		return '<a href="javascript:void(0)" onclick="editAttibute('+index+')">编辑属性</a>';  
		} 
		
		function editAttibute(index){
			$('#dg').datagrid('selectRow',index);
			var row = $('#dg').datagrid('getSelected');
			if(row){
				window.open('csvDetail?supplierId='+row.supplierId, '','');
			}			
		}
	</script>
	
</head>

<body>	

<h2>通用配置csv供货商操作界面</h2>
	<div class="demo-info" style="margin-bottom:10px">
		<div class="demo-tip icon-tip">&nbsp;</div>
		<div>Click the buttons on datagrid toolbar to do crud actions.</div>
	</div>
	
	<table id="dg" title="" class="easyui-datagrid" 
			url="CsvSuppliers"
			toolbar="#toolbar" pagination="true"
			rownumbers="true" fitColumns="true" singleSelect="true">
		<thead>
			<tr>
				<th field="supplierId" width="80">supplierId</th>
				<th field="supplierNo" width="50">supplierNo</th>
				<th field="crontime" width="80">cron time</th>
				<th field="fetchUrl" width="200">fetchUrl</th>
				<th field="classPath" width="100">classPath</th>
				<th field="dataType" width="50">dataType</th>
				<th field="filePath" width="100">filePath</th>
				<th field="toMapCondition" width="50">策略</th>
				<th field="sep" width="50">分隔符</th>
				<th field="xmlSkuTag" width="50">xmlSkuTag</th>
				<th field="xmlSpuTag" width="50">xmlSpuTag</th>
				<th field="picFlag" width="50">picFlag</th>
				<th field="picPath" width="100">picPath</th>
				<th field="state" width="50">status</th>
				<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th>  
				<!-- <th data-options="field:'status',width:50,align:'center',editor:{type:'checkbox',options:{on:'P',off:''}}">status</th> -->
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">添加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeUser()">删除</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:500px;height:630px;padding:5px 30px"
			closed="true" buttons="#dlg-buttons">
		<div class="ftitle">c-s-v</div>
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<label>supplierId:</label>
				<input style="width:250px;height:20px" id="supplierId" name="supplierId" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>supplierNo:</label>
				<input style="width:250px;height:20px" id="supplierNo" name="supplierNo" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>cron time:</label>
				<input style="width:250px;height:20px" id="crontime" name="crontime" class="easyui-validatebox" >
			</div>
			<div class="fitem">
				<label>fetchUrl:</label>
				<input style="width:250px;height:20px" id="fetchUrl" name="fetchUrl" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>status:</label>
				<input style="width:250px;height:20px" id="status" name="status" class="easyui-validatebox" > 
			</div>
			<div class="fitem">
				<label>classPath:</label>
				<input style="width:250px;height:20px" id="classPath" name="classPath" class="easyui-validatebox" > 
			</div>
			<div class="fitem">
				<label>dataType:</label>
				<input style="width:250px;height:20px" id="dataType" name="dataType" class="easyui-validatebox" > 
			</div>
			<div class="fitem">
				<label>filePath:</label>
				<input style="width:250px;height:20px" id="filePath" name="filePath" class="easyui-validatebox" > 
			</div>
			<div class="fitem">
				<label>策略:</label>
				<input style="width:250px;height:20px" id="toMapCondition" name="toMapCondition" class="easyui-validatebox" > 
			</div>
			<div class="fitem">
				<label>分隔符:</label>
				<input style="width:250px;height:20px" id="sep" name="sep" class="easyui-validatebox" > 
			</div>
			<div class="fitem">
				<label>xmlSkuTag:</label>
				<input style="width:250px;height:20px" id="xmlSkuTag" name="xmlSkuTag" class="easyui-validatebox" > 
			</div>
			<div class="fitem">
				<label>xmlSpuTag:</label>
				<input style="width:250px;height:20px" id="xmlSpuTag" name="xmlSpuTag" class="easyui-validatebox" > 
			</div>
			<div class="fitem">
				<label>picFlag:</label>
				<input style="width:250px;height:20px" id="picFlag" name="picFlag" class="easyui-validatebox" > 
			</div>
			<div class="fitem">
				<label>picPath:</label>
				<input style="width:250px;height:20px" id="picPath" name="picPath" class="easyui-validatebox" > 
			</div>
			
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">Save</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>
		
</body>
</html>
