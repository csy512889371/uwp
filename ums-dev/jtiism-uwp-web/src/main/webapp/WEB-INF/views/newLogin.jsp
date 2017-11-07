<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="common.inc" %>

<!DOCTYPE html>
<html lang="en">
<head >
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<meta content="yes" name="apple-mobile-web-app-capable" />
	<meta content="black" name="apple-mobile-web-app-status-bar-style" />
	<meta content="telephone=no" name="format-detection" />
	<meta content="email=no" name="format-detection" />
    <title>登录</title>

	<link rel="stylesheet" href="<%=basePath%>resources/admin/css/initStyle.css">
	<link rel="stylesheet" href="<%=basePath%>resources/admin/css/bootstrap.css">
	<link rel="stylesheet" href="<%=basePath%>resources/admin/css/login/Login.css">
    
    <script src="<%=basePath%>resources/admin/js/jquery-1.11.3.min.js"></script>
<%--	<script type="text/javascript" src="<%=basePath%>resources/admin/js/rsa/jsbn.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/admin/js/rsa/prng4.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/admin/js/rsa/rng.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/admin/js/rsa/rsa.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/admin/js/rsa/base64.js"></script>--%>
	<script src="<%=basePath%>resources/admin/js/cookie/jquery.cookie.js"></script>

	<!--[if lte IE 9]>
	<script type="text/javascript" src="<%=basePath%>resources/admin/js/respond.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/admin/js/mmgrid/html5shiv.js"></script>
	<![endif]-->
<style>
	input::-webkit-input-placeholder{
		color:#fff
	}
	input:-moz-placeholder{
		color:#fff
	}
	input::-moz-placeholder{
		color:#fff
	}
	input:-ms-input-placeholder{
		color:#fff
	}

</style>


</head>
<body onkeyup="enterKey(event)" onload="initField();">

	<div class="wrapper">
		<form id="loginForm" action="/login/login.do" method="post">
			<ul class="login">
				<li>
					<input type="hidden" id="enPassword" name="enPassword" />
					<input name="username" id="username" type="text" class="a1"  placeholder="请输入用户名"><%--onblur="if(this.value=='')this.value='请输入用户名'" onfocus="if(this.value=='请输入用户名')this.value=''"--%>
				</li>
				<li><input name="password" id="password" type="password" class="a1"   placeholder="请输入密码"></li>
				<li class="forgot-box"><span id="loginTip"></span><%--<a  onclick="parent.window.location.href='/member/register/index.do'" class="register">注册</a>--%><a href="" class="forgot-password">忘记密码</a></li>
				<li><button type="submit" class="btn login_btn">登 录</button></li>
			</ul>
		</form>
	</div>

	<script>
	    
	    $().ready( function() {
	    	var message = '${error}';
	    	if (message != "") {
	    		$('#loginTip').html(message);
	    	}
	    	
	    	var $loginForm = $("#loginForm");
			var $enPassword = $("#enPassword");
			var $username = $("#username");
			var $password = $("#password");
			
			// 表单验证、记住用户名
			$loginForm.submit( function() {
				if ($username.val() == "") {
					$.message("warn", "请输入您的用户名");
					return false;
				}
				
				$.cookie('cmis_username', $username.val());
				
				if ($password.val() == "") {
					$.message("warn", "请输入您的密码");
					return false;
				}
				
				/*var rsaKey = new RSAKey();
				rsaKey.setPublic(b64tohex("${modulus}"), b64tohex("${exponent}"));
				var enPassword = hex2b64(rsaKey.encrypt($password.val()));
				$enPassword.val(enPassword);*/
			});
	    	
		});

    	function enterKey(event){
	  		if(event.keyCode == "13"){
	  			$('#lgoinBtn').css('background-color','#00cccc');
	  			$("#loginForm").submit();
	  			
	  		}
	  	}

		function initField(){
			 var username = $.cookie('cmis_username');
		     var password = $.cookie('pcenter_password');
		     if(username!=null && username!=''){
		    	$("#username").val(username);
		     }
		     if(password!=null && password!=''){
		     $("#password").val(password);
		     	$("#check_pass").attr("checked","checked");
		    }
		}
    </script>

</body>
</html>