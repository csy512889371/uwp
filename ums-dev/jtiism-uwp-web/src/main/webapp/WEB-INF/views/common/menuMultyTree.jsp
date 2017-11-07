<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../common.inc" %>
<body>
	<div id="menuMultyDiv">
		<ul id="menuMultyTree" class="ztree" style="width:90%;"></ul>
	</div>
	<script>
	
		var roleMenuSetting = {
			check: {
				enable: true,
				nocheckInherit: true
			},
			data: {
				simpleData: {
					enable: true
				}
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
			//$('#menuMultyDiv').html('<ul id="menuMultyTree" class="ztree" style="width:90%;"></ul>');
			getMenuData();
			$.fn.zTree.init($("#menuMultyTree"), roleMenuSetting, menuNodes);
		}
		
		showMenuTree();
	</script>
</body>