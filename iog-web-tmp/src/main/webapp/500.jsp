<%@ page import="sun.misc.BASE64Encoder" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" isErrorPage="true" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>系统错误</title>
<link href="css/bcye.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="bc_mai">
  <div class="bc_box">
    <div class="bc_txt"><p>抱歉这里暂时不能通行!</p>


      <div style="display: none">
          <%
              StringWriter sw = new StringWriter();
              exception.printStackTrace(new PrintWriter(sw));
              String outerror = new BASE64Encoder().encode(sw.toString().getBytes());
              response.getWriter().write(outerror);
          %>
      </div>
  </div>
</div>
</body>
</html>
