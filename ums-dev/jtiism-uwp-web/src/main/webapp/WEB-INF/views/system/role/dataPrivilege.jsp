<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<%-- 权限设置页面 --%>
<body>
	<div id="searchItems">
		<div id="basicTreeDiv" style="width:180px;float: left;height: 590px;border-right: solid 1px #2898fd;">
			<ul id="basicTree" class="ztree ztree1"></ul>
		</div>
		<div id="dataAttrList" style="margin-top:0px;margin-left:200px;">
			<!-- 基本信息项 -->
		</div>
	</div>
	<script type="text/javascript">
	//$('#searchItems').height(commonTalbeHeight - 225);
	//$('#dataAttrList').height($(window).height() - 335);
	//$('#basicTreeDiv').height(commonTalbeHeight+40);
	var codeAttrs=JSON.parse('${CODEATTRS}');
	var roleId='${roleId}';//角色ID
	var cadreAttrSetting = {
			view: {
				showLine: false,
				showIcon: false,
				selectedMulti: false,
				dblClickExpand: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeClick: beforeClick,
				onClick : changeInfo
			}
		};
		/**以下数据需要配置在表中**/
		var basicNodes =[
		];
		for(var i in codeAttrs){
			if(codeAttrs[i].parentId=="0"){
				basicNodes.push({id:codeAttrs[i].id,pId:codeAttrs[i].parentId,name:codeAttrs[i].attrName,infoSet:"",open:true});
			}else{
				basicNodes.push({id:codeAttrs[i].id,pId:codeAttrs[i].parentId,name:codeAttrs[i].attrName,infoSet:codeAttrs[i].attrCode});
			}
		}
		function beforeClick(treeId, treeNode) {
			if (treeNode.level == 0 ) {
				var zTree = $.fn.zTree.getZTreeObj("basicTree");
				zTree.expandNode(treeNode);
				return false;
			}
			return true;
		}
		function changeInfo(event, treeId, treeNode){
			$('#dataAttrList').load('<%=basePath%>'+treeNode.infoSet,{'roleId':roleId});
		}
		$('document').ready(function() {
			var treeObj = $("#basicTree");
			$.fn.zTree.init(treeObj, cadreAttrSetting, basicNodes);
			zTree_Menu = $.fn.zTree.getZTreeObj("basicTree");
			
			curMenu = zTree_Menu.getNodes()[0].children[0];
			zTree_Menu.selectNode(curMenu);
			$('#dataAttrList').load('<%=basePath%>'+curMenu.infoSet,{'roleId':roleId});
		});
		function roleCadreList(){
			$('#dataAttrList').load('<%=basePath%>sys/role/roleCadretypeList.do',{});
		}
		function roleDeptList(){
			$('#dataAttrList').load('<%=basePath%>sys/role/roleDeptList.do',{});
		}
	
	</script>
</body>
