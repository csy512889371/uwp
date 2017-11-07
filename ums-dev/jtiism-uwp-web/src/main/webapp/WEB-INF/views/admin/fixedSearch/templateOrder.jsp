<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common.inc"%>
<!-- 模板排序页面 -->
<style>
	.group_div{width:250px;float: left;}
	.temp_div{position: relative;margin-left: 270px;}
	.order_div{margin:0 10px 0 10px;}
	.order_btn{padding-top: 5px;}
</style>
<body>
	<div class="order_div">
		<div class="group_div">
			<div class="oper_menu">
				<button class="btn" onclick="groupMoveUp()"><i class="icon-arrow-up"></i> 上移</button>
				<button class="btn" onclick="groupMoveDown()"><i class="icon-arrow-down"></i> 下移</button>
				<button class="btn" onclick="saveGroupSort()"><i class="icon-print"></i> 保存排序</button>
			</div>
			<table id="groupList" class="mmg">
				<tr>
				  <th rowspan="" colspan=""></th>
				</tr>
			  </table>
		</div>
		
		<div class="temp_div">
			<div class="oper_menu order_btn">
				<button class="btn" onclick="tempMoveUp()"><i class="icon-arrow-up"></i> 上移</button>
				<button class="btn" onclick="tempMoveDown()"><i class="icon-arrow-down"></i> 下移</button>
				<button class="btn" onclick="saveTempSort()"><i class="icon-print"></i> 保存排序</button>
			</div>
			<table id="tempList" class="mmg">
				<tr>
				  <th rowspan="" colspan=""></th>
				</tr>
			  </table>
		</div>
	</div>
	<script>
		var groupList = null,
			tempList  = null,
			groupId	  = null;
	
		var groupCols=[
				   		  { title: '分组名称', name: 'groupName', width: 234, align: 'center', sortable: false, type: 'text' },
				   		  { title: 'id', name: 'id', width: 100, align: 'center', sortable: false, type: 'text', hidden: true }
			          ];
		var tempCols =[
				   		  { title: '模板名称', name: 'templateName', width: 280, align: 'center', sortable: false, type: 'text' },
				   		  { title: 'id', name: 'queryTemplateId', width: 100, align: 'center', sortable: false, type: 'text', hidden: true },
				   		  { title: '排序', name: 'inpfrq', width: 100, align: 'center', sortable: false, type: 'text', hidden: true }
			          ]; 
		
		$(function(){
			groupList=$('#groupList').mmGrid({
				height: commonTalbeHeight
		        , cols: groupCols
		        , url: '<%=basePath%>admin/fixedSearch/findGroupList.do'
		        , method: 'get'
		        , checkCol: false
		        , autoLoad: false
			});
			groupList.on('cellSelected', function(e, item, rowIndex, colIndex) {
				loadTemplate(item.id);
			}).load();
		});
		
		/**
		* 加载模板列表
		* @param groupId 分组ID
		*/
		function loadTemplate(groupId){
			tempList=$('#tempList').mmGrid({
				height: commonTalbeHeight
		        , cols: tempCols
		        , items : []
		        , method: 'get'
		        , checkCol: false
		        , autoLoad: false
			}); 
			
			$.ajax({
				type : 'post',
				url	 : '<%=basePath%>admin/fixedSearch/findTemplateList.do',
				data : {groupId:groupId},
				success : function(data){
					var dt = [];
					for(var i in data){
						dt.push({templateName:data[i].templateName,queryTemplateId:data[i].queryTemplateId,inpfrq:data[i].inpfrq});
					}					
					tempList.load(dt);
				}
			});
		}

		/**
		* 分组上移
		*/
		function groupMoveUp(){
			resetOrder(groupList,'up');	
		}
		
		/**
		* 分组下移
		*/
		function groupMoveDown(){
			resetOrder(groupList,'down');
		}
		
		/**
		* 保存分组排序
		*/
		function saveGroupSort(){
			var rows = groupList.rows();
			var ids = '';
			if(rows==0) return;
			for(var i in rows){
				if(ids==''){
					ids += rows[i].id;
				}else{
					ids += ',' + rows[i].id;
				}
			}
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>admin/fixedSearch/saveGroupOrder.do',
				data : {ids : ids},
				success: function(data){
					openAlert(data.content);
    				if(data.type == "success"){
    		    		loadTemplateTree();
    		    	}
				}
			});
		}
		
		/**
		* 模板上移
		*/
		function tempMoveUp(){
			resetOrder(tempList,'up');	
		}
		
		/**
		* 模板下移
		*/
		function tempMoveDown(){
			resetOrder(tempList,'down');
		}
		
		/**
		* 保存模板排序
		*/
		function saveTempSort(){
			var rows = tempList.rows();
			var ids = '';
			if(rows==0) return;
			for(var i in rows){
				if(ids==''){
					ids += rows[i].queryTemplateId;
				}else{
					ids += ',' + rows[i].queryTemplateId;
				}
			}
			$.ajax({
				type : 'post',
				url  : '<%=basePath%>admin/fixedSearch/saveTemplateOrder.do',
				data : {ids : ids},
				success: function(data){
					openAlert(data.content);
    				if(data.type == "success"){
    		    		loadTemplateTree();
    		    	}
				}
			});
		}
	</script>
</body>