<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<!-- 新增分组 -->
<body>
	<form action="<%=basePath%>admin/fixedSearch/saveGroup.do" method="post" id="addGroupForm">
		<div class="table_div">
			<table class="table table-bordered">
				<tr>
					<td>分组名称：</td>
					<td>
						<input type="text" name="groupName" id="groupName" maxlength="100" class="required">
					</td>
				</tr>
				<tr style="display: none;">
					<td>是否公用：</td>
					<td>
						<label class="radio inline">
							<input type="radio" name="isPub" value="1" checked="checked">是
						</label>
						&nbsp;&nbsp;&nbsp;
						<label class="radio inline">
							<input type="radio" name="isPub" value="0" >否
						</label>
					</td>
				</tr>
			</table>
		</div>
		<div class="btn_div">
			<input type="submit" value="确定" class="btn-style1">
		</div>
	</form>
	<script>
		$(function(){
			$("#addGroupForm").validate({
				 submitHandler: function() {
					 saveGroup();
				 }
			 });
		});
		
		function saveGroup(){
			$('#addGroupForm').ajaxSubmit(function(data){
				closeWindow();
		    	openAlert(data.content);
		    	if(data.type == "success"){
		    		loadTemplateTree();
		    	}
			});
		}
	</script>
</body>