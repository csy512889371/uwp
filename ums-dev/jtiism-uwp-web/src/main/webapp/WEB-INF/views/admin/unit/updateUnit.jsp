<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<body>
	<div>
	     <input type="hidden" value="${b01Hiber.id}" id="b01HiberId"/>
		 <%--<div id="basicTreeDiv" style="width:210px;float: left;overflow: auto;">
				<ul id="basicTree" class="ztree ztree1"></ul>
		 </div> --%>
		 <div style="position: relative;margin-left: 20px;"><%--220px--%>
			<div style="overflow-y: auto; overflow-x: auto;" id="cadreInfoDetail">
			</div>
		</div>
	</div>
<script type="text/javascript">
//$('#cadreInfoDetail').height(250);

var b01HiberId='${b01Hiber.id}';
$(document).ready(function(){
	//loadTreeInfo();
	$('#cadreInfoDetail').load('<%=basePath%>unit/unit/editUnitBaiscPage.do', {'b01HiberId': b01HiberId});
});
var cadreTree;
function loadTreeInfo(){
	var basicNodes =[
	                 {id:1,pId:0,name:'基本单位信息集'}];
	
	var cadreAddSetting = {
			 check: {
 				enable: false,
 				nocheckInherit: true
 			},
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
				onClick : zTreeOnclick2
			}
		};
	cadreTree=$.fn.zTree.init($("#basicTree"), cadreAddSetting, basicNodes);
}
var selectNode=null;
function zTreeOnclick2(event, treeId, treeNode){
	selectNode=treeNode;
	$('#cadreInfoDetail').load('<%=basePath%>unit/unit/editUnitBaiscPage.do', {'b01HiberId': b01HiberId});
}


</script>
</body>

