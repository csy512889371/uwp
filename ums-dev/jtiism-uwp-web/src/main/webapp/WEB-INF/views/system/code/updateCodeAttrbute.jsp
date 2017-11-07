<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
	.operbtn_div{text-align: right;margin-right: 5px;}
	.table .td1{width:40%;text-align: right;}
</style>
<body>
<form action='' method="post" id="updateCodeAttrbuteForm">
	<div style="margin: auto;width:96%;">
		<table class="table table-bordered">
			<tr style="display: none;">
				<td class="td1">代码：</td>
				<td><input type="text" name="attrCode" id="attrCode"></td>
			</tr>
			<tr>
				<td class="td1">名称：</td>
				<td><input type="text" name="attrName" id="attrName"></td>
			</tr>
		</table>
	</div>
	<input type="hidden" name="id" id="attrId">
	<div class="operbtn_div">
		<button class="btn-style1" type="submit">保 存</button>&nbsp;
		<!-- <button class="btn btn-style4" type="reset">重置</button> -->
	</div>
</form>
<script type="text/javascript">
$(document).ready(function(){
	$('#attrId').val(selId);
	$('#attrName').val(selName);
	$('#attrCode').val(selCode);
	var options = {
	        success: function(result){
				openAlert(result.content);
				if(result.type == "success"){
					$('#mainContent').load('<%=basePath%>sys/code/codeIndex.do',{});
					closeWindow();
							    					
				}
			},
	        dataType:  'json',	        
	        url:'<%=basePath%>sys/code/saveCodeAttrbute.do', 
	    };  
	    $("#updateCodeAttrbuteForm").ajaxForm(options);
	    

});
</script>