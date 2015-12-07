<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询订单列表</title>

<style type="text/css">
	.table_border {
		border: solid 1px #B4B4B4;
		border-collapse: collapse;
		-
		-折叠样式
		.
	}
	
	.table_border tr th {
		
		padding-left: 4px;
		height: 27px;
		border: solid 1px #B4B4B4;
	}
	
	.table_border tr td {
		height: 25px;
		padding: 4px;
		border: solid 1px #B4B4B4;
	}
</style>

</head>
<body>
订单列表：
<table width="100%" width="100%" class="table_border" border="0" id="high_light" lang="tabRowData"  
         cellpadding="0" cellspacing="0">
<tr>
	<td>序号</td>
	<td>供货商名称</td>
	<td>尚品订单编号</td>
	<td>采购单编号</td>
	<td>Detail</td>
	<td>Memo</td>
	<td>CreateTime</td>
	<td>UpdateTime</td>
	<td>UuId</td>
</tr>
<c:forEach items="${orderList }" var="order" varStatus="status">
<tr>
	<td>${status.index + 1}</td>
	<td>${order.supplierName }</td>
	<td>${order.spOrderId }</td>
	<td>${order.spPurchaseNo }</td>
	<td>${order.detail }</td>
	<td>${order.memo }</td>
	<td>${order.createTime }</td>
	<td>${order.updateTime }</td>
	<td>${order.uuId }</td>
</tr>
</c:forEach>

</table>
</body>
</html>