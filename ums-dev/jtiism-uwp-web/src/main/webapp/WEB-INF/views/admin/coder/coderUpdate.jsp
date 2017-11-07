<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<!-- 新增字典常量字段页面 -->
<body>
	<form action="" method="post" id="newCoderInfoForm">
		<div class="table_div">
			<table class="table table-bordered">
				<tr>
					<td width="15%"><span style="font-size:16px;color:red;">*</span>表名：</td>
					<td><input type="text" name="tabName" id="tabName" style="width:48%;" value="${coder.tabName}"/></td>
					<td width="15%"><span style="font-size:16px;color:red;">*</span>字段名：</td>
					<td><input type="text" name="colName" id="colName" style="width:48%;" value="${coder.colName}"/></td>
				</tr>
				<tr>
					<td width="15%"><span style="font-size:16px;color:red;">*</span>字段值：</td>
					<td><input type="text" name="val" id="val" style="width:48%;" value="${coder.val}"/></td>
					<td width="15%">字段描述：</td>
					<td><input type="text" name="colDesc" id="colDesc" style="width:48%;" value="${coder.colDesc}"/></td>
				</tr>
				<tr>
					<td width="15%">值描述：</td>
					<td><input type="text" name="valDesc" id="valDesc" style="width:48%;" value="${coder.valDesc}"/></td>
					<td width="15%">值简拼：</td>
					<td><input type="text" name="codeSpelling" id="codeSpelling" style="width:48%;" value="${coder.codeSpelling}"/></td>
				</tr>
				<tr>
					<td width="15%">状态：</td>
					<td><input type="number" name="state" id="state" style="width:48%;" value="${coder.state}"/></td>
				</tr>
			</table>
			<input type="hidden" name="id" value="${coder.id}" />
		</div>
		<div class="btn_div">
			<button type="submit" class="btn-style1">&nbsp;保存&nbsp;</button>
		</div>
	</form>
	<script>
	
	$().ready(function() {
		$("#newCoderInfoForm").validate({
			rules:{
				tabName:{
					required: true,
					maxlength:20
				},
				colName:{
					required: true,
					maxlength:20
				},
				colDesc:{
					maxlength:20
				},
				val:{
					required: true,
					maxlength:2
				},
				valDesc:{
					maxlength:30
				},
				codeSpelling:{
					maxlength:20
				},
				state:{
					digits:true
				}
			},
			messages: {
				
			}
		});
		   var options = {
		        success: function(result){
    				openAlert(result.content);
    				if(result.type == "success"){
    					closeWindow();
    					//重新加载一次页面
    					$('#mainContent').load('<%=basePath%>sys/coder/coderList.do',{});
					}
				},
		        dataType:  'json',
		        url:'<%=basePath%>sys/coder/updateCoder.do',
		    };  
		    $("#newCoderInfoForm").ajaxForm(options);
		    
	});
    	
	</script>
</body>
