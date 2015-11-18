<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>库存更新错误</title>
</head>

<body>
<div>
 <table border="1" >
  	<tr>
  		<td>supplierId</td>
  		<td>supplierName</td>
  		<td>updateTime</td>
  		<td>当前时间差</td>
  	</tr>
  	<c:forEach items="${allData}" var="data">
	  	<tr>
	  		<td>${data.supplierId}</td>
	  		<td>${data.supplierName}</td>
	  		<td><fmt:formatDate value="${data.updateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	  		<td>${data.dif }</td>
	  	</tr>
  	</c:forEach>
  </table>
</div>
</body>
</html>
