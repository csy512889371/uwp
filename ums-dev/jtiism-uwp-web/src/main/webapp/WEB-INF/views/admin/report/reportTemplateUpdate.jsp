<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
  
  <body>
    <div>
      <form action="" id="reportTemplateUpdateForm" enctype="multipart/form-data">
       <input id="templateId" name="id" type="hidden" value="${reportTempl.id}">
	  <table id="reportTemplateTable" class="table table-bordered">
	  	<tr>
		  <td class="input_title" >模板名称</td>
		  <td>
		    <input type="text" id="templateName" name="templateName"  value="${reportTempl.templateName}">
		  </td>
		  <td class="input_title">模板类别</td>
		  <td >
		  		<select id="valueChoose_selects" name="templateType" style="width: 180px;">
		  			<option <c:if test= '${reportTempl.templateType == "appointDismiss"}'>selected</c:if> value="appointDismiss">调研表类</option>
					<option <c:if test= '${reportTempl.templateType == "muster"}'>selected</c:if> value="muster">名册类</option>
					<option <c:if test= '${reportTempl.templateType == "statistics"}'>selected</c:if>  value="statistics">统计类</option>
				</select>
		  </td>
		</tr>
		<tr>
		  <td class="input_title" >模板文件</td>
		  <td>
		    <input type="file" id="templateFile" name="templateFile">
		  </td>
		  <td class="input_title">是否启用</td>
		  <td>
		    <label><input type="checkbox" class="checkbox" <c:if test="${reportTempl.isvalid}">checked</c:if> name="isvalid" value="1">启用</label>
		  </td>
		</tr>
		<tr>
			<td colspan="4">
				脚本定义:
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<textarea name="rules" rows="5" cols="6" style="width:96%;height:170px;">${reportTempl.rules}</textarea>
			</td>
		</tr>
	  </table>
	  <div style="display: none;">
	    <button class="btn btn-success" id="updateReportTemplateBtn" type="submit"></button>
	  </div>
    </form>
    <div class="right_btn">
		<button class="btn-style1" onclick="updateeReportTemplate()">保存</button>
	</div>
    </div>
    <script type="text/javascript">
    
    function updateeReportTemplate(){
    	var templfile=$('#templateFile').val();
    	if(templfile!=null&&$.trim(templfile)!=''){
    		openConfirm('确定要覆盖模板文件么？',function() {
    			$('#updateReportTemplateBtn').click();
    		});
    	}else{
    		$('#updateReportTemplateBtn').click();
    	}
    	
    }
    $(document).ready(function(){
		$("#reportTemplateUpdateForm").validate({
			focusInvalid: false,
			rules : {
				templateName: {
					required:true
				}, 
				templateType:{
					required:true
				}
			}
		});
		
	var options = {
		    type:"post",
			dataType : 'json',
			url : '<%=basePath%>report/engine/updateReportTemplate.do',
			success : function(data){
				openAlert(data.content);
			    if(data.type == "success"){
			    	loadReportTemplateList();
			    }
		    }
		};
		$("#reportTemplateUpdateForm").ajaxForm(options);
		
	});
    </script>
  </body>