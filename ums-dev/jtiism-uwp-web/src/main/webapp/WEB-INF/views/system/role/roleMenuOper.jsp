<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
  <style>
  	 #menuMultyDiv{width:55%;float: left;overflow: auto;height: 590px;}
  	 #handlerDiv{width: 90%;margin-left: 30px;padding-left: 30px;}
  	 #menuMultyTree{width:90%;margin-top: 60px;}
  	 #menuMultyDiv label, #handlerDiv label{font-weight: bolder;}
  </style>
  <body>
    <div id="menuMultyDiv">
        <div style="position: absolute;width:280px;background-color: #fff;z-index: 9999;padding-bottom: 10px;">
        	<label>菜单列表</label>
        	<button class="btn-style4" onclick="expandAll()">展开全部</button>
        	<button class="btn-style4" onclick="closeAll()">闭合全部</button>&nbsp;&nbsp;&nbsp;&nbsp;
        	<button class="btn-style1" onclick="saveRoleMenu()">保 存</button>	
        </div>
        <!-- <div style="text-align: left;margin-left: 5px;">
        	
        </div> -->
		<ul id="menuMultyTree" class="ztree"></ul>
	</div>
	<div id="handlerDiv">
	  <div style="margin: auto;"><label>操作列表</label></div>
	  <div id="operDiv"></div>
	</div>
	
	<script>
		//$('#menuMultyDiv').height($(window).height() - 150);
		var roleMenuSetting = {
			check: {
				enable: true,
				nocheckInherit: true
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback:{
				onClick : this.onClick
			}
		};
		
		var menuNodes = [];
		
		function getMenuData(){
			//获取数据
			var data = JSON.parse('${menuList}');
	       	for(var s in data){
	       		if(data[s].checked=="true"){//已勾选项显示勾选
	       			menuNodes.push({'id':data[s].id,'pId':data[s].parentid,'name':data[s].title,'checked':true,'open':true});
	       		}else{
	       			menuNodes.push({'id':data[s].id,'pId':data[s].parentid,'name':data[s].title,'open':true});
	       		}
	       	}
		}
		
		function showMenuTree(){
			getMenuData();
			$.fn.zTree.init($("#menuMultyTree"), roleMenuSetting, menuNodes);
		}
		
		showMenuTree();
		
		 function onClick(e, treeId, node) {
	         var menuId = node.id;
	         var menuCheck = node.checked;
	         var url = '<%=basePath%>sys/role/getMenuOper.do?roleId='+${roleId}+'&menuId='+menuId +'&menuCheck='+menuCheck;
		     $('#operDiv').load(url);
	     }
		 
		 function saveRoleMenu(){
			var treeObj = $.fn.zTree.getZTreeObj("menuMultyTree");
     		var nodes = treeObj.getCheckedNodes(true);
     		var menuIds = "";
     		for (var i = 0; i < nodes.length; i++) {
					menuIds = menuIds + nodes[i].id + ",";
				}
     		
     		if(menuIds != ""){
     			menuIds = menuIds.substring(0,menuIds.length-1);
     		}
     		$.ajax({
         		type : 'post',
         		url  : '<%=basePath%>commonTree/grantRoleMenu.do',
         		data : {roleId:'${roleId}',menuIds:menuIds},
         		success: function(result){
         			openAlert(result.content);
         		}
         	});
     		$('#operDiv').html('');
     		
     	}
		 
		 function expandAll(){
			 var treeObj = $.fn.zTree.getZTreeObj("menuMultyTree");
			 treeObj.expandAll(true);
		 }
		 
		 function closeAll(){
			 var treeObj = $.fn.zTree.getZTreeObj("menuMultyTree");
			 treeObj.expandAll(false);
		 }
	</script>
  </body>
