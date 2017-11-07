<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../common.inc" %>

<body>
	<div class="zTreeDemoBackground">
		<ul id="menuSingleTree" class="ztree" style="width:90%;"></ul>
	</div>
	
	<script>
		var setting = {
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onDblClick: getMenu
			}
		};

		//获取数据
		var menuNodes = [];
		var data = JSON.parse('${menuList}');
       	for(var s in data){
       		menuNodes.push({'id':data[s].id,'pId':data[s].parentid,'name':data[s].title,'menutype':data[s].menutype});
       	}

		function getMenu(event, treeId, treeNode, clickFlag){
			//只能选择子节点，父节点不能选择
			//if(typeof(treeNode.children)!='undefined') return;
			$('#'+nameObj).val(treeNode.name);
			$('#'+idObj).val(treeNode.id);
			selTreeObject = treeNode;
			if(method!=null && typeof(method)!='undefined'){//回调方法
				var callbacks = $.Callbacks();
				callbacks.add(method);
				callbacks.fireWith( window, new Array(treeNode));
			}
			$('#common_window').dialog("close");
		}

		$.fn.zTree.init($("#menuSingleTree"), setting, menuNodes);
	</script>
</body>
