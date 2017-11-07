<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<style>
</style>
<body>
	<div>
		<div id="groupListDiv" class="oper_menu">
			<button class="btn-style4" onclick="addGroup()">
				<i class="icon-plus"></i> 新增分组
			</button>
			<button class="btn-style4" onclick="updateGroup()">
				<i class="icon-pencil"></i> 编辑分组
			</button>
			<button class="btn-style4" onclick="deleteGroup()">
				<i class="icon-trash"></i> 删除分组
			</button>
			<button class="btn-style4" type="button" name="mup" onclick="UpSequence()">
			<i class="icon-arrow-up"></i>上移</button>
		    <button class="btn-style4" type="button"  name="mdown"	onclick="DownSequence()">
		    <i class="icon-arrow-down"></i> 下移</button>
		    <button class="btn-style1" type="button" onclick="savePersonSort()">
		    	保存排序</button>
		</div>
		<!-- 用户列表 -->
		<div>
			<table id="groupList" class="mmg">
				<tr>
					<th rowspan="" colspan=""></th>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">

/**
* 窗口自适应
*/
function adjustWindow(){
	if(groupList!=null) groupList.resize($(window).height()-200);
}
var cols = [
            { title:'用户组名', name:'groupName' ,width:100, align:'left', sortable: true, type: 'text', renderer: function(val){//rowIndex从0开始
                return val;
            }},
            { title:'上级用户组名', name:'parGroupName' ,width:100, align:'left', sortable: false, type: 'text', renderer: function(val,item){
            	console.log(item);
                return item.parentGroup.groupName;
            }},
            { title:'用户组描述', name:'groupDesc' ,width:100, align:'left', sortable: false, type: 'text', renderer: function(val){
                return val;
            }},
            { title:'id', name:'id' ,width:300, align:'center', sortable: false, type: 'text', hidden: true},
            { title:'seqno', name:'seqno' ,width:300, align:'center', sortable: false, type: 'text', hidden: true}
        ];

var groupList;

$(document).ready(function(){
	loadGroupList();
});
function loadGroupList(){
	groupList = $('#groupList').mmGrid({
           height: commonTalbeHeight+20,
            cols: cols,
           url: '<%=basePath%>sys/user/groupList.do',
				method : 'get',
				remoteSort : true,
				sortName : 'seqno',
				sortStatus : 'asc',
				root : 'result',
				multiSelect : false,
				checkCol : true,
				fullWidthRows : true,
				autoLoad : false,
				showBackboard : false,
				plugins : [],
				params : function() {
					//如果这里有验证，在验证失败时返回false则不执行AJAX查询。
					return {
						secucode : $('#secucode').val()
					};
				}
			});

			groupList.on('cellSelected', function(e, item, rowIndex, colIndex) {
				//查看
				if ($(e.target).is('.btn-info, .btnPrice')) {
					e.stopPropagation(); //阻止事件冒泡
					alert(JSON.stringify(item));
				}
			}).load({});
	}
		
		
		function deleteGroup(){
			 var rows = groupList.selectedRows();
			 var item = rows[0];
			 if(typeof(item) == 'undefined'){
				 openAlert('请选择一条记录！');
				 return;
			 }
			 openConfirm('确定删除该分组吗？',function(){
				 $.ajax({
						type:'post',
						url:'<%=basePath%>sys/user/deleteGroup.do',
						data:{id:item.id},
						success:function(result){
							openAlert(result.content);
							if(result.type=="success"){
								loadGroupList();
							}
						}
					});
			 });
		}
		
		function addGroup(){
			openWindow('新增分组','sys/user/groupAdd.do',1,400,240);
		}
		
		function updateGroup(){
			 var rows = groupList.selectedRows();
			 var item = rows[0];
			 if(typeof(item) == 'undefined'){
				 openAlert('请选择一条记录！');
				 return;
			 }
			openWindow('编辑分组','sys/user/groupUpdate.do',1,400,240,{groupId:item.id});
		}
		
		 function UpSequence() {
			 resetOrder(groupList,'up');

        }
        function DownSequence() {
        	 resetOrder(groupList,'down');
        } 
        
        function savePersonSort(){
			var rows = groupList.rows();
			if(typeof(rows[0]) == "undefined" || rows[0] == null){
				openAlert("数据无需排序！");
				return;
			}
			openConfirm('你确定要保存排序吗?',function(){
				var groupIds = "";//人员id
				for (var i = 0; i < rows.length; i++) {
					groupIds += rows[i].id + ",";
				}
				groupIds = groupIds.substring(0, groupIds.length-1);
				
				$.ajax({
					type :'post',
					url  :'<%=basePath%>sys/user/saveGroupSort.do',
					dataType: 'json',
					data : {groupIds:groupIds},
					success: function(data){
						openAlert(data.content);
					}
				});
			});
        	
		}
		
	</script>
</body>
