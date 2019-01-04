<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" import="java.io.*"%>
<html>
<head>
    <title></title>
</head>
<body>
 <%--　<%=exception.getMessage()%><br>--%>
<%--　　<%=exception.getLocalizedMessage()%>--%>
　　<%
     exception.printStackTrace(response.getWriter());
 %>
</body>
</html>