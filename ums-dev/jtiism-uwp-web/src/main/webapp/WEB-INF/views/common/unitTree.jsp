 <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common.inc"%>
<style>
	#chooseUnitPage{
		height:35px;line-height: 35px;padding: 5px 0px 5px 10px;border-bottom: 1px solid #e5e5e5;text-align: left;
	}
	.left_btn{margin-left: 10px;}
</style>
<body>
<div style="border: 1px solid #e5e5e5;height: 475px;">
	<div id="showTable" style="width:97%;margin: auto;">
	<div id="unitTreeDiv_s" style="overflow: auto;" >
		<ul id="unitTreeContainer" class="ztree" style="width:90%;"></ul>
	</div>
	</div>
</div>
<div style="text-align: right;margin-right: 5px;">
		<button class="btn-style1" onclick="getUnitValues()">确 定</button>
		<button class="btn-style4" onclick="closeUnitPage()">取 消</button>
</div>
	<script type="text/javascript">
	$('#unitTreeDiv_s').height(390);
	
	var unitTreeNodeObjs = null;
	var unitTreeObj;
	
	$(document).ready(function(){
		loadUnitTreeData();
		
	});	
	
	/**根据加载单位树	**/
	function loadUnitTreeData(){	
		$.ajax({
			type : 'post',
			url  : '<%=basePath%>common/unitTree/getTree.do',
			data : {},
			success: function(data){		
				loadCommonUnitTree(data);
			}
		});
	}
	
	/**树形展示**/
	function loadCommonUnitTree(data){
		var dictNodes = [];
	    for(var i in data){
	    	dictNodes.push({'id':data[i].id, 'pId':data[i].pid, 'name':data[i].name, 'fullName':data[i].fullName,'type':data[i].type,
	    		'unitId':data[i].unitId, 'unitName':data[i].unitName
	    	});
	    }
	    
	    var setting = null;
	    if('${selType}'==1){//单选
	    	setting = {
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: unitTreeOnclick,
					onDblClick: unitTreeOnDblClick
				},
				view:{showIcon:false}
			};
	    }else if('${selType}'==7){//单选
	    	setting = {
					data: {
						simpleData: {
							enable: true
						}
					},
					callback: {
						onClick: unitTreeOnclick,
						onDblClick: unitTreeOnDblClick2
					},
					view:{showIcon:false}
				};
		}else{//多选
	    	setting = {
				data: {
					simpleData: {
						enable: true
					}
				},
				check: {
					enable: true,
					chkStyle: "checkbox",
					nocheckInherit: true,
					chkboxType: {"Y": "s", "N": "s"}
				},
				callback: {
					onClick: treeOnclick,
					onCheck: unitTreeOncheck
				},
				view:{showIcon:false}
			};
	    }
	    
	    unitTreeObj = $.fn.zTree.init($("#unitTreeContainer"), setting, dictNodes);
	    unitTreeObj.expandAll(true);
	}
	
	function treeOnclick(event, treeId, treeNode){
		var unitTreeObj = $.fn.zTree.getZTreeObj("unitTreeContainer");
		var checkNodes = unitTreeObj.getCheckedNodes(true);
		if (treeNode.checked == true) {
			unitTreeObj.cancelSelectedNode(treeNode);
			unitTreeObj.checkNode(treeNode, false, true);
   		} else {
   			unitTreeObj.checkNode(treeNode, true, true);
   		}
		unitTreeObj = $.fn.zTree.getZTreeObj("unitTreeContainer");
		checkNodes = unitTreeObj.getCheckedNodes(true);
   		for (var i = 0; i < checkNodes.length; i++) {
   			if (checkNodes[i].checked == true) {
   				unitTreeObj.selectNode(checkNodes[i], true);
   			} else {
   				unitTreeObj.cancelSelectedNode(checkNodes[i]);
   			}
		}
	}

	function unitTreeOnclick(event,treeId,treeNode){
		unitTreeNodeObjs = new Array(); 
		unitTreeNodeObjs.push(treeNode);
	}
	
	function unitTreeOnDblClick(event, treeId, treeNode) {
		unitTreeNodeObjs = new Array(); 
		unitTreeNodeObjs.push(treeNode);
		getUnitValues();
	}
	function unitTreeOnDblClick2(event, treeId, treeNode) {
		unitTreeNodeObjs = new Array(); 
		unitTreeNodeObjs.push(treeNode);
		getUnitValues();
	}
	
	function unitTreeOncheck(event, treeId, treeNode) {
		var treeObj = $.fn.zTree.getZTreeObj("unitTreeContainer");
		var allNodes = treeObj.transformToArray(treeObj.getNodes());
		for (var i = 0; i < allNodes.length; i++) {
			if (allNodes[i].checked == true) {
				unitTreeObj.selectNode(allNodes[i], true);
			} else {
				unitTreeObj.cancelSelectedNode(allNodes[i]);
			}
		}
		unitTreeObj = $.fn.zTree.getZTreeObj("unitTreeContainer");
		var checkNodes = unitTreeObj.getCheckedNodes(true);
		for (var i = 0; i < checkNodes.length; i++) {
			unitTreeObj.selectNode(checkNodes[i], true);
		}
	};
	
	/**确认**/
	function getUnitValues(){
		var selType = '${selType}';
		if(selType==1){
			if(unitTreeNodeObjs!=null && typeof(unitTreeNodeObjs)!='undefined') {
				if(unitTreeNodeObjs.length==0) {
					openAlert('请选择一条记录！');
					return;
				}
				//调用回调方法
				if(unitCallbacks!=null && typeof(unitCallbacks)!='undefined'){
					unitCallbacks.fireWith(window, new Array(unitTreeNodeObjs));
					//清空回调
					unitCallbacks.empty();
				}
			} else {
				openAlert('请选择一条记录！');
				return;
			}
		}else if(selType==2){
			var treeObj = $.fn.zTree.getZTreeObj("unitTreeContainer");
			var unitCheckNodeObjs = treeObj.getCheckedNodes(true);
			
			if(unitCheckNodeObjs!=null && typeof(unitCheckNodeObjs)!='undefined') {
				
				var unitCheckNodes = new Array();
				for ( var int = 0; int < unitCheckNodeObjs.length; int++) {
					unitCheckNodes.push(unitCheckNodeObjs[int]);
				}
				//调用回调方法
				if(unitCallbacks!=null && typeof(unitCallbacks)!='undefined'){
					unitCallbacks.fireWith(window, new Array(unitCheckNodes));
					//清空回调
					unitCallbacks.empty();
				}
			}else{
				openAlert('请选择一条记录！');
				return;
			}
		}else if(selType==7){
			if(unitTreeNodeObjs!=null && typeof(unitTreeNodeObjs)!='undefined') {
				var unitCheckNodes = new Array();
				for ( var int = 0; int < unitTreeNodeObjs.length; int++) {
					unitCheckNodes.push(unitTreeNodeObjs[int]);
				}
				//调用回调方法
				if(unitCallbacks!=null && typeof(unitCallbacks)!='undefined'){
					unitCallbacks.fireWith(window, new Array(unitCheckNodes));
					//清空回调
					unitCallbacks.empty();
				}
			}else{
				openAlert('请选择一条记录！');
				return;
			}
		}

		closeUnitPage();
	}
	/**取消**/
	function closeUnitPage(){
		$("#dict_window").dialog('close');
	}
	
	</script>
</body>