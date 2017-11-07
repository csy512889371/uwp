<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<body>
	<div style="width:100%;float: left;overflow: auto;height: 550px;">
    <div class="oper_menu" style="margin-top:0px;position:absolute;width:71%;background-color: #fff;z-index: 9999;">
    	<!-- <button class="btn-style4" onclick="roleDeptList()">管理权限</button> -->
    	<!-- <button class="btn-style4" onclick="roleCadreList()">干部类别</button> -->
    	<button class="btn-style1" onclick="saveData()">保存</button>
	</div>
	<!-- 干部管理数据权限-->
   	<div id="deptListDiv" style="padding-top: 40px;">
		<table id="deptList" class="mmg">
			<tr>
	          <th rowspan="" colspan=""></th>
	        </tr>
		</table>
	</div>
	</div>
	<script type="text/javascript">
		var selectedId = null;
		var deptList;
		var deptListcols = [
	        { title:'DEPARTMENT_ID', name:'DEPARTMENT_ID' ,width:300, sortable: false, type: 'text',hidden:true},
	        { title:'ROLE_DEPT_RELA_ID', name:'ROLE_DEPT_RELA_ID' ,width:300, sortable: false, type: 'text',hidden:true},
	        { title:'所属权限名称', name:'DEPARTMENT_NAME' ,width:300, sortable: false, type: 'text'},
	    ];
		$(document).ready(function(){
			deptList();
			
		});
		function deptList(){
			deptList = $('#deptList').mmGrid({
	              height: commonTalbeHeight
	              , cols: deptListcols
	              , url: '<%=basePath%>sys/role/deptList.do?roleId='+roleId
	              , method: 'get'
	              , remoteSort:true
	              , sortName: 'SECUCODE'
	              , sortStatus: 'asc'
	              , root: 'result'
	              , multiSelect: true
	              , checkCol: true
	              , fullWidthRows: true
	              , autoLoad: false
	              , showBackboard : false
	              , plugins: [
	              ]
	              , params: function(){
	                  //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
	                return {
	                    secucode: $('#secucode').val()
	                };
	              }
	          });
	
			deptList.on('cellSelected', function(e, item, rowIndex, colIndex){
	              //查看
	              if($(e.target).is('.btn-info, .btnPrice')){
	                  e.stopPropagation();  //阻止事件冒泡
	                  alert(JSON.stringify(item));
	              }
	          }).on('loadSuccess',function(e, data){
	        	var rows = deptList.rows();
	        	var item = rows[0];
	        	if(typeof(item)=='undefined'){//无管理权限数据时不执行后续操作
	        		return;
	        	}
    			for(var s in rows){
    				if(typeof rows[s].ROLE_DEPT_RELA_ID != 'undefined' && rows[s].ROLE_DEPT_RELA_ID != ''){
    					deptList.select(parseInt(s));
    				}
    			}
           });
           deptList.load();
		}
		function saveData(){
			var deptListObj = deptList.selectedRows();
			var deptListIdArr = "";
       		for (var int = 0; int < deptListObj.length; int++) {
       			deptListIdArr += deptListObj[int].DEPARTMENT_ID + ",";
			}
       		deptListIdArr = deptListIdArr.substring(0, deptListIdArr.length-1);
       		$.ajax({
       			type : 'post',
       			url  : '<%=basePath%>sys/role/saveRoleDept.do',
       			data : {roleId:roleId, deptIdStr:deptListIdArr},
       			success: function(result){
       				openAlert(result.content);
       				$('#dataAttrList').load('<%=basePath%>sys/role/roleDeptList.do',{});
       			}
       		});
		}
	</script>
</body>
