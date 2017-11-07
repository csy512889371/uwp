<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<!-- 新增字段页面 -->
<body>
	<form action="" method="post" id="newVersionScriptForm">
		<div style="width: 96%;margin: auto;">
			<table class="table table-bordered">
				<tr >
					<td><span style="font-size:16px;color:red;">*</span><span>变更说明：</span></td>
					<td><input type="text" value="" name="chnDesc" /></td>
				</tr>
				<tr>
					<td><span style="font-size:16px;color:red;">*</span><span>变更时间：</span></td>
					<td>
					<input type="text" id="chnTime_" name="chnTime" onfocus="getDate(this,'yyyy-MM-dd')" readonly="readonly">
					</td>
				</tr>
				<tr >
					<td><span style="font-size:16px;color:red;">*</span><span>变更类型：</span></td>
					<td>
					<select name="chnType">
						<option value="0">结构</option>
						<option value="1">数据</option>
					</select>
					</td>
				</tr>
				<tr >
					<td><span>变更脚本</span></td>
					<td><textarea rows="10" cols="20" style="width: 450px;height: 220px;" name="chnScript"></textarea></td>
				</tr>
			</table>
		</div>
		<div style="text-align: right;margin-right: 8px;">
			<button type="submit" class="btn-style1">&nbsp;保存&nbsp;</button>
		</div>
	</form>
	<script>
	$('document').ready(function() {
		$("#newVersionScriptForm").validate({
			rules:{
			chnDesc:{
				required:true
			},
			chnTime:{
				required:true
			}
			}
		});
		   var options = {
		        success: function(result){
		        	openAlert(result.content);
    				if(result.type == "success"){
				        closeWindow();
					}
				},
		        dataType:  'json',
		        url:'<%=basePath%>sys/version/addVersionScript.do', 
		    };  
		    $("#newVersionScriptForm").ajaxForm(options);
	});
	</script>
</body>
