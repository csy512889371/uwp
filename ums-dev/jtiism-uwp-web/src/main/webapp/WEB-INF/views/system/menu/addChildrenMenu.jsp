<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
<style>
	#bigicon, #smallicon{width:100px;}
</style>
<body>
	<div>
	  <form action="<%=basePath%>sys/menu/saveNewMenu.do" method="post" id="childrenMenuInfoForm">
	 	 <div class="table_div">
			<table class="table table-bordered">
			   <tr>
		 		  <td>菜单标题：</td>
		 		  <td><input class="required" type="text" id="title" name="title" maxlength="30"></td>
		 		</tr>
		 		<tr>
		 		  <td>上级菜单：</td>
		 		  <td>
		 		  	<input class="required" type="text" id="parentTitle" name="parent.title" value="${cfgUmsMenu.title}">
		 		  	<input type="hidden" id="parentid" name="parent.id" value="${cfgUmsMenu.id}">
		 		  </td>
		 		</tr>
		 		<tr>
		 		  <td>菜单类型：</td>
		 		  <td>
		 		    <select id="menutype" name="menutype">
		 		  		<option value="1" >系统菜单</option>
		 		  		<option value="2" <c:if test="${cfgUmsMenu.menutype == 1}">selected</c:if>>二级菜单</option>
		 		  		<option value="3" <c:if test="${cfgUmsMenu.menutype == 2}">selected</c:if>>三级菜单</option>
		 		  	</select>
		 		  </td>
		 		</tr>
		 		<tr>
		 		  <td>URL：</td>
		 		  <td><input type="text" id="url" name="url" maxlength="100"></td>
		 		</tr>
		 		<tr>
		 		  <td>排序：</td>
		 		  <td>
	                <input class="required number" type="text" id="sort" name="sort" maxlength="4">
		 		  </td>
		 		</tr>
		 		<tr>
		 		  <td>编码：</td>
		 		  <td>
		 		    <input class="required" type="text" id="code" name="code" maxlength="100">
		 		  </td>
		 		</tr>
				<tr>
					<td>类型：</td>
					<td>
						<select id="type" name="type" class="required">
							<option value="0">门户</option>
							<option value="1">后台系统</option>
						</select>
					</td>
				</tr>
		 		<tr>
		 		  <td>状态：</td>
		 		  <td>
		 		    <label class="radio inline"><input type="radio" name="state" id="state" value="true" checked="checked">有效</label>
	                <label class="radio inline"><input type="radio" name="state" id="state" value="false">无效</label>
	              </td>
		 		</tr>
		 		<tr>
		 		  <td>菜单图标：</td>
		 		  <td>
		 		  	<img src="<%=basePath%>resources/admin/img/menuicon/blank.png" id="menuIcon" style="height:21px;">&nbsp;&nbsp;
		 		    <input type="hidden" id="bigicon" name="bigicon" maxlength="100">
		 		    <input type="hidden" id="smallicon" name="smallicon" maxlength="100">
		 		    <input type="button" id="choBtn1" class="btn-style2" value="选择图标" style="width:100px;">
		 		  </td>
		 		</tr>
		 		<!-- <tr>
		 		  <td>小图标：</td>
		 		  <td>
		 		    
		 		    <input type="button" id="choBtn2" class="btn-style2" value="选择图标" style="width:100px;">
		 		  </td>
		 		</tr> -->
		 		<tr>
		 		  <td>备注：</td>
		 		  <td><input type="text" id="remark" name="remark" maxlength="300"></td>
		 		</tr>
		 	</table>
	 	</div>
	 	<div class="btn_div">
			<button class="btn-style1" type="submit">保 存</button>
		</div>
	   </form>
	</div>
<script type="text/javascript">
$().ready(function() {
	$('#choBtn1').click(function(){
		openWindow('选择图标','sys/menu/chooseIcon.do?icon='+$('#bigicon').val(),2,360,400);
	});
	
	/* $('#choBtn2').click(function(){
		openWindow('选择图标','sys/menu/chooseIcon.do?icon='+$('#bigicon').val(),2,360,250);
	}); */
	
	$("#childrenMenuInfoForm").validate({
	    submitHandler: function() {
	    	saveChildrenMenu();
		}
	});
});
function saveChildrenMenu(){
	$('#childrenMenuInfoForm').ajaxSubmit(function(data){
		closeWindow();
		openAlert(data.content);
    	if(data.type == "success"){
    		$('#mainContent').load('<%=basePath%>sys/menu/searchMenu.do',{});
    	}
	});
 }
</script>
</body>
