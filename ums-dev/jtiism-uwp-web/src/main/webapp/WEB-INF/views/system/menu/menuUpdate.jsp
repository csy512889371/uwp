<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>
<style>
	#bigicon, #smallicon{width:100px;}
</style>
<body>
  <div>
    <form action="<%=basePath%>sys/menu/updateMenu.do" method="post" id="menuUpdateForm">
      <input type="hidden" id="id" name="id" value="${cfgUmsMenu.id}">
      <div class="table_div">
		<table class="table table-bordered" style="width: 100%;">
	 		<tr>
	 			<td>菜单标题：</td>
	 			<td><input class="required" type="text" id="title" name="title" maxlength="30" value="${cfgUmsMenu.title}"></td>
	 		</tr>
	 		<tr>
	 			<td>上级菜单：</td>
	 			<td>
	 				<input type="text" id="parentTitle" name="parent.title" value="${cfgUmsMenu.parent.title}" onclick="commonWindow('菜单选择',1,'parentTitle','parentid')">
	 				<input type="hidden" id="parentid" name="parent.id" value="${cfgUmsMenu.parent.id}">
	 			</td>
	 		</tr>
	 		<tr>
	 		  <td>菜单类型：</td>
	 	      <td>
	 	        <select id="menutype" name="menutype">
	 		  		<option value="1" <c:if test="${cfgUmsMenu.menutype == 1}">selected</c:if>>系统菜单</option>
	 		  		<option value="2" <c:if test="${cfgUmsMenu.menutype == 2}">selected</c:if>>二级菜单</option>
	 		  		<option value="3" <c:if test="${cfgUmsMenu.menutype == 3}">selected</c:if>>三级菜单</option>
	 		  	</select>
	 	      </td>
	 		</tr>
	 		<tr>
	 			<td>URL：</td>
	 			<td><input type=text id="url" name="url" maxlength="100" value="${cfgUmsMenu.url}"></td>
	 		</tr>
	 		<tr>
	 			<td>排序：</td>
	 			<td><input class="number" type="text" id="sort" name="sort" maxlength="4" value="${cfgUmsMenu.sort}"></td>
	 		</tr>
	 		<tr>
	 			<td>编码：</td>
	 			<td><input class="required" type="text" id="code" name="code" maxlength="100" value="${cfgUmsMenu.code}"></td>
	 		</tr>
			<tr>
				<td>类型：</td>
				<td>
					<select id="type" name="type" class="required">
						<option value="0" <c:if test="${cfgUmsMenu.type == 0}">selected</c:if>>门户</option>
						<option value="1" <c:if test="${cfgUmsMenu.type == 1}">selected</c:if>>后台系统</option>
					</select>
				</td>
			</tr>
	 		<tr>
	 			<td>状态：</td>
	 			<td>
	 			  <label class="radio inline"><input type="radio" name="state" id="state" <c:if test="${cfgUmsMenu.state == true}">checked</c:if> value="true">有效</label>
                  <label class="radio inline"><input type="radio" name="state" id="state" <c:if test="${cfgUmsMenu.state == false}">checked</c:if> value="false">无效</label>
	 			</td>
	 		</tr>
	 		<tr>
	 		  <td>菜单图标：</td>
	 		  <td>
	 		  	<img src="<%=basePath%>resources/admin/img/menuicon/blank.png" id="menuIcon" style="height:21px;">&nbsp;&nbsp;
	 		    <input type="hidden" id="bigicon" name="bigicon" value="${cfgUmsMenu.bigicon}" maxlength="100">
	 		    <input type="hidden" id="smallicon" name="smallicon" value="${cfgUmsMenu.smallicon}" maxlength="100">
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
	 			<td><input type="text" id="remark" name="remark" maxlength="300" value="${cfgUmsMenu.remark}"></td>
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
		openWindow('选择图标','sys/menu/chooseIcon.do?icon='+$('#smallicon').val(),2,360,400);
	});
	
	if($('#smallicon').val()!=''){
		$('#menuIcon').attr('src','<%=basePath%>resources/admin/img/menuicon/'+$('#smallicon').val());
	}
	
	/* $('#choBtn2').click(function(){
		openWindow('选择图标','sys/menu/chooseIcon.do?icon='+$('#smallicon').val(),2,360,250);
	}); */
	
	$("#menuUpdateForm").validate({
	    submitHandler: function() {
	    	saveMenuInfo();
		}
	});
});
function saveMenuInfo(){
	$('#menuUpdateForm').ajaxSubmit(function(data){
		closeWindow();
		openAlert(data.content);
		if(data.type == 'success'){
			$('#mainContent').load('<%=basePath%>sys/menu/searchMenu.do',{});
		}
	});
 }
 
</script>
</body>
