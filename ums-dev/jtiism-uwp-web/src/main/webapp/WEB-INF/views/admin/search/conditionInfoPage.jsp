<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>

<body>
	<div>
		<form method="post" id="QueryTemplateForm">
			<input type="hidden" id="queryTemplateId" name="queryTemplateId" value="">
			<table class="table table-bordered">
				<tr>
					<td width="35%"><span style="font-size: 16px; color: red;">*</span>模板名称:</td>
					<td><input id="templateName" name="templateName" type="text"
						style="width: 88%;" maxlength='50'/></td>
				</tr>
				<tr>
					<td>模板描述：</td>
					<td><textarea id="condtionDesc" name="templateDesc" rows="1" cols="15" style="width: 88%;" maxlength='100'></textarea></td>
				</tr>
				<tr style="display: none;">
					<td colspan="2" style="text-align: center;">
					<label><input id="isPubTmplBox" type="checkbox" class="checkbox"   onclick="changeisPublicTmplate(this)">设置全局模板</label>
						<input type="hidden" id="isPublicTmplate" name="isPubTmpl" value="1">
					</td>
				</tr>
				<tr id="groupTr">
					<td>所属分组：</td>
					<td>
						<select id="groupId" name="groupId"></select>
					</td>
				</tr>
			</table>
			<div style="margin-left: 80%;">
				<button type="submit" class="btn btn-success">&nbsp;保存&nbsp;</button>
			</div>
		</form>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#QueryTemplateForm").validate({
				rules : {
					templateName:{
						required: true,
				        maxlength: 50
					},
					templateDesc:{
						 maxlength: 100
					}
				}
	        });
			
			var options = {
	    		success:function(result){
	    			openAlert(result.content);
	   				if(result.type=="success"){
            			closeWindow();
            		}
	    		},
			    dataType : 'json',
			    data:{shareEtps:'',datas:submitDatas},
			    url:'<%=basePath%>admin/udsearch/savaQueryData.do',
			};

			$("#QueryTemplateForm").ajaxForm(options);
			
			var list = eval('('+'${groupList}'+')');
			if(list!=null && list.length>0){
				for(var i in list){
					$('#groupId').append('<option value='+list[i].id+'>'+list[i].groupName+'</option>');
				}
			}
			
		});

		function changeisPublicTmplate(item){
			if($(item).is(':checked')){
				$('#isPublicTmplate').val(1);
				$('#groupTr').show();
			 }else{
				$('#isPublicTmplate').val(0);
				$('#groupTr').hide();
			 }
		}
	</script>
</body>