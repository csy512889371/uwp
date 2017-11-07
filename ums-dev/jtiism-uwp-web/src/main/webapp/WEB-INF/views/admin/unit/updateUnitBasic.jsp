<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<body>
	<div>
			<!-- 单位基本信息集 -->
		  <form action="" method="post" id="basicUnitForm" name="basicUnitForm">
			<input type="hidden" id="b01HiberId" name="b01HiberId" value="${b01Hiber.id}">
			<input type="hidden" name="unitType" value="${b01Hiber.unit.unitType}" />
		 	<table class="table table-bordered" style="width: 100%;">
			<tr>
				<td>上级单位</td>
				<td>
				<input type="text" id="unitName" name="unitName" style="background-color: rgb(232, 242, 254);" onclick="chooseUnit()" value="${b01Hiber.parentB01Hiber.unit.b0101}">
				<input type="hidden" id="parentB01Hiber_id" name="parentB01Hiber.id" value="${b01Hiber.parentB01Hiber.id}">
				</td>
				<td><span style="color:red;">*</span>管理权限</td>
				<td><input type="text" id="b0120n" name="b0120n" ondblclick="dictWindow('UNIT_INFO','b0120','管理权限','b0120n','b0120')" class="required selector"
						   onfocus="loadDict('UNIT_INFO','b0120','b0120n','b0120')" onblur="cleanNull('b0120n','b0120')" value="${indexDict:getCodeName('UNIT_INFO','b0120',b01Hiber.unit.b0120n)}">
				<input type="hidden" id="b0120" name="b0120" value="${b01Hiber.unit.b0120}">
				</td>
			</tr>
			<tr class="show1">
				<td><span style="color:red;">*</span>单位全称</td>
				<td><input  type="text" id="b0101a" name="b0101" value="${b01Hiber.unit.b0101}" maxlength="60"  class="required"/></td>
				<td><span style="color:red;">*</span>单位简称</td>
				<td>
					<input  type="text" id="b0104" name="b0104" value="${b01Hiber.unit.b0104}" maxlength="20"  class="required">
				</td>
			</tr>
			<tr>
				<td>单位代码</td>
				<td><input  type="text" id="b0111" name="b0111" maxlength="150" value="${b01Hiber.unit.b0111}"></td>
				<td><span style="color:red;">*</span>单位性质类别</td>
				<td><input type="text" id="b0131n" name="b0131n" ondblclick="dictWindow('UNIT_INFO','b0131','单位性质类别','b0131n','b0131')" class="required selector"
				onfocus="loadDict('UNIT_INFO','b0131','b0131n','b0131')" onblur="cleanNull('b0131n','b0131')" value="${indexDict:getCodeName('UNIT_INFO','b0131',b01Hiber.unit.b0131n)}">
					<input type="hidden" id="b0131" name="b0131" value="${b01Hiber.unit.b0131}">
				</td>
			</tr>
			<tr>
				<td><span style="color:red;">*</span>单位隶属关系</td>
				<td><input type="text" id="b0124n" name="b0124n" ondblclick="dictWindow('UNIT_INFO','b0124','单位隶属关系','b0124n','b0124')" class="required selector"
				onfocus="loadDict('UNIT_INFO','b0124','b0124n','b0124')" onblur="cleanNull('b0124n','b0124')" value="${indexDict:getCodeName('UNIT_INFO','b0124',b01Hiber.unit.b0124n)}">
					<input type="hidden" id="b0124" name="b0124" value="${b01Hiber.unit.b0124}">
				</td>
				<td colspan="2"></td>
			</tr>
		  </table>
		  	<div style="text-align: right;margin-right: 15px;padding-top: 5px;">
		  		<input type="submit" class="btn-style1" id="basicUnitSave" value="保存" />
				<input type="reset" class="btn-style4" id="basicUnitClear" value="重置" />
		  	</div>
		  <input type="hidden" id="b00" name="b00" value="${b01Hiber.unit.b00}" >
		 </form>
	</div>
<script type="text/javascript">
$(document).ready(function(){
	$('#basicUnitForm').validate({				
		 
	 });
	//提交单位基本信息集
	var options={
		success:function(result){
			openAlert(result.content);
			if(result.type=="success"){
				closeWindow();
				var data =JSON.parse(result.params);
				updateTreeNode(data);
				$('#unitInfoDiv2').load('<%=basePath%>unit/unit/unitInfoPage.do',{'b01HiberId':data.id});
				$("#unitInfoDiv2").show();
			}
		},
		dataType:'json',
		url:'<%=basePath%>unit/unit/updateBasicUnit.do',
	};
	
	$("#basicUnitForm").ajaxForm(options);
});
//更新选中的节点名称
function updateNode(){
	 var treeObj = $.fn.zTree.getZTreeObj("coderTree");	
	 var nodes = treeObj.getSelectedNodes();
	 var b0101 = $("#b0101a").val();
	 if(nodes.length>0){
		 nodes[0].name=b0101;
		 treeObj.updateNode(nodes[0]);
	 }
}

function selectDepartment(){
	 var method = function(treeObj){
		// 选中的ID
		var checkId = "";
		// 名称
  		var checkName = "";
  		if (typeof(selTreeObject) != 'undefined' ) {
  			for (var i = 0; i < selTreeObject.length; i ++) {
      			checkId += selTreeObject[i].id + ",";
      			checkName += selTreeObject[i].name + ",";
			}
      		checkId = checkId.substring(0, checkId.length - 1);
      		checkName = checkName.substring(0, checkName.length - 1);
      		$('#' + nameObj).val(checkName);
			$('#' + idObj).val(checkId);
			cadreTypeId = checkId;
  		} else {
  			$('#' + nameObj).val('');
			$('#' + idObj).val('');
  			cadreTypeId = null;
  		}
  		selTreeObject = undefined;
	};
	var checkedId = $('#egxb0115').val();
	commonWindow('处室选择', 10, 'egxb0115a', 'egxb0115', 300, 400, method, checkedId);
}

function chooseUnit(){
 	unitWindow('选择单位', 7, 7, '${libraryId}', function(treeObjList) {
 		selectedNode=null;
		if(treeObjList !=null && typeof(treeObjList) != 'undefined') {
			if(treeObjList.length!=0){
				var obj = treeObjList[0];
				if ($('#basicUnitForm #b00').val() == obj.unitId) {
					openAlert('上级单位不能是当前单位');
					return false;
				}
	 			$('#basicUnitForm #unitName').val(obj.name);//上级单位
				$('#basicUnitForm #parentB01Hiber_id').val(obj.id);
			}
		}
 	});
} 

</script>
</body>

