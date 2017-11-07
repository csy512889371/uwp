<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>

<html>
<%-- 单位机构新增页面 --%>
<head>
<script type="text/javascript">
	$(document).ready(function() {
		
		 $("input[name=b0194]").click(function() {
			var radioVal = $(" input[name='b0194']:checked").val();
			if (radioVal == 1) {
				$("#add_new").show();
			}else if(radioVal == 2) {
				$("#add_new").hide();
			}
		}); 
		
		$("#add_new").show();
	});
	$("#unitAddForm .show2").hide();

</script>
</head>
<body>	
		<input type="hidden" value="${b01Hiber.id}" id="b01HiberId_add"/>
		<div style="width: 95%; white-space: nowrap;text-align:center;margin-bottom:30px; display:none;">
				<label class="radio inline"><input type="radio" name="b0194" value="1" checked="checked">新增 </label>&nbsp;&nbsp;
		</div>		
		 <div id="unitAddDiv">
		 <!-- 新增 -->
		  <div id="add_new">
		  	<form action="" method="post" id="unitAddForm" name="unitAddForm">
				<table class="table table-bordered" style="width: 96%;margin: auto;">
					<tr>
						<td>上级单位</td>
						<td>
							<input type="text" id="unitName" name="unitName" style="background-color: rgb(232, 242, 254);" onclick="chooseUnit()" value="${b01Hiber.unit.b0101}" readonly>
							<input type="hidden" id="parentB01Hiber_id" name="parentB01Hiber.id" value="${b01Hiber.id}">
						</td>
						<td><span style="color:red;">*</span>管理权限</td>
						<td><input type="text" id="b0120n2" name="b0120n" ondblclick="dictWindow('UNIT_INFO','B0120','管理权限','b0120n2','b0120n')" class="required selector"
						     onfocus="loadDict('UNIT_INFO','B0120','b0120n2','b0120n')" onblur="cleanNull('CMIS_DEPARTMENT','CODE')">
						<input type="hidden" id="b0120" name="b0120" >
						</td>
					</tr>
					<tr class="show1">
						<td><span style="color:red;">*</span>单位全称</td>
						<td><input  type="text" id="b0101_add" name="b0101" class="required"  maxlength="60"/>
							 <input type="hidden" id="b00" name="b00"></td>
						<td><span style="color:red;">*</span>单位简称</td>
						<td>
							<input  type="text" id="b0104_add" name="b0104" maxlength="20"  class="required">
						</td>
					</tr>
					<tr>
						<td>单位代码</td>
						<td><input  type="text" id="b0111_add" name="b0111" maxlength="150"></td>
						<td><span style="color:red;">*</span>单位性质类别</td>
						<td><input type="text" id="b0131n2" name="b0131n" ondblclick="dictWindow('UNIT_INFO','b0131','单位性质类别','b0131n2','b0131')" class="required selector"
						     onfocus="loadDict('UNIT_INFO','b0131','b0131n2','b0131')" onblur="cleanNull('b0131n2','b0131')">
							 <input type="hidden" id="b0131" name="b0131">
						</td>
					</tr>
					<tr>
						<td><span style="color:red;">*</span>单位隶属关系</td>
						<td style="border: 1px solid #dddddd;">
						<input type="text" id="b0124n2" name="b0124n" ondblclick="dictWindow('UNIT_INFO','b0124','单位隶属关系','b0124n2','b0124')" class="required selector"
						     onfocus="loadDict('UNIT_INFO','b0124','b0124n2','b0124')" onblur="cleanNull('b0124n2','b0124')">
						<input type="hidden" id="b0124" name="b0124" >
						</td>
						<td colspan="2"></td>
					</tr>
					<tr>

					</tr>
				  </table>
				 	 <!--保存 -->
					<div style="text-align: right;margin-right: 15px;padding-top: 5px;">
						<input type="submit" class="btn-style1" id="add_unitSave" value="保存" />
						<input type="reset" class="btn-style4" id="add_unitClear" onclick="$('#b01H_isHidden').val(false);" value="重置" />
					</div>
				</form>
			</div>

	</div>
</body>
<script type="text/javascript">

	$(document).ready(function(){
		$('#unitAddForm').validate({				
		 });
	});

	/**新增  */
	var options={
		success:function(result){
			openAlert(result.content);
			if(result.type=="success"){
				var data =JSON.parse(result.params);
				addTreeNode(data);
				//保存完加载显示页
				$('#unitInfoDiv2').load('<%=basePath%>unit/unit/unitInfoPage.do',{'b01HiberId':data.id});
				$("#unitInfoDiv2").show();
				closeWindow();
			}
		},
		dataType:'json',
		url:'<%=basePath%>unit/unit/saveUnit.do',
	};
		
	$("#unitAddForm").ajaxForm(options);
	 function chooseUnit(){
		 	unitWindow('选择单位', 7, 7, '${libraryId}', function(treeObjList) {
				if(treeObjList !=null && typeof(treeObjList) != 'undefined') {
					if(treeObjList.length!=0){
						var obj = treeObjList[0];
			 			$('#unitAddForm #unitName').val(obj.name);//上级单位
						$('#unitAddForm #parentB01Hiber_id').val(obj.id);
					}
				}
		 	});
	} 
	 
	 function chooseUnitInfo() {
		var method = function(treeObjList) {
			if(treeObjList !=null && typeof(treeObjList) != 'undefined') {
				if(treeObjList.length!=0){
					var obj = treeObjList[0];
					//alert(obj.unitCode);
					//全称
		 	 		$("#b0101_add").val(obj.codeName);
					//alert($("#b0101"));
		 	 		//简称
		 			$("#b0104_add").val(obj.codeAName);
		 	 		//单位代码
		 			$("#b0111_add").val(obj.unitCode);
				}
			}
	 	};
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
    	commonWindow('管理权限选择', 10, 'egxb0115a', 'egxb0115', 300, 400, method, checkedId);
	 }
	 
</script>
</html>
