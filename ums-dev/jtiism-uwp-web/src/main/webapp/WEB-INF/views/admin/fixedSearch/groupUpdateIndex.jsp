<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<!-- 更新分组 -->
<body>
	<div>
		<form action="<%=basePath%>admin/fixedSearch/updateGroup.do" method="post" id="updateGroupForm">
			<div class="table_div">
				<input type="hidden" name="id" value="${queryTemplateGroup.id}">
				<input type="hidden" name="createTime" value="${queryTemplateGroup.createTime}">
				<input type="hidden" name="inpfrq" value="${queryTemplateGroup.inpfrq}">
				<table class="table table-bordered">
					<tr>
						<td>分组名称：</td>
						<td>
							<input type="text" name="groupName" maxlength="100"  value="${queryTemplateGroup.groupName}">
						</td>
					</tr>
					<tr>
						<td>是否公用：</td>
						<td>
							<label class="radio inline">
								<input type="radio" name="isPub" value="1">是
							</label>
							&nbsp;&nbsp;&nbsp;
							<label class="radio inline">
								<input type="radio" name="isPub" value="0">否
							</label>
						</td>
					</tr>
				</table>
			</div>
			<div class="btn_div">
				<input type="submit" value="确定" class="btn-style1">
			</div>
		</form>
	</div>
	<script>
		$(function(){
			$('input[type="radio"][value='+'${queryTemplateGroup.isPub}'+']').attr('checked','checked');
			
			$("#updateGroupForm").validate({
				 submitHandler: function() {
					 saveGroup();
				 }
			 });
		});
		
		function saveGroup(){
			$('#updateGroupForm').ajaxSubmit(function(data){
				closeWindow();
		    	openAlert(data.content);
		    	if(data.type == "success"){
		    		loadTemplateTree();
		    	}
			});
		}
	</script>
</body>