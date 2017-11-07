 <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<body>
<div>
	<div id="chooseUnitPage" style="height:35px;line-height: 35px;margin: 5px auto auto 10px;">
		 库：<select name="unitLibraryId" id="unitLibraryId">		
		 		<c:forEach items="${codeList}" var="i">
		     		<option value="${i.id}">${i.name}</option>
				</c:forEach> 
			</select>
	</div>
	<div id="showTable" style="width:97%;margin: auto;">
		 <ul>
			 <li><a href="javascript:void(0);">单位选择</a></li>
	 	</ul>
	<div id="coderTreeDiv2" style="overflow: scroll;" >
		<ul id="coderTree2" class="ztree" style="width:90%;"></ul>
	</div>
	</div>
</div>
<div >
		<button class="btn btn-oper" onclick="ok()">确 定</button>
		<button class="btn btn-oper" onclick="cancel()">取 消</button>
</div>
	<script type="text/javascript">
	$('#coderTreeDiv2').height(360);
	
	$("#showTable").tabs({
	      collapsible: false
	});
	
	$(document).ready(function(){
		var library = '${codeList}';
		if(library!=null){
			<c:choose>
				<c:when test="${not empty libraryId}">
					var libId = '${libraryId}';
					$("#unitLibraryId option[value='${libraryId}']").attr("selected", "selected")
				</c:when>
				<c:otherwise>
					var libId = $("#unitLibraryId option:first").val();
				</c:otherwise>
			</c:choose>
			//alert(libId);
			loadUnitTreeData(libId);
		}
		$("#unitLibraryId").change(function(){
			var libId = $("#unitLibraryId").val();
			loadUnitTreeData(libId);
		});
	});	
	
	/**根据libraryId加载单位树	**/
	function loadUnitTreeData(libId){	
		$.ajax({
			type : 'post',
			url  : '<%=basePath%>unit/unit/getUnitList.do',
			data : {libraryId : libId},
			success: function(data){		
				loadUnitTree(data);
			}
		});
	}
	
	/**树形展示**/
	function loadUnitTree(data){
		var dictNodes = [];
	    for(var i in data){
	    	dictNodes.push({'id':data[i].id, 'pId':data[i].pid, 'name':data[i].name, 'fullName':data[i].fullName,'type':data[i].type,
	    		'groupId':data[i].groupId, 	'groupName':data[i].groupName, 'unitId':data[i].unitId, 'unitName':data[i].unitName,
	    		'libraryId':data[i].libraryId, 'libraryName':data[i].libraryName
	    	});
	    }
		var coderTreeSetting = {
    		check: {
    			enable: false,
    			nocheckInherit: true
    		},
    		data: {
    			simpleData: {
    				enable: true
    			}
    		},
    		callback : {
    			onClick : unitTreeOnclick,
    			onDblClick : unitTreeOnDblClick
			}
    	};
		$.fn.zTree.init($("#coderTree2"), coderTreeSetting, dictNodes);
	}

	
	/**节点双击事件**/
	var unitTreeSelectedNode=null;
	function unitTreeOnclick(event,treeId,treeNode){
		unitTreeSelectedNode = treeNode;
	}
	
	function unitTreeOnDblClick(event, treeId, treeNode) {
		unitTreeSelectedNode=treeNode;
		ok();
	};
	
	/**确认**/
	var pageType='${pageType}';
	function ok(){
		if(unitTreeSelectedNode.type=="unitGroup"){
			openAlert('这是归口！');
			return;
		} if(pageType=="add"){
			$('#unitAddForm #unitName').val(unitTreeSelectedNode.name);//上级单位
			$('#unitAddForm #parentB01Hiber_id').val(unitTreeSelectedNode.id);		
			//$('#unitAddForm #unitLib_id').val(selectedNode.libraryId);//库
		 	$('#unitAddForm #groupName').val(unitTreeSelectedNode.groupName);//归口
			$('#unitAddForm #unitGroup_dmcod').val(unitTreeSelectedNode.groupId); 
			//$('#unitAddForm #parentUnit_b00').val(selectedNode.unitId);//parentUnit
		}else if(pageType=="copy"){
			$('#unitAddCopyForm #unitName').val(unitTreeSelectedNode.name);//上级单位
			$('#unitAddCopyForm #parentB01Hiber_id').val(unitTreeSelectedNode.id);
			//$('#unitAddCopyForm #unitLib_id').val(selectedNode.libraryId);//库
			$('#unitAddCopyForm #groupName').val(unitTreeSelectedNode.groupName);//归口
			$('#unitAddCopyForm #unitGroup_dmcod').val(unitTreeSelectedNode.groupId);
			//$('#unitAddCopyForm #parentUnit_b00').val(selectedNode.unitId);//parentUnit
		}else if(pageType=="shared"){
			$('#unitAddCopyForm #sharedUnit').val(unitTreeSelectedNode.name);
			$('#unitAddCopyForm #unit_b00').val(unitTreeSelectedNode.unitId);
		}else if(pageType="vunit"){
			$('#addVunitUnitForm #unitName').val(unitTreeSelectedNode.name);
			$('#addVunitUnitForm #parentB01Hiber_id').val(unitTreeSelectedNode.id);		
			//$('#addVunitUnitForm #unitLib_id').val(selectedNode.libraryId);
			$('#addVunitUnitForm #groupName').val(unitTreeSelectedNode.groupName);//归口
			$('#addVunitUnitForm #unitGroup_dmcod').val(unitTreeSelectedNode.groupId);
			//$('#addVunitUnitForm #parentUnit_b00').val(selectedNode.unitId);//parentUnit
		}
		closeWindow(2);			
	}
	/**取消**/
	function cancel(){		
		closeWindow(2);
	}
	</script>
</body>