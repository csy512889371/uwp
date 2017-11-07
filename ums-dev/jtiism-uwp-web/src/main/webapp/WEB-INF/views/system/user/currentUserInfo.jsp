<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%-- <%@ include file="../../common.inc"%> --%>
 <style>
 	#userBasicInfo, #updatePassword{height:195px;border: none;padding-top: 10px;}
 </style>
  <body>
  		<div id="userTab" style="border: none;">
    	<div>
	    	<ul>
				<li><a href="#userBasicInfo" onclick="noSetInput()">基础信息</a></li>
				<li><a href="#updatePassword" onclick="setInput()">密码修改</a></li>
			</ul>
		</div>
		<div class="table_div" id="userBasicInfo">
	      <table class="table table-bordered">
	        <tr>
	          <td>帐号：</td>
	          <td>${sessionScope.current_user.username}</td>
	        </tr>
	        <tr>
	          <td>角色：</td>
	          <td> 
	             <c:forEach var="role" items="${roleList}">
	               ${role.rolename}
	             </c:forEach>
	          </td>
	        </tr>
	        <tr>
	          <td>状态：</td>
	          <td>${state}</td>
	        </tr>
	        <tr>
	          <td>备注：</td>
	          <td>${remark}</td>
	        </tr>
	      </table>
     </div>
     <div id="updatePassword">
	     <form action="<%=basePath%>sys/user/updatePassword.do" method="post" id="currentUserInfoForm">
	     	<div class="table_div">
		     	<table class="table table-bordered">
		        <tr>
		          <td>输入旧登录密码：</td>
		          <td><input name="oldPwd" id="oldPwd" type="password" placeholder="请输入密码"></td>
		        </tr>
		        <tr>
		          <td>输入新登录密码：</td>
		          <td><input name="newPwd" id="newPwd" type="password" placeholder="请输入密码"></td>
		        </tr>
		        <tr>
		          <td>再次输入新密码：</td>
		          <td><input name="rePwd" id="rePwd" type="password" placeholder="请输入密码"></td>
		        </tr>
		      </table>
	      </div>
	      <div class="btn_div">
	        <button class="btn-style1" type="submit">保存</button>
	     </div>
	     </form>
    </div>
    </div>
 <script type="text/javascript">
 	$( "#userTab" ).tabs({
	    collapsible: false
	 });
 
   $(document).ready(function(){
	   $("#currentUserInfoForm").validate({
		   rules:{
			   oldPwd:{
				   required:true,
				   remote:{
					   type:"post",
					   url:"<%=basePath%>sys/user/isExistPassword.do",
					   dataType:'json',
					   data:{ 
						   oldPassword:function(){
							   return $("#oldPwd").val();
						   },
						   newPassword:function(){
							   return $("#Pwd").val();
						   }
					   }
				   }
			   },
			   newPwd:{
				   required:true,
				   oldPwdEqualnewPwd:true
			   },
			   rePwd:{
				   required:true,
				   equalTo:"#newPwd"
			   }
		   },
		   messages:{
			   oldPwd:{
				   remote:jQuery.format("旧密码输入错误")
			   },
			   newPwd:{
				   oldPwdEqualnewPwd:"新密码不能与旧密码相同"
			   },
			   rePwd:{
				   equalTo:"两次输入的密码不一致"
			   }
		   },
		   submitHandler: function(){
			   updatePassword();
		   }
	   });
	   
	   jQuery.validator.addMethod("oldPwdEqualnewPwd",function(value,element){
		   var oldPwd = $("#oldPwd").val();
		   var newPwd = $("#newPwd").val();
		   if(oldPwd == newPwd){
			   return false;
		   } else {
			   return true;
		   }
	   },"新密码不能与旧密码相同");
   });
   
   function updatePassword(){
		var newPassword = $("#newPwd").val();
		openConfirm('确认修改密码？',function(){
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>sys/user/updatePassword.do',
				data : {password : newPassword},
				success: function(result){
					openAlert(result.content);
					closeWindow();
				}
			});
		});
	}
 </script>   
  </body>
