<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
  
  <body>
    <div>
      <c:if test="${operListNull == true}">
       ${message}
      </c:if>
      <c:if test="${operListNull == false}">
       <button class="btn-style4" onclick="selectAll()">全选</button>
       <button class="btn-style4" onclick="disSelectAll()">全不选</button>&nbsp;&nbsp;&nbsp;&nbsp;
       <button class="btn-style1" onclick="saveRoleMenuOper()">保 存</button>
      </c:if>
      <ul id="roleMenuOperTree" class="ztree" style="width:90%;"></ul>
    </div>
    <script type="text/javascript">
    var menuOperSetting = {
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
		
		var menuOperNodes = [];
		
		function getMenuOperData(){
			//获取数据
			var data = JSON.parse('${operList}');
	       	for(var s in data){
	       		if(data[s].checked==true){//已勾选项显示勾选
	       			menuOperNodes.push({'id':data[s].id,'pId':0,'name':data[s].opername,'checked':true,'open':true});
	       		}else{
	       			menuOperNodes.push({'id':data[s].id,'pId':0,'name':data[s].opername,'open':true});
	       		}
	       	}
		}
		
		function showMenuOperTree(){
			getMenuOperData();
			$.fn.zTree.init($("#roleMenuOperTree"), menuOperSetting, menuOperNodes);
		}
		
		showMenuOperTree();
    function saveRoleMenuOper(){
    	var treeObj = $.fn.zTree.getZTreeObj("roleMenuOperTree");
		var nodes = treeObj.getCheckedNodes(true);
		var checkOperId = "";
		for (var i = 0; i < nodes.length; i++) {
			checkOperId = checkOperId + nodes[i].id + ",";
		}
		if('${menuCheck}'=='false'){
			openAlert('该角色尚未分配此菜单，无法为其分配操作！');
			return;
		}
		if(checkOperId != ""){
			checkOperId = checkOperId.substring(0, checkOperId.length-1);
		}
   		$.ajax({
			type : 'post',
			url  : '<%=basePath%>sys/role/grantMenuOper.do',
			data : {roleId :'${roleId}',menuId : '${menuId}',checkOperIds :checkOperId},
			success: function(result){
				openAlert(result.content);
				if(result.success==true){
					
				}
			}
		});
	
    		
	}
    
    function selectAll(){
    	var treeObj = $.fn.zTree.getZTreeObj("roleMenuOperTree");
    	treeObj.checkAllNodes(true);
    }
    
    function disSelectAll(){
    	var treeObj = $.fn.zTree.getZTreeObj("roleMenuOperTree");
    	treeObj.checkAllNodes(false);
    }
    </script>
  </body>
