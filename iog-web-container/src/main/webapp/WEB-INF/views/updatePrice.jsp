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

<title>填写要更新的skuId</title>
    <link rel="stylesheet" href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/icon.css" type="text/css" media="screen" />
    <link rel="stylesheet"  href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/default/easyui.css" type="text/css" />
    <link rel="stylesheet" href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/main.css" type="text/css" />
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery-1.10.1.min.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.json-2.4.js"></script>

<script type="text/javascript"	src="<%=bathPath%>/js/DatePicker/config.js"></script>
<script type="text/javascript"	src="<%=bathPath%>/js/DatePicker/WdatePicker.js"></script>
</head>
<body>

<div style="padding: 20px 20px 20px 20px;">
	<h4>输入skuid，用 ,隔开，如123,321,456,88</h4>
    <form action="updatePrice">
    	<input type="hidden" name="supplierId" value="${supplierId}">
  		<textarea name="skuIds" style="width:400px;height:160px;"></textarea>
  		<input type="submit" value="提交">
    </form>
</div>


</body>
</html>