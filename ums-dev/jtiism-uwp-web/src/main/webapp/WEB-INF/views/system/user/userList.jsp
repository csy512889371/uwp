<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<style>
#userListDiv .input1 {
	margin-bottom: 0px;
	width: 150px;
	padding-left: 6px;
}

.divLeft {
	float: left;
	width: 32%;
	background-color: #f5f5f5;
}

.divRight {
	float: right;
	width: 68%;
	background-color: #f5f5f5;
}

	#groupTreeDiv{float: left;width: 260px;border-right:1px solid #b8c0cc;}
	#groupTreeDiv{text-align: left;}
	#groupTreeDiv table{margin-top:5px;}
	#groupTreeDiv.td1{width:15%;}
</style>
<body>
	<div id="groupTreeDiv">
		<div id="cadreShowTab" class="border-right" style="margin-top: 0px;">
			<div id="groupTreeTab" style="overflow: auto;">
				<ul id="groupTree" class="ztree" style="width: 95%;"></ul>
			</div>
		</div>
	</div>
	
	<div style="margin-left: 273px; position: relative; padding-top: 0px;">
		<div id="userListDiv" class="oper_menu">
			<auth:auth ifAllGranted="user:add">
				<button class="btn-style4" onclick="addUser()">
					<i class="icon-plus"></i> 新增用户
				</button>
			</auth:auth>
			<auth:auth ifAllGranted="user:edit">
				<button class="btn-style4" onclick="updateUser()">
					<i class="icon-pencil"></i> 编辑
				</button>
			</auth:auth>
			<auth:auth ifAllGranted="user:delete">
				<button class="btn-style4" onclick="deleteUser()">
					<i class="icon-trash"></i> 删除
				</button>
			</auth:auth>
			<auth:auth ifAllGranted="user:setRole">
				<button class="btn-style4" onclick="grantRole()">
					<i class="icon-edit"></i> 配置角色
				</button>
			</auth:auth>

			<auth:auth ifAllGranted="user:reset">
				<button class="btn-style4" onclick="resetPassword()">
					<i class="icon-refresh"></i> 重置密码
				</button>
			</auth:auth>
			&nbsp;&nbsp;&nbsp;
			<auth:auth ifAllGranted="user:search">
				<input type="text" id="userName" name="userName" placeholder="请输入姓名"
					class="input1">
				<button class="btn-style1" onclick="searchByName()">查询</button>
				<button class="btn-style2" id="clear" onclick="clearSer();">重置</button>
			</auth:auth>
		</div>
		<!-- 用户列表 -->
		<div>
			<table id="userList" class="mmg">
				<tr>
					<th rowspan="" colspan=""></th>
				</tr>
			</table>
			<div id="pg" style="text-align: right;"></div>
		</div>
		<div id="role_user" title="选择">
			<div id="role_user_content"></div>
		</div>
		<div id="offices_user" title="选择">
			<div id="offices_user_content"></div>
		</div>
	</div>
	<script type="text/javascript">
$('#groupTreeDiv').height($(window).height()-115);
/**
* 窗口自适应
*/
function adjustWindow(){
	$('#groupTreeDiv').height($(window).height()-115);
//	if(userList!=null) userList.resize($(window).height()-200);
}
var selectedId = null;
//表头
var cols = [
    { title:'帐号', name:'username' ,width:100, align:'left', sortable: true, type: 'text', renderer: function(val){//rowIndex从0开始
        return val;
    }},
    { title:'状态', name:'state' ,width:100, align:'left', sortable: true, type: 'text', renderer: function(val){
        if(val==true){
        	return "有效";
        }else{
        	return "无效";
        };
    }},
    { title:'所属权限', name:'deptName' ,width:100, align:'left', sortable: false, type: 'text', renderer: function(val){
        return val;
    }},
    { title:'角色', name:'rolesName' ,width:100, align:'left', sortable: false, type: 'text', renderer: function(val){
        return val;
    }},
    { title:'备注', name:'remark' ,width:100, align:'left', sortable: false, type: 'text', renderer: function(val){
        return val;
    }},
    { title:'id', name:'id' ,width:300, align:'center', sortable: false, type: 'text', hidden: true
    }
];
var userList;
var groupTree;
var type="group";
var groupId;
function searchList(page){
	if(!page) page = 1;
	userList.load({userName:$('#userName').val(),groupId:groupId,type:type,pageNumber:page});
}

function loadGroup(){
	$.ajax({
		type : 'post',
		url : '<%=basePath%>sys/user/getGroupList.do',
		async:false,
		success: function(data){
			loadGroupTree(data);
		}
	});
}
function loadGroupTree(data){
	var dictNodes = [];
    for(var i in data){
    	dictNodes.push({'id':data[i].id, 'pId':data[i].pId, 'name':data[i].groupName});
    }
    var groupTreeSetting = {
    		check: {
    			enable: false,
    			nocheckInherit: true
    		},
    		data: {
    			simpleData: {
    				enable: true
    			}
    		},
    		callback : {
				onClick: zTreeOnclick,
				/* onCheck: zTreeOncheck */
			},
			/* view: {
				fontCss: getFontCss
			} */
    	};
    groupTree = $.fn.zTree.init($("#groupTree"), groupTreeSetting, dictNodes);
	groupTree.expandAll(true);
}
function zTreeOnclick(event, treeId, treeNode){
	if(treeNode.id!='0'){
		type="group";
		groupId=treeNode.id;
		searchList(1);
	}
}
$(document).ready(function(){
	loadGroup();
	loadUserList();
});

function loadUserList(){
	userList = $('#userList').mmGrid({
           height: commonTalbeHeight-20
           , cols: cols
           , url: '<%=basePath%>sys/user/userList.do'
           , method: 'get'
           , remoteSort:true
           , sortName: 'SECUCODE'
           , sortStatus: 'asc'
           , root: 'resultMapList'
           , multiSelect: false
           , checkCol: true
           , fullWidthRows: true
           , autoLoad: false
           , showBackboard : false
           , plugins: [
               $('#pg').mmPaginator({})
           ]
           , params: function(){
               //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
             return {
                 secucode: $('#secucode').val()
             };
           }
       });


       userList.on('cellSelected', function(e, item, rowIndex, colIndex){
       	   selectedId = item.id;
           //查看
           if($(e.target).is('.btn-info, .btnPrice')){
               e.stopPropagation();  //阻止事件冒泡
               alert(JSON.stringify(item));
           }
       }).load({page: 1,groupId:"",type:type});
}

/**
* 删除用户
*/
function deleteUser(){
	var item = checkSelected();
	if(item==null){
		openAlert('请选择一条记录！');
		return;
	}
	openConfirm('你确定要删除该行记录吗?',function(){
		$.ajax({
			   type : 'post',
			   url  : '<%=basePath%>sys/user/deleteUser.do',
			   data : {userId : item.id},
			   success: function(data){
				   openAlert(data.content);
				   if(data.type == 'success'){
					   //重新加载一次页面
					   $('#mainContent').load('<%=basePath%>sys/user/searchUser.do',{});
				   }
			   }
		   });	
	});
}

/**
* 修改用户
*/
function updateUser(){
	var item = checkSelected();
	if(item==null){
		openAlert('请选择一条记录！');
		return;
	}
	openWindow('修改用户信息','sys/user/userUpdate.do?userId='+item.id,1,500,390);
}

/**
* 检验是否选择数据
*/
function checkSelected(){
	var rows = userList.selectedRows();
	var item = rows[0];
	if(typeof(item)=='undefined'){
		return null;
	}
	return item;
}

/**
* 新增用户
*/
function addUser(){
	openWindow('新增用户','sys/user/userAdd.do',1,500,390);
}

/**
* 重置密码
*/
function resetPassword(){
	var item = checkSelected();
	if(item==null){
		openAlert('请选择一条记录！');
		return;
	}
	openConfirm('确认重置密码？',function(){
		$.ajax({
			type : 'post',
			url  : '<%=basePath%>sys/user/resetPassword.do',
			data : {userId : item.id},
			success: function(result){
				openAlert(result.content);
				if(result.success==true){
					//重新加载一次页面
					$('#mainContent').load('<%=basePath%>sys/user/searchUser.do',{});
				}
			}
		});
	});
}

var userRoleIdArr = new Array();
var userRoleId = null;
function grantRole(userId){
	var item = checkSelected();
	if(item==null){
		openAlert('请选择一条记录！');
		return;
	}
	
	$('#role_user_content').load('<%=basePath%>sys/user/getRoleList.do',{'userId':item.id});
    $("#role_user").dialog({
        resizable: false,
        draggable: true,
        height: 500,
        width: 400,
        modal: true,
        title: '分配角色',
        buttons: {
        	"保存": function () {
        		var userRoleIdObj = userRoleList.selectedRows();
        		/*
        		if(userRoleIdObj.length < 1){
        			openAlert('请选择角色！');
        			return;
        		}
        		*/
        		var userRoleIdArr = "";
        		for (var int = 0; int < userRoleIdObj.length; int++) {
        			userRoleIdArr += userRoleIdObj[int].ID + ",";
				}
        		if(userRoleIdArr.length>0){
        			userRoleIdArr = userRoleIdArr.substring(0, userRoleIdArr.length-1);
        		}
        		$.ajax({
        			type : 'post',
        			url  : '<%=basePath%>sys/user/grantRole.do',
        			data : {userId:item.id, roleIdStr:userRoleIdArr},
        			success: function(result){
        				userRoleId = null;
        				openAlert(result.content);
        				userList.load({page:1});
        			}
        		});
        		$(this).dialog("close");
        	}
        }
    });
}
/**
* 清除查询条件
*/
function clearSer(){
	$('#userName').val('');
	// type="byName";
	//searchList();
}

function searchByName(){
	type='byName';
	searchList();
}
function grantOffices(){
	var item = checkSelected();
	if(item == null){
		openAlert('请选择一条记录！');
		return;
	}
	$('#offices_user_content').load('<%=basePath%>sys/user/getOfficesPage.do',{'userId':item.id});
	 $("#offices_user").dialog({
	        resizable: false,
	        draggable: true,
	        height: 500,
	        width: 400,
	        modal: true,
	        title: '分配所属权限',
	        buttons: {
	        	"保存": function () {
	        		var userDeptObj = userDeptList.selectedRows();
	        		var dept = userDeptObj[0];
	        		if(typeof(dept)=='undefined'){
	        			openAlert("请选择归属的所属权限");
	        			return;
	        		}
	        		$.ajax({
	        			type : 'post',
	        			url  : '<%=basePath%>sys/user/grantUserOfficesRela.do',
						data : {
							deptId : dept.id,
							userId : item.id
						},
						success : function(result) {
							openAlert(result.content);
						}
					});
					$(this).dialog("close");
				}
			}
		});
		}
	</script>
</body>
