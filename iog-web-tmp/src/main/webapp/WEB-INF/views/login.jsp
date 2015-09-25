<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    String bathPath = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- cache -->
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">

<title>登录</title>
    <link rel="stylesheet" href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/icon.css" type="text/css" media="screen" />
    <link rel="stylesheet"  href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/default/easyui.css" type="text/css" />
    <link rel="stylesheet" href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/main.css" type="text/css" />
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery-1.10.1.min.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.json-2.4.js"></script>
<script type="text/javascript">

   function login(){
	   var formdata=$("#loginForm").serialize();
	   $.ajax({
		   url:"<%=bathPath%>/login",
		   type:"post",
		   data:formdata,
		   dataType:"json",
		   success:function(rtn){
			   console.log(JSON.stringify(rtn))
			   if(rtn.err){
				   alert(rtn.err);
			   }else{
				   document.location.href="<%=bathPath%>/"+rtn.url;
			   }
		   },
		   error:function(rtn){
			   console.log(rtn);
		   }
	   })
   }

</script>
</head>
<body>

<div style="padding: 20px 20px 20px 20px;">
    <form id="loginForm">
        <table>
            <tr>
                <td>
                    <input name="userName" placeholder="用户名/手机号/邮箱"/>
                </td>
            </tr>
            <tr>
                <td>
                    <input name="password" placeholder="密码"/>
                </td>
            </tr>
        </table>
        
    </form>
</div>
<div style="text-align: left; padding: 0px 20px 20px 20px;"><a
	href="javascript:void(0)" onclick="login()" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>登录</a>
</div>
</body>
</html>