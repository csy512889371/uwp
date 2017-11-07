<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<style>
	#roleListDiv .input1 {margin-bottom: 0px;width: 150px;padding-left: 6px;}
</style>  
  <body>
  <div>
    <div id="roleListDiv">
		<div class="oper_menu">
			<auth:auth ifAllGranted="role:add">
				<button class="btn-style4" onclick="addRole()"><i class="icon-plus"></i> 新增角色</button>
			</auth:auth>
			<auth:auth ifAllGranted="role:edit">
				<button class="btn-style4" onclick="updateRole()"><i class="icon-pencil"></i> 编辑</button>
			</auth:auth>
			<auth:auth ifAllGranted="role:delete">
				<button class="btn-style4" onclick="deleteRole()"><i class="icon-trash"></i> 删除</button>
			</auth:auth>
			<%--<button class="btn" onclick="grant()"><i class="icon-edit"></i> 授权</button> --%>
			<%-- <auth:auth ifAllGranted="role:unitGrant">
				<button class="btn-style4" onclick="roleUnitGrant()"><i class="icon-edit"></i> 单位机构授权</button>
			</auth:auth> --%>
			<auth:auth ifAllGranted="role:grantToUser">
				<button class="btn-style4" onclick="grantUser()"><i class="icon-edit"></i> 分配用户</button>
			</auth:auth>
			<auth:auth ifAllGranted="role:setGrant">
				<button class="btn-style4" onclick="dataPrivilege()"><i class="icon-edit"></i> 权限设置</button>
			</auth:auth>

			&nbsp;&nbsp;&nbsp;
			<auth:auth ifAllGranted="role:search">
					<input type="text" id="roleName" name="roleName" placeholder="请输入角色名" class="input1">
					<button class="btn-style1" onclick="searchList()">查询</button>
					<button class="btn-style2" id="clear" onclick="clearSer();">重置</button>
			</auth:auth>
		</div>
	</div>
	<!-- 角色列表 -->
   	<div>
	  <table id="roleList" class="mmg">
        <tr>
          <th rowspan="" colspan=""></th>
        </tr>
      </table>
      <div id="pg" style="text-align: right;"></div>
	</div>
  </div>
  
  <div id="role_menu" title="选择"><div id="role_menu_content"></div></div>
  <div id="role_users" title="选择"><div id="role_user_contents"></div></div>
<script type="text/javascript">
	/**
	* 窗口自适应
	*/
	function adjustWindow(){
		if(roleList!=null) roleList.resize($(window).height()-200);
	}
   var selectedId = null;
 //表头
   var cols = [
        { title:'帐号', name:'rolename' ,width:100, align:'left', sortable: false, type: 'text', renderer: function(val,item,rowIndex){//rowIndex从0开始
            //return (val / 100).toFixed(2);
            return val;
        }},
        { title:'状态', name:'state' ,width:100, align:'left', sortable: false, type: 'text', renderer: function(val){
            if(val==true){
	        	return "有效";
	        }else{
	        	return "无效";
	        };
        }},
        { title:'备注', name:'remark' ,width:100, align:'left', sortable: false, type: 'text', renderer: function(val){
            return val;
        }},
        { title:'id', name:'id' ,width:300, align:'center', sortable: false, type: 'text', hidden: true
        }
    ];

	function searchList(page){
		if(!page) page = 1;
		roleList.load({roleName:$('#roleName').val(),pageNumber:page});
	}
	
	var roleList;
	$(document).ready(function(){
		var roleName = $('#roleName').val();
		roleList = $('#roleList').mmGrid({
              height: commonTalbeHeight-20
              , cols: cols
              , url: '<%=basePath%>sys/role/roleList.do'
              , method: 'get'
              , remoteSort:true
              , sortName: 'SECUCODE'
              , sortStatus: 'asc'
              , root: 'result'
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


		roleList.on('cellSelected', function(e, item, rowIndex, colIndex){
          	selectedId = item.id;
              //查看
              if($(e.target).is('.btn-info, .btnPrice')){
                  e.stopPropagation();  //阻止事件冒泡
                  alert(JSON.stringify(item));
              }
          }).load({page: 1});
	});

	/**
	* 检验是否选择数据
	*/
	function checkSelected(){
		var rows = roleList.selectedRows();
		var item = rows[0];
		if(typeof(item)=='undefined'){
			return null;
		}
		return item;
	}

	/**
	* 修改角色
	*/
	function updateRole(){
		var item = checkSelected();
		if(item==null){
			openAlert('请选择一条记录！');
			return;
		}
		openWindow('修改角色信息','sys/role/roleUpdate.do?roleId='+item.id,1,500,350);
	}

	/**
	* 新增角色
	*/
	function addRole(){
		openWindow('新增角色','sys/role/roleAdd.do',1,500,350);
	}
	
	/**
	* 删除角色
	*/
	function deleteRole(){
		var item = checkSelected();
		if(item==null){
			openAlert('请选择一条记录！');
			return;
		}
		$.ajax({
			type:'post',
			url:'<%=basePath%>sys/role/findUsersByRoleId.do',
			data:{roleId : item.id},
			success:function(data){
				var alertName = '确定删除该角色？';
				if(data.type == 'success'){
					alertName = '该角色有用户，确定删除该角色？';		
				}
				openConfirm(alertName,function(){
					$.ajax({
						type : 'post',
						url  : '<%=basePath%>sys/role/deleteRole.do',
						data : {roleId : item.id},
						success: function(data){
							openAlert(data.content);
							if(data.type == 'success'){
								//重新加载一次页面
								$('#mainContent').load('<%=basePath%>sys/role/searchRole.do',{});
							}
						}
					});				
				});
			}
		});
		
		

	}
	
	/**
	* 角色授权(过时方法)
	*/
	function grant(){
		var item = checkSelected();
		if(item==null){
			openAlert('请选择一条记录！');
			return;
		}
		
		$('#role_menu_content').load('<%=basePath%>commonTree/getMultyMenuTree.do',{'roleId':item.id});
	    $("#role_menu").dialog({
	        resizable: false,
	        draggable: true,
	        height: 500,
	        width: 400,
	        modal: true,
	        title: '菜单授权'
	    });
	}
	
	/**
	 * 单位机构授权
	 */
	function roleUnitGrant(){
		var item = checkSelected();
		if(item==null){
			openAlert('请选择一条记录！');
			return;
		}
		var url = 'sys/role/gotoRoleUnitGrant.do?roleId='+item.id;
		openWindow('单位机构授权', url, 1, '700','700');
	} 
	
	/**
	 * 分配用户
	 */
	function grantUser(roleId){
		var item = checkSelected();
		if(item==null){
			openAlert('请选择一条记录！');
			return;
		}
		
		$('#role_user_contents').load('<%=basePath%>sys/role/getRoleUserList.do',{'roleId':item.id});
	    $("#role_users").dialog({
	        resizable: false,
	        draggable: true,
	        height: 500,
	        width: 400,
	        modal: true,
	        title: '分配用户',
	        buttons: {
	        	"保存": function () {
	        		var roleUserIdObj = roleUserList.selectedRows();
	        		/*
	        		if(roleUserIdObj.length < 1){
	        			openAlert('请选择用户！');
	        			return;
	        		}
	        		*/
	        		var roleUserIdArr = "";
	        		for (var int = 0; int < roleUserIdObj.length; int++) {
	        			roleUserIdArr += roleUserIdObj[int].ID + ",";
					}
	        		if(roleUserIdArr.length>0){
	        			roleUserIdArr = roleUserIdArr.substring(0, roleUserIdArr.length-1);
	        		}
	        		$.ajax({
	        			type : 'post',
	        			url  : '<%=basePath%>sys/role/grantUser.do',
	        			data : {roleId:item.id, userIdStr:roleUserIdArr},
	        			success: function(result){
	        				openAlert(result.content);
	        			}
	        		});
	        		$(this).dialog("close");
	        	}
	        }
	    });
	}
	/**
	*单位权限
	*/
	function dataPrivilege(){
		var item = checkSelected();
		if(item==null){
			openAlert('请选择一条记录！');
			return;
		}
		var rows = roleList.selectedRows();
		var roleName = '';
		roleName = rows[0].rolename;
		openWindow(roleName + '权限设置', 'sys/role/dataPrivilegePage.do?roleId='+item.id, 1, 800,650);
	}

	/**
	* 清除查询条件
	*/
	function clearSer(){
		$('#roleName').val('');
		searchList();
	}
</script>
  </body>
