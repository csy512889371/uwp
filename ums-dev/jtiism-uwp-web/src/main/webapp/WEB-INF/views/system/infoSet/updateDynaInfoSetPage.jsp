<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<style>
.red{ color:red; padding-right:3px;}
</style>
<body>
	<div>
		<form id="dynaInfoSetUpdateForm" action="" method="post">
			<table class="table table-bordered">
				<tr>
					<td>信息集类型：</td>
					<td>
					<select name="infoSetType" disabled="disabled">
						<option value="cadretype" <c:if test= "${codeAttr.infoSetType == 'cadretype'}">selected="selected"</c:if>>人员管理信息集</option>
						<option value="unittype" <c:if test= "${codeAttr.infoSetType == 'unittype'}">selected="selected"</c:if>>单位管理信息集</option>
					</select>
					</td>
				</tr>
				<tr>
					<td>上级分组：</td>
					<td>
					<select name="parentId"  disabled="disabled">
						 <c:forEach items="${groups}" var="group">
						    <option value="${group.id}" <c:if test= "${codeAttr.parentId == group.id}">selected="selected"</c:if> >${group.attrName}</option>
						  </c:forEach>
					</select>
					</td>
				</tr>
				<tr>
					<td><span class="red">*</span>信息集中文名：</td>
					<td><input type="text" name="attrName" maxlength="50" value="${codeAttr.attrName}" class="required" ></td>
				</tr>
				<tr>
					<td><span class="red">*</span>信息集表名：</td>
					<td><input type="text" name="attrCode" maxlength="50" value="${codeAttr.attrCode}" class="required" readonly="readonly"></td>
				</tr>
				<tr>
					<td>类型：</td>
					<td>
						<select name="recordType" disabled="disabled">
							<option value="singleRecord" <c:if test= "${codeAttr.recordType == 'singleRecord'}">selected="selected"</c:if>>单记录类型</option>
							<option value="multiRecord" <c:if test= "${codeAttr.recordType == 'multiRecord'}">selected="selected"</c:if>>多记录类型</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>启用</td>
					<td>
					<label>
					<input type="checkbox" class="checkbox" name="status" value="1" <c:if test= "${codeAttr.status == 1}">checked</c:if>>
					</label>
					</td>
				</tr>
				<tr>
					<td>启用批处理</td>
					<td>
						<label>
						<input type="checkbox" class="checkbox" name="isBatch" value="true" <c:if test= "${codeAttr.isBatch == true}">checked</c:if>>
						</label>
					</td>
				</tr>
				<tr>
					<td>常用</td>
					<td>
						<label>
						<input type="checkbox" class="checkbox" name="isCommon" value="true" <c:if test= "${codeAttr.isCommon == true}">checked</c:if>>
						</label>
					</td>
				</tr>
				<tr>
					<td>脚本</td>
					<td>
						<label>
							<textarea rows="4" name="script" id="script">${codeAttr.script}</textarea>
						</label>
					</td>
				</tr>
			</table>
			<input type="hidden" name="isBasic" value="${codeAttr.isBasic}">
			<input type="hidden" name="id" value="${codeAttr.id}">
			<div style="float:right;margin-right: 6px;">
				<input type="submit" class="btn-style1" value="保存" />
			</div>
		</form>
	</div>
	<script type="text/javascript">
	$('document').ready(function() {
		$("#dynaInfoSetUpdateForm").validate({
			rules:{
				attrName:{
					required:true
				},
				attrCode:{
					required:true
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
	    					infoSetList.load({groupId:currentGroupId});
						}
					},
			        dataType:  'json',
			        url:'<%=basePath%>sys/infoSetManage/updateDynaInfoSet.do', 
			    };  
		$("#dynaInfoSetUpdateForm").ajaxForm(options);
	});
	</script>
</body>