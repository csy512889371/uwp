<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>登录</title>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    %>
    <link rel="stylesheet" href="<%=basePath%>resources/admin/css/sysChoose/sysChoose.css">


    <script language="JavaScript" type="text/JavaScript">
        function adminLogin() {
            parent.window.location.href='/login/login.do';
        }
        function memberLogin() {
            parent.window.location.href='/member/login/login.do';
        }
    </script>

</head>
<body>
    <div class="wrapper">
        <i class="icon"></i>
        <span class="title"></span>
        <div class="a-box">
            <a onclick="adminLogin()"><span>政府登录</span></a>
            <a onclick="memberLogin()"><span>企业登录</span></a>
        </div>
    </div>
</body>
</html>