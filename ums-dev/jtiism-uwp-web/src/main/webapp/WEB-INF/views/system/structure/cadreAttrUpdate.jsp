<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<!-- 新增活动页面 -->
<body>
	<form action="" method="post" id="updateCadreAttrForm">
		<div style="width: 96%;margin: auto;">
			<table class="table table-bordered">
				<tr id="attr_codeNameTr">
					<td><span>字段名(code)：</span></td>
					<td><input type="text" value="${ATTR.propertyName}"  disabled="disabled"/>
					<input type="hidden" value="${ATTR.propertyName}" name="propertyName" />
					</td>
				</tr>
				<tr id="attr_codeTr">
					<td><span>代码(code)：</span></td>
					<td><input type="text" value="${ATTR.propertyCode}" disabled="disabled"/>
					<input type="hidden" name="propertyCode" value="${ATTR.propertyCode}"/>
					</td>
				</tr>
				<tr>
					<td><span style="font-size:16px;color:red;">*</span><span>展示名：</span></td>
					<td><input type="text" value="${ATTR.name}" name="name" /></td>
				</tr>
				<tr>
					<td><span>所属信息集：</span></td>
					<td><input type="text" value="${ATTR.infoSet}" disabled="disabled"/>
					<input type="hidden" value="${ATTR.infoSet}" name="infoSet" />
					</td>
				</tr>
				<tr>
					<td><span>字段类型：</span></td>
					<td><select disabled="disabled" onchange="typeChange(this.value)">
					<my:outOptionsByCode tabName="SYS_COLUMN_SHOW" colName="type" value="${ATTR.type}" />
					</select>
					<input type="hidden" name="type" value="${ATTR.type}">
					</td>
				</tr>
				 <c:if test="${ATTR.type=='textarea'}">
					<tr id="rowHeightId">
						<td><span>行高：</span></td>
						<td><input type='text' name="rowHeight" value="${ATTR.rowHeight}" /></td>
					</tr>
				</c:if>
				<c:if test="${ATTR.type=='datetemp'}">
					<tr id="dataFormat">
						<td><span>时间类型：</span></td>
						<td><select name="dataFormat">
							<option value="">请选择时间类型</option>
							<option value="yyyy" <c:if test="${ATTR.dataFormat=='yyyy'}">selected</c:if>>年</option>
							<option value="yyyyMmDd" <c:if test="${ATTR.dataFormat!='yyyy'}">selected</c:if>>年月日</option>
						</select></td>
					</tr>
				</c:if>
				<tr>
					<td><span>长度限制：</span></td>
					<td><input type='text' name="lengthLimit" value="${ATTR.lengthLimit}" /></td>
				</tr>
				<tr>
					<td><span>验证规则：</span></td>
					<td><input type='text' name="validateRule" id="ucaForm_validateRule" value="${ATTR.validateRule}"  onclick="selectValidateRules(this)"/></td>
				</tr>
				<tr>
					<td><span>序列：</span></td>
					<td><input type='text' name="seq" value="${ATTR.seq}" /></td>
				</tr>
				<tr>
					<td><span>说明：</span></td>
					<td><textarea rows="4" name="comment">${ATTR.comment}</textarea></td>
				</tr>
			</table>
			<input type="hidden" name="id" value="${ATTR.id}" />
			<input type="hidden" name="allowBatch" value="${ATTR.allowBatch}" />
			<input type="hidden" name="isGridItem" value="${ATTR.isGridItem}" />
		</div>
		<div id= 'updateButtonDiv' style="text-align: right;margin-right: 8px;">
			<button type="submit" class="btn-style1">&nbsp;保存&nbsp;</button>
		</div>
	</form>
	<script>
	$('document').ready(function() {
		var operType= '${operType}';
		if ('view' == operType) {
			$('#updateButtonDiv').hide();
		}
		
		$("#updateCadreAttrForm").validate({
			rules:{
			name:{
				required:true
			},
			seq:{
				required:true,
				digits:true
			},  
			 rowHeight:{
				digits:true,
				max:20
			},
			lengthLimit:{
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
    					var attr=JSON.parse(result.params);
    					var td=$('#attrInfo input[value="'+attr.id+'"]').parents("td:eq(0)");
    					var tr=td.parent();
    					
    					tr.find("td:eq(0) span").html(attr.eq);
    					td.find("span").html(attr.name);
    					tr.find("td:eq(4) span").html(attr.propertyName);
    					
    					td.find("input[type='hidden']:eq(1)").val(attr.name);
    					td.find("input[type='hidden']:eq(2)").val(attr.propertyName);
    					td.find("input[type='hidden']:eq(3)").val(attr.type);
    					td.find("input[type='hidden']:eq(5)").val(attr.seq);
    					td.find("input[type='hidden']:eq(6)").val(attr.propertyCode);
    					td.find("input[type='hidden']:eq(8)").val(attr.lengthLimit);
    					td.find("input[type='hidden']:eq(9)").val(attr.validateRule);
    					<c:if test="${ATTR.type=='textarea'}">
    						td.find("input[type='hidden']:eq(11)").val(attr.rowHeight);
    					</c:if>
    					closeWindow();
					}
				},
		        dataType:  'json',
		        url:'<%=basePath%>sys/structure/saveCadreAttrUpdate.do', 
		    };  
		    $("#updateCadreAttrForm").ajaxForm(options);
		    
		    if('${ATTR.type}'!='dataList'){
		    	$('#attr_codeTr').hide();
		    	$('#attr_codeNameTr span').html("字段名：");
		    }else{
		    	$('#attr_codeNameTr').hide();
		    }
		    
	});
	
	function typeChange(value){
		if(value=='dataList'){
			$('#attr_codeTr').show();
			$('#attr_codeNameTr').hide();
		}else{
			$('#attr_codeNameTr').show();
			$('#attr_codeTr').hide();
		}
	}
	</script>
</body>
