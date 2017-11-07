<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
.red {
	color: red;
	padding-right: 3px;
}
</style>
<body>
	<div>
		<form id="dynaInfoSetAddForm" action="" method="post">
			<table class="table table-bordered">
				<tr>
					<td>信息集类型：</td>
					<td><select disabled="disabled">
							<option value="cadretype" selected="selected">人员管理信息集</option>
							<option value="cadretype">单位管理信息集</option>
					</select> 
					<input type="hidden" name="infoSetType" value="cadretype">
					</td>
				</tr>
				<tr>
					<td>上级分组：</td>
					<td><select name="parentId">
							<c:forEach items="${codeAttrs}" var="codeAttr">
								<option value="${codeAttr.id}">${codeAttr.attrName}</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td><span class="red">*</span>信息集中文名：</td>
					<td><input type="text" name="attrName" maxlength="50"
						class="required"></td>
				</tr>
				<tr>
					<td><span class="red">*</span>信息集表名：</td>
					<td><input type="text" id="attrCode" name="attrCode" maxlength="50"
						class="required"></td>
				</tr>
				<tr>
					<td><span class="red">*</span>类型：</td>
					<td><select name="recordType">
							<option value="singleRecord" selected="selected">单记录类型</option>
							<option value="multiRecord">多记录类型</option>
					</select></td>
				</tr>
				<tr>
					<td>启用</td>
					<td><label><input type="checkbox" class="checkbox"
							name="status" value="1"></label></td>
				</tr>
				<tr>
					<td>启用批处理</td>
					<td><label><input type="checkbox" class="checkbox"
							name="isBatch" value="true"></label></td>
				</tr>
				<tr>
					<td>常用</td>
					<td><label><input type="checkbox" class="checkbox"
							name="isCommon" value="true"></label></td>
				</tr>
				<tr>
					<td>脚本</td>
					<td><label><textarea rows="4" name="script" id="script"></textarea></label></td>
				</tr>
			</table>
			<input type="hidden" name="isBasic" value="false">
			<div style="float: right; margin-right: 6px;">
				<input type="submit" class="btn-style1" value="保存" />
			</div>
		</form>
	</div>
	<script type="text/javascript">
	
	jQuery.validator.addMethod("isIllegeAttrCode", function(value, element, param) {
	    var length = value.length;
	    if(value.length<1){
	    	return false;
	    }
	  return value.match(/^[a-zA-Z][a-z_A-Z0-9]+$/g);   
	}, $.validator.format("信息集表名必须以字母打头，且只允许输入数字，英文字母以及下划线"));
	$('document').ready(function() {
		
		$("#dynaInfoSetAddForm").validate({
			rules:{
				attrName:{
					required:true
				},
				attrCode:{
					required:true,
					isIllegeAttrCode:true,
					remote:{
						  url: "<%=basePath%>sys/infoSetManage/checkUniqueDynaName.do", 
						  type: "post",         
						  dataType: "json",
						  data: {attrCode: function() { return $('#attrCode').val();}}
					 }
				}
			},
			messages: {
				attrCode:{
					remote:"该信息集名已存在"
				}
			}
		});
		 var options = {
			success: function(result){
				openAlert(result.content);
				if(result.type == "success"){
					closeWindow();
					var groupId=result.params;
					var treeNode =  zTree_Menu.getNodeByParam('id',groupId,null);

					zTree_Menu.selectNode(treeNode);
					currentGroupId=groupId;
					infoSetList.load({groupId:currentGroupId});
				}
			},
			dataType:  'json',
			url:'<%=basePath%>sys/infoSetManage/saveDynaInfoSet.do',
		};
		$("#dynaInfoSetAddForm").ajaxForm(options);
	});
	</script>
</body>