<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>订单错误</title>
</head>

<body>
<div>
 <table>
  	<tr>
  		<td>id</td>
  		<td>supplierId</td>
  		<td>excState</td>
  		<td>excDesc</td>
  	</tr>
  	<c:forEach items="${orders }" var="order">
	  	<tr>
	  		<td>${order.id }</td>
	  		<td>${order.supplierId }</td>
	  		<td>${order.excState }</td>
	  		<td>${order.excDesc }</td>
	  	</tr>
  	</c:forEach>
  </table>
</div>
</body>
</html>
