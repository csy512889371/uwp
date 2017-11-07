<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<!-- 信息集管理页面 -->
<style>
	#switchBar{height:100%;cursor: pointer;float: left;width:13px;background-color: #fff;}
    #switchBar:hover{background-color: #f7f7f7;}
    #switchHref{top: 50%;position: absolute;}
    #searchItems{float:left;width:100%;border: none;}
    #basicTreeDiv{width:170px;float: left;overflow: auto;border-right: 1px solid #b8c0cc;}
    #infoSetTable{margin-left: 185px; position: relative;}
</style>
<body>
	<div id="searchItems">
		<div id="basicTreeDiv">
				<div  style="overflow: auto;" class="cadre_search">
					<ul id="basicTree" class="ztree"></ul>
				</div>
		</div>
		<div id="switchBar" onclick="switchShow()">
			<i id="switchHref" class="icon-chevron-left"></i>&nbsp;&nbsp;&nbsp;
		</div>
		<div id="infoSetTable">
			<div class="oper_menu">
			<button class="btn" onclick="infoSetMoveUp()">
				<i class="icon-arrow-up"></i> 上移
			</button>
			<button class="btn" onclick="infoSetMoveDown()">
				<i class="icon-arrow-down"></i> 下移
			</button>
			<button class="btn" onclick="saveInfoSetSort()">
				<i class="icon-print"></i> 保存排序
			</button>
			<button class="btn-style4" onclick="addDynaInfoSet()"><i class="icon-plus"></i>新增</button>
			<button class="btn-style4" onclick="updateDynaInfoSet()"><i class="icon-pencil"></i>编辑</button>
	<!-- 	<button class="btn-style4" onclick="deleteDynaInfoSet()"><i class="icon-trash"></i>删除</button> -->	
			</div>
			<div  style="width:100%;">
				<table id="infoSetList" class="mmg">
					<tr>
						<th rowspan="" colspan=""></th>
					</tr>
				</table>
			</div>
			
		</div>
			
	</div>
	
<script type="text/javascript">
var showLeft = true;
var currentGroupId="";
$('#searchItems').height($(window).height() - 170);
$('#infoSetTable').height($(window).height() - 125);
$('#basicTreeDiv').height($(window).height() - 115);
$('#switchBar').height($(window).height()-115);

function switchShow(){
	if(showLeft){
		showLeft = false;
		$('#basicTreeDiv').hide();
		$('#infoSetTable').css('margin-left','13px');
		$('#switchHref').removeClass('icon-chevron-left');
		$('#switchHref').addClass('icon-chevron-right');
	}else{
		showLeft = true;
		$('#basicTreeDiv').show();
		$('#infoSetTable').css('margin-left','185px');
		$('#switchHref').removeClass('icon-chevron-right');
		$('#switchHref').addClass('icon-chevron-left');
	}
}
var infoSetCols=[
{ title:'名称', name:'attrName' ,width:260, align:'left', sortable: false, type: 'text'},
{ title:'状态', name:'status' ,width:210, align:'left', sortable: false, type: 'text',renderer : function (val, item) {
   if(val==true){
	   return "启用";
   }else{
	   return "禁用";
   }
}},
{ title:'表类型', name:'isBasic' ,width:160, align:'left', sortable: false, type: 'text',renderer : function (val, item) {
	   if(val==true){
		   return "基础表";
	   }else{
		   return "动态表";
	   }
	}},
	{ title:'用于批处理', name:'isBatch' ,width:160, align:'left', sortable: false, type: 'text',renderer : function (val, item) {
		   if(val==true){
			   return "是";
		   }else{
			   return "否";
		   }
		}},
		{ title:'是否常用', name:'isCommon' ,width:160, align:'left', sortable: false, type: 'text',renderer : function (val, item) {
			   if(val==true){
				   return "是";
			   }else{
				   return "否";
			   }
			}},
			{ title:'URL', name:'url' ,width:300, align:'left', sortable: false, type: 'text',hidden: true},
{ title:'attrCode', name:'attrCode' ,width:150, align:'left', sortable: false, type: 'text',hidden: true},
 {title:'seq', name:'seq' ,width:150, align:'left', sortable: true, type: 'text',hidden: true},
{ title:'id', name:'id' ,width:150, align:'left', sortable: false, type: 'text',hidden: true},
{ title:'parentId', name:'parentId' ,width:150, align:'left', sortable: false, type: 'text',hidden: true}
                 ];



var infoSetList=null;
var zTree_Menu;
var codeAttrs=JSON.parse('${codeAttrs}');
$('document').ready(function() {
	var cadreAttrSetting = {
			view: {
				enable : false,
				nocheckInherit : true
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick : changeInfoSets
			}
		};
	
	var basicNodes =[];
	for(var i in codeAttrs){
		basicNodes.push({id:codeAttrs[i].id,pId:0,name:codeAttrs[i].attrName});
	}
	var treeObj = $("#basicTree");
	$.fn.zTree.init(treeObj, cadreAttrSetting, basicNodes);
	zTree_Menu = $.fn.zTree.getZTreeObj("basicTree");
	currentGroupId=codeAttrs[0].id;
	loadInfoSetList(codeAttrs[0].id);
});

function loadInfoSetList(groupId){
	infoSetList = $('#infoSetList').mmGrid({
			height : commonTalbeHeight+20,
			cols : infoSetCols,
			url : '<%=basePath%>sys/infoSetManage/searchInfoSetList.do',
			method : 'post',
			remoteSort : true,
			sortName : 'SECUCODE',
			sortStatus : 'asc',
			multiSelect : false,
			checkCol : true,
			fullWidthRows : true,
			autoLoad : false,
			showBackboard : false,
			plugins : [$('#pg').mmPaginator({})],
			params : function() {
				//如果这里有验证，在验证失败时返回false则不执行AJAX查询。
		        return {
					secucode: $('#secucode').val()
		        };
		    }
		});
		
	infoSetList.on('cellSelected', function(e, item, rowIndex, colIndex){
			//查看
		    if ($(e.target).is('.btn-info, .btnPrice')) {
		    	e.stopPropagation();  //阻止事件冒泡
		        alert(JSON.stringify(item));
		    }
		}).load({groupId:groupId});
}

function changeInfoSets(event, treeId, treeNode){
	currentGroupId=treeNode.id;
	infoSetList.load({groupId:currentGroupId});
}

function infoSetMoveUp(){
	 resetOrder(infoSetList,'up');
}

function infoSetMoveDown(){
	resetOrder(infoSetList,'down');
}

function saveInfoSetSort(){
	var infoSetListObj = infoSetList.rows();
	if(typeof(infoSetListObj[0]) == "undefined" || infoSetListObj[0] == null){
		openAlert("无需排序！");
		return;
	}
	openConfirm('你确定要保存排序吗?',function(){
		var groupId=infoSetListObj[0].parentId;
		var infoSetIds = "";
		for (var i = 0; i < infoSetListObj.length; i++) {
			infoSetIds += infoSetListObj[i].id + ",";
		}
		infoSetIds = infoSetIds.substring(0, infoSetIds.length-1);
		
		$.ajax({
			type :'post',
			url  :'<%=basePath%>sys/infoSetManage/infoSetSortSave.do',
			dataType: 'json',
			data : {groupId : groupId,infoSetIds : infoSetIds},
			success: function(data){
				openAlert(data.content);
				if(data.type == 'success'){
				}
			}
		});
	});
}

function addDynaInfoSet(){
	openWindow('新增动态信息集','sys/infoSetManage/addDynaInfoSet.do',1,410,500,null);
}

function updateDynaInfoSet(){
	var rows = infoSetList.selectedRows();
	 var item = rows[0];
	 if(typeof(item) == 'undefined'){
		 openAlert('请选择一条记录！');
		 return;
	 }
	 openWindow('编辑信息集','sys/infoSetManage/updataDynaInfoSetPage.do',1,410,500,{infoSetId:item.id});
}

function deleteDynaInfoSet(){
	var rows = infoSetList.selectedRows();
	 var item = rows[0];
	 if(typeof(item) == 'undefined'){
		 openAlert('请选择一条记录！');
		 return;
	 }
	 if(item.isBasic==true){
		 openAlert('基础信息集不允许删除！');
		 return;
	 }
	 openConfirm('确定删除改动态信息集么？',function(){
	     $.ajax({
		     type: 'post',
			 url: '<%=basePath%>sys/infoSetManage/deleteDynaInfoSet.do',
			 data: {id:item.id},
			 success: function(data){
			     openAlert(data.content);
				 if(data.type == 'success'){
					 infoSetList.load({groupId:currentGroupId});
				 }
			 }
		 });				
	 });
}
</script>
</body>