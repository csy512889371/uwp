<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<body>
	<div>
		<form action="" method="post" id="commonCodeForm">
			<div class="table_div">
				<p></p>
				<table class="table table-bordered">
					<tr>
						<td style="width: 50%;text-align: center" onclick="isCommon()">
							<input id="isCommon" type="radio" name="isCommonCode" value="1">&nbsp;&nbsp;&nbsp;常用
						</td>
						<td style="text-align: center" onclick="notCommon()">
							<input id="notCommon" type="radio" name="isCommonCode" value="0">&nbsp;&nbsp;&nbsp;不常用
						</td>
					</tr>
				</table>
			</div>
			<div class="btn_div">
		        <button class="btn-style1" type="button" onclick="saveCommonCode()">保&nbsp;存</button>
		    </div>
		</form>
	</div>
	<script type="text/javascript">
		var selectCodes = eval('('+'${selectCodes}'+')');
		selectCodes = selectCodes.join(",")
		function saveCommonCode(){
			var isCommon = $("input[name='isCommonCode']:checked").val();
			if(isCommon == null){
				openAlert('请选择选项！');
				return;
			}
			var options = {
		        success: function(result){
    				openAlert(result.content);
    				if(result.type == "success"){
    					//重新加载这颗树
    					loadCode(tCode);
    					closeWindow(1);	    					
					}
				},
		        dataType:  'json',
		        data:{tCode : tCode, selectCodes : selectCodes, isCommon : isCommon},
		        url:'<%=basePath%>sys/code/commonCodeSettings.do', 
		    };  
		    $("#commonCodeForm").ajaxSubmit(options);
		}
		
		function isCommon(){
			$("#isCommon").prop("checked", true);
			$("#notCommon").prop("checked", false);
		}
		
		function notCommon(){
			$("#isCommon").prop("checked", false);
			$("#notCommon").prop("checked", true);
		}
	</script>
</body>