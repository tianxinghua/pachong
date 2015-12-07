<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询订单列表</title>
</head>
<body>
订单列表：
<table width="100%" border=1 >
<tr>
	<td>供货商名称</td>
	<td>尚品订单编号</td>
	<td>采购单编号</td>
	<td>Detail</td>
	<td>Memo</td>
	<td>CreateTime</td>
	<td>UpdateTime</td>
	<td>UuId</td>
</tr>
<c:forEach items="${orderList }" var="order">
<tr>
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