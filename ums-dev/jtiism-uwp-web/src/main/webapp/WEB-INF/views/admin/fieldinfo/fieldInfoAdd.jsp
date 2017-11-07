<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
	#newFieldInfoForm table tr .td1{width:90px;}
</style>
<!-- 新增元数据页面 -->
<body>
	<form action="" method="post" id="newFieldInfoForm">
		<div class="table_div">
			<table class="table table-bordered">
				<tr>
					<td class="td1"><span style="font-size:16px;color:red;">*</span>数据库表名：</td>
					<td><input type="text" name="srcTable" id="srcTable" style="width:170px;" /></td>
					<td class="td1"><span style="font-size:16px;color:red;">*</span>原字段名：</td>
					<td><input type="text" name="srcField" id="srcField" style="width:170px;" /></td>
				</tr>
				<tr>
					<td><span style="font-size:16px;color:red;">*</span>字典表名：</td>
					<td><input type="text" name="codeTable" id="codeTable" style="width:170px;" /></td>
					<td><span style="font-size:16px;color:red;">*</span>对应字典表<br>字段名：</td>
					<td><input type="text" name="desField" id="desField" style="width:170px;" /></td>
				</tr>
				
			</table>
		</div>
		<div class="btn_div">
			<button type="submit" class="btn-style1">&nbsp;保存&nbsp;</button>
		</div>
	</form>
	<script>
	$().ready(function() {
		$("#newFieldInfoForm").validate({
			rules:{
				srcTable:{
					required:true,
					maxlength:100
				},srcField:{
					required:true,
					maxlength:100
				},
				codeTable:{
					required:true,
					maxlength:100
				},
				desField:{
					required:true,
					maxlength:100
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
    					$('#mainContent').load('<%=basePath%>sys/fieldinfo/fieldInfoList.do',{});
					}
				},
		        dataType:  'json',
		        url:'<%=basePath%>sys/fieldinfo/saveFieldInfo.do',
		    };  
		    $("#newFieldInfoForm").ajaxForm(options);
		    
	});
    	
	</script>
</body>
