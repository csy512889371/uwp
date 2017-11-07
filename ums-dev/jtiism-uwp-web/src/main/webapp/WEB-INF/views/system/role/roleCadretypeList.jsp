<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc"%>
<body>
	<div>
    <div class="oper_menu">
    	<!-- <button class="btn-style4" onclick="roleDeptList()">管理权限</button> -->
    	<!-- <button class="btn-style4" onclick="roleCadreList()">干部类别</button> -->
    	<button class="btn-style1" onclick="saveData()">保存</button>
	</div>
	<!-- 干部管理数据权限-->
   	<div id="cadretypeListDiv">
		<table id="cadretypeList" class="mmg">
			<tr>
	          <th rowspan="" colspan=""></th>
	        </tr>
		</table>
	</div>
	</div>
	<script type="text/javascript">
		var selectedId = null;
		var cadretypeList;
		var cadretypeListcols = [
	        { title:'CADRETYPE_ID', name:'CADRETYPE_ID' ,width:300, sortable: false, type: 'text',hidden:true},
	        { title:'ROLE_CADRETYPE_RELA_ID', name:'ROLE_CADRETYPE_RELA_ID' ,width:300, sortable: false, type: 'text',hidden:true},
	        { title:'人员类别名称', name:'CADRETYPE_TYPENAME' ,width:300, sortable: false, type: 'text'},
	    ];
		$(document).ready(function(){
			cadretypeList();
			
		});
		function cadretypeList(){
			cadretypeList = $('#cadretypeList').mmGrid({
	              height: commonTalbeHeight
	              , cols: cadretypeListcols
	              , url: '<%=basePath%>sys/role/cadreList.do?roleId='+roleId
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
	
			cadretypeList.on('cellSelected', function(e, item, rowIndex, colIndex){
	              //查看
	              if($(e.target).is('.btn-info, .btnPrice')){
	                  e.stopPropagation();  //阻止事件冒泡
	                  alert(JSON.stringify(item));
	              }
	          }).on('loadSuccess',function(e, data){
           		var rows = cadretypeList.rows();
    			for(var s in rows){
    				if(typeof rows[s].ROLE_CADRETYPE_RELA_ID != 'undefined' && rows[s].ROLE_CADRETYPE_RELA_ID != ''){
    					cadretypeList.select(parseInt(s));
    				}
    			}
           });
           cadretypeList.load();
		}
		function saveData(){
			var cadretypeListObj = cadretypeList.selectedRows();
			var cadretypeListIdArr = "";
			
       		for (var int = 0; int < cadretypeListObj.length; int++) {
       			cadretypeListIdArr += cadretypeListObj[int].CADRETYPE_ID + ",";
			}
       		cadretypeListIdArr = cadretypeListIdArr.substring(0, cadretypeListIdArr.length-1);
       		$.ajax({
       			type : 'post',
       			url  : '<%=basePath%>sys/role/saveRoleCadretype.do',
       			data : {roleId:roleId, cadretypeIdStr:cadretypeListIdArr},
       			success: function(result){
       				openAlert(result.content);
       				$('#dataAttrList').load('<%=basePath%>sys/role/roleCadretypeList.do',{});
       			}
       		});
		}
	</script>
</body>
