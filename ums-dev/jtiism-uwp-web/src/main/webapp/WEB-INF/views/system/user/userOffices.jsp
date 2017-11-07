<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common.inc" %>  
<body>
  <div style="margin-top:10px;">
	<table id="userDeptList" class="mmg">
      <tr>
        <th rowspan="" colspan=""></th>
      </tr>
    </table>
  </div>
<script type="text/javascript">
var selectedId = null;
var userDeptList;
var deptList=JSON.parse('${deptList}');
var userId='${userId}';
//表头
var cols = [
    { title:'所属权限名称', name:'deptname' ,width:100, align:'left', sortable: false, type: 'text'},
    { title:'ID', name:'id' ,width:300, align:'center', sortable: false, type: 'text', hidden: true}
];

$(document).ready(function(){
	userDeptList = $('#userDeptList').mmGrid({
           height: 400
           , cols: cols
           , url: '<%=basePath%>sys/user/getUserDeptList.do'
           , method: 'post'
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
               //$('#pg').mmPaginator({})
           ]
           , params: function(){
               //如果这里有验证，在验证失败时返回false则不执行AJAX查询。
             return {
                 secucode: $('#secucode').val()
             };
           }
       });


	userDeptList.on('cellSelected', function(e, item, rowIndex, colIndex){
           //查看
           if($(e.target).is('.btn-info, .btnPrice')){
               e.stopPropagation();  //阻止事件冒泡
               alert(JSON.stringify(item));
           }
       }).on('loadSuccess',function(e, data){
    	   if(deptList.length == 0)
    		   return;
       		var rows = userDeptList.rows();
			for(var s in rows){
				if(rows[s].id == deptList['0']){
					userDeptList.select(parseInt(s));
				}
			}
       });
	userDeptList.load();
	
});

</script>
  </body>
