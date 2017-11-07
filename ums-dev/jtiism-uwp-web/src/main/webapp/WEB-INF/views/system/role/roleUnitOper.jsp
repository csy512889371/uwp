<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<%-- 单位机构授权 --%>

<style>
<!--
.radio.inline, .checkbox.inline {
    padding-top: 0px;
    padding-left: 15px;
    padding-bottom: 0px;
}
-->
</style>
<body>
<div style="width: 95%;margin: auto;">
	<input type="hidden" id="unitOper_roleId" value="${roleId }">
	<div id="roleUnidOperDiv" style="float: left;">
		<input id="r_u_o_SaveBtn" type="submit" class="btn btn-oper" value="保存" />
		<ul id="roleUnidOperTree" class="ztree"></ul>
	</div>
</div>
<script>

$(document).ready(function(){
	var unitTreeSetting;
	var unitNodes = [];
	var dwTree;
	var pName = "";
	var pId = "";
	var ids=[];
	
	//机构树
    unitTreeSetting = {
			check: {
				enable: true,
				nocheckInherit: true,
				chkboxType: { "Y": "s", "N": "s" }
			},
    		data: {
    			simpleData: {
    				enable: true
    			}
    		},
			view: {
				addDiyDom: addDiyDom
			},
    		callback:{
    			onClick : ztreeOnClick,
    			onCheck : ztreeOnCheck
    			
    		}
    	};

	//获取所有机构树的数据
    function getUnitData(){
		//获取数据
		var data = JSON.parse('${unitList }');
       	for(var s in data){
       		unitNodes.push
       		({
       			'id':data[s].ID,
       			'pId':data[s].PARENTID,
       			'name':data[s].CAPTION,
       			'open':true,
       			'oper':0,
       			'unitType':data[s].UNITTYPE
       		});
       	}
	}
    
    //展示树方法
    function showUnitTree(){
		getUnitData();
		dwTree = $.fn.zTree.init($("#roleUnidOperTree"), unitTreeSetting, unitNodes);
		//加载完成后默认选中根节点
		var treeObj = $.fn.zTree.getZTreeObj("roleUnidOperTree");
		var sNodes = treeObj.getNodes();
		if (sNodes.length>0) {
			treeObj.selectNode(sNodes[0]);
		}
		//单位机构树加载完成后加载已有的权限
		loadOldRoleUnitOper();
	}
	
    //初始化展示树
	showUnitTree();
	
	//树节点点击事件
	function ztreeOnClick(event, treeId, treeNode){
		ids.length = 0;//每次点击树节点清空ids数组
		pName = treeNode.name;
		pId = treeNode.id;
	    ids=getChildren(ids,treeNode);//递归查询树节点下所有子孙节点
	}
	
	//递归查询树节点下所有子孙节点id集合
	function getChildren(ids,treeNode){
		ids.push(treeNode.id);
		 if (treeNode.isParent){
			for(var obj in treeNode.children){
				getChildren(ids,treeNode.children[obj]);
			}
	    }
		return ids;
	}
	
	//递归查询树节点下所有子孙节点集合[包含自己]
	var childrenNodes = [];
	function getChildrenNodes(parentNode, childrenNodes){
		childrenNodes.push(parentNode);
		 if (parentNode.isParent){
			for(var obj in parentNode.children){
				getChildrenNodes(parentNode.children[obj],childrenNodes);
			}
	    }
		return childrenNodes;
	}
	
	
	//勾选中单位则默认拥有该单位的只读权限，取消勾选单位则没有该单位的任何权限
	function ztreeOnCheck(event, treeId, treeNode){
		//如果勾选/取消勾选的是父节点，则循环操作子节点
		childrenNodes=[];
		if (treeNode.isParent) {
			childrenNodes=getChildrenNodes(treeNode,childrenNodes);//递归查询树节点下所有子孙节点
		}else{
			childrenNodes = [treeNode];
		}
		if(treeNode.checked){
			for ( var i = 0; i < childrenNodes.length; i++) {
				var radio = document.getElementById("rid_1_"+childrenNodes[i].id);
				radio.checked=true;
				childrenNodes[i].oper = 1;
			}
		}else{
			for ( var i = 0; i < childrenNodes.length; i++) {
				var radio = document.getElementById("rid_0_"+childrenNodes[i].id);
				radio.checked=true;
				childrenNodes[i].oper = 0;
			}
		}
	}
	
	//机构后方添加只读可写单选框
	function addDiyDom(treeId, treeNode) {
		var aObj = $("#" + treeNode.tId + "_a");
		var radionStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" 
					+ "<label class='radio inline'><input type='radio' value='0' id='rid_0_"+treeNode.id+"' name='radio_"+treeNode.id+"' style='display:none;' /></label>"
					+ "<label class='radio inline'><input type='radio' value='2' id='rid_2_"+treeNode.id+"' name='radio_"+treeNode.id+"' /><span>可写</span></label>"
					+ "<label class='radio inline'><input type='radio' value='1' id='rid_1_"+treeNode.id+"' name='radio_"+treeNode.id+"' /><span>只读</span></label>";
//		aObj.append(radionStr);
		aObj.after(radionStr);
		var treeObj = $.fn.zTree.getZTreeObj("roleUnidOperTree");
		var bt = $("input[name='radio_"+treeNode.id+"']");
		if (bt) {
			bt.bind("click", function(){
				//设置权限等级[0：无权限；1：只读权限；2：可写权限]
				treeNode.oper = $("input[name='radio_"+treeNode.id+"']:checked").val();
				//选择权限就说明角色可看到树菜单，即默认选中树菜单前的多选框
				treeObj.checkNode(treeNode, true);
			});
		}

	}

	//保存
	$("#r_u_o_SaveBtn").click(function(){
		var treeObj = $.fn.zTree.getZTreeObj("roleUnidOperTree");
		var nodes = treeObj.getCheckedNodes(true);
		
		var roleId = $("#unitOper_roleId").val();
		var unitIds = "";
		var perlvs = "";
		for ( var i = 0; i < nodes.length; i++) {
			unitIds += nodes[i].id + ",";
			perlvs += nodes[i].oper + ",";
		}
		if(unitIds != "" && perlvs != ""){
			unitIds=unitIds.substring(0, unitIds.length-1);
			perlvs=perlvs.substring(0, perlvs.length-1);
		}
		
		$.ajax({
			type : 'post',
			url  : '<%=basePath%>sys/role/grantRoleUnitOper.do',
			data : {roleId:roleId, unitIds:unitIds, perlvs:perlvs},
			success: function(result){
				openAlert(result.content);
				if(result.success==true){
					
				}
			}
		});
	});
	
	//更改权限前加载旧的单位权限
	function loadOldRoleUnitOper(){
		var ruoList = JSON.parse('${ruoList }');
		if (ruoList==null) {
			return;
		}
		var treeObj = $.fn.zTree.getZTreeObj("roleUnidOperTree");
		for ( var s in ruoList) {
			var ruo = ruoList[s];
			//拥有的权限前面多选框打勾
			var node = treeObj.getNodeByParam("id", ruo.UNITID, null);
			treeObj.checkNode(node, true);
			//权限的等级
			$(":radio[name='radio_"+ruo.UNITID+"'][value='"+ruo.PERLV+"']").attr("checked","checked");
			node.oper = ruo.PERLV;
		}

		
	}
});	
</script>
</body>
