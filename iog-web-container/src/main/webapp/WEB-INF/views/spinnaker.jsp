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

<title>导出</title>
    <link rel="stylesheet" href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/icon.css" type="text/css" media="screen" />
    <link rel="stylesheet"  href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/default/easyui.css" type="text/css" />
    <link rel="stylesheet" href="<%=bathPath%>/js/jquery-easyui-1.3.3/themes/main.css" type="text/css" />
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery-1.10.1.min.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=bathPath%>/js/jquery-easyui-1.3.3/jquery.json-2.4.js"></script>


<script type="text/javascript">


    function exportProduct() {

        if(""!=$('pageIndex').val()){
            if(isNaN($('#pageIndex').val())){
                alert("请输入数字");
                $('#pageIndex').focus();
                return ;
            }
            if(""==$('pageSize').val()){
                alert("页码有数值时，导出行数必须也要有数值");
                return ;
            }
        }

        if(""!=$('pageSize').val()){
            if(isNaN($('#pageSize').val())){
                alert("请输入数字");
                $('#pageSize').focus();
                return ;
            }
            if(""==$('pageIndex').val()){
                alert("导出行数有数值时，页码必须也要有数值");
                return ;
            }
        }



        var search = {

            startDate:    $('#startDate').val(),
            endDate:      $('#endDate').val(),
            pageIndex: $('#pageIndex').val(),
            pageSize:$('#pageSize').val()

        };
        window.open('spinnaker?queryJson='+$.toJSON(search), '','');
    }

    function clearText(){
        $('#pageIndex').val('');
        $('#pageSize').val('');
        $('#startDate').val('');
        $('#endDate').val('');



    }


</script>
<script type="text/javascript"	src="<%=bathPath%>/js/DatePicker/config.js"></script>
<script type="text/javascript"	src="<%=bathPath%>/js/DatePicker/WdatePicker.js"></script>
</head>
<body>

<div style="padding: 20px 20px 20px 20px;">
    <form action="" id="brandForm">
        <table>
            <tr>

                <td>导出时间:</td>
                <td><input id="startDate" name="startDate" style="width: 90px;" onFocus="var endDate=$dp.$('endDate');WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}',minDate:'#F{$dp.$D(\'endDate\',{M:-1})}'})">

                <input id="endDate" name="endDate" style="width: 90px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}',maxDate:'#F{$dp.$D(\'startDate\',{M:+1})}'})"></td>
                <td></td><td></td>
            </tr>
            <tr>
                <td>页码</td><td><input type="text" id="pageIndex" name="pageIndex"/> </td>
                <td>导出行数</td><td><input type="text" id="pageSize" name="pageSize"/> </td>
            </tr>
        </table>
    </form>
</div>
<div style="text-align: left; padding: 0px 20px 20px 20px;"><a
	href="javascript:void(0)" onclick="exportProduct()" id="btn-save"
	icon="icon-search" class='easyui-linkbutton'>导出</a> <a href="javascript:void(0)"
	onclick="clearText()" id="btn-cancel" icon="icon-cancel" class='easyui-linkbutton'>清空</a></div>
</div>





</body>
</html>