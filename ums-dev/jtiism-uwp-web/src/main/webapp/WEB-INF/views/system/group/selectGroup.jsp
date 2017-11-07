<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc" %>
<body>
	<div>
		<!-- 库及机构树 -->
		<div style="margin-left: 15px;">
			<!-- 机构树 -->
		    <div id="selectCoderTreeDiv" style="height: 260px;overflow: auto;">
			    <div id="selectTreeTab" style="overflow: auto;">
					<ul id="selectTree" class="ztree" style="width: 95%;"></ul>
				</div>
			</div>
		</div>
		<div style="border-top: 1px solid #ddd;">
			<button class="btn-style1" style="margin-top: 5px;" onclick="okSelectDept()">确定</button>
			<button class="btn-style4" style="margin-top: 5px;" onclick="noSelectDept()">取消</button>
		</div>
	</div>
	<script type="text/javascript">
	var inputCodeId='${inputCodeId}';
	var inputShowId='${inputShowId}';
	var filterIds='${filterIds}';
	var rootSelected='${rootSelected}';
	
		$(document).ready(function(){	
			loadSelectCode();
		});
		/**加载单位信息**/
		function loadSelectCode(){	
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>sys/user/getGroupList.do',
				data :{},
				async:false,
				success: function(data){
					loadSelectCoderTree(data);
				}
			});
		}
		
		/**树形展示**/
		function loadSelectCoderTree(data){
			var dictNodes = [];
		    for(var i in data){
		    	if(filterIds!='' && data[i].id==filterIds){
		    		dictNodes.push({'id':data[i].id, 'pId':data[i].pId, 'name':data[i].groupName,isHidden:true});
		    	}else{
		    		dictNodes.push({'id':data[i].id, 'pId':data[i].pId, 'name':data[i].groupName});
		    	}
		    	
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
	    		callback: {
	    			onDblClick: dblClickSelectGroup
				}
	    	};
			$.fn.zTree.init($("#selectTree"), coderTreeSetting, dictNodes);
			var treeObj = $.fn.zTree.getZTreeObj("selectTree");
			treeObj.expandAll(true);
		}
		
		function dblClickSelectGroup(){
			okSelectDept();
		}
		/**
		 * 确定
		 */
		function okSelectDept(){
			var treeObj = $.fn.zTree.getZTreeObj("selectTree");
			var node = treeObj.getSelectedNodes();
			if(node.length == 0){
				$("#"+inputCodeId).val("");
				$("#"+inputShowId).val("");
			} else {
				if(rootSelected=='false' && node[0].id=='0'){
					openAlert('根节点不能选择！');
					return ;
				}
				$("#"+inputCodeId).val(node[0].id);
				$("#"+inputShowId).val(node[0].name);
			}
			noSelectDept();
		}
		
		/**
		 * 取消
		 */
		function noSelectDept(){
			closeWindow(2);
		}
		
	</script>
</body>