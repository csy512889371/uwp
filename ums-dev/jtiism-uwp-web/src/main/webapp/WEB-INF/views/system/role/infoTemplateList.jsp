<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
  <!-- 信息项模板 -->
  <style>
	.radio.inline, .radio.inline {
		float:right;
	    padding-top: 0px;
	    padding-left: 15px;
	    padding-bottom: 0px;
	    margin: 0px;
	}
	.radio.first{
		float:right;
		padding-left: 30px;
	}
  </style>
  <body>
    <div id="infoListDiv" style="width:100%;float: left;overflow: auto;height: 580px;">
        <div style="position: absolute;width:550px;background-color: #fff;z-index: 9999;padding-bottom: 10px;">
        	<button class="btn-style4" onclick="expandAll()">展开全部</button>
        	<button class="btn-style4" onclick="closeAll()">闭合全部</button>&nbsp;&nbsp;&nbsp;&nbsp;
        	<button class="btn-style1" onclick="saveRoleInfoSet()">保 存</button>	
        </div>
		<ul id="infoListTree" class="ztree" style="width:90%;margin-top: 40px;"></ul>
	</div>
	
	<script>
		//$('#infoListDiv').height($(window).height() - 150);
		var roleInfoSetSetting = {
			check: {
				enable: true,
				nocheckInherit: false,
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
		var infoNodes = [];
		
		function getInfoSetData(){
			//获取数据
			var data = JSON.parse('${infoList}');
	       	for(var s in data){
	       		infoNodes.push({'id':data[s].id,'pId':data[s].parentId,'attrCode':data[s].attrCode,'name':data[s].attrName,'status':data[s].status,'open':true,'oper':0});
	       	}
		}
		
		function showInfoSetTree(){
			getInfoSetData();
			$.fn.zTree.init($("#infoListTree"), roleInfoSetSetting, infoNodes);
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
			var treeObj = $.fn.zTree.getZTreeObj("infoListTree");
     		var nodes = treeObj.getCheckedNodes(true);
     		var privCodes = "";
     		var privs = "";
     		for (var i = 0; i < nodes.length; i++) {
     			if(nodes[i].attrCode != null && nodes[i].attrCode != '' && nodes[i].attrCode.indexOf("_") > 0){////剔除不是该级的节点(_表示信息项的数据)
					privCodes = privCodes + nodes[i].attrCode + ",";
					privs = privs + nodes[i].oper + ",";
     			}
			}
     		
     		if(privCodes != ""){
     			privCodes = privCodes.substring(0,privCodes.length-1);
     			privs = privs.substring(0,privs.length-1);
     		}
     		$.ajax({
         		type : 'post',
         		url  : '<%=basePath%>sys/role/grantRoleInfoCadre.do',
         		data : {roleId:'${roleId}',privCodes:privCodes,privs:privs,infoType:1},
         		success: function(result){
         			openAlert(result.content);
         		}
         	});
     	}
		//信息项标题后添加只读可写单选框
		function addDiyDom(treeId, treeNode) {
			var aObj = $("#" + treeNode.tId + "_a");
			var radionStr = '';
			if(treeNode.status == -1){
				radionStr = "<span class='radio inline first'><span class='radio inline first'><input type='radio' value='' id='rid_0_"+treeNode.attrCode+"' name='radio_"+treeNode.attrCode+"' style='display:none;' /></span>";
				if(treeNode != null && treeNode.attrCode != 'A01_ega0128a' && treeNode.attrCode != 'A01_ega0129' &&treeNode.attrCode != 'A01_ega0119' && treeNode.attrCode != 'A01_ega0126' && treeNode.attrCode != 'A01_ega0125' && treeNode.attrCode != 'A01_ega0107' && treeNode.attrCode != 'A01_ega0130' && treeNode.attrCode != 'A01_ega0132') {
					radionStr += "<span class='radio inline'><input type='radio' value='1' id='rid_2_"+treeNode.attrCode+"' name='radio_"+treeNode.attrCode+"' /><span>写</span>)</span>";
					radionStr += "(<span class='radio inline'><input type='radio' value='0' id='rid_1_"+treeNode.attrCode+"' name='radio_"+treeNode.attrCode+"' /><span>读</span></span></span>";
				} else {
					radionStr += "(<span class='radio inline'><input type='radio' value='0' id='rid_1_"+treeNode.attrCode+"' name='radio_"+treeNode.attrCode+"' /><span>读</span>)</span></span>";
				}
				
			} else if (treeNode.status == -2 ) {
				radionStr = "<span class='radio inline first'><span style='color:red;'><span class='radio inline first'><input type='radio' id='ridall_0_"+treeNode.attrCode+"' name='radioall_"+treeNode.attrCode+"' style='display:none;'/></span>"
							+ "(全选：<span class='radio inline'><input type='radio' onclick='checkOper("+2+",\""+treeNode.id+"\")' id='ridall_2_"+treeNode.attrCode+"' name='radioall_"+treeNode.attrCode+"'/><span>写</span>)</span>"
							+ "<span class='radio inline'><input type='radio' onclick='checkOper("+1+",\""+treeNode.id+"\")' id='ridall_1_"+treeNode.attrCode+"' name='radioall_"+treeNode.attrCode+"'/><span>读</span></span></span></span>";
			}
			aObj.after(radionStr);
			var treeObj = $.fn.zTree.getZTreeObj("infoListTree");
			var bt = $("input[name='radio_"+treeNode.attrCode+"']");
			if (bt) {
				bt.bind("click", function(){
					//设置权限等级[0：无权限；1：只读权限；2：可写权限]
					treeNode.oper = $("input[name='radio_"+treeNode.attrCode+"']:checked").val();
					//选择权限就说明角色可看到信息项，即默认选中树菜单前的多选框
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
		//勾选中单位则默认拥有该信息项的只读权限，取消勾选单位则没有该信息项的任何权限
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
				}
			}
		}
		
		function loadOldRoleCadreOper(){
			var ripList = JSON.parse('${ripList}');
			if (ripList==null) {
				return;
			}
			
			var treeObj = $.fn.zTree.getZTreeObj("infoListTree");
			for ( var s in ripList) {
				var rip = ripList[s];
				//拥有的权限前面多选框打勾
				var node = treeObj.getNodeByParam("attrCode", rip.privCode, null);
				if(node!=null) {
                    treeObj.checkNode(node, true);
                    //权限的等级
                    $("input[name='radio_"+rip.privCode+"'][value='"+rip.priv+"']").attr("checked","checked");
                    node.oper = rip.priv;
                }
			}


		}
		function expandAll(){
			var treeObj = $.fn.zTree.getZTreeObj("infoListTree");
			treeObj.expandAll(true);
		}
		
		function closeAll(){
			var treeObj = $.fn.zTree.getZTreeObj("infoListTree");
			treeObj.expandAll(false);
		}
		function checkOper(type,id){
			for (var i = 0; i < infoNodes.length; i++) {
				if (infoNodes[i].attrCode != null && infoNodes[i].pId == id) {
					var treeObj = $.fn.zTree.getZTreeObj("infoListTree");
					var treeNode = treeObj.getNodeByParam("id",infoNodes[i].id);
					
					var radio1 = document.getElementById("rid_1_"+infoNodes[i].attrCode);
					var radio2 = document.getElementById("rid_2_"+infoNodes[i].attrCode);
					if (type == 1) {//全选读
						if(radio1 != null) {
							radio1.checked = true;
							//设置权限等级[0：无权限；1：只读权限；2：可写权限]
							treeNode.oper = $("input[name='radio_"+treeNode.attrCode+"']:checked").val();
							//选择权限就说明角色可看到信息集，即默认选中树菜单前的多选框
							treeObj.checkNode(treeNode, true);
						} else {
							treeObj.checkNode(treeNode, false);
						}
						if(radio2 != null) {
							radio2.checked = false;
						}
						treeNode.oper = 0;
					} else if(type == 2) {//全选写
						if (radio1 != null) {
							radio1.checked = false;
						}
						if(radio2 != null) {
							radio2.checked = true;
							//设置权限等级[0：无权限；1：只读权限；2：可写权限]
							treeNode.oper = $("input[name='radio_"+treeNode.attrCode+"']:checked").val();
							//选择权限就说明角色可看到信息集，即默认选中树菜单前的多选框
							treeObj.checkNode(treeNode, true);
						} else {
							treeObj.checkNode(treeNode, false);
						}
						treeNode.oper = 1;
					}
				}
			}
		}
	</script>
  </body>
