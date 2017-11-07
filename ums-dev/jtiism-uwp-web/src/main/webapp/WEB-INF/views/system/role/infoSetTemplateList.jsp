<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
  <!-- 信息集模板 -->
  <style>
	.radio.inline, .radio.inline {
		float:right;
	    padding-top: 0px;
	    padding-left: 15px;
	    padding-bottom: 0px;
	}
	.radio.first{
		float:right;
		padding-left: 30px;
	}
  </style>
  <body>
    <div id="infoSetListDiv" style="width:100%;float: left;overflow: auto;height: 580px;">
        <div style="position: absolute;width:550px;background-color: #fff;z-index: 9999;">
        	<button class="btn-style4" onclick="expandAll()">展开全部</button>
        	<button class="btn-style4" onclick="closeAll()">闭合全部</button>&nbsp;&nbsp;&nbsp;&nbsp;
        	<button class="btn-style1" onclick="saveRoleInfoSet()">保 存</button>	
        </div>
		<ul id="infoSetListTree" class="ztree" style="width:90%;margin-top: 40px;"></ul>
	</div>
	
	<script>
		//$('#infoSetListDiv').height($(window).height() - 150);
		var roleInfoSetSetting = {
			check: {
				enable: true,
				nocheckInherit: true,
				chkboxType: {"Y" : "ps", "N" : "ps"},
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
		var ids=[];
		var infoSetNodes = [];
		
		function getInfoSetData(){
			//获取数据
			var data = JSON.parse('${infoSetList}');
	       	for(var s in data){
	       		infoSetNodes.push({'id':data[s].id,'pId':data[s].parentId,'attrCode':data[s].attrCode,'name':data[s].attrName,'open':true,'oper':0});
	       	}
		}
		
		function showInfoSetTree(){
			getInfoSetData();
			$.fn.zTree.init($("#infoSetListTree"), roleInfoSetSetting, infoSetNodes);
			//信息集树加载完成后加载已有的权限
			loadOldRoleCadreOper();
		}
		
		showInfoSetTree();
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
		 function saveRoleInfoSet(){
			var treeObj = $.fn.zTree.getZTreeObj("infoSetListTree");
     		var nodes = treeObj.getCheckedNodes(true);
     		var privCodes = "";
     		var privs = "";
     		for (var i = 0; i < nodes.length; i++) {
     			if(nodes[i].attrCode != null && nodes[i].attrCode != ''){//剔除不是该级的节点
					privCodes = privCodes + nodes[i].attrCode + ",";
					privs = privs + nodes[i].oper + ",";
					console.log("nodes[i]:"+nodes[i]);
     			}
			}
     		
     		if(privCodes != ""){
     			privCodes = privCodes.substring(0,privCodes.length-1);
     			privs = privs.substring(0,privs.length-1);
     		}
     		$.ajax({
         		type : 'post',
         		url  : '<%=basePath%>sys/role/grantRoleInfoCadre.do',
         		data : {roleId:'${roleId}',privCodes:privCodes,privs:privs,infoType:0},
         		success: function(result){
         			openAlert(result.content);
         		}
         	});
     	}
		//信息集标题后添加只读可写单选框
		function addDiyDom(treeId, treeNode) {
			var aObj = $("#" + treeNode.tId + "_a");
			var radionStr = '';
			if(treeNode.attrCode != null && treeNode.attrCode != ''){
				radionStr = "<span class='radio inline first'><span class='radio inline first'><input type='radio' value='' id='rid_0_"+treeNode.attrCode+"' name='radio_"+treeNode.attrCode+"' style='display:none;' /></span>"
								+ "(<span class='radio inline'><input type='radio' value='1' id='rid_2_"+treeNode.attrCode+"' name='radio_"+treeNode.attrCode+"' /><span>写</span>)</span>"
								+ "<span class='radio inline'><input type='radio' value='0' id='rid_1_"+treeNode.attrCode+"' name='radio_"+treeNode.attrCode+"' /><span>读</span></span></span>";
			} else if (treeNode.pId == '241') {
				radionStr = "<span class='radio inline first'><span style='color:red;'><span class='radio inline first'><input type='radio' id='ridall_0_"+treeNode.attrCode+"' name='radioall_"+treeNode.attrCode+"' style='display:none;'/></span>"
							+ "(全选：<span class='radio inline'><input type='radio' onclick='checkOper("+2+",\""+treeNode.id+"\")' id='ridall_2_"+treeNode.attrCode+"' name='radioall_"+treeNode.attrCode+"'/><span>写</span>)</span>"
							+ "<span class='radio inline'><input type='radio' onclick='checkOper("+1+",\""+treeNode.id+"\")' id='ridall_1_"+treeNode.attrCode+"' name='radioall_"+treeNode.attrCode+"'/><span>读</span></span></span></span>";
			}
			aObj.after(radionStr);
			var treeObj = $.fn.zTree.getZTreeObj("infoSetListTree");
			var bt = $("input[name='radio_"+treeNode.attrCode+"']");
			if (bt) {
				bt.bind("click", function(){
					//设置权限等级[0：无权限；1：只读权限；2：可写权限]
					treeNode.oper = $("input[name='radio_"+treeNode.attrCode+"']:checked").val();
					//选择权限就说明角色可看到信息集，即默认选中树菜单前的多选框
					treeObj.checkNode(treeNode, true);
				});
			}
		}
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
		//勾选中单位则默认拥有该信息集的只读权限，取消勾选单位则没有该信息集的任何权限
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
					var radio = document.getElementById("rid_1_"+childrenNodes[i].attrCode);
					if(radio != null) {
						radio.checked=true;
					}
					childrenNodes[i].oper = 0;
				}
			}else{
				for ( var i = 0; i < childrenNodes.length; i++) {
					var radio1 = document.getElementById("rid_1_"+childrenNodes[i].attrCode);
					if(radio1 != null) {
						radio1.checked=false;
					}
					var radio2 = document.getElementById("rid_2_"+childrenNodes[i].attrCode);
					if(radio2 != null) {
						radio2.checked=false;
					}
					//childrenNodes[i].oper = 0;
				}
			}
		}

		function loadOldRoleCadreOper(){
			var ripList = JSON.parse('${ripList}');
			if (ripList==null) {
				return;
			}
			
			var treeObj = $.fn.zTree.getZTreeObj("infoSetListTree");
			for ( var s in ripList) {
				var rip = ripList[s];
				//拥有的权限前面多选框打勾
				var node = treeObj.getNodeByParam("attrCode", rip.privCode, null);
				treeObj.checkNode(node, true);
				//权限的等级
				$("input[name='radio_"+rip.privCode+"'][value='"+rip.priv+"']").attr("checked","checked");
				node.oper = rip.priv;
			}

		}
		function expandAll(){
			var treeObj = $.fn.zTree.getZTreeObj("infoSetListTree");
			treeObj.expandAll(true);
		}
		
		function closeAll(){
			var treeObj = $.fn.zTree.getZTreeObj("infoSetListTree");
			treeObj.expandAll(false);
		}
		function checkOper(type,id){
			for ( var i = 0; i < infoSetNodes.length; i++) {
				if(infoSetNodes[i].attrCode != null && infoSetNodes[i].pId == id) {
					var treeObj = $.fn.zTree.getZTreeObj("infoSetListTree");
					var treeNode = treeObj.getNodeByParam("id",infoSetNodes[i].id);
					//设置权限等级[0：无权限；1：只读权限；2：可写权限]
					treeNode.oper = $("input[name='radio_"+treeNode.attrCode+"']:checked").val();
					//选择权限就说明角色可看到信息集，即默认选中树菜单前的多选框
					treeObj.checkNode(treeNode, true);
					
					if(type == 1) {//全选读
						document.getElementById("rid_1_"+infoSetNodes[i].attrCode).checked = true;
						document.getElementById("rid_2_"+infoSetNodes[i].attrCode).checked = false;
						treeNode.oper = 0;
					}else if(type == 2) {//全选写
						document.getElementById("rid_1_"+infoSetNodes[i].attrCode).checked = false;
						document.getElementById("rid_2_"+infoSetNodes[i].attrCode).checked = true;
						treeNode.oper = 1;
					}

				}
			}
		}
	</script>
  </body>
